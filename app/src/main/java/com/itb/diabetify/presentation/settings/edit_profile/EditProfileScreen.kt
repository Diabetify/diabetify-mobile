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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.itb.diabetify.R
import com.itb.diabetify.presentation.common.DatePickerModal
import com.itb.diabetify.presentation.common.DropdownField
import com.itb.diabetify.presentation.common.ErrorNotification
import com.itb.diabetify.presentation.common.InputField
import com.itb.diabetify.presentation.common.PrimaryButton
import com.itb.diabetify.presentation.common.SuccessNotification
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
    // States
    val nameState by viewModel.nameState
    val emailState by viewModel.emailState
    val genderState by viewModel.genderState
    val birthDateState by viewModel.dobState
    var showImagePicker by remember { mutableStateOf(false) }
    val showDatePicker = remember { mutableStateOf(false) }
    val isLoading = viewModel.editProfileState.value.isLoading
    val errorMessage = viewModel.errorMessage.value
    val successMessage = viewModel.successMessage.value

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
                                    painter = painterResource(id = R.drawable.ic_profile_picture),
                                    contentDescription = "Profile Image",
                                    modifier = Modifier
                                        .size(140.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Name field
                    Text(
                        text = "Nama",
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.primary),
                        textAlign = TextAlign.Start
                    )
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

                    // Email field
                    Text(
                        text = "Email",
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.primary),
                        textAlign = TextAlign.Start
                    )
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

                    // Gender dropdown
                    val options = listOf("Laki-laki", "Perempuan")
                    Text(
                        text = "Jenis Kelamin",
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.primary),
                        textAlign = TextAlign.Start
                    )
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

                    // Birth date field
                    Text(
                        text = "Tanggal Lahir",
                        fontFamily = poppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = colorResource(id = R.color.primary),
                        textAlign = TextAlign.Start
                    )
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

            // Bottom button
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
                enabled = nameState.error == null && emailState.error == null && genderState.error == null && birthDateState.error == null,
                isLoading = isLoading
            )
        }

        // Error notification
        ErrorNotification(
            showError = errorMessage != null,
            errorMessage = errorMessage,
            onDismiss = { viewModel.onErrorShown() },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .zIndex(1000f)
        )

        // Success notification
        SuccessNotification(
            showSuccess = successMessage != null,
            successMessage = successMessage,
            onDismiss = { viewModel.onSuccessShown() },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .zIndex(1000f)
        )
    }
}