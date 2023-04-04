//package com.example.shopping_list.archiv
//
//import android.os.Bundle
//import androidx.appcompat.app.AppCompatActivity
//import androidx.compose.runtime.Composable
//import androidx.navigation.NavController
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.rememberNavController
//import androidx.navigation.fragment.NavHostFragment
//import androidx.navigation.ui.setupWithNavController
//import com.example.shopping_list.databinding.ActivityMainBinding
//import com.google.android.material.bottomnavigation.BottomNavigationView
//import dagger.hilt.android.AndroidEntryPoint
//
//@AndroidEntryPoint
//class MainActivity : AppCompatActivity() {
//
//    private lateinit var binding: ActivityMainBinding
//    private lateinit var navController: NavController
//    // Instance of the AppComponent that will be used by all the Activities in the project
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        val navView: BottomNavigationView = binding.bottomNavigationView
//        val navHostFragment = supportFragmentManager.findFragmentById(
//            R.id.nav_host_fragment_activity_main ) as NavHostFragment
//        navController = navHostFragment.navController
//        navView.setupWithNavController(navController)
//    }
//    @Composable
//    fun createNavigationComposeView(){
//        val navController = rememberNavController()
//        NavHost(navController = navController, graph = "list_basket"){
//            composable("list_basket")
//        }
//    }
////    NavHost(navController = navController, startDestination = "profile") {
////////        composable("profile") { Profile(/*...*/) }
////////        composable("friendslist") { FriendsList(/*...*/) }
//////    }
//}