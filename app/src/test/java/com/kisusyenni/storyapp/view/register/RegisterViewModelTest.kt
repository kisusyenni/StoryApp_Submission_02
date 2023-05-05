package com.kisusyenni.storyapp.view.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.kisusyenni.storyapp.data.source.StoryAppRepository
import com.kisusyenni.storyapp.data.source.remote.ApiResponse
import com.kisusyenni.storyapp.data.source.remote.response.RegisterResponse
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
class RegisterViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @Mock
    private lateinit var storyAppRepository: StoryAppRepository

    @Mock
    private lateinit var registerViewModel: RegisterViewModel

    @Before
    fun setUp() {
        registerViewModel = RegisterViewModel(storyAppRepository)
    }

    private val dummyName = "hallo"
    private val dummyEmail = "hallo11@email.com"
    private val dummyPassword = "hallo123"
    private val dummyResponse = DataDummy.generateDummyRegisterResponse()

    @Test
    fun `When register is successful`() = runTest {

        val expectedResponse = MutableLiveData<ApiResponse<RegisterResponse?>>()
        expectedResponse.value = ApiResponse.Success(dummyResponse)

        Mockito.`when`(
            storyAppRepository.register(
                name = dummyName,
                email = dummyEmail,
                password = dummyPassword
            )
        ).thenReturn(expectedResponse)

        val actualResponse = registerViewModel.register(
            name = dummyName,
            email = dummyEmail,
            password = dummyPassword
        ).getOrAwaitValue()

        Mockito.verify(storyAppRepository)
            .register(name = dummyName, email = dummyEmail, password = dummyPassword)
        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is ApiResponse.Success)
        Assert.assertEquals(dummyResponse, actualResponse.data)
    }

    @Test
    fun `When register is failed`() {
        val expectedResponse = MutableLiveData<ApiResponse<RegisterResponse?>>()
        expectedResponse.value = ApiResponse.Error("Error")

        Mockito.`when`(storyAppRepository.register(dummyName, dummyEmail, dummyPassword)).thenReturn(
            expectedResponse
        )

        val actualResponse =
            registerViewModel.register(dummyName, dummyEmail, dummyPassword).getOrAwaitValue()

        Mockito.verify(storyAppRepository).register(dummyName, dummyEmail, dummyPassword)

        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is ApiResponse.Error)
        Assert.assertEquals(
            expectedResponse.value?.message,
            actualResponse.message
        )
    }

}