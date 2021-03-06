package com.example.customnavigationdrawerexample



import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.customnavigationdrawerexample.manager.SessionManager
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    lateinit var drawerLayout: DrawerLayout
    private lateinit var adapter: NavigationRVAdapter
    private var items = arrayListOf(
        NavigationItemModel(R.drawable.ic_home, "Login"),
        NavigationItemModel(R.drawable.ic_book, "Map"),
        NavigationItemModel(R.drawable.ic_movie, "Movies"),
        NavigationItemModel(R.drawable.ic_book, "Books"),
        NavigationItemModel(R.drawable.ic_profile, "Profile"),
        NavigationItemModel(R.drawable.ic_settings, "Settings"),
        NavigationItemModel(R.drawable.ic_social, "Like us on facebook")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var authContext = SessionManager(this)
        var btnLogin = findViewById<Button>(R.id.btn_login_viewer)
        if (authContext.fetchAuthToken() != null) {

            drawerLayout = findViewById(R.id.drawer_layout)
            // Set the toolbar
            setSupportActionBar(activity_main_toolbar)

            // Setup Recyclerview's Layout
            navigation_rv.layoutManager = LinearLayoutManager(this)
            navigation_rv.setHasFixedSize(true)
            // Add Login Button Listener
            if (authContext.fetchAuthToken() != null) {
                btnLogin.setText("Sign Out")
                btnLogin.setOnClickListener {
                    authContext.removeToken(this)
                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    startActivity(intent);
                }
            }

            // Add Item Touch Listener
            navigation_rv.addOnItemTouchListener(
                RecyclerTouchListener(
                    this,
                    object : ClickListener {
                        override fun onClick(view: View, position: Int) {
                            when (position) {
                                0 -> {
                                    // # Profile Activity
                                    val intent = Intent(this@MainActivity, DemoActivity::class.java)
                                    intent.putExtra("activityName", "Login Activity")
                                    startActivity(intent)
                                }
                                1 -> {
                                    // # Map Fragment
                                    val bundle = Bundle()
                                    bundle.putString("fragmentName", "Map Fragment")
                                    val moviesFragment = MapsFragment()
                                    moviesFragment.arguments = bundle
                                    supportFragmentManager.beginTransaction()
                                        .replace(R.id.activity_main_content_id, moviesFragment)
                                        .commit()
                                }
                                2 -> {
                                    // # Movies Fragment
                                    val bundle = Bundle()
                                    bundle.putString("fragmentName", "Movies Fragment")
                                    val moviesFragment = DemoFragment()
                                    moviesFragment.arguments = bundle
                                    supportFragmentManager.beginTransaction()
                                        .replace(R.id.activity_main_content_id, moviesFragment)
                                        .commit()
                                }
                                3 -> {
                                    // # Movies Fragment
                                    val bundle = Bundle()
                                    bundle.putString("fragmentName", "Movies Fragment")
                                    val moviesFragment = DemoFragment()
                                    moviesFragment.arguments = bundle
                                    supportFragmentManager.beginTransaction()
                                        .replace(R.id.activity_main_content_id, moviesFragment)
                                        .commit()
                                }
                                4 -> {
                                    // # Profile Activity
                                    val intent = Intent(this@MainActivity, DemoActivity::class.java)
                                    intent.putExtra("activityName", "Profile Activity")
                                    startActivity(intent)
                                }
                                5 -> {
                                    // # Settings Fragment
                                    val bundle = Bundle()
                                    bundle.putString("fragmentName", "Settings Fragment")
                                    val settingsFragment = DemoFragment()
                                    settingsFragment.arguments = bundle
                                    supportFragmentManager.beginTransaction()
                                        .replace(R.id.activity_main_content_id, settingsFragment)
                                        .commit()
                                }
                                6 -> {
                                    // # Open URL in browser
                                    val uri: Uri = Uri.parse("https://johnc.co/fb")
                                    val intent = Intent(Intent.ACTION_VIEW, uri)
                                    startActivity(intent)
                                }
                            }
                            // Don't highlight the 'Profile' and 'Like us on Facebook' item row
                            if (position != 6 && position != 4) {
                                updateAdapter(position)
                            }
                            Handler().postDelayed({
                                drawerLayout.closeDrawer(GravityCompat.START)
                            }, 200)
                        }
                    })
            )

            // Update Adapter with item data and highlight the default menu item ('Home' Fragment)
            updateAdapter(0)

            // Set 'Home' as the default fragment when the app starts
            val bundle = Bundle()
            bundle.putString("fragmentName", "Home Fragment")
            val homeFragment = DemoFragment()
            homeFragment.arguments = bundle
            supportFragmentManager.beginTransaction()
                .replace(R.id.activity_main_content_id, homeFragment).commit()


            // Close the soft keyboard when you open or close the Drawer
            val toggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
                this,
                drawerLayout,
                activity_main_toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
            ) {
                override fun onDrawerClosed(drawerView: View) {
                    // Triggered once the drawer closes
                    super.onDrawerClosed(drawerView)
                    try {
                        val inputMethodManager =
                            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
                    } catch (e: Exception) {
                        e.stackTrace
                    }
                }

                override fun onDrawerOpened(drawerView: View) {
                    // Triggered once the drawer opens
                    super.onDrawerOpened(drawerView)
                    try {
                        val inputMethodManager =
                            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
                    } catch (e: Exception) {
                        e.stackTrace
                    }
                }
            }
            drawerLayout.addDrawerListener(toggle)

            toggle.syncState()


            // Set Header Image
//        navigation_header_img.setImageResource(R.drawable.logo)

            // Set background of Drawer
            navigation_layout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
        } else {
            btnLogin.setOnClickListener {
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent);
            }
            btnLogin.callOnClick()
        }
    }
    override fun onResume() {
        super.onResume()

        Log.v("Resumed", "MainActivity")
    }
    private fun updateAdapter(highlightItemPos: Int) {
        adapter = NavigationRVAdapter(items, highlightItemPos)
        navigation_rv.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            // Checking for fragment count on back stack
            if (supportFragmentManager.backStackEntryCount > 0) {
                // Go to the previous fragment
                supportFragmentManager.popBackStack()
            } else {
                // Exit the app
                super.onBackPressed()
            }
        }
    }

}