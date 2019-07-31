package de.cawolf.quickmock.intention.service

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.jetbrains.php.codeInsight.PhpCodeInsightUtil
import com.jetbrains.php.lang.psi.elements.Method
import com.jetbrains.php.lang.psi.elements.NewExpression
import com.jetbrains.php.lang.psi.elements.PhpPsiElement
import com.jetbrains.php.lang.psi.elements.impl.ClassReferenceImpl

class AliasedUseStatements {
    fun find(psiElementAtCursor: PsiElement): Map<String, String> {
        val aliasedUseStatements = HashMap<String, String>()
        val newExpression = PsiTreeUtil.getParentOfType(psiElementAtCursor, NewExpression::class.java) as NewExpression
        val classReference = newExpression.classReference
        val method = classReference?.resolve() as Method
        val containingClass = method.containingClass
        val useLists = PhpCodeInsightUtil.collectImports(containingClass?.parent?.parent as PhpPsiElement)
        for (useList in useLists) {
            if (useList.declarations.size == 1) {
                useList.declarations[0].aliasName?.let { aliasedUseStatements.put((useList.firstPsiChild?.firstPsiChild as ClassReferenceImpl).type.toString(), it) }
            }
        }
        return aliasedUseStatements
    }
}
