package com.example.weather.search.search_view

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.databinding.FragmentSearchBinding
import com.example.weather.data_source.localSource.ConcretLocalSource
import com.example.weather.data_source.search_repo.SearchApiState
import com.example.weather.data_source.search_repo.SearchRepo
import com.example.weather.data_source.search_repo.search_remote.SearchApiClient
import com.example.weather.data_source.search_repo.search_result_pojo.CityPojo
import com.example.weather.map.CityAdapter
import com.example.weather.map.MapViewModel
import com.example.weather.map.MapViewModelFactory
import com.example.weather.search.SaveCityListener

import kotlinx.coroutines.launch

class SearchView : Fragment(), SaveCityListener {
    private lateinit var searchFactory: MapViewModelFactory
    private lateinit var mapViewModel: MapViewModel
    lateinit var binding: FragmentSearchBinding
    lateinit var cityAdapter: CityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchFactory = MapViewModelFactory(
            SearchRepo.getInstance(
                SearchApiClient.getInstance(),
                ConcretLocalSource.getInstance(requireContext())
            )
        )
        mapViewModel = ViewModelProvider(this, searchFactory)[MapViewModel::class.java]
        //searchViewModel.getSearchData("cairo")
        // cityAdapter = CityAdapter(this)
        binding.cityRecyclerView.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.cityRecyclerView.adapter = cityAdapter

        binding.searchBar.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || (event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)) {
                mapViewModel.search(v.text.toString())
                true
            } else {
                false
            }
        }

        lifecycleScope.launch {
            mapViewModel.responseSearchFlow.collect { result ->
                when (result) {
                    is SearchApiState.Success -> {
                        binding.searchLoading.visibility = View.GONE
                        binding.cityRecyclerView.visibility = View.VISIBLE
                        Log.i("MySearch", result.data.toString())
                        cityAdapter.data = result.data
                    }
                    is SearchApiState.Failure -> {
                        binding.searchLoading.visibility = View.GONE
                        binding.cityRecyclerView.visibility = View.INVISIBLE
                        Log.i("MySearch", result.msg.toString())
                    }
                    else -> {
                        //loading
                        binding.cityRecyclerView.visibility = View.INVISIBLE
                        binding.searchLoading.visibility = View.VISIBLE
                    }
                }

            }
        }
    }

    override fun onCityClick(city: CityPojo) {
        mapViewModel.saveCity(city)
        Toast.makeText(requireContext(), "${city.name} saved", Toast.LENGTH_SHORT).show()
        //_viewmodel.addFav(city)
    }

}