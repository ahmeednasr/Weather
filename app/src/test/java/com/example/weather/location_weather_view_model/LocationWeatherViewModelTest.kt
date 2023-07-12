package com.example.weather.location_weather_view_model

import android.util.Log
import com.example.weather.MainDispatcherRule
import com.example.weather.data.source.FakeLocalSource
import com.example.weather.data.source.FakeLocationWeatherRepo
import com.example.weather.data.source.FakeRemoteDataSource
import com.example.weather.data.source.FakeResponse
import com.example.weather.data_source.location_weather_repo.LocationWeatherApiState
import com.example.weather.data_source.location_weather_repo.LocationWeatherRepo
import com.example.weather.data_source.search_repo.search_result_pojo.CityPojo
import com.example.weather.home.location_weather_view_model.LocationWeatherViewModel
import com.example.weather.system.companion.MyCompanion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.MainCoroutineDispatcher
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.internal.MainDispatcherFactory
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.hamcrest.core.IsEqual
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class LocationWeatherViewModelTest {
    val fakeResponse = FakeResponse.locationWeatherResponse
    lateinit var fakeLocalSource: FakeLocalSource
    lateinit var fakeRemoteDataSource: FakeRemoteDataSource

    lateinit var fakeRepo: FakeLocationWeatherRepo
    lateinit var fakeViewModel: LocationWeatherViewModel
    lateinit var repo: LocationWeatherRepo
    lateinit var viewModel: LocationWeatherViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        fakeLocalSource =
            FakeLocalSource(listOf<CityPojo>().toMutableList(), MyCompanion.GPS, null, null)
        fakeRemoteDataSource = FakeRemoteDataSource(fakeResponse)
        fakeRepo = FakeLocationWeatherRepo(fakeRemoteDataSource, fakeLocalSource)
        fakeViewModel = LocationWeatherViewModel(fakeRepo)
        repo = LocationWeatherRepo.getInstance(fakeRemoteDataSource, fakeLocalSource)
        viewModel = LocationWeatherViewModel(repo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getCurrentLocationResponse locationLatLongAndLanguage ApiStateIs`() = runTest {
        //when
        val state = fakeViewModel.responseFlow.value
        assertThat(
            LocationWeatherApiState.Loading,
            IsEqual(state)
        )
        fakeViewModel.getCurrentLocationResponse(37.77, -122.42, "en")
        val sState = fakeViewModel.responseFlow.value
        //then
        when (sState) {
            is LocationWeatherApiState.Success -> assertThat(
                sState.data, IsEqual(fakeResponse)
            )
            is LocationWeatherApiState.Loading -> assertThat(
                sState,
                IsEqual(LocationWeatherApiState.Loading)
            )
            else -> {}
        }
    }

    @Test
    fun `getCurrentLocationResponse locationLatLongAndLanguage ApiStateIsSuccess`() = runTest {
        //when
        val state = viewModel.responseFlow.value
        assertThat(
            LocationWeatherApiState.Loading,
            IsEqual(state)
        )
        viewModel.getCurrentLocationResponse(37.77, -122.42, "en")
        val sState = viewModel.responseFlow.value
        //then
        when (sState) {
            is LocationWeatherApiState.Success -> assertThat(
                sState.data, IsEqual(fakeResponse)
            )
            is LocationWeatherApiState.Loading -> assertThat(
                sState,
                IsEqual(LocationWeatherApiState.Loading)
            )
            else -> {}
        }
    }
}

