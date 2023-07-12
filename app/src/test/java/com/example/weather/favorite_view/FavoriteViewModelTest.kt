package com.example.weather.favorite_view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weather.data.source.FakeLocalSource
import com.example.weather.data_source.favorite_repo.FavoriteRepo
import com.example.weather.data_source.search_repo.search_result_pojo.CityPojo
import com.example.weather.getOrAwaitValue
import com.example.weather.system.companion.MyCompanion
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FavoriteViewModelTest {
    @get: Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private val cairo = CityPojo("EG", 30.0444, 31.2357, "Cairo", "Greater Cairo")
    private val london = CityPojo("GB", 51.5073219, -0.1276474, "London", "England")
    private val madrid = CityPojo("ES", 40.4167047, -3.7035825, "Madrid", "Madrid")

    private val savedCities = listOf(cairo)
    private val fakeRmList = listOf(cairo)
    private val fakeAddedList = listOf(cairo, london, madrid)
    lateinit var fakeLocalSource: FakeLocalSource
    lateinit var repo: FavoriteRepo
    lateinit var viewModel: FavoriteViewModel

    @Before
    fun setUp() {
        fakeLocalSource =
            FakeLocalSource(listOf<CityPojo>().toMutableList(), MyCompanion.GPS, null, null)
        repo = FavoriteRepo.getInstance(fakeLocalSource)
        viewModel = FavoriteViewModel(repo)
    }

    @Test
    fun `getCities noSavedCities`() {
        viewModel.getCities()
        val value = viewModel.citiesList.value
        assertEquals(emptyList<CityPojo>(), value)
    }

    @Test
    fun `getCities foundSavedCities`() {
        //given
        fakeLocalSource.cities = savedCities.toMutableList()
        //when
        viewModel.getCities()
        val value = viewModel.citiesList.value
        //then
        assertEquals(savedCities, value)
    }

    @Test
    fun `insertCity newCity setNewCityInRepo`() {
        //when
        viewModel.insertCity(cairo)
        viewModel.getCities()
        val liveData = viewModel.citiesList.getOrAwaitValue {}
        //then
        assertEquals(liveData, savedCities)
    }

    @Test
    fun `deleteCity City removeCityFromSavedCityListInLocal`() {
        //given
        fakeLocalSource.cities = savedCities.toMutableList()
        //when
        viewModel.deleteCity(cairo)
        val liveData = viewModel.citiesList.getOrAwaitValue {}
        //then
        assertEquals(liveData, emptyList<CityPojo>())
    }

}