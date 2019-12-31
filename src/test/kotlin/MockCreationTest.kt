import com.intellij.openapi.components.ServiceManager
import de.cawolf.quickmock.Settings

class MockCreationTest: BaseTest() {
    fun testMocksObjects() {
        assertQuickMockCreationWorksWith("mocks-objects")
    }

    fun testMocksArrays() {
        assertQuickMockCreationWorksWith("mocks-arrays")
    }

    fun testMocksInts() {
        assertQuickMockCreationWorksWith("mocks-ints")
    }

    fun testMocksFloats() {
        assertQuickMockCreationWorksWith("mocks-floats")
    }

    fun testMocksBools() {
        assertQuickMockCreationWorksWith("mocks-bools")
    }

    fun testMocksStrings() {
        assertQuickMockCreationWorksWith("mocks-strings")
    }

    fun testMocksMixeds() {
        assertQuickMockCreationWorksWith("mocks-mixeds")
    }

    fun testMocksObjectsWithoutDocBlock() {
        val settings = ServiceManager.getService(Settings::class.java)
        settings.addDocBlockForMembers = false
        assertQuickMockCreationWorksWith("mocks-objects-without-doc-block")
    }

    fun testMocksMultipleParameters() {
        assertQuickMockCreationWorksWith("mocks-multiple-parameters")
    }

    fun testDontRegenerateExistingMocks() {
        assertQuickMockCreationWorksWith("dont-regenerate-existing-mocks")
    }

    fun testMocksAliases() {
        assertQuickMockCreationWorksWith("mocks-aliases")
    }

    fun testMocksExistingPropertiesWithSuffix() {
        setupScenario(
                "mocks-existing-properties-with-suffix/SubjectTest.php",
                listOf("mocks-existing-properties-with-suffix/SubjectUnderTest.php", "mocks-existing-properties-with-suffix/TestCase.php")
        )
        assertIntentionIsAvailable()
        executeIntention()
        assertResultMatches("mocks-existing-properties-with-suffix/SubjectTest.php.expected")
    }

    fun testMocksDocBlockTypeHintedArrays() {
        assertQuickMockCreationWorksWith("mocks-doc-block-type-hinted-arrays")
    }
}