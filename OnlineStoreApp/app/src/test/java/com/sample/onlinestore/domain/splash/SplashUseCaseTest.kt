package com.sample.onlinestore.domain.splash

import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SplashUseCaseTest {
    private lateinit var splashRepository: FakeSplashRepository
    private lateinit var splashUseCase: SplashUseCase

    @Before
    fun setUp() {
        splashRepository = FakeSplashRepository()
        splashUseCase = SplashUseCase(splashRepository)
    }

    @Test
    fun `checkUserStatus returns correct user status`() = runTest {
        val userStatus = splashUseCase.checkUserStatus()
        assertFalse(userStatus.isAppIntroFinished)
    }

    @Test
    fun `WHEN app intro is not finished THEN sessionToken in UserStatus should be null`() =
        runTest {
            splashRepository.appIntroFinished = false
            splashRepository.sessionToken = null
            val userStatus = splashUseCase.checkUserStatus()

            if (!userStatus.isAppIntroFinished) {
                assertNull(userStatus.sessionToken)
            }
        }

    @Test
    fun `WHEN app intro is finished THEN checkUserStatus returns correct user status`() = runTest {
        splashRepository.appIntroFinished = true
        val userStatus = splashUseCase.checkUserStatus()
        assertTrue(userStatus.isAppIntroFinished)
        assertNull(userStatus.sessionToken)
    }

    @Test
    fun `WHEN user is loggedIn, THEN checkUserStatus returns correct user status`() = runTest {
        splashRepository.appIntroFinished = true
        splashRepository.sessionToken = "valid token"
        val userStatus = splashUseCase.checkUserStatus()
        assertTrue(userStatus.isAppIntroFinished)
        assertNotNull(userStatus.sessionToken)
    }

    @Test
    fun `WHEN user is loggedIn, THEN app Intro status shouldn't be false`() = runTest {
        splashRepository.appIntroFinished = true
        splashRepository.sessionToken = "valid token"

        val userStatus = splashUseCase.checkUserStatus()
        if (userStatus.sessionToken != null) {
            assertTrue(userStatus.isAppIntroFinished)
        }
    }
}
