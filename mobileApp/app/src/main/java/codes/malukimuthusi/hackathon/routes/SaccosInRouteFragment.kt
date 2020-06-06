package codes.malukimuthusi.hackathon.routes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import codes.malukimuthusi.hackathon.adapters.SaccosForListAdapter
import codes.malukimuthusi.hackathon.databinding.FragmentSaccosInRouteBinding
import codes.malukimuthusi.hackathon.viewModels.SaccosInRouteViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SaccosInRouteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SaccosInRouteFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val viewModel: SaccosInRouteViewModel by viewModels()
    private lateinit var binding: FragmentSaccosInRouteBinding
    val arg by navArgs<SaccosInRouteFragmentArgs>()

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
        binding = FragmentSaccosInRouteBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        val id = arg.routeId
        viewModel.fetchSaccos(id.split(":").last())

        val adapter = SaccosForListAdapter()
        binding.recycler.adapter = adapter

        viewModel.saccoListLD.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
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
         * @return A new instance of fragment SaccosInRouteFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SaccosInRouteFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
