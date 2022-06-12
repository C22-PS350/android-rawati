package com.bangkit.rawati.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.rawati.R
import com.bangkit.rawati.data.remote.response.FoodActivityData

class FoodAdapter(val foodList: List<FoodActivityData>?) : RecyclerView.Adapter<FoodAdapter.ViewHolder>() {
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val foodName = itemView.findViewById<TextView>(R.id.item_food_name)
        val foodCalories = itemView.findViewById<TextView>(R.id.item_food_cals)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_food_row, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.foodName?.text = foodList?.get(position)?.name
        holder.foodCalories?.text = foodList?.get(position)?.calories.toString()
    }

    override fun getItemCount(): Int {
        return foodList?.size ?: 0
    }
}
