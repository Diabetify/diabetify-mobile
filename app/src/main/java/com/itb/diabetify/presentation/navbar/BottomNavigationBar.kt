package com.itb.diabetify.presentation.navbar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun BottomNavigationBar(
    modifier: Modifier = Modifier,
    viewModel: NavigationViewModel = NavigationViewModel(),
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
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
        modifier = modifier
    ) {
        NavigationBar(
            modifier = Modifier.fillMaxWidth(),
            containerColor = backgroundColor,
            contentColor = contentColor,
            tonalElevation = elevation
        ) {
            items.forEachIndexed { index, item ->
                if (index == middleIndex) {
                    // Empty space for the add button
                    NavigationBarItem(
                        selected = false,
                        onClick = { },
                        icon = {
                            // Transparent spacer
                            Spacer(modifier = Modifier.size(24.dp))
                        },
                        label = { Text("") }
                    )
                }

                NavigationBarItem(
                    selected = selectedItem == item.route,
                    onClick = {
                        viewModel.onNavigationItemSelected(item.route)
                        onItemSelected(item.route)
                    },
                    icon = {
                        Icon(
                            painter = painterResource(id = item.icon),
                            contentDescription = item.contentDescription
                        )
                    },
                )
            }
        }

        // Floating Add Button
        FloatingActionButton(
            onClick = {
                showPopup = true
            },
            modifier = Modifier
                .align(Alignment.Center),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            Icon(
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
            tonalElevation = 8.dp
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