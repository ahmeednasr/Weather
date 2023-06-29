package com.example.weather.favorite.favorite_view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.weather.R
import com.example.weather.databinding.FragmentFavoriteBinding
import com.example.weather.databinding.FragmentHomeBinding


class FavoriteFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteBinding
    lateinit var controller: NavController
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
        binding.addFloating.setOnClickListener {
            onAddClicked()
        }
        binding.locationFloating.setOnClickListener {

        }
        binding.searchFloating.setOnClickListener {
            controller.navigate((FavoriteFragmentDirections.actionFavoriteFragmentToSearchFragment()))
        }
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