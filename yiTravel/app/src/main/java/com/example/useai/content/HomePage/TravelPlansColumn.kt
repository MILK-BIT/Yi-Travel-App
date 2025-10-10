package com.example.useai.content.HomePage

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.ui.graphics.Color
import com.example.useai.R  // 替换为你的应用包名

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.example.useai.dataBase.entity.TravelStrategy
import com.example.useai.dataBase.viewModel.DBViewModel
import com.example.useai.ui.theme.BlueNew
import com.example.useai.ui.theme.WhiteBlue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale

@Composable
fun MyTravelPlan(navController: NavController,modifier: Modifier,navAllController: NavController,dbViewModel: DBViewModel){
    val lifecycleOwner = LocalLifecycleOwner.current
    Card (modifier = modifier.fillMaxSize().padding(5.dp),
        ){
        Scaffold(
            topBar = {
                @OptIn(ExperimentalMaterial3Api::class)
                TopAppBar(
                    title = { Text("我的计划") },
                    navigationIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.icon_small),
                            contentDescription = ""
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = BlueNew, // 背景色
                        titleContentColor = Color.White, // 标题颜色
                        navigationIconContentColor = Color.White // 图标颜色
                    )
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navController.navigate("add") },
                    containerColor = BlueNew, // 设置背景色
                    contentColor = Color.White // 设置图标颜色
                ) {
                    Icon(Icons.Default.Add, contentDescription = "添加")
                }
            }
        ) {paddingValue->
            val strategies by dbViewModel.getALlStrategies().observeAsState(initial = emptyList())
            Column (modifier = Modifier.fillMaxWidth().padding(paddingValue).padding(16.dp)){
               CarouselExample_MultiBrowse({id->navAllController.navigate("myTrip/${id}")})
                MyPlan(strategies, navAllController)
            }
        }
    }
}

@Composable
fun MyPlan(strategies:List<TravelStrategy>, navAllController: NavController){
    LazyColumn (modifier = Modifier.fillMaxHeight()){
        items(strategies){strategy ->
            if (strategy.id > 4) {
                Spacer(modifier = Modifier.height(7.dp))
                Card(
                    modifier = Modifier.fillMaxWidth().height(100.dp).padding(5.dp)
                        .clickable(onClick = { navAllController.navigate("myTrip/${strategy.id}") }),
                    colors = CardDefaults.cardColors(
                        containerColor = WhiteBlue // 设置为白色背景
                    )
                ) {
                    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                        Text(
                            text = strategy.title,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(1.dp))
                    }
                }
            }
        }
    }
}


//轮播图
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarouselExample_MultiBrowse(onClickImage:(Long)-> Unit) {
    data class CarouselItem(
        val id: Int,
        @DrawableRes val imageResId: Int,
        val contentDescription: String
    )

    val items = remember {
        listOf(
            CarouselItem(1, R.drawable.beijing, "北京——一场时空交错的“沉浸式史诗体验”"),
            CarouselItem(2, R.drawable.hongkong, "香港——“东方赛博朋克”式的多感官盛宴"),
            CarouselItem(3, R.drawable.taiyuan, "太原——“ 醋缸里泡出来的水浒传"),
            CarouselItem(4, R.drawable.xinjiang, "新疆——从火焰到冰川的史诗狂想曲"),
        )
    }

    HorizontalMultiBrowseCarousel(
        state = rememberCarouselState { items.count() },
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 16.dp, bottom = 16.dp),
        preferredItemWidth = 186.dp,
        itemSpacing = 8.dp,
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) { i ->//i这里是列表序号，存在0
        val item = items[i]
        Column {
            Image(
                modifier = Modifier
                    .height(205.dp)
                    .maskClip(MaterialTheme.shapes.extraLarge)
                    .clickable(onClick = {onClickImage((i+1).toLong())}),
                painter = painterResource(id = item.imageResId),
                contentDescription = item.contentDescription,
                contentScale = ContentScale.Crop
            )
            Column (horizontalAlignment = Alignment.CenterHorizontally){
            Text(item.contentDescription)}
        }
    }
}