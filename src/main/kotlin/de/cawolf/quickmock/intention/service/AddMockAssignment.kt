package de.cawolf.quickmock.intention.service

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType
import com.jetbrains.php.lang.psi.PhpPsiElementFactory
import com.jetbrains.php.lang.psi.elements.Parameter

class AddMockAssignment {
    fun invoke(project: Project, constructStatement: PsiElement, parameter: Parameter) {
        val whitespaceType = IElementType.enumerate { it -> it.toString() == "WHITE_SPACE" }.first()
        val mockAssignment = PhpPsiElementFactory.createStatement(
                project,
                "\$this->${parameter.name} = ${mockValueFromType(parameter)};"
        )

        val currentMethod = constructStatement.parent
        currentMethod.addBefore(mockAssignment, constructStatement)
        currentMethod.addBefore(PhpPsiElementFactory.createFromText(project, whitespaceType, ""), constructStatement)
    }

    private fun mockValueFromType(parameter: Parameter) =
            when (parameter.type.toString()) {
                "string" -> "''"
                "int" -> "0"
                "float" -> "0.0"
                "bool" -> "true"
                "array" -> "[]"
                "" -> "null" // mixed
                "object" -> "new \\stdClass()"
                else -> "\$this->prophesize(${parameter.node.firstChildNode.text}::class)"
            }
}
