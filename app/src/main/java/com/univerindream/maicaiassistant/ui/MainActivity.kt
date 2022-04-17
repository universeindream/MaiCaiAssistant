package com.univerindream.maicaiassistant.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import com.blankj.utilcode.util.AppUtils
import com.elvishew.xlog.XLog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.univerindream.maicaiassistant.BuildConfig
import com.univerindream.maicaiassistant.R
import com.univerindream.maicaiassistant.api.GithubApi
import com.univerindream.maicaiassistant.databinding.ActivityMainBinding
import com.univerindream.maicaiassistant.utils.VersionComparator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


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

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            if (destination.id == R.id.ConfigFragment) {
                binding.toolbar.title = "${destination.label}(${BuildConfig.VERSION_NAME})"
            }
        }

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.help -> {
                    val uri: Uri =
                        Uri.parse("https://github.com/universeindream/MaiCaiAssistant")
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        checkAppVersion()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.top_app_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_settings)
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_settings)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    fun checkAppVersion() {
        lifecycleScope.launch {
            try {
                val releases =
                    GithubApi.get().searchRepos("universeindream", "MaiCaiAssistant").firstOrNull() ?: return@launch
                val githubVersion = releases.tag_name
                val curVersion = "v" + AppUtils.getAppVersionName()
                XLog.v("$curVersion %s", githubVersion)

                if (VersionComparator.INSTANCE.compare(curVersion, githubVersion) < 0) {
                    withContext(Dispatchers.Main) {
                        MaterialAlertDialogBuilder(this@MainActivity)
                            .setTitle("新版本提示")
                            .setMessage("$githubVersion 已发布，请及时更新")
                            .setPositiveButton("下载") { _, _ ->
                                val uri: Uri =
                                    Uri.parse("https://github.com/universeindream/MaiCaiAssistant/releases/latest/download/app-release.apk")
                                val intent = Intent(Intent.ACTION_VIEW, uri)
                                startActivity(intent)
                            }
                            .setNegativeButton("放弃") { a, _ ->
                                a.cancel()
                            }
                            .setCancelable(false)
                            .show()
                    }
                }
            } catch (e: Exception) {
                XLog.e(e)
            }
        }
    }
}