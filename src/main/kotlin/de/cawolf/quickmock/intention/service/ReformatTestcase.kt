package de.cawolf.quickmock.intention.service

import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.codeStyle.CodeStyleManager
import com.jetbrains.php.lang.psi.elements.PhpClass

class ReformatTestcase {
    fun invoke(project: Project, psiElement: PsiElement, clazz: PhpClass) {
        val pdm = project.getComponent(PsiDocumentManager::class.java)
        val document = pdm.getDocument(psiElement.containingFile)
        pdm.doPostponedOperationsAndUnblockDocument(document!!)

        val csm = ServiceManager.getService(project, CodeStyleManager::class.java)
        csm.reformat(clazz, true)
    }
}
