package com.example.useai.content.travelPlace

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.useai.ACCEPT_EXPENSE_FIELD
import com.example.useai.ACCEPT_FINISH_TIME_FIELD
import com.example.useai.ACCEPT_START_TIME_FIELD
import com.example.useai.BuildConfig
import com.example.useai.retrofit.ChatRequest
import com.example.useai.retrofit.Message
import com.example.useai.PREF_FILE_NAME
import com.example.useai.RESIDENTIAL_PLACE_FIELD
import com.example.useai.retrofit.RetrofitClient
import com.example.useai.SEX_FIELD
import com.example.useai.TRAVEL_WITH_FIELD
import com.example.useai.dataBase.entity.TravelStrategy
import com.example.useai.dataBase.viewModel.DBViewModel
import com.example.useai.viewModel.AIOutputViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.text.trimStart

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TravelSmallPlace(navAllController: NavController,aiOutputViewModel: AIOutputViewModel,dbViewModel: DBViewModel) {
    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text("我的旅游规划") },
                navigationIcon = {
                    IconButton(onClick = { navAllController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                }
            )
        }
    ){paddingValues ->
        TravelSmallPlaceMainContent(navAllController,paddingValues,aiOutputViewModel,dbViewModel)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TravelSmallPlaceMainContent(navAllController: NavController,paddingValues: PaddingValues,vm: AIOutputViewModel,dbViewModel: DBViewModel){
    var placenTime by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val AIOutputSmall by vm.AIOutputSmall.observeAsState()
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = placenTime,
                onValueChange = { placenTime = it },
                label = { Text("请输入旅游地，如北京环球影城") },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )


            Button(
                onClick = {
                    if (placenTime.isNotBlank()) {
                        isLoading = true
                        coroutineScope.launch(Dispatchers.IO) {
                            try {
                                //获取Preference
                                val pref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)

                                // 替换为DeepSeek API Key
                                val apiKey = "Bearer ${BuildConfig.DEEPSEEK_API_KEY}"

                                val messages = listOf(
                                    Message(
                                        content = "你是一位专业的旅游规划助手，下面用户将会给出一个地点，请根据地点给出相应的行程规划。" +
                                                "有以下几点要求\n"+
                                                "1.要涵盖交通、必玩项目、餐饮及实用贴士"+
                                                "2. 不要有多余的字符，尤其是文段最前面不要有空格或者###，不用标出天数，不要使用MarkDown格式。"+
                                                "3. 要注意格式美观，虽然不使用Markdown格式但是要按时间段换行。注意注意！！！不要使用Markdown格式！"+
                                                "4. 要尽量详细，写出具体几点到几点干什么，要考虑路程远近和路上的行程时间，注意给景点详细的介绍"+
                                                "5. 以下是用户的详细个人信息\n" +
                                                "用户居住地：${pref.getString(RESIDENTIAL_PLACE_FIELD,"未知")}"+
                                                "用户性别：${pref.getString(SEX_FIELD,"未知")}"+
                                                "用户更喜欢和${pref.getString(TRAVEL_WITH_FIELD,"未知")}一起旅行"+
                                                "用户能接受的出发时间为${pref.getString(ACCEPT_START_TIME_FIELD,"未知")}，能接受的回酒店时间:${pref.getString(
                                                    ACCEPT_FINISH_TIME_FIELD,"未知"
                                                )}"+
                                                "用户对经济和舒适度的要求：${pref.getFloat(
                                                    ACCEPT_EXPENSE_FIELD,0.5f
                                                )}，1为完全舒适优先，0为完全经济优先，中间值看更偏向于哪边"+
                                                "请结合以上要求与用户信息输出内容，注意不要MarkDown格式,一定要详细，尽可能考虑出行住宿吃喝全方面服务，要给出景点的详细介绍，让用户有最好的旅游体验。",
                                        role = "system"

                                    ),
                                    Message(
                                        role = "user",
                                        content = placenTime
                                    )
                                )

                                val response = RetrofitClient.instance.getChatResponse(
                                    apiKey = apiKey,
                                    request = ChatRequest(messages = messages)
                                )

                                //选第一个
                                if (response.choices.isNotEmpty()) {
                                    vm.changeAIOutputSmall(response.choices[0].message.content)
                                }
                            } catch (e: Exception) {
                                vm.changeAIOutputSmall("请求失败: ${e.message}")
                            } finally {
                                isLoading = false
                            }
                        }
                    }
                },
                enabled = !isLoading && placenTime.isNotBlank()
            ) {
                Text(if (isLoading) "处理中..." else "发送")
            }
        }




        Row (modifier = Modifier.fillMaxWidth().height(50.dp).padding(5.dp).clickable(onClick = {navAllController.navigate("set")}),
            verticalAlignment = Alignment.CenterVertically){
            Text("我的偏好设置", fontSize = 20.sp)
            Spacer(modifier = Modifier.width(5.dp))
            Icon(Icons.Default.Settings,"")
        }

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            val scrollState = rememberScrollState()
            if (AIOutputSmall == null){
                Text("暂无数据\n先填写“我的偏好设置”得到的结果更佳哦~\n本应用使用AI进行旅行规划\nAI反应有点慢，请耐心等待哦~")
            }
            else {
                // 显示触发按钮（只在有数据时显示）
                Button(
                    onClick = { showBottomSheet = true },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("点击显示旅游攻略")
                }
                AMapScreen(AIOutputSmall!!)
            }
//            Column (modifier = Modifier.fillMaxSize().padding(16.dp)){
//                DayCards(AIOutput!!.trimStart(), modifier = Modifier
//                    .fillMaxWidth()
//                    .weight(1f)  //  分配剩余空间
//                    .padding(16.dp))
//                Button(
//                    onClick = {
//                        val newTravelStrategy = TravelStrategy(0,title = placenTime, content = AIOutput!!)
//                        dbViewModel.save(newTravelStrategy)
//                        Toast.makeText(context,"已保存该攻略，可在“我的行程”中查看", Toast.LENGTH_LONG).show()
//                    },
//                    modifier = Modifier.fillMaxWidth().height(50.dp)
//                ) { Text("保存") }
//            }

            if(showBottomSheet && AIOutputSmall != null){
                ModalBottomSheet(
                    onDismissRequest = {
                        showBottomSheet = false
                    },
                    sheetState = sheetState
                ) {
                    Column (modifier = Modifier.fillMaxSize().padding(16.dp)){
                        DayCards(AIOutputSmall!!.trimStart(), modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)  //  分配剩余空间
                            .padding(16.dp))
                        Button(
                            onClick = {
                                val newTravelStrategy = TravelStrategy(0,title = placenTime, content = AIOutputSmall!!)
                                dbViewModel.save(newTravelStrategy)
                                Toast.makeText(context,"已保存该攻略，可在“我的行程”中查看", Toast.LENGTH_LONG).show()
                            },
                            modifier = Modifier.fillMaxWidth().height(50.dp)
                        ) { Text("保存") }
                    }
                }
            }
        }

    }

}