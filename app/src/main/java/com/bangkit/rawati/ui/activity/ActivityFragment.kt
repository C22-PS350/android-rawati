package com.bangkit.rawati.ui.activity

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bangkit.rawati.data.local.datastore.AccountPreferences
import com.bangkit.rawati.databinding.FragmentActivityBinding
import com.bangkit.rawati.helper.ApiCallbackString
import com.bangkit.rawati.ui.main.ViewModelFactory
import java.text.DateFormat
import java.text.SimpleDateFormat

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
class ActivityFragment : Fragment() {
    private var viewModel: ActivityViewModel? = null
    private var _binding: FragmentActivityBinding? = null
    private val binding get() = _binding!!
    private val activityDate: DateFormat = SimpleDateFormat("EEE, d MMM yyyy")
    private val iso8601Date: DateFormat = SimpleDateFormat("yyyy-MM-dd")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentActivityBinding.inflate(inflater, container, false)

        val pref = AccountPreferences.getInstance(requireContext().dataStore)
        viewModel = ViewModelProvider(this, ViewModelFactory(pref))[ActivityViewModel::class.java]

        // Initialize value
        retrieveDate()
        retrieveActivity()

        binding.apply {
            // Date button
            btnDate.setOnClickListener {

            }
            btnPrev.setOnClickListener {
                Log.d("Previous date", viewModel!!.getDate().toString())
                viewModel!!.prevDate()
                Log.d("Previous date", viewModel!!.getDate().toString())
                retrieveDate()
                retrieveActivity()
            }
            btnNext.setOnClickListener {
                Log.d("Previous date", viewModel!!.getDate().toString())
                viewModel!!.nextDate()
                Log.d("Previous date", viewModel!!.getDate().toString())
                retrieveDate()
                retrieveActivity()
            }

            // Add button
            btnAddFood.setOnClickListener {

            }
            btnAddExercise.setOnClickListener {

            }
        }
        return binding.root
    }

    /**
     * Retrieve activity data
     */
    private fun retrieveActivity() {
        binding.apply {

            // Exercise
            var sumExerciseCal = 0
            viewModel!!.getUser().observe(requireActivity()) {
                viewModel!!.setExerciseActivity(
                    it.token,
                    it.user_id,
                    iso8601Date.format(viewModel!!.getDate())) {
                    if (it?.exerciseActivityData != null) {
                        val exerciseData = it?.exerciseActivityData
                        for (exercise in exerciseData) {
                            // Apply to recycler view

                            // Count the calories
                            sumExerciseCal += exercise.calories
                        }
                        // Set the calories text
                        exerciseCalories.text = "$sumExerciseCal Cal"
                    }
                }
            }

            // Food
            var sumFoodCal = 0
            viewModel!!.getUser().observe(requireActivity()) {
                viewModel!!.setFoodActivity(
                    it.token,
                    it.user_id,
                    iso8601Date.format(viewModel!!.getDate())) {
                    if (it?.foodActivityData != null) {
                        val foodData = it?.foodActivityData
                        for (food in foodData) {
                            // Apply to recycler view

                            // Count the calories
                            sumFoodCal += food.calories
                        }
                        // Set the calories text
                        foodCalories.text = "$sumFoodCal Cal"
                    }
                }
            }
            var sumNetCal = sumFoodCal + sumExerciseCal
            netCalories.text = "$sumNetCal Cal"
        }
    }

    /**
     * Change text based on date
     */
    private fun retrieveDate() {
        binding.apply {
            if (iso8601Date.format(viewModel!!.getDate()) == iso8601Date.format(viewModel!!.todayDate())) {
                btnDate.text = "Today"
            } else if (iso8601Date.format(viewModel!!.getDate()) == iso8601Date.format(viewModel!!.yesterdayDate())) {
                btnDate.text = "Yesterday"
            } else {
                btnDate.text = activityDate.format(viewModel!!.getDate())
            }
        }
    }
}