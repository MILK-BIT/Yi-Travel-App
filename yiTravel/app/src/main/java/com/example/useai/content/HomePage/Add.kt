package com.example.useai.content.HomePage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.useai.ui.theme.BlueNew
import com.example.useai.ui.theme.WhiteBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Add( modifier: Modifier = Modifier,navAllController: NavController) {

        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 1. 省市选择卡片
                DestinationCard(
                    text = "游省/市",
                    onClick = { navAllController.navigate("travelBigPlace") }
                )

                Spacer(modifier = Modifier.height(60.dp))

                // 2. 景点选择卡片
                DestinationCard(
                    text = "游景点",
                    onClick = { navAllController.navigate("travelSmallPlace")}
                )

            }
        }
    }

@Composable
private fun DestinationCard(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BlueNew),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = WhiteBlue,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}
