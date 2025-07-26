package com.itb.diabetify.e2e.presentation.home

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import com.itb.diabetify.MainActivity
import com.itb.diabetify.e2e.di.TestAppModule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@UninstallModules(TestAppModule::class)
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class HomeTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @get:Rule(order = 2)
    val grantPermissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        android.Manifest.permission.POST_NOTIFICATIONS
    )

    private lateinit var testHelper: HomeTestHelper

    @Before
    fun setUp() {
        hiltRule.inject()
        testHelper = HomeTestHelper(composeTestRule)
    }

    @Test
    fun homeFlow_Complete() {
        testHelper.startAppAndNavigateToHome()
        testHelper.verifyHomeScreenDisplayed()
        testHelper.verifyWelcomeMessage()
        testHelper.waitForLoadingToComplete()
        testHelper.verifyNoErrorMessages()
    }

    @Test
    fun homeScreen_DisplaysAllMainContent() {
        testHelper.startAppAndNavigateToHome()
        
        testHelper.verifyLastPredictionInfo()
        testHelper.verifyQuickStatsCardDisplayed()
        testHelper.verifyRiskPercentageCardDisplayed()
        testHelper.verifyRiskFactorChartsDisplayed()

        testHelper.verifyBMICardDisplayed()
        testHelper.verifyBMIValues()
        
        testHelper.verifyHealthStatusCardDisplayed()
        testHelper.verifyHealthStatusValues()
        
        testHelper.verifyHistoryHealthStatusCardDisplayed()
        testHelper.verifyFamilyHistoryStatus()
        testHelper.verifyCholesterolStatus()
        testHelper.verifyPregnancyHistoryStatus()

        testHelper.verifyLifestyleFactorsCardDisplayed()
        testHelper.verifySmokingStatus()
        testHelper.verifyPhysicalActivityStatus()
    }

    @Test
    fun homeScreen_NavigateToRiskDetail_WorksCorrectly() {
        testHelper.startAppAndNavigateToHome()
        
        testHelper.navigateToRiskDetail()
        testHelper.verifyRiskDetailScreenDisplayed()
        
        testHelper.verifyRiskDetailPageContent()
        
        testHelper.navigateBackToHome()
        testHelper.verifyHomeScreenDisplayed()
    }

//    @Test
//    fun homeScreen_NavigateToRiskFactorDetail_WorksCorrectly() {
//        testHelper.startAppAndNavigateToHome()
//
//        testHelper.navigateToRiskFactorDetail()
//        testHelper.verifyRiskFactorDetailScreenDisplayed()
//
//        testHelper.verifyRiskFactorDetailPageContent()
//
//        testHelper.navigateBackToHome()
//        testHelper.verifyHomeScreenDisplayed()
//    }
//
//    @Test
//    fun homeScreen_NavigateToWhatIfSimulation_WorksCorrectly() {
//        testHelper.startAppAndNavigateToHome()
//
//        // Test navigation to What-If simulation
//        testHelper.navigateToWhatIfSimulation()
//        testHelper.verifyWhatIfScreenDisplayed()
//
//        // Test performing a simulation
//        testHelper.performWhatIfSimulation()
//        testHelper.verifySimulationResultsDisplayed()
//
//        // Navigate back to home
//        testHelper.navigateBackToHome()
//        testHelper.verifyHomeScreenDisplayed()
//    }
//
//    @Test
//    fun homeScreen_InteractiveElements_WorkCorrectly() {
//        testHelper.startAppAndNavigateToHome()
//
//        // Test interactive elements on home screen
//        testHelper.verifyInteractiveElementsClickable()
//        testHelper.testChartTabSwitching()
//
//        // Test other interactive features
//        testHelper.testHomeScreenInteractivity()
//    }
//
//    @Test
//    fun homeScreen_DataConsistencyAndStability() {
//        testHelper.startAppAndNavigateToHome()
//
//        // Test data display consistency
//        testHelper.verifyDataDisplayConsistency()
//
//        // Test data refresh capability
//        testHelper.verifyDataCanBeRefreshed()
//
//        // Test loading states
//        testHelper.verifyLoadingStatesHandledCorrectly()
//
//        // Test long-term stability
//        testHelper.verifyHomeScreenStability()
//    }
}