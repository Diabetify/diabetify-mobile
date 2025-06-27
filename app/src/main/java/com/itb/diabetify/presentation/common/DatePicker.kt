package com.itb.diabetify.presentation.common

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerFormatter
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.itb.diabetify.R
import com.itb.diabetify.ui.theme.poppinsFontFamily
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@SuppressLint("RememberReturnType")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val today = remember {
        Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }.timeInMillis
    }

    val datePickerState = rememberDatePickerState(
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis <= today
            }
        }
    )

    val indonesianFormatter = remember {
        object : DatePickerFormatter {
            private val indonesianLocale = Locale("id", "ID")

            override fun formatDate(
                dateMillis: Long?,
                locale: Locale,
                forContentDescription: Boolean
            ): String? {
                return dateMillis?.let {
                    val pattern = if (forContentDescription) {
                        "EEEE, dd MMMM yyyy"
                    } else {
                        "d"
                    }
                    SimpleDateFormat(pattern, indonesianLocale).format(it)
                }
            }

            override fun formatMonthYear(monthMillis: Long?, locale: Locale): String? {
                return monthMillis?.let {
                    SimpleDateFormat("MMMM yyyy", indonesianLocale).format(it)
                }
            }
        }
    }

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    ) {
        DatePicker(
            state = datePickerState,
            title = {},
            headline = {
                Text(
                    text = "Pilih Tanggal",
                    fontFamily = poppinsFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 25.sp,
                    color = colorResource(id = R.color.white),
                    modifier = Modifier.padding(20.dp)
                )
            },
            dateFormatter = indonesianFormatter,
        )
    }
}