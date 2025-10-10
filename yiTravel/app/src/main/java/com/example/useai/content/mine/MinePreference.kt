package com.example.useai.content.mine

import android.content.Context
import android.content.SharedPreferences.Editor
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.useai.ACCEPT_EXPENSE_FIELD
import com.example.useai.ACCEPT_FINISH_TIME_FIELD
import com.example.useai.ACCEPT_START_TIME_FIELD
import com.example.useai.PREF_FILE_NAME
import com.example.useai.RESIDENTIAL_PLACE_FIELD
import com.example.useai.SEX_FIELD
import com.example.useai.TRAVEL_WITH_FIELD

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Set(navAllController: NavHostController, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val pref =context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)
    val editor: Editor = pref.edit()
    var residence by remember { mutableStateOf(pref.getString(RESIDENTIAL_PLACE_FIELD,"")) }
    val genderOptions = listOf("男", "女")
    var selectedGender by remember { mutableStateOf(pref.getString(SEX_FIELD,"男")) }
    var companion by remember { mutableStateOf(pref.getString(TRAVEL_WITH_FIELD,"")) }
    var startTime by remember { mutableStateOf(pref.getString(ACCEPT_START_TIME_FIELD,"")) }
    var endTime by remember { mutableStateOf(pref.getString(ACCEPT_FINISH_TIME_FIELD,"")) }
    var preference by remember { mutableFloatStateOf(pref.getFloat(ACCEPT_EXPENSE_FIELD,0f)) }
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("我的旅游设置") },
                navigationIcon = {
                    IconButton(onClick = { navAllController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                }
            )
        }
    ) { paddingValues ->
        // 主内容区域
        Card(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                // 居住地输入
                ResidenceInputSection(
                    residence = residence!!,
                    onResidenceChange = { residence = it }
                )

                Divider(modifier = Modifier.padding(vertical = 8.dp))

                // 性别选择
                GenderSelectionSection(
                    options = genderOptions,
                    selected = selectedGender!!,
                    onSelectedChange = { selectedGender = it }
                )

                Divider(modifier = Modifier.padding(vertical = 8.dp))

                // 旅行同伴
                TravelCompanionSection(
                    companion = companion!!,
                    onCompanionChange = { companion = it }
                )

                Divider(modifier = Modifier.padding(vertical = 8.dp))

                // 行程时间
                ScheduleInputSection(
                    startTime = startTime!!,
                    onStartTimeChange = { startTime = it },
                    endTime = endTime!!,
                    onEndTimeChange = { endTime = it }
                )

                Divider(modifier = Modifier.padding(vertical = 8.dp))

                // 偏好滑动条
                PreferenceSliderSection(
                    preference = preference,
                    onPreferenceChange = { preference = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 保存按钮
                Button(
                    onClick = {
                        editor.putString(RESIDENTIAL_PLACE_FIELD,residence)
                        editor.putString(SEX_FIELD,selectedGender)
                        editor.putString(TRAVEL_WITH_FIELD,companion)
                        editor.putString(ACCEPT_START_TIME_FIELD,startTime)
                        editor.putString(ACCEPT_FINISH_TIME_FIELD,endTime)
                        editor.putFloat(ACCEPT_EXPENSE_FIELD,preference)
                        editor.apply()
                              },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text("保存设置", fontSize = 16.sp)
                }
            }
        }
    }
}


// 居住地输入组件
@Composable
private fun ResidenceInputSection(residence: String,onResidenceChange:(String) -> Unit) {

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("我的居住地", modifier = Modifier.width(100.dp))
        Spacer(modifier = Modifier.width(8.dp))
        OutlinedTextField(
            value = residence,
            onValueChange = onResidenceChange,
            placeholder = { Text("例如：北京", fontSize = 14.sp) },
            singleLine = true,
            modifier = Modifier.weight(1f),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )
    }
}

// 性别选择组件
@Composable
private fun GenderSelectionSection(
    options: List<String>,
    selected: String,
    onSelectedChange: (String) -> Unit
) {
    Column {
        Text("性别", style = MaterialTheme.typography.bodyLarge)
        Row {
            options.forEach { option ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .selectable(
                            selected = (option == selected),
                            onClick = { onSelectedChange(option) }
                        )
                        .padding(end = 16.dp)
                ) {
                    RadioButton(
                        selected = (option == selected),
                        onClick = null
                    )
                    Text(option)
                }
            }
        }
    }
}

// 旅行同伴输入组件
@Composable
private fun TravelCompanionSection(
    companion: String,
    onCompanionChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("我和", modifier = Modifier.width(40.dp))
            OutlinedTextField(
                value = companion,
                onValueChange = onCompanionChange,
                placeholder = { Text("例如：家人/朋友") },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
            Text("一起旅行", modifier = Modifier.padding(start = 8.dp))
        }
    }
}
// 行程时间输入组件
@Composable
private fun ScheduleInputSection(
    startTime: String,
    onStartTimeChange: (String) -> Unit,
    endTime: String,
    onEndTimeChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("行程时间", modifier = Modifier.padding(bottom = 8.dp))
        TimeInputRow(
            label = "出发",
            value = startTime,
            onValueChange = onStartTimeChange,
            hint = "例如：08:00"
        )
        Spacer(modifier = Modifier.height(8.dp))
        TimeInputRow(
            label = "返回",
            value = endTime,
            onValueChange = onEndTimeChange,
            hint = "例如：20:00"
        )
    }
}

@Composable
private fun TimeInputRow(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    hint: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text("$label：", modifier = Modifier.width(60.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(hint, fontSize = 14.sp) },
            singleLine = true,
            modifier = Modifier.weight(1f),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
}

// 偏好滑动条组件
@Composable
private fun PreferenceSliderSection(
    preference: Float,
    onPreferenceChange: (Float) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("旅行偏好", modifier = Modifier.padding(bottom = 8.dp))
        Slider(
            value = preference,
            onValueChange = onPreferenceChange,
            valueRange = 0f..1f,
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("经济优先", color = MaterialTheme.colorScheme.primary)
            Text("舒适优先", color = MaterialTheme.colorScheme.primary)
        }
    }
}
