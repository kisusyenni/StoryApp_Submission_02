package com.kisusyenni.storyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kisusyenni.storyapp.data.SessionPreference
import com.kisusyenni.storyapp.data.source.local.entity.Session
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class DatastoreViewModel(private val pref: SessionPreference) : ViewModel() {

    private val _session = MutableLiveData<Session>()
    val session: LiveData<Session> = _session

    private val _darkMode = MutableLiveData<Boolean>()
    val darkMode: LiveData<Boolean> = _darkMode

    fun fetchSession() {
        viewModelScope.launch {
            _session.value = pref.getSession().first()
        }
    }

    fun saveSession(session: Session) {
        viewModelScope.launch {
            pref.saveSession(session)
            _session.value = session
        }
    }

    fun removeSession() {
        _session.value = Session("", "", "", false)
        viewModelScope.launch {
            pref.removeSession()
        }
    }

    fun getTheme() {
        viewModelScope.launch {
            _darkMode.value = pref.getTheme().first()
        }
    }

    fun saveTheme(isDarkMode: Boolean) {
        viewModelScope.launch {
            _darkMode.value = isDarkMode
            pref.saveTheme(isDarkMode)
        }
    }
}