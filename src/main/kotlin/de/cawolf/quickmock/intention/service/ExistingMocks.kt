package de.cawolf.quickmock.intention.service

import com.jetbrains.php.lang.psi.elements.Field
import com.jetbrains.php.lang.psi.elements.Parameter
import com.jetbrains.php.lang.psi.elements.PhpClass
import de.cawolf.quickmock.intention.GENERATED_SUFFIX

class ExistingMocks {
    fun filter(parameter: Parameter, clazz: PhpClass): Boolean {
        val existingField = clazz.findOwnFieldByName(parameter.name, false)
        val existingSuffixedField = clazz.findOwnFieldByName(parameter.name + GENERATED_SUFFIX, false)
        return notMocked(existingField) || notMocked(existingSuffixedField)
    }

    private fun notMocked(existingField: Field?): Boolean {
        val isPrivate = existingField?.modifier?.isPrivate
        return isPrivate == null || !isPrivate
    }
}
