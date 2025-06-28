package com.itb.diabetify.presentation.guide

data class FAQCardData(
    val question: String = "",
    val answer: String = ""
)

val faqCards = listOf(
    FAQCardData(
        question = "Seberapa akurat prediksi risiko diabetes dari aplikasi ini?",
        answer = "Aplikasi Diabetify menggunakan model AI yang telah dilatih dengan data medis yang luas dan divalidasi oleh para ahli. Tingkat akurasi mencapai sekitar 85-90%, namun hasil ini bukanlah diagnosis medis. Prediksi ini adalah estimasi risiko berdasarkan faktor-faktor yang Anda masukkan. Untuk diagnosis pasti, Anda tetap perlu konsultasi dengan dokter dan melakukan pemeriksaan medis yang tepat."
    ),
    FAQCardData(
        question = "Apakah hasil prediksi bisa menggantikan konsultasi dokter?",
        answer = "Tidak. Hasil prediksi Diabetify adalah alat bantu untuk mengetahui risiko diabetes, bukan pengganti konsultasi medis profesional. Aplikasi ini membantu Anda memahami faktor risiko dan memberikan rekomendasi gaya hidup sehat. Jika hasil menunjukkan risiko tinggi atau Anda memiliki gejala diabetes, sangat disarankan untuk segera berkonsultasi dengan dokter untuk pemeriksaan dan diagnosis yang tepat."
    ),
    FAQCardData(
        question = "Apakah data pribadi saya aman di aplikasi ini?",
        answer = "Ya, keamanan data Anda adalah prioritas utama. Diabetify menggunakan enkripsi end-to-end untuk melindungi semua informasi kesehatan Anda. Data disimpan secara aman di server yang memenuhi standar keamanan medis. Kami tidak membagikan data pribadi Anda kepada pihak ketiga tanpa persetujuan."
    ),
    FAQCardData(
        question = "Seberapa sering saya harus melakukan input data kesehatan saya?",
        answer = "Disarankan untuk melakukan input data kesehatan harian (aktivitas merokok dan aktivitas fisik) setiap hari, sedangkan untuk data kesehatan non-harian diubah ketika ada perubahan signifikan pada kondisi kesehatan Anda, seperti perubahan berat badan, tekanan darah, dan lainnya. Ini membantu memantau perkembangan risiko diabetes Anda dari waktu ke waktu. Aplikasi juga akan mengingatkan Anda untuk update data secara berkala."
    ),
    FAQCardData(
        question = "Apa yang harus dilakukan jika hasil prediksi menunjukkan risiko tinggi?",
        answer = "Jika hasil menunjukkan risiko tinggi, jangan panik. Segera konsultasi dengan dokter untuk pemeriksaan lebih lanjut seperti tes gula darah puasa atau HbA1c. Gunakan fitur tips dan panduan dalam aplikasi untuk memulai gaya hidup sehat. Ingat, deteksi dini memungkinkan pencegahan yang lebih efektif."
    ),
    FAQCardData(
        question = "Apakah aplikasi ini cocok untuk semua usia?",
        answer = "Diabetify dirancang untuk dewasa berusia 20 tahun ke atas. Model prediksi dioptimalkan untuk kelompok usia ini karena risiko diabetes tipe 2 umumnya meningkat setelah usia 20 tahun. Untuk anak-anak atau remaja di bawah 20 tahun yang memiliki faktor risiko diabetes, disarankan untuk langsung berkonsultasi dengan dokter anak atau ahli endokrin."
    )
)