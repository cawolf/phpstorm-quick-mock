package de.cawolf.quickmock.intention.service

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType
import com.jetbrains.php.lang.psi.PhpPsiElementFactory
import com.jetbrains.php.lang.psi.elements.Parameter

class AddArguments {
    fun invoke(psiElementAtCursor: PsiElement, parameter: MutableList<Parameter>, project: Project) {
        val parameterListType = IElementType.enumerate { it -> it.toString() == "Parameter list" }.first()
        val joinedParameterNames = parameter.joinToString { it -> "\$this->" + it.name }
        val newParameterList = PhpPsiElementFactory.createFromText(project, parameterListType, "f($joinedParameterNames);")
        psiElementAtCursor.prevSibling.replace(newParameterList)
    }
}
