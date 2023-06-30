package com.example.weather.search.search_view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.example.weather.localSource.ConcretLocalSource
import com.example.weather.search.SaveCityListener
import com.example.weather.search.search_repo.SearchApiState
import com.example.weather.search.search_repo.SearchRepo
import com.example.weather.search.search_repo.remote.SearchApiClient
import com.example.weather.search.search_repo.search_result_pojo.CityPojo
import kotlinx.coroutines.launch

class SearchView : Fragment(), SaveCityListener {
    private lateinit var searchFactory: SearchViewModelFactory
    private lateinit var searchViewModel: SearchViewModel
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
        searchFactory = SearchViewModelFactory(
            SearchRepo.getInstance(
                SearchApiClient.getInstance(),
                ConcretLocalSource.getInstance(requireContext())
            )
        )
        searchViewModel = ViewModelProvider(this, searchFactory)[SearchViewModel::class.java]
        //searchViewModel.getSearchData("cairo")
        cityAdapter = CityAdapter(this)
        binding.cityRecyclerView.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.cityRecyclerView.adapter = cityAdapter

        binding.searchBar.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || (event.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)) {
                searchViewModel.search(v.text.toString())
                true
            } else {
                false
            }
        }

        lifecycleScope.launch {
            searchViewModel.responseSearchFlow.collect { result ->
                when (result) {
                    is SearchApiState.Success -> {
                        binding.searchLoading.visibility = View.GONE
                        binding.cityRecyclerView.visibility = View.VISIBLE
                        Log.i("MySearch", result.data.toString())
                        cityAdapter.submitList(result.data)
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
        searchViewModel.saveCity(city)
        Toast.makeText(requireContext(), "${city.name} saved", Toast.LENGTH_SHORT).show()
        //_viewmodel.addFav(city)
    }

}