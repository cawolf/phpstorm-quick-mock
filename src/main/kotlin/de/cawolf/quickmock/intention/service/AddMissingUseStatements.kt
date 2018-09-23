package de.cawolf.quickmock.intention.service

import com.intellij.psi.PsiElement
import com.jetbrains.php.codeInsight.PhpCodeInsightUtil
import com.jetbrains.php.lang.psi.elements.Parameter
import com.jetbrains.php.refactoring.PhpAliasImporter

class AddMissingUseStatements {
    fun invoke(namespace: PsiElement, parameter: Parameter) {
        val interfaceFqn = parameter.type.toString()
        val scopeHolder = PhpCodeInsightUtil.findScopeForUseOperator(namespace)
        if (scopeHolder != null && PhpCodeInsightUtil.alreadyImported(scopeHolder, interfaceFqn) == null && PhpCodeInsightUtil.canImport(scopeHolder, interfaceFqn)) {
            PhpAliasImporter.insertUseStatement(interfaceFqn, scopeHolder)
        }
    }
}
