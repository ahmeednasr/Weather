package com.example.weather.location_weather_view_model

import com.example.weather.MainDispatcherRule
import com.example.weather.data.source.FakeLocalSource
import com.example.weather.data.source.FakeLocationWeatherRepo
import com.example.weather.data.source.FakeRemoteDataSource
import com.example.weather.data.source.FakeResponse
import com.example.weather.data_source.location_weather_repo.LocationWeatherApiState
import com.example.weather.data_source.location_weather_repo.LocationWeatherRepo
import com.example.weather.data_source.search_repo.search_result_pojo.CityPojo
import com.example.weather.system.companion.MyCompanion
import com.example.weather.views.location_weather_view_model.LocationWeatherViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.hamcrest.core.IsEqual
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class LocationWeatherViewModelTest {
    val fakeResponse = FakeResponse.locationWeatherResponse
    val fakeCurrentCity = FakeResponse.currentCity
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
            FakeLocalSource(listOf<CityPojo>().toMutableList(), null, null, null)
        fakeRemoteDataSource = FakeRemoteDataSource(fakeResponse, fakeCurrentCity)
        fakeRepo = FakeLocationWeatherRepo(fakeRemoteDataSource, fakeLocalSource)
        fakeViewModel = LocationWeatherViewModel(fakeRepo)
        repo = LocationWeatherRepo.getInstance(fakeRemoteDataSource, fakeLocalSource)
        viewModel = LocationWeatherViewModel(repo)
    }

    @Test
    fun `getCurrentLocationResponse locationLatLongAndLanguage ApiStateStateIsLoading`() = runTest {
        //when
        val state = fakeViewModel.responseFlow.value
        assertThat(
            LocationWeatherApiState.Loading,
            IsEqual(state)
        )
        fakeViewModel.getCurrentLocationResponse(37.77, -122.42, "en")
        val sState = fakeViewModel.responseFlow.value
        assertThat(
            LocationWeatherApiState.Loading,
            IsEqual(sState)
        )
    }

    @Test
    fun `getCurrentLocationResponse locationLatLongAndLanguage ApiStates`() =
        runTest {

            //when
            viewModel.getCurrentLocationResponse(37.77, -122.42, "en")
            val state = viewModel.responseFlow.value
            //then
            when (state) {
                is LocationWeatherApiState.Success -> assertThat(
                    state.data, IsEqual(fakeResponse)
                )
                is LocationWeatherApiState.Loading -> assertThat(
                    state,
                    IsEqual(LocationWeatherApiState.Loading)
                )
                else -> {
                    val e: Exception = Exception()
                    assertThat(
                        state,
                        IsEqual(LocationWeatherApiState.Failure(e))
                    )
                }
            }
        }
    @Test
    fun `getLocationType KeyIsnullIsGetGPSAsDefault`() {
        //when
        val key = viewModel.getLocationType()
        //then
        assertThat(key, IsEqual(MyCompanion.GPS))
    }

    @Test
    fun `getLocationType afterChangeKey`() {
        //when
        viewModel.setLocationType(MyCompanion.MAP)
        val key = viewModel.getLocationType()
        //then
        assertThat(key, IsEqual(MyCompanion.MAP))
    }

}

