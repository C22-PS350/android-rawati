package com.bangkit.rawati.ui.recommendation

import android.content.Context
import android.os.Bundle
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
import com.bangkit.rawati.R
import com.bangkit.rawati.data.local.datastore.AccountPreferences
import com.bangkit.rawati.data.remote.response.RecommendationRequest
import com.bangkit.rawati.databinding.FragmentRecommendationBinding
import com.bangkit.rawati.ui.main.ViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import java.lang.Integer.parseInt

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
class RecommendationFragment : Fragment() {
    private var viewModel: RecommendationViewModel? = null
    private var _binding: FragmentRecommendationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecommendationBinding.inflate(inflater, container, false)

        val pref = AccountPreferences.getInstance(requireContext().dataStore)
        viewModel = ViewModelProvider(this, ViewModelFactory(pref))[RecommendationViewModel::class.java]

        binding.apply {
            // Add button
            llFood.setOnClickListener {
                val dialog = BottomSheetDialog(requireContext())
                val view = layoutInflater.inflate(R.layout.popup_one_field, null)
                val btnAdd = view.findViewById<Button>(R.id.one_field_submit)
                val dialogTitle = view.findViewById<TextView>(R.id.one_field_title)
                val dialogDescription = view.findViewById<TextView>(R.id.one_field_description)
                val caloriesInput = view.findViewById<TextInputEditText>(R.id.one_field_text)

                dialogTitle.text = "Food Recommendation"
                dialogDescription.text = "Enter your desired calories to get your food recommendation"
                btnAdd.setOnClickListener {
                    val recommendationRequest = RecommendationRequest(
                        calories = parseInt(caloriesInput.text.toString())
                    )
                    viewModel!!.getUser().observe(requireActivity()) {
                        viewModel!!.getFoodRecommendations(
                            it.token,
                            recommendationRequest,
                            ) {
                            if (it == null) {
                                showError()
                            } else {
                                // Recommendations handling
                                val foodList = it.foodRecommendation
                                if (foodList != null) {
                                    // TODO(Astrada): Save to shared preferences
                                }
                            }
                        }
                    }
                    dialog.dismiss()
                }
                dialog.setContentView(view)
                dialog.show()
            }

            llExercise.setOnClickListener {
                val dialog = BottomSheetDialog(requireContext())
                val view = layoutInflater.inflate(R.layout.popup_one_field, null)
                val btnAdd = view.findViewById<Button>(R.id.one_field_submit)
                val dialogTitle = view.findViewById<TextView>(R.id.one_field_title)
                val dialogDescription = view.findViewById<TextView>(R.id.one_field_description)
                val caloriesInput = view.findViewById<TextInputEditText>(R.id.one_field_text)

                dialogTitle.text = "Exercise Recommendation"
                dialogDescription.text = "Enter your desired calories to get your exercise recommendation"
                btnAdd.setOnClickListener {
                    val recommendationRequest = RecommendationRequest(
                        calories = parseInt(caloriesInput.text.toString())
                    )

                    viewModel!!.getUser().observe(requireActivity()) {
                        viewModel!!.getExerciseRecommendations(
                            it.token,
                            recommendationRequest,
                        ) {
                            if (it == null) {
                                showError()
                            } else {
                                // Recommendations handling
                                val exerciseList = it.exerciseRecommendation
                                if (exerciseList != null) {
                                    // TODO(Astrada): Save to shared preferences
                                }
                            }
                        }
                    }
                    dialog.dismiss()
                }
                dialog.setContentView(view)
                dialog.show()
            }
        }
        return binding.root
    }

    private fun showError() {
        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.popup_info_reset, null)
        val closebtn = view.findViewById<Button>(R.id.btn_resetsuccess)
        val imgicon = view.findViewById<ImageView>(R.id.img_icon)
        val textinfo = view.findViewById<TextView>(R.id.txt_info)
        val textmessage = view.findViewById<TextView>(R.id.txt_message)

        imgicon.setImageResource(R.drawable.ic_error)
        textinfo.text = "Ooops Sorry..."
        textmessage.text = "There is an error while getting recommendations"
        closebtn.text = "OK"

        closebtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setCancelable(false)
        dialog.setContentView(view)
        dialog.show()
    }
}