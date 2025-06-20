package com.itb.diabetify.presentation.onboarding

import com.itb.diabetify.R

data class PageData(
    val isGetStartedPage: Boolean = false,
    val imageResId: Int = 0,
    val title: String = "",
    val description: String = ""
)

val pages = listOf(
    PageData(
        imageResId = R.drawable.onboarding1,
        title = "Kenali Risiko\nDiabetes Anda",
        description = "Lakukan survei awal dan input data harian Anda. Diabetify akan memprediksi risiko diabetes berdasarkan data kesehatan dan gaya hidup Anda."
    ),
    PageData(
        imageResId = R.drawable.onboarding2,
        title = "Prediksi Transparan\ndengan XAI",
        description = "Teknologi Explainable AI (XAI) membantu Anda memahami alasan di balik setiap prediksi risiko. Ketahui faktor mana yang paling berpengaruh terhadap kesehatan Anda."
    ),
    PageData(
        imageResId = R.drawable.onboarding3,
        title = "Simulasikan dan\nEksplorasi Faktor Risiko",
        description = "Gunakan fitur simulasi untuk memahami dampak perubahan gaya hidup terhadap risiko Anda. Dapatkan penjelasan transparan dari setiap faktor risiko."
    ),
    PageData(
        imageResId = R.drawable.onboarding4,
        title = "Ambil Kendali\natas Kesehatan Anda",
        description = "Tetap termotivasi dengan tujuan yang jelas, aktivitas harian, dan panduan personal. Diabetify mendukung Anda dalam membentuk kebiasaan sehat secara bertahap."
    )
)

