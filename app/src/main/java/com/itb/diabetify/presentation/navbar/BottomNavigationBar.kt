package com.itb.diabetify.presentation.navbar

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.indicatorColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.itb.diabetify.R

@Composable
fun BottomNavigationBar(
    modifier: Modifier = Modifier,
    viewModel: NavigationViewModel,
    backgroundColor: Color = Color.White,
    selectedColor: Color = colorResource(id = R.color.primary),
    unselectedColor: Color = colorResource(id = R.color.tertiary),
    indicatorColor: Color = colorResource(id = R.color.primary),
    elevation: Dp = 8.dp,
    onItemSelected: (String) -> Unit = {},
    onAddButtonClicked: () -> Unit = {}
) {
    val selectedItem = viewModel.selectedItem.value
    val items = viewModel.navigationItems
    val middleIndex = items.size / 2

    var showPopup by remember { mutableStateOf(false) }

    if (showPopup) {
        AddActionPopup(
            onDismiss = { showPopup = false },
            onActionSelected = { action ->
                showPopup = false
                onAddButtonClicked()
            }
        )
    }

    Box(
        modifier = modifier.background(color = backgroundColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = elevation,
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                )
        ) {
            NavigationBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp),
                containerColor = backgroundColor,
                tonalElevation = 0.dp
            ) {
                items.forEachIndexed { index, item ->
                    if (index == middleIndex) {
                        NavigationBarItem(
                            selected = false,
                            onClick = { },
                            icon = {
                                Spacer(modifier = Modifier.size(24.dp))
                            },
                            label = { Text("") },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = selectedColor,
                                unselectedIconColor = unselectedColor,
                                indicatorColor = Color.Transparent
                            )
                        )
                    }

                    val iconSize by animateDpAsState(
                        targetValue = if (selectedItem == item.route) 28.dp else 24.dp,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        ),
                        label = "iconSize"
                    )

                    val iconColor by animateColorAsState(
                        targetValue = if (selectedItem == item.route) selectedColor else unselectedColor,
                        animationSpec = tween(300),
                        label = "iconColor"
                    )

                    NavigationBarItem(
                        selected = selectedItem == item.route,
                        onClick = {
                            viewModel.onNavigationItemSelected(item.route)
                            onItemSelected(item.route)
                        },
                        icon = {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    painter = painterResource(id = item.icon),
                                    contentDescription = item.contentDescription,
                                    modifier = Modifier.size(iconSize),
                                    tint = iconColor
                                )

                                if (selectedItem == item.route) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Box(
                                        modifier = Modifier
                                            .size(5.dp)
                                            .clip(CircleShape)
                                            .background(indicatorColor)
                                    )
                                }
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = selectedColor,
                            unselectedIconColor = unselectedColor,
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }

        FloatingActionButton(
            onClick = {
                showPopup = true
            },
            modifier = Modifier
                .align(Alignment.Center)
                .shadow(8.dp, CircleShape),
            shape = CircleShape,
            containerColor = colorResource(id = R.color.primary),
            contentColor = colorResource(id = R.color.white)
        ) {
            Icon(
                modifier = Modifier.scale(1.5f),
                imageVector = Icons.Rounded.Add,
                contentDescription = "Add"
            )
        }
    }
}

@Composable
fun AddActionPopup(
    onDismiss: () -> Unit,
    onActionSelected: (String) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp,
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .width(280.dp)
            ) {
                Text(
                    text = "Add New",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                val actions: List<Pair<String, androidx.compose.ui.graphics.vector.ImageVector>> = listOf(
                    "Post" to Icons.Default.Clear,
                    "Photo" to Icons.Default.Clear,
                    "Event" to Icons.Default.Clear,
                    "Document" to Icons.Default.Clear
                )

                actions.forEach { pair ->
                    val actionName = pair.first
                    val icon = pair.second
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onActionSelected(actionName) }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = actionName,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = actionName,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 8.dp)
                ) {
                    Text("Cancel")
                }
            }
        }
    }
}