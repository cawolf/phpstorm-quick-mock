package de.cawolf.quickmock.intention.service

import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType
import com.jetbrains.php.lang.psi.PhpPsiElementFactory
import com.jetbrains.php.lang.psi.elements.Parameter
import com.jetbrains.php.lang.psi.elements.PhpClass
import de.cawolf.quickmock.intention.PRIMITIVES

class AddProperty {
    fun invoke(project: Project, parameter: Parameter, parameterName: String, currentAnchor: PsiElement, clazz: PhpClass, addDocBlockForMembers: Boolean): PsiElement {
        val classFieldsType = IElementType.enumerate { it.toString() == "Class fields" }.first()
        val docCommentType = IElementType.enumerate { it.toString() == "PhpDocComment" }.first()

        val field = PhpPsiElementFactory.createFromText(project, classFieldsType, "class C { private \$$parameterName; }")
        val fieldAnchor = clazz.addAfter(field, currentAnchor)

        if (addDocBlockForMembers) {
            val foldDocBlockTypeHintedArray = ServiceManager.getService(project, FoldDocBlockTypeHintedArray::class.java)

            val objectProphecy = if (PRIMITIVES.contains(foldDocBlockTypeHintedArray.invoke(parameter.type.toString()))) "" else "|ObjectProphecy"
            val docBlock = PhpPsiElementFactory.createFromText(project, docCommentType, "/** @var ${parameter.node.firstChildNode.text}$objectProphecy */\n\$foo = null;")
            clazz.addBefore(docBlock, fieldAnchor)
        }

        return fieldAnchor
    }
}
