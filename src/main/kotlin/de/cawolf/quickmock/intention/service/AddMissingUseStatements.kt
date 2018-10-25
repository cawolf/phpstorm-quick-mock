package de.cawolf.quickmock.intention.service

import com.intellij.psi.PsiElement
import com.jetbrains.php.codeInsight.PhpCodeInsightUtil
import com.jetbrains.php.lang.psi.elements.Parameter
import com.jetbrains.php.refactoring.PhpAliasImporter

class AddMissingUseStatements {
    fun invoke(namespace: PsiElement, fqcn: String) {
        val scopeHolder = PhpCodeInsightUtil.findScopeForUseOperator(namespace)
        if (scopeHolder != null && PhpCodeInsightUtil.alreadyImported(scopeHolder, fqcn) == null && PhpCodeInsightUtil.canImport(scopeHolder, fqcn)) {
            PhpAliasImporter.insertUseStatement(fqcn, scopeHolder)
        }
    }
}
