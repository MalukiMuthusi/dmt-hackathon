package codes.malukimuthusi.hackathon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import codes.malukimuthusi.hackathon.data.ChartViewHolder
import codes.malukimuthusi.hackathon.data.FareChartListener
import codes.malukimuthusi.hackathon.data.Route
import codes.malukimuthusi.hackathon.databinding.FragmentHomeBinding
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.firebase.ui.database.SnapshotParser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import timber.log.Timber

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
    private lateinit var binding: FragmentHomeBinding
    private lateinit var db: DatabaseReference
    private val sharedModel by activityViewModels<SharedHomeViewModel>()

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
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this





        binding.chooseRoute.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToSaccoFareFragment(null)
            )
        }

        // firebaseUI adapter
        db = Firebase.database.getReference("Routes")
        val query = db

        // custom Snapshot parser
        val snapshotParser = object : SnapshotParser<Route> {
            override fun parseSnapshot(snapshot: DataSnapshot): Route {
                val route = snapshot.getValue<Route>()!!
                route.key = snapshot.key!!
                return route
            }
        }

        // firebase recycler options
        val options = FirebaseRecyclerOptions.Builder<Route>()
            .setQuery(query, snapshotParser)
            .setLifecycleOwner(this)
            .build()

        // navigate to saccos when a route is clicked
        val navigateToRoute = FareChartListener {
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToSaccoFareFragment(it.key)
            )
            sharedModel.sharedChartData = it
        }

        // recycler adapter
        val adapter = object : FirebaseRecyclerAdapter<Route, ChartViewHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChartViewHolder {
                return ChartViewHolder.from(parent)
            }

            override fun onError(error: DatabaseError) {
                super.onError(error)
                Timber.e(error.toException(), "RecyclerView Error")
            }

            override fun onBindViewHolder(holder: ChartViewHolder, position: Int, model: Route) {
                holder.bind(getItem(position), navigateToRoute)
            }

        }
        binding.recyclerView.adapter = adapter


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
