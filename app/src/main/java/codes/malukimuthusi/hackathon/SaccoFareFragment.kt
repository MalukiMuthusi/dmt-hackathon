package codes.malukimuthusi.hackathon

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import codes.malukimuthusi.hackathon.data.SaccoDetailAdapter
import codes.malukimuthusi.hackathon.data.saccosList
import codes.malukimuthusi.hackathon.databinding.FragmentSaccoFareBinding

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
        val binding = FragmentSaccoFareBinding.inflate(inflater)

        val items = listOf("CBD", "Kitengela", "Lang'ata", "Kasarani", "Roysambu")
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
        binding.fromText.setAdapter(arrayAdapter)
        binding.toText.setAdapter(arrayAdapter)

        val recyclerViewAdapter = SaccoDetailAdapter()
        binding.recyclerView.adapter = recyclerViewAdapter

        recyclerViewAdapter.submitList(saccosList)

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
