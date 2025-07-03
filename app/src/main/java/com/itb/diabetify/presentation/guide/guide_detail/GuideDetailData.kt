package com.itb.diabetify.presentation.guide.guide_detail

import com.itb.diabetify.R

data class GuideDetailData(
    val title: String,
    val content: List<GuideSection>
)

data class GuideSection(
    val title: String,
    val content: String,
    val imageResId: Int? = null,
    val source: GuideSource? = null
)

data class GuideSource(
    val title: String,
    val url: String
)

val guideDetails = mapOf(
    "about_diabetes" to GuideDetailData(
        title = "Tentang Diabetes",
        content = listOf(
            GuideSection(
                title = "Apa itu Diabetes?",
                content = """Diabetes adalah penyakit kronis yang ditandai dengan kadar gula darah (glukosa) yang tinggi akibat gangguan produksi atau kerja insulin. Insulin adalah hormon yang membantu glukosa masuk ke dalam sel untuk digunakan sebagai energi. Jika insulin tidak cukup atau tidak bekerja dengan baik, glukosa menumpuk di dalam darah dan dapat menyebabkan berbagai komplikasi kesehatan.

Diabetes dapat menyebabkan masalah serius seperti penyakit jantung, kerusakan ginjal, gangguan penglihatan, dan gangguan saraf jika tidak dikelola dengan baik.""",
                source = GuideSource(
                    title = "World Health Organization - Diabetes",
                    url = "https://www.who.int/news-room/fact-sheets/detail/diabetes"
                )
            ),
            GuideSection(
                title = "Jenis-jenis Diabetes",
                content = """1. Diabetes Tipe 1
• Disebabkan oleh kerusakan sistem imun yang menyerang sel penghasil insulin di pankreas
• Umumnya muncul pada anak-anak atau dewasa muda
• Penderita memerlukan suntikan insulin setiap hari

2. Diabetes Tipe 2
• Terjadi ketika tubuh tidak menggunakan insulin secara efektif (resistensi insulin) atau produksi insulin menurun
• Merupakan jenis diabetes yang paling banyak ditemukan (sekitar 90% kasus)
• Sering terkait dengan gaya hidup seperti pola makan tidak sehat, kurang aktivitas fisik, dan obesitas
• Dapat dicegah atau dikendalikan dengan perubahan gaya hidup sehat

3. Diabetes Gestasional
• Terjadi pada wanita selama masa kehamilan
• Biasanya hilang setelah melahirkan, namun meningkatkan risiko diabetes tipe 2 di kemudian hari bagi ibu dan anak""",
                source = GuideSource(
                    title = "CDC - Diabetes Basics",
                    url = "https://www.cdc.gov/diabetes/about/?CDC_AAref_Val=https://www.cdc.gov/diabetes/basics/diabetes.html"
                )
            ),
            GuideSection(
                title = "Gejala Umum Diabetes",
                content = """• Sering merasa haus dan sering buang air kecil
• Mudah merasa lapar
• Penurunan berat badan tanpa sebab yang jelas
• Penglihatan kabur
• Luka yang sulit sembuh
• Mudah lelah atau lemas
• Kesemutan atau mati rasa di tangan dan kaki""",
                source = GuideSource(
                    title = "Kementerian Kesehatan - Mengenal Gejala Diabetes Melitus",
                    url = "https://upk.kemkes.go.id/new/mengenal-gejala-diabetes-melitus"
                )
            )
        )
    ),
    "risk_factors" to GuideDetailData(
        title = "Faktor Risiko",
        content = listOf(
            GuideSection(
                title = "Faktor Risiko yang Dapat Dimodifikasi",
                content = """1. Merokok (Status Merokok & Indeks Brinkman)
• Merokok meningkatkan risiko resistensi insulin dan komplikasi diabetes.
• Brinkman Index (jumlah batang rokok per hari dikali tahun merokok) digunakan untuk mengukur paparan rokok seumur hidup. Semakin tinggi nilainya, semakin besar risikonya.

2. Indeks Massa Tubuh (BMI)
• Kelebihan berat badan atau obesitas meningkatkan risiko diabetes tipe 2 secara signifikan.

3. Frekuensi Aktivitas Fisik
• Kurangnya frekuensi aktivitas fisik meningkatkan risiko diabetes.
• Aktivitas rutin seperti berjalan cepat, bersepeda, atau berenang membantu menurunkan risiko.

4. Hipertensi
• Tekanan darah tinggi sering ditemukan bersamaan dengan diabetes dan meningkatkan risiko komplikasi.

5. Kolesterol Tinggi
• Kadar kolesterol tinggi berhubungan dengan risiko diabetes dan penyakit kardiovaskular.""",
                source = GuideSource(
                    title = "CDC - Diabetes Risk Factors",
                    url = "https://www.cdc.gov/diabetes/risk-factors/?CDC_AAref_Val=https://www.cdc.gov/diabetes/basics/risk-factors.html"
                )
            ),
            GuideSection(
                title = "Faktor Risiko yang Tidak Dapat Dimodifikasi",
                content = """1. Usia
• Risiko diabetes meningkat seiring bertambahnya usia.

2. Riwayat Keluarga
• Memiliki orang tua yang mempunyai riwayat diabetes meningkatkan risiko karena faktor genetik.

3. Riwayat Bayi Makrosomia
• Wanita yang pernah melahirkan bayi dengan berat lahir ≥4 kg lebih berisiko terkena diabetes tipe 2.""",
                source = GuideSource(
                    title = "CDC - Diabetes Risk Factors",
                    url = "https://www.cdc.gov/diabetes/risk-factors/?CDC_AAref_Val=https://www.cdc.gov/diabetes/basics/risk-factors.html"
                )
            )
        )
    ),
    "about_xai" to GuideDetailData(
        title = "Tentang XAI",
        content = listOf(
            GuideSection(
                title = "Apa itu XAI?",
                content = """XAI (Explainable Artificial Intelligence) atau Kecerdasan Buatan yang Dapat Dijelaskan adalah pendekatan dalam pengembangan sistem AI yang tidak hanya memberikan prediksi atau keputusan, tetapi juga dapat menjelaskan alasan di balik keputusan tersebut.
                    
Dalam konteks Diabetify, XAI membantu menjelaskan bagaimana berbagai faktor berkontribusi terhadap risiko diabetes seseorang.""",
                source = GuideSource(
                    title = "Explaining Explanations - An Overview of Interpretability of Machine Learning",
                    url = "https://arxiv.org/pdf/1806.00069"
                )
            ),
            GuideSection(
                title = "Mengapa XAI Penting?",
                content = """1. Transparansi
• Memahami alasan di balik prediksi
• Meningkatkan kepercayaan terhadap sistem

2. Edukasi
• Membantu pengguna memahami faktor risiko mereka
• Memberikan wawasan untuk perbaikan gaya hidup

3. Personalisasi
• Memungkinkan rekomendasi yang lebih tepat sasaran
• Membantu fokus pada faktor yang paling berpengaruh""",
                source = GuideSource(
                    title = "Towards A Rigorous Science of Interpretable Machine Learning",
                    url = "https://arxiv.org/pdf/1702.08608"
                )
            ),
            GuideSection(
                title = "Hasil XAI di Diabetify",
                content = """Gambar berikut menunjukkan bagaimana XAI digunakan di Diabetify untuk menjelaskan kontribusi berbagai faktor risiko terhadap skor risiko diabetes pengguna. Setiap faktor ditampilkan dengan persentase kontribusi dan penjelasan singkat mengenai pengaruhnya terhadap risiko diabetes.""",
                imageResId = R.drawable.food,
            )
        )
    ),
    "ai_prediction" to GuideDetailData(
        title = "Prediksi AI",
        content = listOf(
            GuideSection(
                title = "Bagaimana AI dan XAI Bekerja di Diabetify",
                content = """1. Pengumpulan Data
• Informasi kesehatan dasar
• Gaya hidup dan kebiasaan
• Riwayat medis

2. Analisis
• Mengidentifikasi pola dan hubungan
• Menghitung kontribusi setiap faktor
• Membandingkan dengan data populasi

3. Penjelasan
• Visualisasi kontribusi faktor risiko
• Narasi tentang pengaruh masing-masing faktor risiko"""
            ),
            GuideSection(
                title = "Validitas Hasil Prediksi",
                content = """Diabetify menggunakan model AI canggih yang telah dilatih dengan data kesehatan dari ribuan individu. Model ini menganalisis berbagai faktor risiko untuk memberikan prediksi risiko diabetes yang akurat dan personal.
                    
Proses prediksi melibatkan:
1. Pengumpulan data pengguna
2. Analisis faktor risiko
3. Perhitungan skor risiko
4. Penjelasan kontribusi setiap faktor
                """
            ),
            GuideSection(
                title = "Menggunakan Hasil Prediksi",
                content = """1. Pahami Faktor Risiko Anda
• Identifikasi faktor dengan kontribusi tertinggi
• Fokus pada faktor yang dapat dimodifikasi

2. Ambil Tindakan
• Tetapkan target perbaikan yang realistis
• Pantau perubahan secara berkala

3. Konsultasi dengan Profesional
• Bagikan hasil prediksi dengan dokter
• Dapatkan panduan medis yang sesuai
• Buat rencana pencegahan personal"""
            )
        )
    )
) 