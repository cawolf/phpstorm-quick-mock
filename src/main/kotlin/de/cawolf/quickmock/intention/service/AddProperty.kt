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

            val foldedType = foldDocBlockTypeHintedArray.invoke(parameter.type.toString())
            val objectProphecy = if (PRIMITIVES.contains(foldedType)) "" else "|ObjectProphecy"
            val docBlock = PhpPsiElementFactory.createFromText(project, docCommentType, "/** @var ${determineTypeHint(parameter, foldedType)}$objectProphecy */\n\$foo = null;")
            clazz.addBefore(docBlock, fieldAnchor)
        }

        return fieldAnchor
    }

    private fun determineTypeHint(parameter: Parameter, foldedType: String): String {
        return when {
            foldedType == "" -> "mixed"
            foldedType.contains('\\') -> when {
                foldedType.split('\\').last() != parameter.node.firstChildNode.text -> parameter.node.firstChildNode.text
                else -> foldedType.split('\\').last()
            }
            else -> foldedType
        }
    }
}
