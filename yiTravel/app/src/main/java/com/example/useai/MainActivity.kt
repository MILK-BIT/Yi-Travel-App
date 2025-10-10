package com.example.useai

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.amap.api.location.AMapLocationClient
import com.amap.api.maps.MapsInitializer
import com.example.useai.content.HomePage.preprocess
import com.example.useai.dataBase.viewModel.DBViewModelFactory
import com.example.useai.dataBase.viewModel.DBViewModel
import com.example.useai.ui.theme.UseAiTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

val PREF_FILE_NAME = "userdata"
val RESIDENTIAL_PLACE_FIELD = "residentialplace"
val SEX_FIELD = "sex"
val TRAVEL_WITH_FIELD = "travelwith"
val ACCEPT_START_TIME_FIELD = "accesptstarttime"
val ACCEPT_FINISH_TIME_FIELD = "acceptfinishtime"
val ACCEPT_EXPENSE_FIELD = "acceptenpense"
class MainActivity : ComponentActivity() {
    private lateinit var viewModel: DBViewModel
    private lateinit var viewModelFactory: DBViewModelFactory
    private var glContextLost = false

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        // 隐私合规必须最先调用
        MapsInitializer.updatePrivacyShow(this, true, true)
        MapsInitializer.updatePrivacyAgree(this, true)

        // 然后设置API Key
        AMapLocationClient.setApiKey("bd31b7d3f7500a8971476600a4ae7c66")
        MapsInitializer.setApiKey("bd31b7d3f7500a8971476600a4ae7c66")

        enableEdgeToEdge()
        viewModelFactory = DBViewModelFactory(application)
        viewModel = ViewModelProvider(this,viewModelFactory)
            .get(DBViewModel::class.java)
        if(isFirstRun())
            lifecycleScope.launch(Dispatchers.IO) {
            try {
                preprocess(resources = resources, dbViewModel = viewModel)
            } catch (e: Exception) {
                Log.e("MainActivity", "预处理失败", e)
                // 可选：显示错误提示
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@MainActivity,
                        "数据加载失败: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }


        setContent {
            UseAiTheme {

                MyApp(viewModel)

            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (isFinishing) {
            glContextLost = true
        }
    }

    private fun isFirstRun(): Boolean {
        val prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)
        return prefs.getBoolean("is_first_run", true).also {
            if (it) prefs.edit().putBoolean("is_first_run", false).apply()
        }
    }

}


