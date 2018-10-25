package de.cawolf.quickmock.intention.service

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace

class RemoveSurroundingWhitespaces {
    fun invoke(psiElement: PsiElement) {
        if (psiElement.prevSibling is PsiWhiteSpace) psiElement.prevSibling.delete()
        if (psiElement.nextSibling is PsiWhiteSpace) psiElement.nextSibling.delete()
    }
}
