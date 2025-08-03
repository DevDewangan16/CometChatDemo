package com.example.cometchatdemo.ui
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
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
        private val DEFAULT_UID = "test-user-${System.currentTimeMillis() % 1000}" // Replace with actual user ID
        private const val INIT_DELAY = 2000L // 2 seconds delay for initialization
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupWindowInsets()
        observeViewModel()
        setupBottomNavigation(savedInstanceState)

        // Create and login test user
        Handler(Looper.getMainLooper()).postDelayed({
            createAndLoginTestUser()
        }, INIT_DELAY)
    }

    private fun createAndLoginTestUser() {
        viewModel.createTestUser(
            uid = DEFAULT_UID,
            name = "Test User ${DEFAULT_UID.takeLast(3)}"
        ) { success ->
            if (success) {
                viewModel.loginUser(DEFAULT_UID)
            } else {
                // Fallback to default test user
                viewModel.loginUser("superhero1")
            }
        }
    }
    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun observeViewModel() {
        viewModel.loginState.observe(this) { state ->
            when (state) {
                is MainViewModel.LoginState.Success -> {
                    Log.d(TAG, "Login successful for user: ${state.user.uid}")
                    Toast.makeText(this, "Welcome ${state.user.name}", Toast.LENGTH_SHORT).show()
                }
                is MainViewModel.LoginState.Error -> {
                    Log.e(TAG, "Login failed: ${state.message}")
                    Toast.makeText(this, "Login failed: ${state.message}", Toast.LENGTH_LONG).show()

                    // Retry login after a delay
                    Handler(Looper.getMainLooper()).postDelayed({
                        viewModel.loginUser(DEFAULT_UID)
                    }, 3000L)
                }
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            // Handle loading state if needed
            Log.d(TAG, "Loading state: $isLoading")
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

    override fun onDestroy() {
        super.onDestroy()
        viewModel.logout()
    }
}