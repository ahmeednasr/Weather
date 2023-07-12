package com.example.weather.views.alerts_view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.data_source.ConnectionResult
import com.example.weather.data_source.alert_repo.AlertRepoInterface
import com.example.weather.data_source.alert_repo.alert_pojo.AlertResponse
import com.example.weather.data_source.alert_repo.alert_pojo.SavedAlert
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AlertViewModel(private var repo: AlertRepoInterface) : ViewModel() {
    private var _postAlertResponse =
        MutableStateFlow<ConnectionResult<AlertResponse>>(ConnectionResult.Loading)
    val AlertStateFlow: StateFlow<ConnectionResult<AlertResponse>>
        get() = _postAlertResponse

    private var _liveData = MutableLiveData<List<SavedAlert>>()
    val savedAlertsList: LiveData<List<SavedAlert>>
        get() = _liveData

    init {
        getSavedAlerts()
    }

    //remote
    fun getRemoteAlert(lat: Double, long: Double) {
        viewModelScope.launch {
            try {
                repo.getRemoteAlerts(lat, long).collect { data ->
                    _postAlertResponse.value = ConnectionResult.Success(data)
                }
            } catch (e: Exception) {
                _postAlertResponse.value = ConnectionResult.Error(e)
            }

        }
    }

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