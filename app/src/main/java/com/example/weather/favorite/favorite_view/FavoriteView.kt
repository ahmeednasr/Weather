package com.example.weather.favorite.favorite_view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.companion.MyCompanion
import com.example.weather.databinding.FragmentFavoriteBinding
import com.example.weather.favorite.favorite_repo.FavoriteRepo
import com.example.weather.localSource.ConcretLocalSource
import com.example.weather.map.MyMapFragmentArgs
import com.example.weather.map.MyMapFragmentDirections
import com.example.weather.search.search_repo.search_result_pojo.CityPojo
import com.example.weather.search.search_view.CityAdapter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class FavoriteView : Fragment(), OnCityClickListener {
    private lateinit var binding: FragmentFavoriteBinding
    lateinit var controller: NavController
    lateinit var viewModel: FavoriteViewModel
    lateinit var factory: FavoriteViewModelFactory
    lateinit var favAdapter: FavoriteAdapter

    private val rotateOpen: Animation by lazy {
        AnimationUtils.loadAnimation(
            context,
            R.anim.rotate_open_ainm
        )
    }
    private val rotateClose: Animation by lazy {
        AnimationUtils.loadAnimation(
            context,
            R.anim.rotate_close_ainm
        )
    }
    private val fromBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            context,
            R.anim.from_bottom_anim
        )
    }
    private val toBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            context,
            R.anim.to_bottom_anim
        )
    }
    private var clicked: Boolean = false

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
        //Log.i("FLAG", city.toString())
        val args = FavoriteViewArgs.fromBundle(requireArguments())
        val flag = args.flag
        //  val city = FavoriteViewArgs.fromBundle(arguments!!).city
        Log.i("FLAG", flag.toString())
//        if (flag != MyCompanion.MAP_FRAGMENT) {
//            Log.i("FLAG", "nulllllllll")
//        } else {
//            Log.i("FLAG", flag)
//            //Log.i("FLAG", args.city.toString())
//        }
        factory = FavoriteViewModelFactory(
            FavoriteRepo.getInstance(
                ConcretLocalSource.getInstance(requireContext())
            )
        )
        viewModel = ViewModelProvider(this, factory)[FavoriteViewModel::class.java]
        if (flag) {
            val city = args.city
            Log.i("FLAG", city.toString())
            if (city != null) {
                viewModel.insertCity(city)
            }
        }
        favAdapter = FavoriteAdapter(this)
        binding.favCitiesRecyclerView.adapter = favAdapter
        binding.favCitiesRecyclerView.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        viewModel.citiesList.observe(this) {
            favAdapter.submitList(it)
        }
//        lifecycleScope.launch {
//            viewModel.citiesList.collect {
//
//            }
//        }
        binding.addFloating.setOnClickListener {
            onAddClicked()
        }
        binding.locationFloating.setOnClickListener {
            controller.navigate(FavoriteViewDirections.actionFavoriteViewToMyMapFragment(MyCompanion.FAV_FRAGMENT))
        }
        binding.searchFloating.setOnClickListener {
            controller.navigate(FavoriteViewDirections.actionFavoriteViewToSearchFragment())
        }
    }

    override fun removeCity(city: CityPojo) {
        viewModel.deleteCity(city)
    }

    override fun viewCity(city: CityPojo) {
        val action =
            FavoriteViewDirections.actionFavoriteFragmentToCityView()
        action.city = city
        controller.navigate(action)
        //navCtrl
    }

    private fun onAddClicked() {
        setVisibility(clicked)
        setAnimation(clicked)
        clicked = !clicked
    }

    private fun setAnimation(clicked: Boolean) {
        if (!clicked) {
            binding.locationFloating.startAnimation(fromBottom)
            binding.locationFloating.startAnimation(fromBottom)
            binding.addFloating.startAnimation(rotateOpen)
        } else {
            binding.locationFloating.startAnimation(toBottom)
            binding.locationFloating.startAnimation(toBottom)
            binding.addFloating.startAnimation(rotateClose)
        }
    }

    private fun setVisibility(clicked: Boolean) {
        if (!clicked) {
            binding.locationFloating.visibility = View.VISIBLE
            binding.searchFloating.visibility = View.VISIBLE
        } else {
            binding.locationFloating.visibility = View.INVISIBLE
            binding.searchFloating.visibility = View.INVISIBLE
        }
    }


}