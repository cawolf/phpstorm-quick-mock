package de.cawolf.quickmock.intention

@JvmField
val PRIMITIVES_NOT_TO_ADD_OR_MOCK = arrayOf(
        "string",
        "int",
        "float",
        "bool",
        "array",
        "" // mixed
)

@JvmField
val PRIMITIVES_NOT_TO_USE = PRIMITIVES_NOT_TO_ADD_OR_MOCK.plus(
        "object"
)
