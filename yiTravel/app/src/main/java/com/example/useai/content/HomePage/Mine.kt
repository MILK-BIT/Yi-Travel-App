package com.example.useai.content.HomePage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.useai.R
import com.example.useai.viewModel.UserViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.sp

@Composable
fun Mine(navAllController: NavController,vm: UserViewModel,modifier: Modifier){
    val name by vm.name.observeAsState()
    Card (
        modifier = modifier.padding(16.dp)
    ){
       Column (modifier = Modifier){
           Row (modifier = Modifier.fillMaxWidth().height(125.dp).padding(top = 10.dp)){
               Surface(
                   modifier = Modifier
                       .size(100.dp)//提供宽高为150dp
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

               Spacer(modifier = Modifier.width(5.dp))

               Column (verticalArrangement = Arrangement.Center){
                   TextField(
                       modifier = Modifier.padding(top = 55.dp),
                       value = name!!,
                       onValueChange = { vm.changeName(it) },
                       label = { Text("您的昵称") }  // label需要是Composable函数
                   )
               }
           }
        Divider(modifier = Modifier.height(3.dp))
        Row (modifier = Modifier.fillMaxWidth().height(50.dp).padding(5.dp).clickable(onClick = {navAllController.navigate("set")}),
            verticalAlignment = Alignment.CenterVertically){
            Text("我的偏好设置", fontSize = 20.sp)
            Spacer(modifier = Modifier.width(5.dp))
            Icon(Icons.Default.Settings,"")
        }

           Divider(modifier = Modifier.height(3.dp))

           Row (modifier = Modifier.fillMaxWidth().height(50.dp).padding(5.dp).clickable(onClick = {navAllController.navigate("aboutProgrammer")}),
               verticalAlignment = Alignment.CenterVertically){
               Text("关于开发者", fontSize = 20.sp)
           }
       }
    }
}