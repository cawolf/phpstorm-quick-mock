package de.cawolf.quickmock.intention.service

import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.tree.IElementType
import com.jetbrains.php.lang.PhpFileType
import com.jetbrains.php.lang.psi.elements.Parameter
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor
import de.cawolf.quickmock.intention.PRIMITIVES

class AddArguments {
    fun invoke(parameterList: PsiElement, parameter: List<Pair<String, Parameter>>, project: Project) {
        val parameterListType = IElementType.enumerate { it.toString() == "Parameter list" }.first()
        val joinedParameterValues = parameter.joinToString { "\$this->${it.first}${addReveal(it.second, project)}" }
        val newParameterList = createFirstFromText(project, parameterListType, "f($joinedParameterValues);")
        parameterList.replace(newParameterList)
    }

    private fun addReveal(it: Parameter, project: Project): String {
        val foldDocBlockTypeHintedArray = ServiceManager.getService(project, FoldDocBlockTypeHintedArray::class.java)
        return if (PRIMITIVES.contains(foldDocBlockTypeHintedArray.invoke(it.type.toString()))) "" else "->reveal()"
    }

    private fun createFirstFromText(p: Project, elementType: IElementType, text: String): PsiElement {
        var ret = arrayOf<PsiElement>()
        createDummyFile(p, text).accept(object : PhpElementVisitor() {
            override fun visitElement(element: PsiElement) {
                val node = element.node
                if (node != null && node.elementType === elementType) {
                    ret += element
                }

                element.acceptChildren(this)
            }
        })
        return ret[0]
    }

    private fun createDummyFile(p: Project, fileText: String): PsiFile =
            PsiFileFactory.getInstance(p).createFileFromText("DUMMY__.${PhpFileType.INSTANCE.defaultExtension}", PhpFileType.INSTANCE, "<?php\n$fileText", System.currentTimeMillis(), false)

}
