package de.cawolf.quickmock.intention.service

import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.jetbrains.php.lang.psi.elements.NewExpression
import com.jetbrains.php.lang.psi.elements.Parameter

class ConstructorParameters {
    fun get(psiElementAtCursor: PsiElement, project: Project): MutableList<Parameter> {
        val constructorService = ServiceManager.getService(project, Constructor::class.java)
        val newExpression = PsiTreeUtil.getParentOfType(psiElementAtCursor, NewExpression::class.java) as NewExpression
        val constructor = constructorService.get(newExpression)
        return constructor!!.parameters.toMutableList()
    }
}
