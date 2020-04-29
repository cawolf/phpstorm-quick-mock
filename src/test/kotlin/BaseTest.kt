import com.intellij.openapi.components.ServiceManager
import com.intellij.testFramework.UsefulTestCase
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.intellij.testFramework.fixtures.impl.CodeInsightTestFixtureImpl
import com.jetbrains.php.config.PhpLanguageLevel
import com.jetbrains.php.config.PhpProjectConfigurationFacade
import de.cawolf.quickmock.Settings
import org.junit.Assert

abstract class BaseTest: BasePlatformTestCase() {
    override fun getTestDataPath(): String {
        return "testData"
    }

    override fun setUp() {
        super.setUp()
        val settings = ServiceManager.getService(Settings::class.java)
        settings.addDocBlockForMembers = true
    }

    private fun intentionAvailable(): Boolean {
        val actions = myFixture.availableIntentions
        for (intentionAction in actions) {
            val text = intentionAction.text
            if (text != "Quick Mock: add constructor prophecies") {
                continue
            }
            return true
        }
        return false
    }

    protected fun setupScenario(testFile: String, projectFiles: List<String>) {
        PhpProjectConfigurationFacade(project).languageLevel = PhpLanguageLevel.PHP710
        myFixture.configureByFiles(testFile, *(projectFiles.toTypedArray()))
    }

    protected fun assertIntentionIsNotAvailable() {
        Assert.assertFalse(intentionAvailable())
    }

    protected fun assertIntentionIsAvailable() {
        Assert.assertTrue(intentionAvailable())
    }

    protected fun executeIntention() {
        val action = myFixture.findSingleIntention("Quick Mock: add constructor prophecies")
        CodeInsightTestFixtureImpl.invokeIntention(action, myFixture.file, myFixture.editor)
    }

    protected fun assertResultMatches(expectedResultFile: String) {
        UsefulTestCase.assertSameLinesWithFile("$testDataPath/$expectedResultFile", myFixture.file.text)
    }

    protected fun assertQuickMockCreationWorksWith(testDataFolder: String) {
        setupScenario("$testDataFolder/SubjectTest.php", listOf("$testDataFolder/SubjectUnderTest.php"))
        assertIntentionIsAvailable()
        executeIntention()
        assertResultMatches("$testDataFolder/SubjectTest.php.expected")
    }
}