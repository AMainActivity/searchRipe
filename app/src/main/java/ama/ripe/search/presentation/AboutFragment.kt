package ama.ripe.search.presentation

import ama.ripe.search.databinding.FragmentAboutBinding
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment

class AboutFragment : Fragment() {
    private var _binding: FragmentAboutBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException("FragmentAboutBinding == null")


    private val component by lazy {
        (requireActivity().application as MyApp).component
    }

    override fun onAttach(context: Context) {
        component.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun setViews() {
        binding.frgmntAbTv.linksClickable = true
        binding.frgmntAbTv.movementMethod = LinkMovementMethod.getInstance()
        binding.frgmntAbTv.text =
            HtmlCompat.fromHtml(
                "getString(R.string.frgmnt_ab_main)",
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as AppCompatActivity).supportActionBar?.subtitle =
            "О приложении"
        setViews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
