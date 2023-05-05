package com.kisusyenni.storyapp.view.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.kisusyenni.storyapp.data.SessionPreference
import com.kisusyenni.storyapp.data.source.StoryAppRepository
import com.kisusyenni.storyapp.data.source.remote.ApiResponse
import com.kisusyenni.storyapp.data.source.remote.response.LoginResponse
import com.kisusyenni.storyapp.utils.CoroutinesTestRule
import com.kisusyenni.storyapp.utils.DataDummy
import com.kisusyenni.storyapp.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @Mock
    private lateinit var storyAppRepository: StoryAppRepository

    @Mock
    private lateinit var pref: SessionPreference

    @Mock
    private lateinit var loginViewModel: LoginViewModel

    @Before
    fun setUp() {
        loginViewModel = LoginViewModel(storyAppRepository)
    }

    private val dummyEmail = "hallo11@email.com"
    private val dummyPassword = "hallo123"
    private val dummyResponse = DataDummy.generateDummyLoginResponse()

    @Test
    fun `When login is successful`() = runTest {

        val expectedResponse = MutableLiveData<ApiResponse<LoginResponse?>>()
        expectedResponse.value = ApiResponse.Success(dummyResponse)

        Mockito.`when`(
            storyAppRepository.login(
                email = dummyEmail,
                password = dummyPassword
            )
        ).thenReturn(expectedResponse)

        val actualResponse = loginViewModel.login(
            email = dummyEmail,
            password = dummyPassword
        ).getOrAwaitValue()

        Mockito.verify(storyAppRepository)
            .login(email = dummyEmail, password = dummyPassword)
        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is ApiResponse.Success)
        Assert.assertEquals(dummyResponse, actualResponse.data)
    }

    @Test
    fun `When login is failed`() {
        val expectedResponse = MutableLiveData<ApiResponse<LoginResponse?>>()
        expectedResponse.value = ApiResponse.Error("Error")

        Mockito.`when`(storyAppRepository.login(dummyEmail, dummyPassword))
            .thenReturn(expectedResponse)

        val actualResponse = loginViewModel.login(dummyEmail, dummyPassword).getOrAwaitValue()

        Mockito.verify(storyAppRepository).login(dummyEmail, dummyPassword)

        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is ApiResponse.Error)
        Assert.assertEquals(
            expectedResponse.value?.message,
            actualResponse.message
        )
    }
}