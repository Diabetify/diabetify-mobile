package com.example.diabetify.presentation.onboarding

import com.example.diabetify.R

data class Page(
    val isGetStartedPage: Boolean = false,
    val imageResId: Int = 0,
    val title: String = "",
    val description: String = ""
)

val pages = listOf(
    Page(
        imageResId = R.drawable.onboarding1,
        title = "Catat Kebiasaan\nHarian Anda",
        description = "Pantau pola makanan, tidur, olahraga, dan lainnya dengan mudah. Input harian Anda membantu Diabetify memberikan wawasan personal untuk manajemen kesehatan yang lebih baik."
    ),
    Page(
        imageResId = R.drawable.onboarding1,
        title = "Kenali Faktor\nRisiko Anda",
        description = "Analisis berbasis AI kami membantu Anda mengidentifikasi potensi faktor risiko diabetes berdasarkan gaya hidup dan data kesehatan Anda. Dapatkan wawasan yang jelas dan mulai langkah pencegahan sejak dini."
    ),
    Page(
        imageResId = R.drawable.onboarding3,
        title = "Tips Kesehatan\nyang Dipersonalisasi",
        description = "Dapatkan rekomendasi khusus untuk mengurangi risiko diabetes dan meningkatkan kesehatan Anda. Perubahan kecil hari ini menciptakan masa depan yang lebih sehat."
    ),
    Page(
        imageResId = R.drawable.onboarding3,
        title = "Kesehatan Anda,\nTujuan Anda",
        description = "Tetapkan tujuan kesehatan yang dipersonalisasi dan pantau progres Anda. Tetap termotivasi dan lakukan perbaikan secara bertahap dengan dukungan Diabetify."
    )
)

