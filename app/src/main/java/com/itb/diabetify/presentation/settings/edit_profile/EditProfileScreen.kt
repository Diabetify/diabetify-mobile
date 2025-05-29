package com.itb.diabetify.presentation.settings.edit_profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.itb.diabetify.R
import com.itb.diabetify.presentation.common.InputField
import com.itb.diabetify.presentation.common.PrimaryButton
import com.itb.diabetify.presentation.settings.SettingsViewModel
import com.itb.diabetify.ui.theme.poppinsFontFamily

@Composable
fun EditProfileScreen(
    navController: NavController,
    viewModel: SettingsViewModel
) {
    val nameState = viewModel.nameState.value
    val emailState = viewModel.emailState.value
    var showImagePicker by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.white))
    ) {
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Scrollable content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState)
                    .padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp)
                ) {
                    IconButton(
                        modifier = Modifier.align(Alignment.CenterStart),
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = colorResource(id = R.color.primary)
                        )
                    }

                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = "Edit Profil",
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = colorResource(id = R.color.primary)
                    )
                }

                Spacer(modifier = Modifier.height(15.dp))

                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Profile image section
                    Box(
                        modifier = Modifier.padding(bottom = 32.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(150.dp)
                                .clip(CircleShape)
                                .background(colorResource(id = R.color.primary).copy(alpha = 0.1f))
                                .clickable { showImagePicker = true },
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_launcher_background),
                                contentDescription = "Profile Image",
                                modifier = Modifier
                                    .size(140.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }

                        // Edit icon overlay
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .align(Alignment.BottomEnd)
                                .offset(x = (-4).dp, y = (-4).dp)
                                .clip(CircleShape)
                                .background(colorResource(id = R.color.primary))
                                .clickable { showImagePicker = true },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "Edit Photo",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Name field
                    InputField(
                        value = nameState.text,
                        onValueChange = { viewModel.setName(it) },
                        placeholderText = "Nama Anda",
                        iconResId = R.drawable.ic_profile,
                        modifier = Modifier.fillMaxWidth(),
                        keyboardType = KeyboardType.Text,
                        isError = nameState.error != null,
                        errorMessage = nameState.error ?: ""
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Email field
                    InputField(
                        value = emailState.text,
                        onValueChange = { viewModel.setEmail(it) },
                        placeholderText = "Email",
                        iconResId = R.drawable.ic_message,
                        modifier = Modifier.fillMaxWidth(),
                        keyboardType = KeyboardType.Email,
                        isError = emailState.error != null,
                        errorMessage = emailState.error ?: ""
                    )
                }
            }

            // Bottom button - fixed at bottom
            PrimaryButton(
                text = "Simpan Perubahan",
                onClick = {
                    val isValid = viewModel.validateEditProfileFields()
                    if (isValid) {
                        viewModel.editProfile()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp),
                enabled = nameState.error == null && emailState.error == null
            )
        }
    }

    if (showImagePicker) {
        AlertDialog(
            onDismissRequest = { showImagePicker = false },
            title = { Text("Change Profile Photo") },
            text = { Text("Choose how you'd like to update your profile photo") },
            confirmButton = {
                TextButton(
                    onClick = {
                        // Handle camera selection
                        showImagePicker = false
                    }
                ) {
                    Text("Camera")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        // Handle gallery selection
                        showImagePicker = false
                    }
                ) {
                    Text("Gallery")
                }
            }
        )
    }
}