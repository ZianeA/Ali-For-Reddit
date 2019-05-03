package com.visualeap.aliforreddit.presentation.frontPage

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.visualeap.aliforreddit.R
import com.visualeap.aliforreddit.domain.entity.Post
import kotlinx.android.synthetic.main.fragment_home.view.*

class FrontPageFragment : Fragment(), FrontPageView {

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //TODO use dependency injection
//        FrontPagePresenter(
//            this,
//            SubmissionDataRepository(),
//            AsyncSchedulerProvider()
//        ).loadPosts()
    }

    override fun displayPosts(posts: List<Post>) {
        Log.d(TAG, "list size = ${posts.size}")
        Log.d(TAG, posts[0].toString())
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
