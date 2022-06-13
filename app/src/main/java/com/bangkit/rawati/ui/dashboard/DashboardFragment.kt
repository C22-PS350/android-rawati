package com.bangkit.rawati.ui.dashboard

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.rawati.data.local.datastore.AccountPreferences
import com.bangkit.rawati.databinding.FragmentDashboardBinding
import com.bangkit.rawati.ui.main.ViewModelFactory
import java.lang.Float.parseFloat
import java.lang.Integer.parseInt
import java.text.DateFormat
import java.text.SimpleDateFormat

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
class DashboardFragment : Fragment() {
    private var viewModel: DashboardViewModel? = null
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private val iso8601Date: DateFormat = SimpleDateFormat("yyyy-MM-dd")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        val pref = AccountPreferences.getInstance(requireContext().dataStore)
        viewModel = ViewModelProvider(this, ViewModelFactory(pref))[DashboardViewModel::class.java]

        viewModel!!.getUser().observe(viewLifecycleOwner) {
            viewModel!!.setDataUser(
                it.token,
                it.user_id) {
                if (it?.userResult != null) {
                    Toast.makeText(requireContext(), "DATA MASUK", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "DATA GAGAL MASUK", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Initialize value
        retrieveActivity()
        retrieveName()
        retrieveProfile()
        retrieveCalories()
        // retrieveRecommendation()

        return binding.root
    }

    /**
     * Retrieve name data
     */
    private fun retrieveName() {
        viewModel!!.getDataUser().observe(requireActivity()) {
            if (it != null) {
                binding.apply {
                    txtName.text = it.userResult!!.name
                }
            }
        }
    }

    private fun retrieveCalories() {
        viewModel!!.getUser().observe(requireActivity()) {
            viewModel!!.getCalories(
                it.token,
                it.user_id,
                iso8601Date.format(viewModel!!.getDate())
            ) {
                if (it?.caloriesData != null) {
                    val data = it?.caloriesData
                    var sumNetCal = data.food - data.exercise
                    binding.apply {
                        txtCalories.text = String.format("%.2f", sumNetCal)
                    }
                }
            }
        }
    }

    /**
     * Retrieve profile data
     */
    private fun retrieveProfile() {
        viewModel!!.getUser().observe(requireActivity()) {
            viewModel!!.getProfilUser(
                it.token,
                it.user_id) {
                if (it?.userProfileResult != null) {
                    val profileData = it?.userProfileResult
                    var currWeight = if (parseInt(profileData.weight) != 0) "${profileData.weight} Kg" else "-"
                    var goalWeight = if (parseInt(profileData.weight_goal) != 0) "${profileData.weight_goal} Kg" else "-"
                    var height = parseFloat(profileData.height) / 100.0F
                    var gender = profileData.gender
                    var age = profileData.birth_date

                    var currBMI = "-"
                    var goalBMI = "-"
                    if (parseInt(profileData.height) != 0) {
                        if (currWeight != "-") {
                            Log.d("a", height.toString())
                            var currBMIVal = 0.0F
                            currBMIVal = parseFloat(profileData.weight) / (height * height)
                            currBMI = String.format("%.2f", currBMIVal)
                        }
                        if (goalWeight != "-") {
                            var goalBMIVal = 0.0F
                            goalBMIVal = parseFloat(profileData.weight_goal) / (height * height)
                            goalBMI = String.format("%.2f", goalBMIVal)
                        }
                    }

                    binding.apply {
                        txtWeight.text = goalWeight
                        txtBmi.text = goalBMI

                        txtCurrent.text = currWeight
                        txtCurrentBmi.text = currBMI
                    }
                }
            }
        }
    }

    /**
     * Retrieve activity data
     */
    private fun retrieveActivity() {
        binding.apply {
            // Food
            viewModel!!.getUser().observe(requireActivity()) {
                viewModel!!.getFoodActivity(
                    it.token,
                    it.user_id,
                    iso8601Date.format(viewModel!!.getDate())) {
                    if (it?.foodActivityData != null) {
                        val foodData = it?.foodActivityData
                        // Apply to recycler view
                        rvFood.apply {
                            layoutManager = LinearLayoutManager(activity)
                            adapter = FoodAdapter(foodData)
                        }
                    }
                }
            }

            // Exercise
            viewModel!!.getUser().observe(requireActivity()) {
                viewModel!!.getExerciseActivity(
                    it.token,
                    it.user_id,
                    iso8601Date.format(viewModel!!.getDate())) {
                    if (it?.exerciseActivityData != null) {
                        val exerciseData = it?.exerciseActivityData
                        // Apply to recycler view
                        rvExercise.apply {
                            layoutManager = LinearLayoutManager(activity)
                            adapter = ExerciseAdapter(exerciseData)
                        }
                    }
                }
            }
        }
    }
}