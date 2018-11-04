package de.cawolf.quickmock.intention

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.IncorrectOperationException
import com.jetbrains.php.lang.psi.PhpPsiElementFactory
import com.jetbrains.php.lang.psi.elements.*
import de.cawolf.quickmock.Settings
import de.cawolf.quickmock.intention.service.*
import java.util.*

class QuickMockCreator : PsiElementBaseIntentionAction(), IntentionAction {

    override fun getText(): String = ResourceBundle.getBundle(RESOURCE_BUNDLE).getString("intention.label")

    override fun getFamilyName(): String = text

    override fun isAvailable(project: Project, editor: Editor, psiElement: PsiElement): Boolean {
        val newExpression = PsiTreeUtil.getParentOfType(psiElement, NewExpression::class.java)
        return newExpression is NewExpression && (newExpression.parameterList?.children?.isEmpty() ?: false)
    }

    @Throws(IncorrectOperationException::class)
    override fun invoke(project: Project, editor: Editor, psiElement: PsiElement) {
        val parameters = getConstructorParameters(psiElement)
        if (parameters.size == 0) {
            return // simple constructor without parameters - nothing to do
        }

        // init and safeguards: do not proceed if the current edited test class is not parsing correctly
        val namespace = PsiTreeUtil.getParentOfType(psiElement, PhpNamespace::class.java)
                ?: return
        val clazz = PsiTreeUtil.getParentOfType(psiElement, PhpClass::class.java)
                ?: return
        val beginningOfClass = PsiTreeUtil.getParentOfType(psiElement, PhpClass::class.java)?.children?.find { it -> it is ImplementsList }?.nextSibling?.nextSibling
                ?: return
        val constructStatement = PsiTreeUtil.getParentOfType(psiElement, AssignmentExpression::class.java)
                ?: return
        val parameterList = PsiTreeUtil.getParentOfType(psiElement, NewExpression::class.java)?.parameterList
                ?: return

        // get helper services
        val addArguments = ServiceManager.getService(project, AddArguments::class.java)
        val addMissingUseStatements = ServiceManager.getService(project, AddMissingUseStatements::class.java)
        val addMockAssignment = ServiceManager.getService(project, AddMockAssignment::class.java)
        val addProperty = ServiceManager.getService(project, AddProperty::class.java)
        val reformatTestcase = ServiceManager.getService(project, ReformatTestcase::class.java)
        val removeSurroundingWhitespaces = ServiceManager.getService(project, RemoveSurroundingWhitespaces::class.java)
        val settings = ServiceManager.getService(Settings::class.java)

        // actually create mocks
        var currentAnchor = beginningOfClass
        if (settings.addDocBlockForMembers) {
            addMissingUseStatements.invoke(namespace, "\\Prophecy\\Prophecy\\ObjectProphecy")
        }

        for (parameter in parameters) {
            addMissingUseStatements.invoke(namespace, parameter.type.toString())
            addMockAssignment.invoke(project, constructStatement, parameter)

            if (!PRIMITIVES_NOT_TO_ADD_OR_MOCK.contains(parameter.type.toString())) {
                currentAnchor = addProperty.invoke(project, parameter, currentAnchor, clazz, settings.addDocBlockForMembers)
            }
        }
        addWhitespaceBetweenMockAssignmentsAnConstructor(constructStatement, project)
        removeSurroundingWhitespaces.invoke(parameterList)
        addArguments.invoke(parameterList, parameters, project)
        reformatTestcase.invoke(project, currentAnchor, clazz)
    }

    private fun addWhitespaceBetweenMockAssignmentsAnConstructor(constructStatement: PsiElement, project: Project) {
        val currentMethod = constructStatement.parent
        currentMethod.addBefore(PhpPsiElementFactory.createFromText(project, PsiWhiteSpace::class.java, "\n")!!, constructStatement)
    }

    private fun getConstructorParameters(psiElementAtCursor: PsiElement): MutableList<Parameter> {
        val newExpression = PsiTreeUtil.getParentOfType(psiElementAtCursor, NewExpression::class.java) as NewExpression
        val classReference = newExpression.classReference
        val method = classReference?.resolve() as Method
        return method.parameters.toMutableList()
    }
}
