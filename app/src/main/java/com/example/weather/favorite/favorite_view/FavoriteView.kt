package com.example.weather.favorite.favorite_view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.companion.MyCompanion
import com.example.weather.databinding.FragmentFavoriteBinding
import com.example.weather.favorite.favorite_repo.FavoriteRepo
import com.example.weather.localSource.ConcretLocalSource
import com.example.weather.map.repo.search_result_pojo.CityPojo


class FavoriteView : Fragment(), OnCityClickListener {
    private lateinit var binding: FragmentFavoriteBinding
    lateinit var controller: NavController
    lateinit var viewModel: FavoriteViewModel
    lateinit var factory: FavoriteViewModelFactory
    lateinit var favAdapter: FavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        controller = Navigation.findNavController(view)

        factory = FavoriteViewModelFactory(
            FavoriteRepo.getInstance(
                ConcretLocalSource.getInstance(requireContext())
            )
        )
        viewModel = ViewModelProvider(this, factory)[FavoriteViewModel::class.java]

        favAdapter = FavoriteAdapter(this)
        binding.favCitiesRecyclerView.adapter = favAdapter
        binding.favCitiesRecyclerView.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)

        //observeLiveData
        viewModel.citiesList.observe(this) {
            favAdapter.submitList(it)
        }

        binding.addFloating.setOnClickListener {
            controller.navigate(FavoriteViewDirections.actionFavoriteViewToMyMapFragment(MyCompanion.FAV_FRAGMENT))
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getCities()
    }

    override fun removeCity(city: CityPojo) {
        viewModel.deleteCity(city)
    }

    override fun viewCity(city: CityPojo) {
        val action =
            FavoriteViewDirections.actionFavoriteFragmentToCityView()
        action.city = city
        controller.navigate(action)
    }
}