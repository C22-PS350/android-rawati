package com.bangkit.rawati.ui.dashboard

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bangkit.rawati.data.local.datastore.AccountPreferences
import com.bangkit.rawati.databinding.FragmentDashboardBinding
import com.bangkit.rawati.ui.main.ViewModelFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings")
class DashboardFragment : Fragment() {
    private var viewModel: DashboardViewModel? = null
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)

        val pref = AccountPreferences.getInstance(requireContext().dataStore)
        viewModel = ViewModelProvider(this, ViewModelFactory(pref))[DashboardViewModel::class.java]

        viewModel!!.getUser().observe(requireActivity()) {
            viewModel!!.setDataUser(
                it.token,
                it.user_id) {
                if (it?.userResult != null) {
                    Toast.makeText(context, "DATA MASUK", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "DATA GAGAL MASUK", Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel!!.getDataUser().observe(requireActivity(), {
            if (it != null) {
                binding.apply {
                    txtName.text = it.userResult!!.name
                }
            }
        })




        return binding.root

    }
}