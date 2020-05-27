package codes.malukimuthusi.hackathon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import codes.malukimuthusi.hackathon.data.Fare
import codes.malukimuthusi.hackathon.data.Fare.Companion.createFareWithSingleValue
import codes.malukimuthusi.hackathon.data.Route
import codes.malukimuthusi.hackathon.data.Sacco
import codes.malukimuthusi.hackathon.databinding.FragmentNewSaccoBinding
import codes.malukimuthusi.hackathon.viewModels.NewSaccoFragmentViewModel
import codes.malukimuthusi.hackathon.viewModels.NewSaccoFragmentViewModel.Companion.pickHoursList
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import timber.log.Timber

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [NewSaccoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewSaccoFragment : Fragment(), AdapterView.OnItemSelectedListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentNewSaccoBinding
    private lateinit var arrayAdapter: ArrayAdapter<Route>
    private var selectedRoute: Route? = null
    private var saccoName: String? = null

    private lateinit var fare: Fare
    private val viewModel by viewModels<NewSaccoFragmentViewModel>()

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
        binding = FragmentNewSaccoBinding.inflate(inflater, container, false)

        binding.spinnerItem.onItemSelectedListener = this


        // spinner
        val routes = viewModel.db.child("Routes")
        routes.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Timber.e(p0.toException())
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (routeSnapshot in p0.children) {
                    val route = routeSnapshot.getValue<Route>()
                    val keyy = routeSnapshot.key
                    route?.let {
                        route.key = keyy!!
                        viewModel.routeList.add(it)
                        arrayAdapter.notifyDataSetChanged()
                    }
                }
            }
        })
        arrayAdapter =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                viewModel.routeList
            )
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerItem.adapter = arrayAdapter

        binding.floatingActionButton.setOnClickListener {
            createObjects(validateInputs())
        }

        return binding.root
    }

    fun collectInputs(): Boolean {
        binding.fareLowPickHours.editText?.text.apply {
            if (isNullOrEmpty()) {
                binding.fareLowPickHours.error = "Enter Fare for Low Pick Hours"
                return false
            } else {
                viewModel.fareLowHours = this.toString().toInt()
            }
        }

        binding.farePickHours.editText?.text.apply {
            if (isNullOrEmpty()) {
                binding.farePickHours.error = "Enter Fare for Pick Hours"
                return false
            } else {
                viewModel.farePickHours = this.toString().toInt()
            }
        }

        binding.saccoName.editText?.text.apply {
            if (isNullOrEmpty()) {
                binding.saccoName.error = "Enter Sacco Name"
                return false
            } else {
                saccoName = this.toString()
            }
        }
        return true
    }

    private fun validateInputs(): Boolean {
        if (selectedRoute == null) {
            Snackbar.make(binding.farePickHours, R.string.select_route, Snackbar.LENGTH_SHORT)
                .show()
            return false
        }
        return collectInputs()

    }

    private fun createObjects(validated: Boolean) {
        disablePosting()
        if (validated == true) {
            fare = createFareWithSingleValue(viewModel.fareLowHours!!)
            pickHoursList.forEach {
                when (it) {
                    "fiveToSeven" -> {
                        fare.five_six = viewModel.farePickHours
                        fare.six_seven = viewModel.farePickHours
                    }
                    "sevenToNine" -> {
                        fare.seven_eight = viewModel.farePickHours
                        fare.eight_nine = viewModel.farePickHours
                    }
                    "nineTo11" -> {
                        fare.nine_ten = viewModel.farePickHours
                        fare.ten_eleven = viewModel.farePickHours
                    }
                    "elevenTo2" -> {
                        fare.eleven_twelve = viewModel.farePickHours
                        fare.twelve_thirteen = viewModel.farePickHours
                        fare.thirteen_fourteen = viewModel.farePickHours
                    }
                    "twoTo4" -> {
                        fare.fourteen_fiveteen = viewModel.farePickHours
                        fare.fiveteen_sixteen = viewModel.farePickHours
                    }
                    "fourToSix" -> {
                        fare.sixteen_seventeen = viewModel.farePickHours
                        fare.seventeen_eighteen = viewModel.farePickHours
                    }
                    "sixTo9" -> {
                        fare.eighteen_nineteen = viewModel.farePickHours
                        fare.nineteen_twenty = viewModel.farePickHours
                        fare.twenty_twentyOne = viewModel.farePickHours
                    }
                    "nine12" -> {
                        fare.twentyone_twentytwo = viewModel.farePickHours
                        fare.twentytwo_twentythree = viewModel.farePickHours
                    }
                }
            }
            val sacco = Sacco(saccoName, fare, mutableMapOf(selectedRoute!!.key to true))
            viewModel.createSacco(sacco, selectedRoute!!.key)
            Timber.d("Finishing activity")
            activity?.finish()
            return
        } else {
            enablePosting()
            return
        }

    }

    private fun enablePosting() {
        binding.floatingActionButton.show()
    }

    private fun disablePosting() {
        binding.floatingActionButton.hide()
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment NewSaccoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NewSaccoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (parent != null) {
            selectedRoute = parent.getItemAtPosition(position) as Route

        }
    }
}
