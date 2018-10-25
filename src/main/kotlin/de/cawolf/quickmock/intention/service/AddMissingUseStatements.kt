package de.cawolf.quickmock.intention.service

import com.intellij.psi.PsiElement
import com.jetbrains.php.codeInsight.PhpCodeInsightUtil
import com.jetbrains.php.refactoring.PhpAliasImporter
import de.cawolf.quickmock.intention.PRIMITIVES_NOT_TO_USE

class AddMissingUseStatements {
    fun invoke(namespace: PsiElement, fqcn: String) {
        if (PRIMITIVES_NOT_TO_USE.contains(fqcn)) return

        val scopeHolder = PhpCodeInsightUtil.findScopeForUseOperator(namespace)
        if (scopeHolder != null && PhpCodeInsightUtil.alreadyImported(scopeHolder, fqcn) == null && PhpCodeInsightUtil.canImport(scopeHolder, fqcn)) {
            PhpAliasImporter.insertUseStatement(fqcn, scopeHolder)
        }
    }
}
