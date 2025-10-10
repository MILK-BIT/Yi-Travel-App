package com.example.useai.content.travelPlace

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.useai.dataBase.viewModel.DBViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTrip(navAllController: NavHostController, id:Long, dbViewModel: DBViewModel, onContentChange: (String) -> Unit) {
    // 使用data class合并加载状态
    data class TripData(val title: String, val content: String)

    var tripData by remember { mutableStateOf<TripData?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    val context = LocalContext.current
    LaunchedEffect(id) {
        // 同步加载title和content
        val title = dbViewModel.getTitleSuspend(id)
        val content = dbViewModel.getContentSuspend(id)
        tripData = TripData(title, content)
        isLoading = false
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(tripData?.title ?: "加载中...") },
                navigationIcon = {
                    IconButton(onClick = { navAllController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    IconButton(onClick = {shareTextToOtherApps(context,tripData!!.content)}){
                        Icon(Icons.Default.Share,"")
                    }
                }
            )
        }
    ) { paddingValues ->
        when {
            isLoading -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            tripData == null -> Text("数据加载失败")
            else -> MyTripContent(
                paddingValues = paddingValues,
                allContent = tripData!!.content,
                onContentChange = onContentChange
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTripContent(paddingValues: PaddingValues,allContent: String?,onContentChange: (String) -> Unit){
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    Column (modifier = Modifier.fillMaxWidth().padding(paddingValues)){
        Button(onClick = {showBottomSheet = true}, modifier = Modifier.fillMaxWidth().padding(5.dp)) { Text("显示文字攻略")}
    Column{
        Card(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            AMapScreen(allContent?:"")
        }
    }
    }

    if (showBottomSheet && allContent != null){
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
            },
            sheetState = sheetState
        ) {
            EditableTripContent(Modifier.fillMaxSize().padding(16.dp),allContent!!,onContentChange)
        }
    }

}

@Composable
fun EditableTripContent(
    modifier: Modifier,
    allContent: String?,
    onContentChange: (String) -> Unit // 新增回调函数用于保存修改
) {
    // 将内容转换为可编辑的每行状态
    val editableLines = remember(allContent) {
        mutableStateListOf<String>().apply {
            addAll(allContent?.split("###") ?: emptyList())
        }
    }

    if (editableLines.isEmpty()) {
        Text("没有计划，来自己设计一个吧")
        return
    }

    LazyColumn(modifier = modifier) {
        itemsIndexed(editableLines) { index, line ->
            var isEditing by remember { mutableStateOf(false) }
            var currentText by remember { mutableStateOf(line) }

            Column(modifier = Modifier.padding(16.dp)) {
                Text("第${index + 1} 天", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(3.dp))

                Card(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    if (isEditing) {
                        // 编辑状态
                        BasicTextField(
                            value = currentText,
                            onValueChange = { currentText = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            textStyle = LocalTextStyle.current.copy(fontSize = 16.sp)
                        )
                    } else {
                        // 展示状态
                        Text(
                            text = currentText,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .clickable { isEditing = true }
                        )
                    }
                }

                // 编辑控制按钮
                if (isEditing) {
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TextButton(onClick = {
                            isEditing = false
                            currentText = editableLines[index] // 重置为原值
                        }) {
                            Text("取消")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        TextButton(onClick = {
                            editableLines[index] = currentText
                            isEditing = false
                            onContentChange(editableLines.joinToString("###")) // 保存修改
                        }) {
                            Text("保存")
                        }
                    }
                }
                Spacer(modifier = Modifier.height(5.dp))
            }
        }
    }
}

fun shareTextToOtherApps(context: Context, text: String) {
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/plain"
    }

    // 创建选择器对话框（可选）
    val shareIntent = Intent.createChooser(sendIntent, "分享文本到...")

    // 验证是否有应用能处理该Intent
    if (sendIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(shareIntent)
    } else {
        Toast.makeText(context, "未找到可用的应用", Toast.LENGTH_SHORT).show()
    }
}