package ama.ripe.search.presentation

import ama.ripe.search.R
import ama.ripe.search.databinding.FragmentInetNumBinding
import ama.ripe.search.presentation.adapters.InetNumAdapter
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

class InetNumFragment : Fragment() {

    private var _binding: FragmentInetNumBinding? = null
    private lateinit var viewModel: InetNumViewModel
    private val component by lazy {
        (requireActivity().application as MyApp).component
    }
    private var orgName: String = EMPTY_STRING
    private val binding get() = _binding ?: throw RuntimeException("FragmentInetNumBinding == null")
    private lateinit var adapter: InetNumAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentInetNumBinding.inflate(inflater, container, false)
        return binding.root

    }

    private fun copyTextToClipboard(textToCopy: String) {
        val clipboardManager =
            requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText(TEXT_STRING, textToCopy)
        clipboardManager.setPrimaryClip(clipData)
        Toast.makeText(
            requireContext(),
            getString(R.string.frgmnt_inetnum_copy_text), Toast.LENGTH_LONG
        ).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_inet_num, menu)
        val mSearchMenuItem = menu.findItem(R.id.action_search)
        val searchView = mSearchMenuItem.actionView as SearchView
        search(searchView)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun search(searchView: SearchView) {

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {

                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {

                (binding.rvInetNumList.adapter as InetNumAdapter).filter(newText)
                return true
            }
        })
    }

    private fun setAdapter() {
        adapter = InetNumAdapter()
        binding.rvInetNumList.adapter = adapter
        binding.rvInetNumList.itemAnimator = null
    }

    private fun setObserverState() {
        viewModel.stateInetNum.observe(viewLifecycleOwner) {
            if (viewLifecycleOwner.lifecycle.currentState != Lifecycle.State.RESUMED) {
                return@observe
            }
            when (it) {
                is StateLoading.Initial -> {
                    binding.progressBarLoading.isVisible = false
                }

                is StateLoading.Loading -> {
                    binding.progressBarLoading.isVisible = true
                }

                is StateLoading.Content -> {
                    binding.progressBarLoading.isVisible = false
                    adapter.modifyList(it.currencyList)
                    //adapter.submitList(it.currencyList)
                }

                is StateLoading.ContentError -> {
                    binding.progressBarLoading.isVisible = false
                    binding.llEmptyList.isVisible = true
                    binding.tvInetNumNoData.text = it.er
                    // adapter.modifyList(listOf())
                }
            }
        }
    }

    private fun setAdapterClick() {
        adapter.onInetNumClickListener = object : InetNumAdapter.OnInetNumClickListener {
            override fun onInetNumClick(tInfo: String) {
                copyTextToClipboard(tInfo)
            }
        }
    }

    private fun setSubTitleActionBar(text: String) {
        (requireActivity() as AppCompatActivity).supportActionBar?.subtitle = text
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel = ViewModelProvider(this, viewModelFactory)[InetNumViewModel::class.java]
        setAdapter()
        setSubTitleActionBar(getString(R.string.frgmnt_inetnum_subtitle))
        viewModel.loadData(orgName)
        setObserverState()
        setAdapterClick()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun parseArgs() {
        val args = requireArguments()
        if (!args.containsKey(ARG_INET_NUM)) {
            throw RuntimeException("$this must contain argument $ARG_INET_NUM")
        }
        args.getString(ARG_INET_NUM)?.let {
            orgName = it
        }
    }

    companion object {
        const val NAME = "InetNumFragment"
        const val EMPTY_STRING = ""
        const val TEXT_STRING = "text"
        const val ARG_INET_NUM = "ARG_INET_NUM"

        fun newInstance(string: String): InetNumFragment {
            return InetNumFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_INET_NUM, string)
                }
            }
        }
    }
}
