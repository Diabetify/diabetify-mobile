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
    onDismiss: () -> Unit,
    disableFutureDates: Boolean = true,
    minimumAgeYears: Int = 0
) {
    val today = remember {
        Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }.timeInMillis
    }

    val minimumDate = remember(minimumAgeYears) {
        if (minimumAgeYears > 0) {
            Calendar.getInstance().apply {
                add(Calendar.YEAR, -minimumAgeYears)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis
        } else {
            Long.MIN_VALUE
        }
    }

    val maxSelectableDate = remember(today, minimumDate, disableFutureDates, minimumAgeYears) {
        when {
            minimumAgeYears > 0 && disableFutureDates -> minOf(today, minimumDate)
            minimumAgeYears > 0 -> minimumDate
            disableFutureDates -> today
            else -> today
        }
    }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = maxSelectableDate,
        initialDisplayedMonthMillis = maxSelectableDate,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val isFutureDateValid = if (disableFutureDates) {
                    utcTimeMillis <= today
                } else {
                    true
                }

                val isAgeValid = if (minimumAgeYears > 0) {
                    utcTimeMillis <= minimumDate
                } else {
                    true
                }

                return isFutureDateValid && isAgeValid
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