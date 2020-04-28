package de.cawolf.quickmock.intention.service

import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.codeStyle.CodeStyleManager
import com.jetbrains.php.lang.psi.elements.PhpClass

class ReformatTestcase {
    fun invoke(project: Project, psiElement: PsiElement, clazz: PhpClass) {
        val psiDocumentManager = ServiceManager.getService(project, PsiDocumentManager::class.java)
        val document = psiDocumentManager.getDocument(psiElement.containingFile)
        psiDocumentManager.doPostponedOperationsAndUnblockDocument(document!!)

        val csm = ServiceManager.getService(project, CodeStyleManager::class.java)
        csm.reformat(clazz, true)
    }
}
