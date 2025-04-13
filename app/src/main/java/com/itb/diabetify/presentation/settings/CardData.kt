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

val cards = listOf(
    CardData(
        title = "Account",
        contents = listOf(
            ContentData(
                icon = R.drawable.ic_personal,
                name = "Personal Data",
                onClick = { }
            )
        )
    ),
    CardData(
        title = "Other",
        contents = listOf(
            ContentData(
                icon = R.drawable.ic_message,
                name = "Contact Us",
                onClick = { }
            ),
            ContentData(
                icon = R.drawable.ic_shield,
                name = "Privacy Policy",
                onClick = { }
            ),
            ContentData(
                icon = R.drawable.ic_settings,
                name = "Settings",
                onClick = { }
            )
        )
    )
)