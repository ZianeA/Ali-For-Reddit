package com.visualeap.aliforreddit.presentation.main.frontPage

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.paging.PagedList
import com.airbnb.epoxy.EpoxyAsyncUtil
import com.visualeap.aliforreddit.R
import com.visualeap.aliforreddit.domain.model.Post
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.fragment_home.view.frontPageRecyclerView
import javax.inject.Inject

class FrontPageFragment : Fragment(), FrontPageView {

    @Inject
    lateinit var presenter: FrontPagePresenter

    private val epoxyController = FrontPageEpoxyController()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)
//        rootView.section_label.text = getString(
//            R.string.section_format, arguments?.getInt(
//                ARG_SECTION_NUMBER
//            )
//        )
        rootView.frontPageRecyclerView.setController(epoxyController)
        return rootView
    }

    override fun onStart() {
        super.onStart()
        presenter.start()
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onStop() {
        super.onStop()
        presenter.stop()
    }

    override fun displayPosts(posts: PagedList<Post>) {
        epoxyController.submitList(posts)
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
