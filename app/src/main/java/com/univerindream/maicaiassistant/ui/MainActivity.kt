package com.univerindream.maicaiassistant.ui

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.blankj.utilcode.util.SPUtils
import com.univerindream.maicaiassistant.BuildConfig
import com.univerindream.maicaiassistant.MHData
import com.univerindream.maicaiassistant.R
import com.univerindream.maicaiassistant.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private val navController by lazy {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_content_settings) as NavHostFragment

        navHostFragment.navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        checkVersion()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_settings)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    fun checkVersion() {
        val versionCode = SPUtils.getInstance().getInt("versionCode", 0)
        if (BuildConfig.VERSION_CODE > versionCode) {
            SPUtils.getInstance().put("versionCode", BuildConfig.VERSION_CODE)

            AlertDialog.Builder(this)
                .setTitle("方案更新提示")
                .setMessage("方案可能有更新，是否重置本地方案")
                .setNegativeButton("取消") { _, _ -> }
                .setPositiveButton("确定") { _, _ ->
                    MHData.curJsonMHSolution = ""
                    MHData.allJsonMHSolution = ""
                }
                .show()
        }
    }
}