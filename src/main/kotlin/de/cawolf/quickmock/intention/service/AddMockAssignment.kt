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
                "\$this->${parameter.name} = \$this->prophesize(${parameter.node.firstChildNode.text}::class);"
        )

        val currentMethod = constructStatement.parent
        currentMethod.addBefore(mockAssignment, constructStatement)
        currentMethod.addBefore(PhpPsiElementFactory.createFromText(project, whitespaceType, ""), constructStatement)
    }
}
