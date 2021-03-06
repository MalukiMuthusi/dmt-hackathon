package codes.malukimuthusi.hackathon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import codes.malukimuthusi.hackathon.adapters.SaccoDetailViewHolder
import codes.malukimuthusi.hackathon.dataModel.Sacco
import codes.malukimuthusi.hackathon.databinding.FragmentSaccoFareBinding
import codes.malukimuthusi.hackathon.viewModels.SaccoFragmentViewModel
import codes.malukimuthusi.hackathon.viewModels.SharedHomeViewModel
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import timber.log.Timber

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SaccoFareFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SaccoFareFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var db: DatabaseReference
    private lateinit var binding: FragmentSaccoFareBinding
    private val sharedViewModel by activityViewModels<SharedHomeViewModel>()
    val viewModel by viewModels<SaccoFragmentViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
            param1 = it?.getString(ARG_PARAM1)
            param2 = it?.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSaccoFareBinding.inflate(inflater, container, false)

        // bundle that contains the passed arguments.
        val args: SaccoFareFragmentArgs by navArgs()

        // Autocomplete list adapter.


        binding.lifecycleOwner = this

        // retrieve the passed routeID. this routeID is used to retrieve Sacco's that operate
        // in that route
        args.routeID?.let {

            db = Firebase.database.reference
            val keyQuery = db.child("Routes").child(it).child("saccos")
            val dataRef = db.child("saccos")
            // firebase
            val options = FirebaseRecyclerOptions.Builder<Sacco>()
                .setLifecycleOwner(this)
                .setIndexedQuery(keyQuery, dataRef, Sacco::class.java)
                .build()

            val adapter = object : FirebaseRecyclerAdapter<Sacco, SaccoDetailViewHolder>(options) {
                override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int
                ): SaccoDetailViewHolder {
                    return SaccoDetailViewHolder.from(parent)
                }

                override fun onError(error: DatabaseError) {
                    super.onError(error)
                    Timber.e(error.toException(), "Recycler viewError")
                }

                override fun onBindViewHolder(
                    holder: SaccoDetailViewHolder,
                    position: Int,
                    model: Sacco
                ) {
                    return holder.bind(getItem(position))
                }
            }
            binding.recyclerView.adapter = adapter
            binding.routeNameText.text = sharedViewModel.sharedChartData.name
        }


        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SaccoFareFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SaccoFareFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
