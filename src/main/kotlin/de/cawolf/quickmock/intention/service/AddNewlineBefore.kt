package de.cawolf.quickmock.intention.service

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import com.jetbrains.php.lang.psi.PhpPsiElementFactory

class AddNewlineBefore {
    fun invoke(psiElement: PsiElement, project: Project) {
        val currentMethod = psiElement.parent
        currentMethod.addBefore(
            PhpPsiElementFactory.createFromText(project, PsiWhiteSpace::class.java, "\n")!!,
            psiElement
        )
    }
}
