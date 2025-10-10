package com.example.useai

import BottomMenu
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.useai.content.mine.AboutProgrammer
import com.example.useai.content.HomePage.Add
import com.example.useai.content.HomePage.Mine
import com.example.useai.content.HomePage.MyTravelPlan
import com.example.useai.content.mine.Set
import com.example.useai.content.travelPlace.MyTrip
import com.example.useai.content.travelPlace.TravelBigPlace
import com.example.useai.content.travelPlace.TravelSmallPlace
import com.example.useai.dataBase.viewModel.DBViewModel
import com.example.useai.viewModel.AIOutputViewModel
import com.example.useai.viewModel.UserViewModel

@Composable
fun MyApp(dbViewModel: DBViewModel){
    val navAllController: NavHostController = rememberNavController()//管控主页到其他脱离了scaffold页面
    val aiOutputViewModel:AIOutputViewModel = viewModel()
    NavigationAll(navAllController,aiOutputViewModel,dbViewModel)
}

@Composable
fun NavigationAll(navAllController: NavHostController,aiOutputViewModel: AIOutputViewModel,dbViewModel: DBViewModel){
    NavHost(navController = navAllController, startDestination = "myHomePage"){
        composable ("myHomePage"){
            MyHomePage(navAllController,dbViewModel)
        }

        composable("travelBigPlace"){
            TravelBigPlace(navAllController,aiOutputViewModel,dbViewModel)
        }

        composable("travelSmallPlace"){
            TravelSmallPlace(navAllController,aiOutputViewModel,dbViewModel)
        }

        composable(
            "myTrip/{id}",  // 用斜杠分隔多个参数
            arguments = listOf(
                navArgument("id") {
                    type = NavType.LongType  // 或 NavType.IntType 根据实际类型
                }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("id") ?: 0L
            MyTrip(
                navAllController = navAllController,
                id = id,
                dbViewModel,
                 { newContent ->
                    dbViewModel.changeContent(newContent,id)
                }
            )
        }

        composable("set"){
            Set(navAllController = navAllController,modifier = Modifier)
        }
        composable ("aboutProgrammer"){
            AboutProgrammer(navController = navAllController, modifier = Modifier)
        }
    }
}


@Composable
fun MyHomePage(navAllController: NavHostController,dbViewModel: DBViewModel){
    val navController: NavHostController = rememberNavController()//管理不同页面的跳转
    val vm: UserViewModel = viewModel()
    Scaffold (
        bottomBar = {BottomMenu(navController = navController)},
    ){it ->
        Navigation(navController,it,vm,navAllController,dbViewModel)
    }
}


@Composable
fun Navigation(navController: NavHostController,paddingValues: PaddingValues,vm: UserViewModel,navAllController: NavHostController,dbViewModel: DBViewModel){
    NavHost(navController = navController, startDestination = "myTravelPlan"){
        composable("myTravelPlan"){
            MyTravelPlan(navController = navController, modifier = Modifier.padding(paddingValues),navAllController,dbViewModel)
        }
        composable("add"){
            Add( modifier = Modifier.padding(paddingValues),navAllController)
        }
        composable("mine"){
            Mine(navAllController = navAllController,vm, modifier = Modifier.padding(paddingValues))
        }
    }
}


