package de.cawolf.quickmock.intention.service

class FoldDocBlockTypeHintedArray {
    fun invoke(fullType: String): String {
        if (fullType.contains("[]")) return "array"

        if (fullType.contains('|')) {
            val allTypes = fullType.split('|')
            val remainingTypes = ArrayList<String>()
            for (type in allTypes) {
                remainingTypes.add(type)
            }

            return remainingTypes.joinToString("|")
        }

        return fullType
    }
}