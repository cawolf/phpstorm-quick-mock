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
import com.jetbrains.php.lang.psi.elements.impl.NewExpressionImpl
import de.cawolf.quickmock.intention.service.*

class QuickMockCreator : PsiElementBaseIntentionAction(), IntentionAction {

    override fun getText(): String {
        return "Create mocks for all parameters of the constructor"
    }

    override fun getFamilyName(): String = text

    override fun isAvailable(project: Project, editor: Editor, psiElement: PsiElement): Boolean {
        return (psiElement.parent is NewExpression
                && psiElement.textMatches(")")
                && psiElement.prevSibling is ParameterList
                && psiElement.prevSibling.children.isEmpty()
                && psiElement.prevSibling.prevSibling.textMatches("("))
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

        // get helper services
        val addArguments = ServiceManager.getService(project, AddArguments::class.java)
        val addMissingUseStatements = ServiceManager.getService(project, AddMissingUseStatements::class.java)
        val addMockAssignment = ServiceManager.getService(project, AddMockAssignment::class.java)
        val addProperty = ServiceManager.getService(project, AddProperty::class.java)
        val reformatTestcase = ServiceManager.getService(project, ReformatTestcase::class.java)

        // actually create mocks
        var currentAnchor = beginningOfClass
        for (parameter in parameters) {
            addMissingUseStatements.invoke(namespace, parameter)
            addMockAssignment.invoke(project, constructStatement, parameter)
            currentAnchor = addProperty.invoke(project, parameter, currentAnchor, clazz)
        }
        addWhitespaceBetweenMockAssignmentsAnConstructor(constructStatement, project)
        addArguments.invoke(psiElement, parameters, project)
        reformatTestcase.invoke(project, psiElement, clazz)
    }

    private fun addWhitespaceBetweenMockAssignmentsAnConstructor(constructStatement: PsiElement, project: Project) {
        val currentMethod = constructStatement.parent
        currentMethod.addBefore(PhpPsiElementFactory.createFromText(project, PsiWhiteSpace::class.java, "\n")!!, constructStatement)
    }

    private fun getConstructorParameters(psiElementAtCursor: PsiElement): MutableList<Parameter> {
        val newExpression = psiElementAtCursor.parent as NewExpressionImpl
        val classReference = newExpression.classReference
        val method = classReference?.resolve() as Method
        return method.parameters.toMutableList()
    }
}
