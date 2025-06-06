package com.itb.diabetify.presentation.guide.guide_detail

data class GuideDetailData(
    val title: String,
    val content: List<GuideSection>
)

data class GuideSection(
    val title: String,
    val content: String,
    val imageResId: Int? = null
)

val guideDetails = mapOf(
    "about_diabetes" to GuideDetailData(
        title = "Tentang Diabetes",
        content = listOf(
            GuideSection(
                title = "Apa itu Diabetes?",
                content = """
                    Diabetes adalah kondisi kronis yang terjadi ketika tubuh tidak dapat menghasilkan insulin yang cukup atau tidak dapat menggunakan insulin secara efektif. Insulin adalah hormon yang diproduksi oleh pankreas yang memungkinkan glukosa dari makanan masuk ke dalam sel-sel tubuh untuk diubah menjadi energi.
                    
                    Ketika seseorang memiliki diabetes, tubuhnya tidak dapat mengatur kadar gula darah dengan baik, yang dapat menyebabkan kadar gula darah yang terlalu tinggi (hiperglikemia) atau terlalu rendah (hipoglikemia).
                """
            ),
            GuideSection(
                title = "Jenis-jenis Diabetes",
                content = """
                    1. Diabetes Tipe 1
                    • Terjadi ketika sistem kekebalan tubuh menyerang dan menghancurkan sel-sel penghasil insulin
                    • Biasanya didiagnosis pada anak-anak dan remaja
                    • Memerlukan insulin setiap hari untuk bertahan hidup
                    
                    2. Diabetes Tipe 2
                    • Terjadi ketika tubuh menjadi resisten terhadap insulin
                    • Paling umum terjadi, sekitar 90% dari semua kasus diabetes
                    • Dapat dicegah atau ditunda dengan gaya hidup sehat
                    
                    3. Diabetes Gestasional
                    • Terjadi selama kehamilan
                    • Biasanya hilang setelah melahirkan
                    • Meningkatkan risiko diabetes tipe 2 di kemudian hari
                """
            ),
            GuideSection(
                title = "Gejala Umum Diabetes",
                content = """
                    • Sering merasa haus dan sering buang air kecil
                    • Merasa sangat lapar
                    • Penurunan berat badan tanpa sebab yang jelas
                    • Penglihatan kabur
                    • Luka yang sulit sembuh
                    • Mudah lelah
                    • Kesemutan pada tangan atau kaki
                """
            )
        )
    ),
    "risk_factors" to GuideDetailData(
        title = "Faktor Risiko",
        content = listOf(
            GuideSection(
                title = "Faktor Risiko yang Dapat Dimodifikasi",
                content = """
                    1. Indeks Massa Tubuh (IMT)
                    • IMT tinggi menunjukkan kelebihan berat badan atau obesitas
                    • Meningkatkan resistensi insulin
                    • Target: IMT 18.5-24.9 kg/m²
                    
                    2. Aktivitas Fisik
                    • Kurang aktivitas fisik meningkatkan risiko diabetes
                    • Rekomendasi: minimal 150 menit aktivitas sedang per minggu
                    • Contoh: jalan cepat, berenang, bersepeda
                    
                    3. Pola Makan
                    • Konsumsi tinggi gula dan lemak jenuh
                    • Kurang serat dan sayuran
                    • Porsi makan berlebihan
                    
                    4. Merokok
                    • Meningkatkan resistensi insulin
                    • Memperburuk komplikasi diabetes
                    • Semakin lama dan banyak merokok, semakin tinggi risiko
                """
            ),
            GuideSection(
                title = "Faktor Risiko yang Tidak Dapat Dimodifikasi",
                content = """
                    1. Usia
                    • Risiko meningkat seiring bertambahnya usia
                    • Terutama setelah usia 45 tahun
                    
                    2. Riwayat Keluarga
                    • Memiliki orangtua atau saudara dengan diabetes
                    • Faktor genetik berperan penting
                    
                    3. Riwayat Diabetes Gestasional
                    • Wanita yang pernah mengalami diabetes selama kehamilan
                    • Melahirkan bayi dengan berat >4 kg
                    
                    4. Etnis
                    • Beberapa kelompok etnis memiliki risiko lebih tinggi
                    • Termasuk Asia Tenggara, termasuk Indonesia
                """
            )
        )
    ),
    "about_xai" to GuideDetailData(
        title = "Tentang XAI",
        content = listOf(
            GuideSection(
                title = "Apa itu XAI?",
                content = """
                    XAI (Explainable Artificial Intelligence) atau Kecerdasan Buatan yang Dapat Dijelaskan adalah pendekatan dalam pengembangan sistem AI yang tidak hanya memberikan prediksi atau keputusan, tetapi juga dapat menjelaskan alasan di balik keputusan tersebut.
                    
                    Dalam konteks Diabetify, XAI membantu menjelaskan bagaimana berbagai faktor berkontribusi terhadap risiko diabetes seseorang.
                """
            ),
            GuideSection(
                title = "Mengapa XAI Penting?",
                content = """
                    1. Transparansi
                    • Memahami alasan di balik prediksi
                    • Meningkatkan kepercayaan terhadap sistem
                    
                    2. Edukasi
                    • Membantu pengguna memahami faktor risiko mereka
                    • Memberikan wawasan untuk perbaikan gaya hidup
                    
                    3. Personalisasi
                    • Memungkinkan rekomendasi yang lebih tepat sasaran
                    • Membantu fokus pada faktor yang paling berpengaruh
                """
            ),
            GuideSection(
                title = "Bagaimana XAI Bekerja di Diabetify",
                content = """
                    1. Pengumpulan Data
                    • Informasi kesehatan dasar
                    • Gaya hidup dan kebiasaan
                    • Riwayat medis
                    
                    2. Analisis
                    • Mengidentifikasi pola dan hubungan
                    • Menghitung kontribusi setiap faktor
                    • Membandingkan dengan data populasi
                    
                    3. Penjelasan
                    • Visualisasi kontribusi faktor risiko
                    • Rekomendasi personal
                    • Panduan perbaikan gaya hidup
                """
            )
        )
    ),
    "ai_prediction" to GuideDetailData(
        title = "Prediksi AI",
        content = listOf(
            GuideSection(
                title = "Cara Kerja Prediksi",
                content = """
                    Diabetify menggunakan model AI canggih yang telah dilatih dengan data kesehatan dari ribuan individu. Model ini menganalisis berbagai faktor risiko untuk memberikan prediksi risiko diabetes yang akurat dan personal.
                    
                    Proses prediksi melibatkan:
                    1. Pengumpulan data pengguna
                    2. Analisis faktor risiko
                    3. Perhitungan skor risiko
                    4. Penjelasan kontribusi setiap faktor
                """
            ),
            GuideSection(
                title = "Interpretasi Hasil",
                content = """
                    Skor Risiko:
                    • 0-30%: Risiko Rendah
                    • 31-50%: Risiko Sedang
                    • 51-65%: Risiko Tinggi
                    • >65%: Risiko Sangat Tinggi
                    
                    Setiap faktor risiko ditampilkan dengan:
                    • Persentase kontribusi
                    • Penjelasan pengaruh
                    • Rekomendasi perbaikan
                """
            ),
            GuideSection(
                title = "Menggunakan Hasil Prediksi",
                content = """
                    1. Pahami Faktor Risiko Anda
                    • Identifikasi faktor dengan kontribusi tertinggi
                    • Fokus pada faktor yang dapat dimodifikasi
                    
                    2. Ambil Tindakan
                    • Ikuti rekomendasi untuk setiap faktor
                    • Tetapkan target perbaikan yang realistis
                    • Pantau perubahan secara berkala
                    
                    3. Konsultasi dengan Profesional
                    • Bagikan hasil prediksi dengan dokter
                    • Dapatkan panduan medis yang sesuai
                    • Buat rencana pencegahan personal
                """
            )
        )
    )
) 