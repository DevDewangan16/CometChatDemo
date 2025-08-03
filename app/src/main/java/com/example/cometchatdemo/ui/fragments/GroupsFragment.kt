package com.example.cometchatdemo.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cometchat.chat.models.Group
import com.cometchat.chatuikit.groups.CometChatGroups
import com.example.cometchatdemo.R
import com.example.cometchatdemo.ui.MainActivity

class GroupsFragment : Fragment() {

    private lateinit var groupsView: CometChatGroups

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_groups, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        setListeners()
    }

    private fun initViews(view: View) {
        groupsView = view.findViewById(R.id.groups_view)
    }

    private fun setListeners() {
        groupsView.setOnItemClick { _, _, group ->
            (activity as? MainActivity)?.navigateToMessage(guid = group.guid)
        }
    }
}