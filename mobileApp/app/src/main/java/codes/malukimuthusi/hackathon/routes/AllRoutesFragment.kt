package codes.malukimuthusi.hackathon.routes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import codes.malukimuthusi.hackathon.adapters.AllRoutesListAdapter
import codes.malukimuthusi.hackathon.adapters.RouteClickListener
import codes.malukimuthusi.hackathon.databinding.FragmentAllRoutesBinding
import codes.malukimuthusi.hackathon.viewModels.AllRoutesViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AllRoutesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AllRoutesFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentAllRoutesBinding
    private val viewModel: AllRoutesViewModel by viewModels()


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
        binding = FragmentAllRoutesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        val adapter = AllRoutesListAdapter(RouteClickListener {
            val action =
                AllRoutesFragmentDirections.actionAllRoutesFragmentToSaccosInRouteFragment(it)
            findNavController().navigate(action)
        })
        binding.recycler.setHasFixedSize(true)
        binding.recycler.adapter = adapter
        viewModel.routesLD.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        viewModel.fetchAllRoutes()

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                binding.errorMessage.visibility = View.VISIBLE
            } else {
                binding.errorMessage.visibility = View.GONE
            }
        })

        viewModel.loadingState.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                binding.progressBar3.visibility = View.VISIBLE
            } else {
                binding.progressBar3.visibility = View.GONE
            }
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
         * @return A new instance of fragment AllRoutesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AllRoutesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
