package de.cawolf.quickmock.intention.service

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.jetbrains.php.lang.psi.elements.Method
import com.jetbrains.php.lang.psi.elements.NewExpression
import com.jetbrains.php.lang.psi.elements.Parameter

class ConstructorParameters {
    fun get(psiElementAtCursor: PsiElement): MutableList<Parameter> {
        val newExpression = PsiTreeUtil.getParentOfType(psiElementAtCursor, NewExpression::class.java) as NewExpression
        val classReference = newExpression.classReference
        val method = classReference?.resolve() as Method
        return method.parameters.toMutableList()
    }
}
