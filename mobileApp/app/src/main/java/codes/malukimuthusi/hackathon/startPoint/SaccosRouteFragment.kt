package codes.malukimuthusi.hackathon.startPoint

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import codes.malukimuthusi.hackathon.adapters.BusStopListAdapter
import codes.malukimuthusi.hackathon.adapters.SaccoListAdapter
import codes.malukimuthusi.hackathon.databinding.FragmentSaccosRouteBinding
import codes.malukimuthusi.hackathon.webService.Place

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SaccosRouteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SaccosRouteFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentSaccosRouteBinding
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val saccoListAdapter = SaccoListAdapter()
    private val busStopListAdapter = BusStopListAdapter()
    private val dummyPlace = Place()

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
        binding = FragmentSaccosRouteBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        // fetch saccos operating on the provided leg, route
        sharedViewModel.fetchSaccos(sharedViewModel.leg.routeId!!.split(":").last())

        binding.saccosList.adapter = saccoListAdapter
        binding.saccosList.setHasFixedSize(true)

        binding.busStopList.adapter = busStopListAdapter
        sharedViewModel.saccoListLD.observe(viewLifecycleOwner, Observer {
            saccoListAdapter.submitList(it)
        })

        binding.busStopList.setHasFixedSize(true)
        sharedViewModel.stopsLD.observe(viewLifecycleOwner, Observer {
            val stop = it as MutableList<Place>
            if (!stop.contains(dummyPlace)) {
                stop.add(0, dummyPlace)
            }
            busStopListAdapter.submitList(stop)
        })


        // return root view
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SaccosRouteFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SaccosRouteFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
