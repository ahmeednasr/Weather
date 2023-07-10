package com.example.weather.alerts_view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.data_source.alert_repo.AlertRepoInterface
import com.example.weather.data_source.alert_repo.alert_pojo.SavedAlert
import kotlinx.coroutines.launch

class AlertViewModel(private var repo: AlertRepoInterface) : ViewModel() {
    private var _liveData = MutableLiveData<List<SavedAlert>>()
    val savedAlertsList: LiveData<List<SavedAlert>>
        get() = _liveData


    //remote


    fun getSavedAlerts() {
        viewModelScope.launch {
            _liveData.postValue(repo.getSavedAlerts())
        }
    }

    fun insertAlert(alert: SavedAlert) {
        viewModelScope.launch {
            repo.insertAlert(alert)
            _liveData.postValue(repo.getSavedAlerts())
        }
    }

    fun deleteAlert(alert: SavedAlert) {
        viewModelScope.launch {
            repo.removeAlert(alert)
            _liveData.postValue(repo.getSavedAlerts())
        }
    }

}