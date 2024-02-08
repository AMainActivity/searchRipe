package ama.ripe.search.presentation

import ama.ripe.search.R
import ama.ripe.search.databinding.FragmentOrganizationBinding
import ama.ripe.search.presentation.adapters.OrganizationAdapter
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject


class OrganizationFragment : Fragment() {

    private lateinit var viewModel: OrganizationViewModel
    private var _binding: FragmentOrganizationBinding? = null
    private lateinit var adapter: OrganizationAdapter
    private val component by lazy {
        (requireActivity().application as MyApp).component
    }

    private val binding
        get() = _binding ?: throw RuntimeException("FragmentOrganizationBinding == null")

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_org, menu)
        val mSearchMenuItem = menu.findItem(R.id.action_search_org)
        val searchView = mSearchMenuItem.actionView as SearchView
        search(searchView)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun search(searchView: SearchView) {

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.loadData(query)
                setSubTitleActionBar(query)
                searchView.onActionViewCollapsed()
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return true
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentOrganizationBinding.inflate(inflater, container, false)
        return binding.root

    }

    private fun setAdapter() {
        adapter = OrganizationAdapter()
        binding.rvOrgList.adapter = adapter
        binding.rvOrgList.itemAnimator = null
    }

    private fun setObserverState() {
        viewModel.stateOrganization.observe(viewLifecycleOwner) {
            when (it) {
                is StateLoading.Initial -> {
                    binding.progressBarLoading.isVisible = false
                }

                is StateLoading.Loading -> {
                    binding.progressBarLoading.isVisible = true
                }

                is StateLoading.Content -> {
                    binding.progressBarLoading.isVisible = false
                    adapter.submitList(it.currencyList)
                }

                is StateLoading.ContentError -> {
                    Toast.makeText(requireContext(), it.er, Toast.LENGTH_SHORT).show()
                    binding.progressBarLoading.isVisible = false
                }
            }
        }
    }

    private fun setAdapterClick() {
        adapter.onOrganizationClickListener =
            object : OrganizationAdapter.OnOrganizationClickListener {
                override fun onOrganizationClick(tInfo: String) {
                    launchSecondFragment(tInfo)
                }
            }
        adapter.onButtonInfoClickListener = object :
            OrganizationAdapter.OnButtonInfoClickListener {
            override fun onButtonInfoClick(info: String) {
                orgInfoAlertDialog(info)
            }
        }
    }

    private fun setSubTitleActionBar(text: String) {
        (requireActivity() as AppCompatActivity).supportActionBar?.subtitle = text
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, viewModelFactory)[OrganizationViewModel::class.java]
        setSubTitleActionBar(getString(R.string.frgmnt_org_ab_subtitle))
        setAdapter()
        setObserverState()
        setAdapterClick()
    }

    private fun orgInfoAlertDialog(info: String) {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.frgmnt_org_alert_title))
            .setMessage(
                HtmlCompat.fromHtml(
                    info,
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
            )
            .setCancelable(true)
            .setNegativeButton(getString(R.string.frgmnt_org_alert_close)) { dialogInterface, i ->
                dialogInterface.dismiss()
            }
            .show()
    }


    private fun launchSecondFragment(string: String) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment_content_main, InetNumFragment.newInstance(string))
            .addToBackStack(InetNumFragment.NAME)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
