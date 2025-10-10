package com.example.useai.content.mine


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.useai.R

@Composable
fun AboutProgrammer(navController: NavHostController,modifier: Modifier){
    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
            title = {Text("关于开发者")},
            navigationIcon = { IconButton(onClick = {navController.popBackStack()}){ Icon(Icons.Default.ArrowBack,"") } }
        )}
    ) {paddingValues ->
        Card(modifier = modifier.padding(paddingValues).padding(16.dp)) {
            Column (modifier = Modifier.fillMaxWidth().padding(16.dp),horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
                Spacer(modifier = Modifier.height(5.dp))
                Surface(
                    modifier = Modifier
                        .size(150.dp)//提供宽高为150dp
                        .padding(5.dp),//添加5dp的内边框
                    shape = CircleShape,
                    border = BorderStroke(0.5.dp,Color.LightGray),//定义边框样式的类BorderStroke
                    shadowElevation = 4.dp//阴影效果 和视觉层级
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.app_logo),
                        contentDescription = "profile image",//强制要求，所有非装饰的图片都需要说明内容，为视障用户描述图片内容
                        modifier = Modifier.size(135.dp)
                    )
                }
                Spacer(Modifier.height(5.dp))
                Text("  使用奕游的朋友你好！\n  我是开发者李奕佳。\n  本App完全免费，希望我的App能让你享受更舒适的旅游行程！\n" +
                        "  使用中如有任何问题请联系我\n" +
                        "  电话：15525063800 \n  邮箱：mie15525063800@outlook.com", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(5.dp))
                Text("In the dark night we find our way\nStars above they guide the play\nNo matter what we'll rise above")
            }
        }
    }
}