package com.kisusyenni.storyapp.view.main.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.kisusyenni.storyapp.R
import com.kisusyenni.storyapp.databinding.FragmentSettingsBinding
import com.kisusyenni.storyapp.view.welcome.WelcomeActivity
import com.kisusyenni.storyapp.viewmodel.DatastoreViewModel
import com.kisusyenni.storyapp.viewmodel.ViewModelFactory

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private fun setViewModel() {
        val factory = ViewModelFactory.getInstance(requireActivity())

        val datastoreViewModel = ViewModelProvider(this, factory)[DatastoreViewModel::class.java]
        datastoreViewModel.session.observe(viewLifecycleOwner) { session ->
            if (!session.isLogin) Intent(activity, WelcomeActivity::class.java).also { intent->
                Toast.makeText(activity, resources.getString(R.string.logout_success), Toast.LENGTH_LONG).show()
                startActivity(intent)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                activity?.finish()
            }
        }

        binding.logOutAction.setOnClickListener {
            datastoreViewModel.removeSession()
            Toast.makeText(
                activity,
                resources.getString(R.string.logout_success),
                Toast.LENGTH_LONG
            ).show()
        }

        datastoreViewModel.getTheme()
        datastoreViewModel.darkMode.observe(viewLifecycleOwner
        ) { isDarkMode: Boolean ->
            if (isDarkMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.switchTheme.isChecked = false
            }
        }

        binding.switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            datastoreViewModel.saveTheme(isChecked)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity != null) {
            setViewModel()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}