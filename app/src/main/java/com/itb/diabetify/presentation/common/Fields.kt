package com.itb.diabetify.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.itb.diabetify.R
import com.itb.diabetify.ui.theme.poppinsFontFamily

@Composable
fun DropdownField(
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    options: List<String>,
    placeHolderText: String,
    iconResId: Int,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorMessage: String = ""
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .background(
                    color = colorResource(id = R.color.white_2),
                    shape = RoundedCornerShape(16.dp)
                )
                .border(
                    width = 1.dp,
                    color = if (isError) Color.Red else Color.Transparent,
                    shape = RoundedCornerShape(16.dp)
                )
                .clickable { expanded = true }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = iconResId),
                    contentDescription = "Dropdown Icon",
                    tint = if (isError) Color.Red else colorResource(id = R.color.gray),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = selectedOption.ifEmpty { placeHolderText },
                        fontFamily = poppinsFontFamily,
                        color = if (selectedOption.isEmpty()) colorResource(id = R.color.gray_2) else Color.Black,
                        fontSize = 12.sp
                    )
                }
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown Arrow",
                    tint = if (isError) Color.Red else colorResource(id = R.color.gray)
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .background(colorResource(id = R.color.white_2))
                    .width(with(LocalDensity.current) {
                        (48 * density).dp
                    })
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        onClick = {
                            onOptionSelected(option)
                            expanded = false
                        },
                        text = {
                            Text(
                                text = option,
                                fontFamily = poppinsFontFamily,
                                color = Color.Black,
                                fontSize = 12.sp
                            )
                        }
                    )
                }
            }
        }

        if (isError) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 10.sp,
                fontFamily = poppinsFontFamily,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .align(Alignment.BottomStart)
                    .offset(y = 18.dp)
            )
        }
    }
}

@Composable
fun InputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholderText: String,
    iconResId: Int,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    visualTransformation: VisualTransformation? = VisualTransformation.None,
    singleLine: Boolean? = true,
    enabled: Boolean? = true,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    errorMessage: String = "",
    testTag: String = ""
) {
    Box(
        modifier = modifier
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .height(48.dp)
                .fillMaxWidth()
                .background(
                    color = colorResource(id = R.color.white_2),
                    shape = RoundedCornerShape(16.dp)
                )
                .border(
                    width = 1.dp,
                    color = if (isError) Color.Red else Color.Transparent,
                    shape = RoundedCornerShape(16.dp)
                )
                .then(if (testTag.isNotEmpty()) Modifier.testTag(testTag) else Modifier),
            textStyle = TextStyle(
                fontFamily = poppinsFontFamily,
                fontSize = 12.sp
            ),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            visualTransformation = visualTransformation ?: VisualTransformation.None,
            enabled = enabled ?: true,
            singleLine = singleLine ?: true,
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = iconResId),
                        contentDescription = null,
                        tint = if (isError) Color.Red else colorResource(id = R.color.gray),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Box(
                        modifier = Modifier.weight(1f)
                    ) {
                        if (value.isEmpty()) {
                            Text(
                                text = placeholderText,
                                fontFamily = poppinsFontFamily,
                                color = colorResource(id = R.color.gray_2),
                                fontSize = 12.sp
                            )
                        }
                        innerTextField()
                    }
                    trailingIcon?.invoke()
                }
            }
        )

        if (isError) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 10.sp,
                fontFamily = poppinsFontFamily,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .align(Alignment.BottomStart)
                    .offset(y = 18.dp)
            )
        }
    }
}