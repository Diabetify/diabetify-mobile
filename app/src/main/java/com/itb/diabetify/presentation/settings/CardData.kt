package com.itb.diabetify.presentation.settings

import android.content.Context
import android.content.Intent
import com.itb.diabetify.R

data class CardData(
    val title: String = "",
    val contents: List<ContentData> = emptyList()
)

data class ContentData(
    val icon: Int = R.drawable.ic_personal,
    val name: String = "",
    val onClick: () -> Unit = {}
)

fun getSettingsCards(context: Context) = listOf(
    CardData(
        title = "Lainnya",
        contents = listOf(
            ContentData(
                icon = R.drawable.ic_message,
                name = "Kontak Kami",
                onClick = {
                    try {
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_EMAIL, arrayOf("diabetify@gmail.com"))
                            putExtra(Intent.EXTRA_SUBJECT, "Kontak Kami - Diabetify")
                            putExtra(Intent.EXTRA_TEXT, "Halo Tim Diabetify,\n\n")
                        }
                        val chooser = Intent.createChooser(intent, "Kirim Email")
                        context.startActivity(chooser)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            ),
            ContentData(
                icon = R.drawable.ic_shield,
                name = "Privasi dan Ketentuan",
                onClick = { }
            ),
        )
    )
)