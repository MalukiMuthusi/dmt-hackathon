package codes.malukimuthusi.hackathon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import codes.malukimuthusi.hackathon.data.Route
import codes.malukimuthusi.hackathon.databinding.FragmentNewSaccoBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
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
 * Use the [NewSaccoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewSaccoFragment : Fragment(), AdapterView.OnItemSelectedListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentNewSaccoBinding
    private lateinit var db: DatabaseReference
    private lateinit var arrayAdapter: ArrayAdapter<Route>
    private val routeList: MutableList<Route> = mutableListOf()

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
        db = Firebase.database.reference
        val routes = db.child("Routes")
        routes.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Timber.e(p0.toException())
            }

            override fun onDataChange(p0: DataSnapshot) {
                for (routeSnapshot in p0.children) {
                    val route = routeSnapshot.getValue<Route>()
                    route?.let {
                        routeList.add(it)
                        arrayAdapter.notifyDataSetChanged()
                    }
                }
            }
        })
        arrayAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, routeList)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerItem.adapter = arrayAdapter


        return binding.root
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
        TODO("Not yet implemented")
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (parent != null) {
            val item = parent.getItemAtPosition(position) as Route

        }
    }
}
