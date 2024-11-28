package com.sample.onlinestore.presentation.appintro

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sample.onlinestore.commonmodule.foundation.base.UiState
import com.sample.onlinestore.domain.sessionmanager.FakePreferenceManagerRepository
import com.sample.onlinestore.presentation.appintro.appintro.AppIntroAction
import com.sample.onlinestore.presentation.appintro.appintro.AppIntroEvent
import com.sample.onlinestore.presentation.appintro.appintro.AppIntroUiModel
import com.sample.onlinestore.presentation.appintro.appintro.AppIntroViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class AppIntroViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    private val dispatcher = StandardTestDispatcher()

    private lateinit var viewModel: AppIntroViewModel
    private lateinit var preferenceManagerRepository: FakePreferenceManagerRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        preferenceManagerRepository = FakePreferenceManagerRepository()
        viewModel = AppIntroViewModel(
            preferenceManagerRepository = preferenceManagerRepository,
            initialScreenState = UiState.Loading(AppIntroUiModel())
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `GIVEN I'm on the screen WHEN initial data is loaded, THEN appIntro pages are loaded to uiState`() =
        runTest {
            viewModel.sendAction(AppIntroAction.LoadIntroPages)
            advanceUntilIdle()
            val isAppIntroPagesLoaded =
                viewModel.uiState.value.data?.appIntroPages?.isNotEmpty() == true
            assertTrue(isAppIntroPagesLoaded)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `GIVEN I'm on the screen WHEN initial data is loaded, THEN appIntro pages should not contain empty ids`() =
        runTest {
            viewModel.sendAction(AppIntroAction.LoadIntroPages)
            advanceUntilIdle()
            val appIntroPagesLoaded = viewModel.uiState.value.data?.appIntroPages
            val isAppIntroPagesLoaded = appIntroPagesLoaded?.isNotEmpty() == true
            assertTrue(isAppIntroPagesLoaded)
            for (page in appIntroPagesLoaded!!) {
                assertTrue(page.imageId != 0)
                assertTrue(page.titleResId != 0)
                assertTrue(page.subTitleResId != 0)
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `WHEN user clicks Next button from last page, then AppIntro Status true should be saved to preference`() =
        runTest {
            preferenceManagerRepository.appIntroFinishedStatus = false
            viewModel.sendAction(AppIntroAction.LoadNextScreen)
            advanceUntilIdle()
            val appIntroStatus = preferenceManagerRepository.appIntroFinishedStatus
            assertTrue(appIntroStatus)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `WHEN user clicks Next button from last page, THEN LoadNextScreen event should be sent`() =
        runTest {
            val events = mutableListOf<AppIntroEvent>()

            // Collect events in a separate coroutine
            val job = launch {
                viewModel.uiEvent.collectLatest { event ->
                    events.add(event)
                }
            }

            // Trigger the action
            viewModel.sendAction(AppIntroAction.LoadNextScreen)
            advanceUntilIdle()

            // Assert that the event is correct
            assertTrue(events.last() == AppIntroEvent.LoadNextScreen)

            // Cancel the collector to avoid hanging
            job.cancel()
        }
}
