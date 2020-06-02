package codes.malukimuthusi.hackathon.startPoint

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import codes.malukimuthusi.hackathon.adapters.ItineraryClickListener
import codes.malukimuthusi.hackathon.adapters.SearchResultsAdapter
import codes.malukimuthusi.hackathon.databinding.SearchResultsFragmentBinding
import codes.malukimuthusi.hackathon.viewModels.DirectionsFragmentViewModel
import codes.malukimuthusi.hackathon.viewModels.SearchResultsViewModel


class SearchResultsFragment : Fragment() {

    companion object {
        fun newInstance() =
            SearchResultsFragment()
    }

    private val viewModel: SearchResultsViewModel by viewModels()
    private lateinit var binding: SearchResultsFragmentBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val directionsViewModel: DirectionsFragmentViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SearchResultsFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        val navController = findNavController()
        appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolBar.setupWithNavController(navController, appBarConfiguration)

        val adapter = SearchResultsAdapter(ItineraryClickListener { clickedItinerary ->
            sharedViewModel.selectedItinerary = clickedItinerary
            val action =
                SearchResultsFragmentDirections.actionSearchResultsFragmentToDirectionsFragment()
            findNavController().navigate(action)
        })
        binding.recyclerView.adapter = adapter
        adapter.submitList(sharedViewModel.tripPlan.itineraries)

        return binding.root
    }

}
