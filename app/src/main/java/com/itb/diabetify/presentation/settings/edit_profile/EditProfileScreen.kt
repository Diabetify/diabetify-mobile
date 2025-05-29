package com.itb.diabetify.presentation.settings.edit_profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.icons.filled.Clear
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.itb.diabetify.R
import com.itb.diabetify.presentation.common.DatePickerModal
import com.itb.diabetify.presentation.common.DropdownField
import com.itb.diabetify.presentation.common.InputField
import com.itb.diabetify.presentation.common.PrimaryButton
import com.itb.diabetify.presentation.settings.SettingsViewModel
import com.itb.diabetify.ui.theme.poppinsFontFamily
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun EditProfileScreen(
    navController: NavController,
    viewModel: SettingsViewModel
) {
    val nameState = viewModel.nameState.value
    val emailState = viewModel.emailState.value
    val genderState = viewModel.genderState.value
    val birthDateState = viewModel.dobState.value

    var showImagePicker by remember { mutableStateOf(false) }
    val showDatePicker = remember { mutableStateOf(false) }

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

                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Box() {
                            // Profile image section
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
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Label for name field
                    Text(
                        text = "Nama",
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.primary),
                        textAlign = TextAlign.Start
                    )

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

                    Spacer(modifier = Modifier.height(6.dp))

                    // Label for email field
                    Text(
                        text = "Email",
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.primary),
                        textAlign = TextAlign.Start
                    )

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

                    Spacer(modifier = Modifier.height(6.dp))

                    // Label for gender field
                    Text(
                        text = "Jenis Kelamin",
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.primary),
                        textAlign = TextAlign.Start
                    )

                    val options = listOf("Laki-laki", "Perempuan")
                    // Gender dropdown
                    DropdownField(
                        selectedOption = genderState.text,
                        onOptionSelected = { viewModel.setGender(it) },
                        options = options,
                        placeHolderText = "Pilih Jenis Kelamin",
                        iconResId = R.drawable.ic_users,
                        modifier = Modifier.fillMaxWidth(),
                        isError = genderState.error != null,
                        errorMessage = genderState.error ?: ""
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Label for birth date field
                    Text(
                        text = "Tanggal Lahir",
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.primary),
                        textAlign = TextAlign.Start
                    )

                    // Birth date field
                    InputField(
                        value = birthDateState.text,
                        onValueChange = { },
                        placeholderText = "Tanggal Lahir",
                        iconResId = R.drawable.ic_calendar,
                        modifier = Modifier.clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            showDatePicker.value = true
                        },
                        enabled = false,
                        trailingIcon = {
                            if (birthDateState.text.isNotEmpty()) {
                                IconButton(
                                    onClick = { viewModel.setDob("") },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Clear date",
                                        tint = colorResource(id = R.color.gray),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        },
                        isError = birthDateState.error != null,
                        errorMessage = birthDateState.error ?: ""
                    )

                    // Date picker dialog
                    if (showDatePicker.value) {
                        DatePickerModal(
                            onDateSelected = { dateMillis ->
                                dateMillis?.let {
                                    val date = Date(it)
                                    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                                    viewModel.setDob(formatter.format(date))
                                }
                            },
                            onDismiss = { showDatePicker.value = false }
                        )
                    }
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