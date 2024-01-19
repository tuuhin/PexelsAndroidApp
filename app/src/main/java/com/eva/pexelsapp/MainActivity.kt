package com.eva.pexelsapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.eva.pexelsapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

	private lateinit var binding: ActivityMainBinding
	private lateinit var navController: NavController

	override fun onCreate(savedInstanceState: Bundle?) {
//		//splash screen from splash api
		installSplashScreen()

		super.onCreate(savedInstanceState)

		binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)

		val navHost = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
		navController = navHost.navController

	}

	override fun onNavigateUp(): Boolean {
		return navController.navigateUp() || super.onNavigateUp()
	}

}