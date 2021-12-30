package de.cawolf.quickmock.intention.service

import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.jetbrains.php.codeInsight.PhpCodeInsightUtil
import com.jetbrains.php.lang.psi.PhpGroupUseElement.PhpUseKeyword
import com.jetbrains.php.refactoring.PhpAliasImporter
import de.cawolf.quickmock.intention.PRIMITIVES

class AddMissingUseStatements {
    fun invoke(project: Project, namespace: PsiElement, fqcn: String, alias: String?): Boolean {
        val foldDocBlockTypeHintedArray = ServiceManager.getService(project, FoldDocBlockTypeHintedArray::class.java)

        if (PRIMITIVES.contains(foldDocBlockTypeHintedArray.invoke(fqcn))) return false

        val scopeHolder = PhpCodeInsightUtil.findScopeForUseOperator(namespace)
        if (scopeHolder != null && PhpCodeInsightUtil.findImportedName(
                scopeHolder,
                fqcn,
                PhpUseKeyword.CLASS
            ) == null && PhpCodeInsightUtil.canImport(scopeHolder, fqcn, PhpUseKeyword.CLASS)
        ) {
            PhpAliasImporter.insertUseStatement(fqcn, alias, scopeHolder)
        }
        return true
    }
}
