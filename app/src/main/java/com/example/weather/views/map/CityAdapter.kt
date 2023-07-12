package com.example.weather.views.map

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.data_source.search_repo.search_result_pojo.CityPojo
import com.example.weather.databinding.SearchCityItemBinding

class CityAdapter(private val listener: OnCitySelected) :
    RecyclerView.Adapter<CityAdapter.ViewHolder>() {

    var data = listOf<CityPojo>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SearchCityItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentCity: CityPojo = data[position]

        holder.binding.cityName.text = currentCity.name
        val description = "${currentCity.country},${currentCity.state ?: ""}"
        holder.binding.cityDiscription.text = description
        holder.binding.cityBtn.setOnClickListener {
            listener.selectCity(currentCity)
        }
    }
    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(val binding: SearchCityItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}
//class CityAdapter(private val listener: OnCitySelected) :
//    ListAdapter<CityPojo, CityAdapter.ViewHolder>(CityDiffUtil()) {
//    private lateinit var binding: SearchCityItemBinding
//
//    class ViewHolder(var binding: SearchCityItemBinding) : RecyclerView.ViewHolder(binding.root)
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val inflater: LayoutInflater =
//            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        binding = SearchCityItemBinding.inflate(inflater, parent, false)
//        return ViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val currentCity: CityPojo = getItem(position)
//
//        Log.i("MySearch", " myTest-> $currentCity")
//        binding.cityName.text = currentCity.name
//        binding.cityDiscription.text = currentCity.country
//        binding.cityBtn.setOnClickListener {
//            listener.selectCity(currentCity)
//        }
//
//    }
//
//    class CityDiffUtil : DiffUtil.ItemCallback<CityPojo>() {
//        override fun areItemsTheSame(oldItem: CityPojo, newItem: CityPojo): Boolean {
//            return oldItem.lat == newItem.lat && oldItem.lon == newItem.lon
//        }
//
//        override fun areContentsTheSame(oldItem: CityPojo, newItem: CityPojo): Boolean {
//            return oldItem == newItem
//        }
//    }
//}