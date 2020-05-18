package codes.malukimuthusi.hackathon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import codes.malukimuthusi.hackathon.data.FareChartAdapter
import codes.malukimuthusi.hackathon.data.FareChartListener
import codes.malukimuthusi.hackathon.data.Repository
import codes.malukimuthusi.hackathon.data.Route
import codes.malukimuthusi.hackathon.databinding.FragmentHomeBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var signedin: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentHomeBinding.inflate(inflater)
        binding.lifecycleOwner = this


        val sharedModel by activityViewModels<SharedHomeViewModel>()

        val viewModel by viewModels<HomeFragmentViewModel>()
        Repository.getRoutes(viewModel.listenerObject)


        binding.button.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToSaccoFareFragment(null)
            )
            sharedModel.sharedChartData = null
        }

        val adapter = FareChartAdapter(
            FareChartListener {
//                sharedModel.sharedChartData = it
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToSaccoFareFragment(
                        it
                    )
                )

            }
        )
        binding.recyclerView.adapter = adapter


//        adapter.submitList(viewModel.updatedRoutes)


        viewModel.updateUI.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it as List<Route>)
        })

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
