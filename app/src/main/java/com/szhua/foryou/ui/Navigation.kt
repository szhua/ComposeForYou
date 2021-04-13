package com.szhua.foryou.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.szhua.foryou.common.Pages
import com.szhua.foryou.data.BMobDiary

/**
@author  SZhua
Create at  2021/4/7
Description: 路由配置相关；
 */
@Composable
fun Navigation(){
    val navController = rememberNavController()
    NavHost(navController =  navController, startDestination =  Screen.SPLASH.route){
        composable(Screen.MAIN.route){ Main(nav = navController) }
        composable(Screen.SPLASH.route){ SplashScreen(navController = navController)}
        composable(Screen.DIARIES.route){ DiariesScreen(nav = navController)}
        composable(Screen.DETAIL.route ,arguments = listOf(navArgument("detail"){
            defaultValue = BMobDiary()
        })){
            val  detail = it.arguments?.getSerializable("detail") as BMobDiary
            DetailScreen(diary = detail , nav = navController )
        }
    }
}


sealed class Screen(val route: String) {
    object MAIN : Screen(Pages.MAIN_PAGE)
    object DIARIES:Screen(Pages.DIARIES_PAGE)
    object DETAIL:Screen(Pages.DETAIL)
    object SPLASH:Screen(Pages.SPLASH)
}