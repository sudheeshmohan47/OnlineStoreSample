package com.sample.onlinestore.presentation.splash

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sample.onlinestore.commonmodule.foundation.base.UiState
import com.sample.onlinestore.domain.splash.SplashUseCase
import com.sample.onlinestore.domain.splash.model.UserStatus
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class SplashViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()
    private val dispatcher = StandardTestDispatcher()

    private lateinit var viewModel: SplashViewModel
    private val splashUseCase = mockk<SplashUseCase>()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)

        coEvery { splashUseCase.checkUserStatus() } returns UserStatus(false, null)
        viewModel = SplashViewModel(
            splashUseCase = splashUseCase,
            initialScreenState = UiState.Loading(SplashUiModel())
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `GIVEN I'm on the screen WHEN screen is loaded THEN it returns token as null`() = runTest {
        advanceUntilIdle()
        assertNull(viewModel.uiState.value.data?.userStatus?.sessionToken)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `GIVEN I'm on the screen WHEN screen is loaded THEN it returns a valid token`() = runTest {
        coEvery {
            splashUseCase.checkUserStatus()
        } returns UserStatus(false, "valid token")

        viewModel = SplashViewModel(
            splashUseCase,
            UiState.Loading(SplashUiModel())
        )

        advanceUntilIdle()
        assertNotNull(viewModel.uiState.value.data?.userStatus?.sessionToken)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `GIVEN I'm on the screen WHEN screen is loaded THEN it returns app intro as not completed`() =
        runTest {
            viewModel = SplashViewModel(
                splashUseCase,
                UiState.Loading(SplashUiModel())
            )

            advanceUntilIdle()
            assertFalse(viewModel.uiState.value.data?.userStatus?.isAppIntroFinished == true)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `GIVEN I'm on the screen WHEN screen is loaded THEN it returns app intro as completed`() =
        runTest {
            coEvery {
                splashUseCase.checkUserStatus()
            } returns UserStatus(true, null)

            viewModel = SplashViewModel(
                splashUseCase,
                UiState.Loading(SplashUiModel())
            )

            advanceUntilIdle()
            assertTrue(viewModel.uiState.value.data?.userStatus?.isAppIntroFinished == true)
        }
}