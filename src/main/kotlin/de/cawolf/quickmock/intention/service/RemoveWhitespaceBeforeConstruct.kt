package de.cawolf.quickmock.intention.service

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace

class RemoveWhitespaceBeforeConstruct {
    fun invoke(constructStatement: PsiElement) {
        if (constructStatement.parent.prevSibling is PsiWhiteSpace) {
            constructStatement.parent.prevSibling.delete()
        }
    }
}
