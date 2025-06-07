package com.itb.diabetify.presentation.guide.tips_detail

import com.itb.diabetify.R

data class TipsDetailData(
    val title: String,
    val image: Int,
    val content: List<TipsSection>
)

data class TipsSection(
    val title: String,
    val content: String
)

val tipsDetails = mapOf(
    "healthy_nutrition" to TipsDetailData(
        title = "Nutrisi Sehat untuk Pencegahan Diabetes",
        image = R.drawable.food,
        content = listOf(
            TipsSection(
                title = "Prinsip Dasar Nutrisi Sehat",
                content = """Nutrisi seimbang adalah kunci dalam pencegahan diabetes. Berikut adalah prinsip-prinsip dasar yang perlu diperhatikan:

• Porsi Makan: Gunakan prinsip "Piring Makan" dengan komposisi:
  - 1/2 piring sayuran
  - 1/4 piring protein
  - 1/4 piring karbohidrat

• Waktu Makan: Atur jadwal makan dengan teratur
  - Sarapan: 06.00-08.00
  - Makan Siang: 12.00-14.00
  - Makan Malam: 18.00-20.00
                """
            ),
            TipsSection(
                title = "Makanan yang Direkomendasikan",
                content = """1. Sumber Protein Sehat
• Ikan (salmon, tuna, makarel)
• Daging tanpa lemak
• Telur
• Tahu dan tempe

2. Sayuran Berwarna
• Bayam
• Brokoli
• Wortel
• Kale

3. Karbohidrat Kompleks
• Nasi merah
• Quinoa
• Oatmeal
• Roti gandum utuh
                """
            ),
            TipsSection(
                title = "Makanan yang Perlu Dibatasi",
                content = """1. Makanan Tinggi Gula
• Minuman manis
• Kue dan biskuit
• Permen dan cokelat
• Es krim

2. Makanan Olahan
• Makanan cepat saji
• Makanan kaleng
• Makanan instan

3. Makanan Tinggi Lemak Jenuh
• Gorengan
• Daging berlemak
• Kulit ayam
• Santan kental
                """
            ),
            TipsSection(
                title = "Tips Praktis Menjaga Pola Makan",
                content = """1. Perencanaan Menu
• Buat rencana menu mingguan
• Siapkan bekal untuk di kantor
• Belanja sesuai daftar kebutuhan

2. Cara Memasak yang Sehat
• Kukus atau rebus makanan
• Panggang dengan minimal minyak
• Gunakan bumbu rempah alami

3. Kebiasaan Makan yang Baik
• Kunyah makanan perlahan
• Hindari makan sambil beraktivitas lain
• Perhatikan porsi makan
• Minum air putih yang cukup
                """
            )
        )
    ),
    "physical_activity" to TipsDetailData(
        title = "Aktivitas Fisik dan Olahraga",
        image = R.drawable.food,
        content = listOf(
            TipsSection(
                title = "Pentingnya Aktivitas Fisik",
                content = """Aktivitas fisik teratur memiliki banyak manfaat untuk pencegahan diabetes:

• Meningkatkan sensitivitas insulin
• Membantu mengontrol berat badan
• Menurunkan risiko penyakit jantung
• Meningkatkan stamina dan energi
• Mengurangi stres dan kecemasan
                """
            ),
            TipsSection(
                title = "Rekomendasi Aktivitas Fisik",
                content = """1. Aktivitas Aerobik
• Jalan cepat: 30 menit/hari
• Bersepeda: 20-30 menit/hari
• Berenang: 2-3 kali/minggu
• Jogging: 20 menit/hari

2. Latihan Kekuatan
• Push-up
• Squat
• Plank
• Lunges

3. Aktivitas Sehari-hari
• Naik tangga
• Berkebun
• Membersihkan rumah
• Bermain dengan anak
                """
            ),
            TipsSection(
                title = "Cara Memulai Program Olahraga",
                content = """1. Mulai Bertahap
• Mulai dari 10 menit per hari
• Tingkatkan durasi secara perlahan
• Pilih aktivitas yang menyenangkan
• Tetapkan target realistis

2. Jadwal Latihan
• 3-5 kali per minggu
• Kombinasikan berbagai jenis aktivitas
• Sisipkan waktu istirahat
• Konsisten dengan jadwal
                """
            ),
            TipsSection(
                title = "Tips Keamanan Berolahraga",
                content = """1. Persiapan
• Pemanasan 5-10 menit
• Pakai sepatu dan pakaian yang sesuai
• Bawa air minum
• Cek kondisi tubuh

2. Saat Berolahraga
• Perhatikan teknik yang benar
• Jaga intensitas sesuai kemampuan
• Istirahat jika lelah
• Minum air secukupnya

3. Setelah Olahraga
• Pendinginan 5-10 menit
• Peregangan ringan
• Evaluasi kondisi tubuh
• Istirahat yang cukup
                """
            )
        )
    )
) 