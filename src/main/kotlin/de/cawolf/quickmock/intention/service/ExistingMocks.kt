package de.cawolf.quickmock.intention.service

import com.jetbrains.php.lang.psi.elements.Parameter
import com.jetbrains.php.lang.psi.elements.PhpClass

class ExistingMocks {
    fun filter(parameter: Parameter, clazz: PhpClass): Boolean {
        val existingField = clazz.findFieldByName(parameter.name, false)
        val isPrivate = existingField?.modifier?.isPrivate
        return isPrivate == null || !isPrivate
    }
}
