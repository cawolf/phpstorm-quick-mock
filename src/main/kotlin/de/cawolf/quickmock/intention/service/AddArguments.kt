package de.cawolf.quickmock.intention.service

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.tree.IElementType
import com.jetbrains.php.lang.PhpFileType
import com.jetbrains.php.lang.psi.elements.Parameter
import com.jetbrains.php.lang.psi.visitors.PhpElementVisitor

class AddArguments {
    fun invoke(parameterList: PsiElement, parameter: MutableList<Parameter>, project: Project) {
        val parameterListType = IElementType.enumerate { it -> it.toString() == "Parameter list" }.first()
        val joinedParameterValues = parameter.joinToString { it -> createParameterValueFromType(it) }
        val newParameterList = createFirstFromText(project, parameterListType, "f($joinedParameterValues);")
        parameterList.replace(newParameterList)
    }

    private fun createParameterValueFromType(it: Parameter) =
        when (it.type.toString()) {
            "string" -> "''"
            "int" -> "0"
            "float" -> "0.0"
            "bool" -> "true"
            "array" -> "[]"
            "" -> "null" // mixed
            "object" -> "\$this->" + it.name
            else -> "\$this->" + it.name + "->reveal()"
        }

    private fun createFirstFromText(p: Project, elementType: IElementType, text: String): PsiElement {
        var ret = arrayOf<PsiElement>()
        createDummyFile(p, text).accept(object : PhpElementVisitor() {
            override fun visitElement(element: PsiElement?) {
                val node = element!!.node
                if (node != null && node.elementType === elementType) {
                    ret += element
                }

                element.acceptChildren(this)
            }
        })
        return ret[0]
    }

    private fun createDummyFile(p: Project, fileText: String): PsiFile =
            PsiFileFactory.getInstance(p).createFileFromText("DUMMY__." + PhpFileType.INSTANCE.defaultExtension, PhpFileType.INSTANCE, "<?php\n$fileText", System.currentTimeMillis(), false)

}
