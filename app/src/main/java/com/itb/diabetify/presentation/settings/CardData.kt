package com.itb.diabetify.presentation.settings

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

fun getSettingsCards(onDataPribadiClick: () -> Unit) = listOf(
    CardData(
        title = "Akun",
        contents = listOf(
            ContentData(
                icon = R.drawable.ic_personal,
                name = "Edit Survey",
                onClick = onDataPribadiClick
            )
        )
    ),
    CardData(
        title = "Lainnya",
        contents = listOf(
            ContentData(
                icon = R.drawable.ic_message,
                name = "Kontak Kami",
                onClick = { }
            ),
            ContentData(
                icon = R.drawable.ic_shield,
                name = "Privasi dan Ketentuan",
                onClick = { }
            ),
            ContentData(
                icon = R.drawable.ic_settings,
                name = "Pengaturan",
                onClick = { }
            )
        )
    )
)