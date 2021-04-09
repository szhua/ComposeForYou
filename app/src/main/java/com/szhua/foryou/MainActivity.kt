package com.szhua.foryou

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import com.jaeger.library.StatusBarUtil
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.szhua.foryou.ui.theme.ForYouTheme
import com.szhua.foryou.ui.Navigation

/**
 * 程序入口
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logger.addLogAdapter(AndroidLogAdapter())
        setContent {
            ForYouTheme {
                Navigation()
            }
        }
    }
}