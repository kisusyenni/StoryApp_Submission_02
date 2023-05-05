package com.kisusyenni.storyapp.view.main.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kisusyenni.storyapp.databinding.FragmentHomeBinding
import com.kisusyenni.storyapp.view.addstory.AddStoryActivity
import com.kisusyenni.storyapp.viewmodel.DatastoreViewModel
import com.kisusyenni.storyapp.viewmodel.ViewModelFactory

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private fun showLoading(loading: Boolean) {
        binding.pbHome.visibility = if (loading) View.VISIBLE else View.GONE
    }

    private fun setPagingList() {
        val factory = ViewModelFactory.getInstance(requireActivity())
        val homeViewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]

        val datastoreViewModel = ViewModelProvider(this, factory)[DatastoreViewModel::class.java]

        val homeAdapter = HomeAdapter()
        binding.rvStoryList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = homeAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    homeAdapter.retry()
                }
            )
        }

        // get token
        datastoreViewModel.fetchSession()
        datastoreViewModel.session.observe(viewLifecycleOwner) { session ->
            homeViewModel.getStories(session.token).observe(viewLifecycleOwner) { stories ->
                if(stories != null) {
                    showLoading(false)
                    homeAdapter.submitData(lifecycle, stories)
                }
            }
        }
    }

    private fun setIntentAddButton () {
        binding.intentAddBtn.setOnClickListener {
            startActivity( Intent(activity, AddStoryActivity::class.java))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading(true)

        if(activity !== null) {
            setPagingList()
            setIntentAddButton()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}