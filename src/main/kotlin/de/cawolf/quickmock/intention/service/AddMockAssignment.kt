package de.cawolf.quickmock.intention.service

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType
import com.jetbrains.php.lang.psi.PhpPsiElementFactory
import com.jetbrains.php.lang.psi.elements.Parameter
import de.cawolf.quickmock.intention.PRIMITIVES_NOT_TO_ADD_OR_MOCK

class AddMockAssignment {
    fun invoke(project: Project, constructStatement: PsiElement, parameter: Parameter) {
        if (PRIMITIVES_NOT_TO_ADD_OR_MOCK.contains(parameter.type.toString())) return

        val whitespaceType = IElementType.enumerate { it -> it.toString() == "WHITE_SPACE" }.first()
        val mockAssignment = PhpPsiElementFactory.createStatement(project, createAssignmentFromType(parameter))

        val currentMethod = constructStatement.parent
        currentMethod.addBefore(mockAssignment, constructStatement)
        currentMethod.addBefore(PhpPsiElementFactory.createFromText(project, whitespaceType, ""), constructStatement)
    }

    private fun createAssignmentFromType(parameter: Parameter) =
            when (parameter.type.toString()) {
                "object" -> "\$this->${parameter.name} = new \\stdClass();"
                else -> "\$this->${parameter.name} = \$this->prophesize(${parameter.node.firstChildNode.text}::class);"
            }

}
