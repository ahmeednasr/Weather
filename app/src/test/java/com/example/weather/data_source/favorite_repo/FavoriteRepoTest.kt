package com.example.weather.data_source.favorite_repo

import com.example.weather.data.source.FakeLocalSource
import com.example.weather.data_source.search_repo.search_result_pojo.CityPojo
import com.example.weather.system.companion.MyCompanion
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.core.IsEqual
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test

class FavoriteRepoTest {
    //repo -> local
    //local -> list of CityPojo
    private val cairo = CityPojo("EG", 30.0444, 31.2357, "Cairo", "Greater Cairo")
    private val london = CityPojo("GB", 51.5073219, -0.1276474, "London", "England")

    private val madrid = CityPojo("ES", 40.4167047, -3.7035825, "Madrid", "Madrid")

    private val savedCities = listOf(cairo, london)
    private val fakeRmList = listOf(cairo)
    private val fakeAddedList = listOf(cairo, london, madrid)
    lateinit var fakeLocalSource: FakeLocalSource
    lateinit var repo: FavoriteRepo

    @Before
    fun setUp() {
        fakeLocalSource = FakeLocalSource(savedCities.toMutableList(), MyCompanion.GPS, null, null)
        repo = FavoriteRepo.getInstance(fakeLocalSource)

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getSavedCities getListOfSavedCity`() = runBlockingTest {
        //when
        var result = repo.getSavedCities()

        //then
        assertThat(result, IsEqual(savedCities))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `insertCity newCity setNewCityInLocal`() = runBlockingTest {
        //when
        repo.insertCity(madrid)
        var result = repo.getSavedCities()

        //then
        assertThat(result, IsEqual(fakeAddedList))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `removeCity City removeCityFromLocal`() = runBlockingTest {
        //when
        repo.removeCity(london)
        var result = repo.getSavedCities()

        //then
        assertThat(result, IsEqual(fakeRmList))
    }
}