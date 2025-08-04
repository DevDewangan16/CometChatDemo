package com.example.cometchatdemo.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.cometchat.chat.core.CometChat
import com.example.cometchatdemo.R
import com.example.cometchatdemo.databinding.ActivityMainBinding
import com.example.cometchatdemo.ui.fragments.CallLogsFragment
import com.example.cometchatdemo.ui.fragments.ChatsFragment
import com.example.cometchatdemo.ui.fragments.GroupsFragment
import com.example.cometchatdemo.ui.fragments.UsersFragment
import com.example.cometchatdemo.ui.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check if user is logged in
        if (!isUserLoggedIn()) {
            redirectToLogin()
            return
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupActionBar()
        setupWindowInsets()
        setupBottomNavigation(savedInstanceState)
    }

    private fun isUserLoggedIn(): Boolean {
        return try {
            CometChat.getLoggedInUser() != null
        } catch (e: Exception) {
            false
        }
    }

    private fun redirectToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun setupActionBar() {
        supportActionBar?.apply {
            title = "CometChat Demo"
            setDisplayShowTitleEnabled(true)
        }
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupBottomNavigation(savedInstanceState: Bundle?) {
        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            val fragment = when (item.itemId) {
                R.id.nav_chats -> ChatsFragment()
                R.id.nav_call_logs -> CallLogsFragment()
                R.id.nav_users -> UsersFragment()
                R.id.nav_groups -> GroupsFragment()
                else -> ChatsFragment()
            }
            replaceFragment(fragment)
            true
        }

        // Set default fragment
        if (savedInstanceState == null) {
            replaceFragment(ChatsFragment())
            binding.bottomNavigationView.selectedItemId = R.id.nav_chats
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    fun navigateToMessage(uid: String? = null, guid: String? = null) {
        val intent = Intent(this, MessageActivity::class.java).apply {
            uid?.let { putExtra("uid", it) }
            guid?.let { putExtra("guid", it) }
        }
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun logout() {
        viewModel.logout()

        // Navigate to login screen
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Don't auto-logout on destroy, let user manually logout
    }
}