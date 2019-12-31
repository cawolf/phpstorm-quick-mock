class IntentionIsNotAvailableTest: BaseTest() {
    fun testCaretNotOnNewExpression() {
        setupScenario("caret-not-on-new-expression/SubjectTest.php", emptyList())
        assertIntentionIsNotAvailable()
    }

    fun testSubjectHasNoConstructor() {
        setupScenario("subject-has-no-constructor/SubjectTest.php", listOf("subject-has-no-constructor/SubjectUnderTest.php"))
        assertIntentionIsNotAvailable()
    }

    fun testConstructorHasNoParameters() {
        setupScenario("constructor-has-no-parameters/SubjectTest.php", listOf("constructor-has-no-parameters/SubjectUnderTest.php"))
        assertIntentionIsNotAvailable()
    }

    fun testParameterCountAlreadyMatches() {
        setupScenario("parameter-count-already-matches/SubjectTest.php", listOf("parameter-count-already-matches/SubjectUnderTest.php"))
        assertIntentionIsNotAvailable()
    }
}