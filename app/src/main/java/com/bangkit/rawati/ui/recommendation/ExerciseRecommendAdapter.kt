package com.bangkit.rawati.ui.recommendation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.rawati.R
import com.bangkit.rawati.data.remote.response.ExerciseRecommendationData

class ExerciseRecommendAdapter(val exerciseList: List<ExerciseRecommendationData>?) : RecyclerView.Adapter<ExerciseRecommendAdapter.ViewHolder>() {
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val exerciseName = itemView.findViewById<TextView>(R.id.item_recommendation_name)
        val exerciseCalories = itemView.findViewById<TextView>(R.id.item_recommendation_cals)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_recommendation_row, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.exerciseName?.text = exerciseList?.get(position)?.name
        holder.exerciseCalories?.text = "("+exerciseList?.get(position)?.calories.toString()+")"
    }

    override fun getItemCount(): Int {
        return exerciseList?.size ?: 0
    }
}