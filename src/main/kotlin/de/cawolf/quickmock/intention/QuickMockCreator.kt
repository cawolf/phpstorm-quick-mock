package de.cawolf.quickmock.intention

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.IncorrectOperationException
import com.jetbrains.php.lang.psi.elements.*
import de.cawolf.quickmock.Settings
import de.cawolf.quickmock.intention.service.*
import java.util.*

class QuickMockCreator : PsiElementBaseIntentionAction(), IntentionAction {

    override fun getText(): String = ResourceBundle.getBundle(RESOURCE_BUNDLE).getString("intention.label")

    override fun getFamilyName(): String = text

    override fun isAvailable(project: Project, editor: Editor, psiElement: PsiElement): Boolean {
        val constructorParameters = ServiceManager.getService(project, ConstructorParameters::class.java)
        val newExpression = PsiTreeUtil.getParentOfType(psiElement, NewExpression::class.java)
        return newExpression is NewExpression
                && newExpression.classReference?.resolve() is Method
                && constructorParameters.get(psiElement).count() != newExpression.parameterList!!.children.count()
    }

    @Throws(IncorrectOperationException::class)
    override fun invoke(project: Project, editor: Editor, psiElement: PsiElement) {
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
        val aliasedUseStatements = ServiceManager.getService(project, AliasedUseStatements::class.java)
        val reformatTestcase = ServiceManager.getService(project, ReformatTestcase::class.java)
        val removeSurroundingWhitespaces = ServiceManager.getService(project, RemoveSurroundingWhitespaces::class.java)
        val constructorParameters = ServiceManager.getService(project, ConstructorParameters::class.java)
        val addNewlineBefore = ServiceManager.getService(project, AddNewlineBefore::class.java)
        val removeWhitespaceBeforeConstruct = ServiceManager.getService(project, RemoveWhitespaceBeforeConstruct::class.java)
        val existingMocks = ServiceManager.getService(project, ExistingMocks::class.java)
        val settings = ServiceManager.getService(Settings::class.java)

        // actually create mocks
        var currentAnchor = beginningOfClass
        var nonPrimitiveMocked = false
        val allParameters = constructorParameters.get(psiElement)
        val parametersWithoutMocks = allParameters.filter { parameter -> existingMocks.filter(parameter, clazz) }
        val aliasedUseStatementList = aliasedUseStatements.find(psiElement)

        removeWhitespaceBeforeConstruct.invoke(constructStatement)

        val parameterMapByName = ArrayList<Pair<String, Parameter>>(parametersWithoutMocks.size)
        for (parameter in parametersWithoutMocks) {
            val parameterClassName = parameter.type.toString()
            val parameterName = determineParameterName(clazz, parameter)
            nonPrimitiveMocked = addMissingUseStatements.invoke(namespace, parameterClassName, aliasedUseStatementList[parameterClassName]) || nonPrimitiveMocked
            addMockAssignment.invoke(project, constructStatement, parameter, parameterName)

            currentAnchor = addProperty.invoke(project, parameter, parameterName, currentAnchor, clazz, settings.addDocBlockForMembers)

            parameterMapByName.add(Pair(parameterName, parameter))
        }

        if (settings.addDocBlockForMembers && nonPrimitiveMocked) {
            addMissingUseStatements.invoke(namespace, "\\Prophecy\\Prophecy\\ObjectProphecy", null)
        }

        addNewlineBefore.invoke(constructStatement, project)
        removeSurroundingWhitespaces.invoke(parameterList)
        addArguments.invoke(parameterList, parameterMapByName, project)
        reformatTestcase.invoke(project, currentAnchor, clazz)
    }

    private fun determineParameterName(clazz: PhpClass, parameter: Parameter): String {
        return if (clazz.findOwnFieldByName(parameter.name, false) == null && clazz.findFieldByName(parameter.name, false) != null) parameter.name + GENERATED_SUFFIX else parameter.name
    }
}
