package com.example.cometchatdemo.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cometchat.chat.models.User
import com.cometchat.chatuikit.users.CometChatUsers
import com.example.cometchatdemo.R
import com.example.cometchatdemo.ui.MainActivity

class UsersFragment : Fragment() {

    private lateinit var usersView: CometChatUsers

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_users, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        setListeners()
    }

    private fun initViews(view: View) {
        usersView = view.findViewById(R.id.users_view)
    }

    private fun setListeners() {
        usersView.setOnItemClick { _, _, user ->
            (activity as? MainActivity)?.navigateToMessage(uid = user.uid)
        }
    }
}