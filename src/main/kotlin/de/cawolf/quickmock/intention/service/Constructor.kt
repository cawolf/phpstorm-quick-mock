package de.cawolf.quickmock.intention.service

import com.jetbrains.php.lang.psi.elements.Method
import com.jetbrains.php.lang.psi.elements.NewExpression
import com.jetbrains.php.lang.psi.elements.PhpClass

class Constructor {
    fun get(newExpression: NewExpression): Method? {
        if (newExpression.classReference?.resolve() is PhpClass && (newExpression.classReference?.resolve() as PhpClass).constructor is Method) {
            return (newExpression.classReference?.resolve() as PhpClass).constructor
        } else if (newExpression.classReference?.resolve() is Method) {
            return newExpression.classReference?.resolve() as Method
        }
        return null
    }
}