import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.useai.ui.theme.BlueNew

sealed class BottomMenuScreen(
    val route: String,
    val icon: ImageVector, // 仅存储 ImageVector
    val title: String
) {
    object myTravelPlan : BottomMenuScreen(
        "myTravelPlan",
        Icons.Default.Create,
        "我的行程"
    )

    object add : BottomMenuScreen(
        "add",
        Icons.Default.AddCircle,
        "添加"
    )

    object mine : BottomMenuScreen(
        "mine",
        Icons.Default.AccountBox,
        "我的"
    )
}

@Composable
fun BottomMenu(navController: NavController) {
    val menuItems = listOf(
        BottomMenuScreen.myTravelPlan,
        BottomMenuScreen.add,
        BottomMenuScreen.mine
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        menuItems.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = { navController.navigate(item.route) },
                icon = {
                   chooseIcon(item)
                },
                label = { Text(item.title) },
                alwaysShowLabel = true
            )
        }
    }
}
@Composable
fun chooseIcon(button:BottomMenuScreen){
    if(button.title == "添加"){
        Box(
            modifier = Modifier
                .background(BlueNew, CircleShape) // 灰色圆形背景
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = button.icon,
                contentDescription = button.title,
                tint = Color.White // 白色图标
            )
        }
    }

    else{
        Icon(imageVector = button.icon, contentDescription =button.title)
    }

}