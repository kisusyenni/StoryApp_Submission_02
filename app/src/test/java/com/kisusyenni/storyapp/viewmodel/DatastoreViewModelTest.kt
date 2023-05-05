package com.kisusyenni.storyapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.kisusyenni.storyapp.data.SessionPreference
import com.kisusyenni.storyapp.data.source.local.entity.Session
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

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class DatastoreViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var coroutinesTestRule = CoroutinesTestRule()

    @Mock
    private lateinit var pref: SessionPreference

    @Mock
    private lateinit var datastoreViewModel: DatastoreViewModel

    private val dummySession = DataDummy.generateDummySession()
    private val dummyEmptySession = DataDummy.generateDummyEmptySession()
    private val dummyTheme = DataDummy.generateDummyTheme()

    @Before
    fun setUp() {
        datastoreViewModel = DatastoreViewModel(pref)
    }

    @Test
    fun `When save and fetch session data successfully`() = runTest {
        datastoreViewModel.saveSession(dummySession)
        Assert.assertNotNull(datastoreViewModel.session.value)
        Assert.assertEquals(dummySession, datastoreViewModel.session.value)

        val expectedResponse = MutableLiveData<Session>()
        expectedResponse.value = datastoreViewModel.session.value

        datastoreViewModel.fetchSession()
        val actualResponse = datastoreViewModel.session.value

        Mockito.verify(pref).getSession()

        Assert.assertNotNull(actualResponse)
        Assert.assertEquals(actualResponse, dummySession)
    }

    @Test
    fun `When save session successfully and return right value`() = runTest {

        val expectedResponse = MutableLiveData<Session>()
        expectedResponse.value = dummySession

        datastoreViewModel.saveSession(dummySession)
        val actualResponse = datastoreViewModel.session.getOrAwaitValue()

        Mockito.verify(pref).saveSession(dummySession)

        Assert.assertNotNull(actualResponse)
        Assert.assertEquals(actualResponse, dummySession)
    }

    @Test
    fun `When remove session successfully and data store value is empty`() = runTest {
        val expectedResponse = MutableLiveData<Session>()
        expectedResponse.value = dummyEmptySession

        datastoreViewModel.saveSession(dummySession)
        Assert.assertEquals(dummySession, datastoreViewModel.session.value)
        Assert.assertNotEquals(expectedResponse, datastoreViewModel.session.value)

        datastoreViewModel.removeSession()
        Assert.assertEquals(dummyEmptySession, datastoreViewModel.session.value)
        Assert.assertNotEquals(datastoreViewModel.session.value, dummySession)

        val actualResponse = datastoreViewModel.session.value

        Mockito.verify(pref).removeSession()

        Assert.assertNotNull(actualResponse)
        Assert.assertEquals(actualResponse, dummyEmptySession)
        Assert.assertEquals(expectedResponse.value, actualResponse)
    }

    @Test
    fun `When save and get theme successfully`() = runTest {
        datastoreViewModel.saveTheme(dummyTheme)
        Assert.assertNotNull(datastoreViewModel.darkMode.value)
        Assert.assertEquals(dummyTheme, datastoreViewModel.darkMode.value)

        val expectedResponse = MutableLiveData<Boolean>()
        expectedResponse.value = datastoreViewModel.darkMode.value

        datastoreViewModel.getTheme()
        val actualResponse = datastoreViewModel.darkMode.value


        Mockito.verify(pref).getTheme()
        Assert.assertNotNull(actualResponse)
        Assert.assertEquals(actualResponse, dummyTheme)
    }

    @Test
    fun `When save theme successfully and return right value`() = runTest {
        val expectedResponse = MutableLiveData<Boolean>()
        expectedResponse.value = dummyTheme

        datastoreViewModel.saveTheme(dummyTheme)
        val actualResponse = datastoreViewModel.darkMode.getOrAwaitValue()

        Mockito.verify(pref).saveTheme(dummyTheme)

        Assert.assertNotNull(actualResponse)
        Assert.assertEquals(actualResponse, dummyTheme)

    }
}