package de.cawolf.quickmock.intention.service

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType
import com.jetbrains.php.lang.psi.PhpPsiElementFactory
import com.jetbrains.php.lang.psi.elements.Parameter
import com.jetbrains.php.lang.psi.elements.PhpClass

class AddProperty {
    fun invoke(project: Project, parameter: Parameter, currentAnchor: PsiElement, clazz: PhpClass): PsiElement {
        val classFieldsType = IElementType.enumerate { it -> it.toString() == "Class fields" }.first()
        val docCommentType = IElementType.enumerate { it -> it.toString() == "PhpDocComment" }.first()

        val field = PhpPsiElementFactory.createFromText(project, classFieldsType, "class C { private \$${parameter.name}; }")
        val docBlock = PhpPsiElementFactory.createFromText(project, docCommentType, "/** @var ${parameter.node.firstChildNode.text} */\n\$foo = null;")

        val docBlockAnchor = clazz.addAfter(docBlock, currentAnchor)
        return clazz.addAfter(field, docBlockAnchor)
    }
}
