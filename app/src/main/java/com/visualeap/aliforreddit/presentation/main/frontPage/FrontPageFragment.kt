package com.visualeap.aliforreddit.presentation.main.frontPage

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.visualeap.aliforreddit.R
import com.visualeap.aliforreddit.domain.entity.Post
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_home.view.*
import javax.inject.Inject

class FrontPageFragment : Fragment(), FrontPageView {

    @Inject
    lateinit var presenter: FrontPagePresenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)
        rootView.section_label.text = getString(
            R.string.section_format, arguments?.getInt(
                ARG_SECTION_NUMBER
            )
        )
        return rootView
    }

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onPause() {
        super.onPause()
        presenter.stop()
    }

    override fun displayPosts(posts: List<Post>) {
        //TODO implement
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        fun newInstance(sectionNumber: Int) = FrontPageFragment().apply {
            arguments = bundleOf(ARG_SECTION_NUMBER to sectionNumber)
        }

        private val TAG = FrontPageFragment::class.java.simpleName
    }
}
