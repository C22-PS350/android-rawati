package com.bangkit.rawati.ui.activity

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.rawati.R
import com.bangkit.rawati.data.local.datastore.AccountPreferences
import com.bangkit.rawati.data.remote.response.FoodRequest
import com.bangkit.rawati.databinding.FragmentActivityBinding
import com.bangkit.rawati.helper.ApiCallbackString
import com.bangkit.rawati.ui.dashboard.ExerciseAdapter
import com.bangkit.rawati.ui.dashboard.FoodAdapter
import com.bangkit.rawati.ui.main.ViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import java.lang.Integer.parseInt
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
                val dialog = BottomSheetDialog(requireContext())
                val view = layoutInflater.inflate(R.layout.popup_form_food, null)
                val btnAdd = view.findViewById<Button>(R.id.form_food_add)
                val foodName = view.findViewById<TextInputEditText>(R.id.txt_food_name)
                val foodCalories = view.findViewById<TextInputEditText>(R.id.txt_food_calories)

                btnAdd.setOnClickListener {
                    val foodRequest = FoodRequest(
                        calories = parseInt(foodCalories.text.toString()),
                        name = foodName.text.toString()
                    )

                    viewModel!!.getUser().observe(requireActivity()) {
                        viewModel!!.createFoodActivity(
                            it.token,
                            it.user_id,
                            foodRequest,
                            object : ApiCallbackString {
                                override fun onResponse(state: Boolean, message: String) {
                                    processChange(state, message)
                                }
                            }) {
                            it?.name
                            it?.calories
                        }
                    }
                    dialog.dismiss()
                    // Get new activity
                    retrieveActivity()
                }
                dialog.setContentView(view)
                dialog.show()
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
            // Food
            var sumFoodCal = 0
            viewModel!!.getUser().observe(requireActivity()) {
                viewModel!!.getFoodActivity(
                    it.token,
                    it.user_id,
                    iso8601Date.format(viewModel!!.getDate())) {
                    if (it?.foodActivityData != null) {
                        val foodData = it?.foodActivityData
                        // Apply to recycler view
                        rvFoodList.apply {
                            layoutManager = LinearLayoutManager(activity)
                            adapter = FoodAdapter(foodData)
                        }
                        for (food in foodData) {
                            // Count the calories
                            sumFoodCal += food.calories
                        }
                        // Set the calories text
                        foodCalories.text = "$sumFoodCal"
                        netCalories.text = "$sumFoodCal"
                    }
                }
            }
            // Exercise
            var sumExerciseCal = 0
            viewModel!!.getUser().observe(requireActivity()) {
                viewModel!!.getExerciseActivity(
                    it.token,
                    it.user_id,
                    iso8601Date.format(viewModel!!.getDate())) {
                    if (it?.exerciseActivityData != null) {
                        val exerciseData = it?.exerciseActivityData
                        // Apply to recycler view
                        rvExerciseList.apply {
                            layoutManager = LinearLayoutManager(activity)
                            adapter = ExerciseAdapter(exerciseData)
                        }
                        for (exercise in exerciseData) {
                            // Count the calories
                            sumExerciseCal += exercise.calories
                        }
                        // Set the calories text
                        exerciseCalories.text = "$sumExerciseCal"
                        var sumNetCal = 0
                        netCalories.text = "$sumNetCal"
                    }
                }
            }
        }
    }

    /**
     * Change text based on date
     */
    private fun retrieveDate() {
        binding.apply {
            if (iso8601Date.format(viewModel!!.getDate()) == iso8601Date.format(viewModel!!.todayDate())) {
                btnDate.text = "Today"
                btnNext.visibility = View.INVISIBLE
            } else if (iso8601Date.format(viewModel!!.getDate()) == iso8601Date.format(viewModel!!.yesterdayDate())) {
                btnDate.text = "Yesterday"
                btnNext.visibility = View.VISIBLE
            } else {
                btnDate.text = activityDate.format(viewModel!!.getDate())
                btnNext.visibility = View.VISIBLE
            }
        }
    }

    private fun processChange(state: Boolean, message: String) {
        if (state) {
            // Show succes dialog
            val dialog = BottomSheetDialog(requireContext())
            val view = layoutInflater.inflate(R.layout.popup_info_reset, null)
            val closebtn = view.findViewById<Button>(R.id.btn_resetsuccess)
            val imgicon = view.findViewById<ImageView>(R.id.img_icon)
            val textinfo = view.findViewById<TextView>(R.id.txt_info)
            val textmessage = view.findViewById<TextView>(R.id.txt_message)

            imgicon.setImageResource(R.drawable.ic_check)
            textinfo.text = "Food added to activities"
            textmessage.text = ""
            closebtn.text = "OK"

            closebtn.setOnClickListener {
                dialog.dismiss()
            }
            dialog.setCancelable(false)
            dialog.setContentView(view)
            dialog.show()
        } else {
            val dialog = BottomSheetDialog(requireContext())
            val view = layoutInflater.inflate(R.layout.popup_info_reset, null)
            val closebtn = view.findViewById<Button>(R.id.btn_resetsuccess)
            val imgicon = view.findViewById<ImageView>(R.id.img_icon)
            val textinfo = view.findViewById<TextView>(R.id.txt_info)
            val textmessage = view.findViewById<TextView>(R.id.txt_message)

            imgicon.setImageResource(R.drawable.ic_error)
            textinfo.text = "Ooops Sorry..."
            textmessage.text = "There is an error while adding food to activities"
            closebtn.text = "OK"

            closebtn.setOnClickListener {
                dialog.dismiss()
            }
            dialog.setCancelable(false)
            dialog.setContentView(view)
            dialog.show()
        }
    }
}