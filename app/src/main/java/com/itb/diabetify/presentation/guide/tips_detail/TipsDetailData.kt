package com.itb.diabetify.presentation.guide.tips_detail

import com.itb.diabetify.R

data class TipsDetailData(
    val title: String,
    val image: Int,
    val content: List<TipsSection>,
    val readMoreLinks: List<ReadMoreLink>
)

data class TipsSection(
    val title: String,
    val content: String
)

data class ReadMoreLink(
    val title: String,
    val url: String,
)

val tipsDetails = mapOf(
    "healthy_nutrition" to TipsDetailData(
        title = "Nutrisi Sehat untuk Pencegahan Diabetes",
        image = R.drawable.food,
        content = listOf(
            TipsSection(
                title = "Makanan yang Harus Dibatasi",
                content = """Penderita diabetes perlu membatasi konsumsi makanan berikut hingga 7-10% dari nilai harian:

• Karbohidrat Sederhana
  - Nasi putih dan roti putih
  - Gula pasir dan makanan manis
  - Minuman isotonik dan kemasan manis

• Daging Olahan
  - Sosis, kornet, dan nugget
  - Makanan dengan pengawet tinggi

• Makanan Tinggi Lemak Jenuh
  - Kulit ayam goreng
  - Gorengan berlebihan
  - Makanan cepat saji

• Makanan Manis Olahan
  - Aneka kue dan bolu
  - Buah kaleng dengan pemanis
  - Minuman berenergi dan soft drink"""
            ),
            TipsSection(
                title = "Sayuran Hijau yang Direkomendasikan",
                content = """Sayuran hijau sangat aman dan bermanfaat untuk penderita diabetes karena rendah kalori dan karbohidrat:

• Sayuran Daun Hijau
  - Bayam - tinggi protein, vitamin, mineral, dan serat
  - Kangkung - mudah diolah dan bergizi
  - Kale - super food dengan antioksidan tinggi

• Sayuran Cruciferous
  - Brokoli - kaya serat, vitamin C, dan potasium
  - Kubis dan pokcoy - mudah didapat dan murah
  - Kembang kol - bisa dijadikan pengganti nasi

• Sayuran Lainnya
  - Seledri - 1 cangkir cincang hanya 3g karbohidrat
  - Zucchini - tinggi antioksidan dan magnesium
  - Tomat - 200g/hari dapat turunkan tekanan darah

Cara penyajian: salad, sup, jus, atau tumis ringan dengan sedikit minyak zaitun."""
            ),
            TipsSection(
                title = "Protein Sehat dan Karbohidrat Kompleks",
                content = """1. Protein Berkualitas Tinggi
• Ikan Berlemak Sehat
  - Salmon - tinggi omega 3, konsumsi minimal 2x/minggu
  - Tuna dan makarel untuk variasi

• Protein Hewani Lainnya
  - Daging tanpa lemak (ayam, kalkun tanpa kulit)
  - Daging sapi bagian tenderloin atau sirloin
  - Telur - 1 butir besar = 0,5g karbohidrat + 7g protein

• Protein Nabati
  - Tahu dan tempe - menurunkan risiko diabetes tipe 2
  - Kacang-kacangan tanpa bumbu berlebihan

2. Karbohidrat Sehat
• Pengganti Nasi
  - Quinoa - biji-bijian tinggi protein
  - Kembang kol cincang sebagai nasi cauliflower
  - Ubi jalar - indeks glikemik rendah

• Alternatif Mie
  - Mie shirataki dari akar konjac (97% air, 3% serat)
  - Sangat rendah kalori dan aman untuk diet diabetes"""
            ),
            TipsSection(
                title = "Buah, Lemak Sehat, dan Tips Praktis",
                content = """1. Buah yang Aman untuk Diabetes
• Apel - kulit kaya serat, menunda lonjakan gula darah
• Alpukat - sumber lemak sehat (perhatikan porsi karena tinggi kalori)

2. Lemak Sehat dan Bumbu
• Minyak zaitun - indeks glikemik rendah
  - Gunakan untuk salad dressing atau menumis
• Kacang-kacangan (almond, mete, walnut, pistachio)
• Bawang putih - membantu mengontrol gula darah

3. Tips Praktis Sehari-hari
• Gunakan prinsip "Piring Sehat": 1/2 sayuran, 1/4 protein, 1/4 karbohidrat
• Makan dalam porsi kecil tapi sering (3 kali makan utama + 2 snack sehat)
• Selalu baca label nutrisi pada makanan kemasan
• Batasi gula, lemak jenuh, dan natrium sesuai anjuran ahli gizi
• Konsultasi rutin dengan dokter dan ahli gizi
• Pantau kadar gula darah secara teratur"""
            )
        ),
        readMoreLinks = listOf(
            ReadMoreLink(
                title = "19 Makanan untuk Diabetes yang Direkomendasikan Ahli Gizi",
                url = "https://mitrakeluarga.com/artikel/makanan-untuk-diabetes",
            )
        )
    ),
    "physical_activity" to TipsDetailData(
        title = "Aktivitas Fisik untuk Penderita Diabetes",
        image = R.drawable.activity,
        content = listOf(
            TipsSection(
                title = "Manfaat Olahraga untuk Diabetes",
                content = """Rutin berolahraga memberikan manfaat luar biasa bagi penderita diabetes dalam mengontrol kadar gula darah:

• Mekanisme Kontrol Gula Darah
  - Otot berkontraksi saat berolahraga
  - Merangsang penggunaan glukosa sebagai energi
  - Sel-sel tubuh mengambil glukosa dari darah
  - Mengubah glukosa menjadi energi untuk aktivitas fisik

• Manfaat Kesehatan Lainnya
  - Menurunkan risiko komplikasi diabetes
  - Mencegah hiperglikemia
  - Meningkatkan metabolisme tubuh
  - Memperbaiki penyerapan insulin
  - Melancarkan sirkulasi darah
  - Membantu menjaga berat badan ideal

• Pentingnya Olahraga Rutin
  - Membantu tubuh merespon insulin dengan baik
  - Memperbaiki fungsi saraf
  - Mengurangi stres dan menjaga kesehatan mental
  - Meningkatkan kualitas hidup secara keseluruhan"""
            ),
            TipsSection(
                title = "Olahraga Aerobik yang Direkomendasikan",
                content = """1. Senam Diabetes
• Gerakan fisik yang disesuaikan dengan irama musik
• Membantu melancarkan sirkulasi udara dalam tubuh
• Meningkatkan metabolisme dan penyerapan insulin
• Gerakan mirip senam pada umumnya
• Bermanfaat untuk meregangkan sendi dan otot

2. Jalan Cepat
• Olahraga ringan dan mudah dilakukan
• Latihan aerobik yang meningkatkan denyut jantung
• Membuat aliran darah lebih lancar
• Intensitas dapat disesuaikan dengan kondisi fisik
• Variasi: hiking atau berjalan menanjak untuk hasil optimal

3. Bersepeda
• Meningkatkan aliran darah ke kaki
• Membakar kalori untuk menjaga berat badan
• Dapat menggunakan sepeda statis untuk keamanan
• Menghindari risiko terjatuh atau cedera
• Olahraga yang menyenangkan dan mudah dilakukan

4. Berenang
• Olahraga ideal tanpa tekanan pada sendi
• Melatih otot seluruh tubuh secara menyeluruh
• Meredakan gejala diabetes seperti kesemutan di kaki
• Aman untuk semua usia dan kondisi fisik
• Risiko cedera sangat minimal"""
            ),
            TipsSection(
                title = "Latihan Kekuatan dan Fleksibilitas",
                content = """1. Yoga untuk Diabetes
• Membangun kelenturan, keseimbangan, dan kekuatan
• Baik untuk kesehatan fisik dan mental
• Membantu terhindar dari stres
• Melawan resistensi insulin
• Memperbaiki fungsi saraf
• Dapat dilakukan sesering mungkin sesuai kondisi

2. Latihan Angkat Beban
• Menaikkan massa otot untuk kontrol gula darah
• Membantu tubuh merespon insulin dengan baik
• Memperbaiki penyerapan dan penggunaan gula darah
• Harus berkonsultasi dengan dokter terlebih dahulu
• Risiko cedera cukup tinggi, perlu pengawasan

3. Senam Kaki Diabetes
• Dapat dilakukan di mana saja dan kapan saja
• Saat menonton TV, berbaring, atau berdiri

Gerakan Senam Kaki:
• Angkat dan turunkan kedua tumit secara bergantian
• Putar pergelangan kaki ke luar dan ke dalam
• Lakukan peregangan jari-jari kaki
• Angkat kaki hingga 90 derajat, turunkan, ulangi bergantian"""
            ),
            TipsSection(
                title = "Tips Keamanan dan Praktis Berolahraga",
                content = """1. Persiapan Sebelum Berolahraga
• Konsultasi dengan dokter untuk menentukan jenis olahraga yang sesuai
• Periksa kadar gula darah sebelum dan sesudah olahraga
• Siapkan cemilan sehat jika gula darah turun
• Minum air putih yang cukup untuk mencegah dehidrasi

2. Perlengkapan yang Dibutuhkan
• Gunakan alas kaki yang tebal dan nyaman
• Pastikan sepatu sesuai bentuk kaki
• Hindari risiko lecet atau cedera pada kaki
• Pakai pakaian yang menyerap keringat
• Bawa handuk dan air minum

3. Intensitas dan Durasi
• Mulai dengan intensitas ringan, tingkatkan bertahap
• Durasi 30-45 menit per sesi
• Frekuensi 3-5 kali per minggu
• Sesuaikan dengan kondisi kesehatan dan kemampuan fisik
• Dengarkan tubuh, istirahat jika merasa lelah berlebihan

4. Tanda Bahaya yang Perlu Diwaspadai
• Pusing atau mual saat berolahraga
• Nyeri dada atau sesak napas
• Gula darah terlalu rendah (hipoglikemia)
• Luka atau lecet pada kaki yang tidak sembuh
• Segera hentikan olahraga dan konsultasi dokter jika mengalami gejala ini"""
            )
        ),
        readMoreLinks = listOf(
            ReadMoreLink(
                title = "7 Olahraga untuk Penderita Diabetes yang Direkomendasikan",
                url = "https://www.siloamhospitals.com/informasi-siloam/artikel/jenis-olahraga-untuk-penderita-diabetes",
            )
        )
    ),
    "smoking" to TipsDetailData(
        title = "Tips Berhenti Merokok untuk Kesehatan",
        image = R.drawable.smoking,
        content = listOf(
            TipsSection(
                title = "Mengapa Berhenti Merokok Itu Penting",
                content = """Berhenti merokok merupakan keputusan terbaik untuk kesehatan tubuh, terutama bagi penderita diabetes:

• Dampak Rokok pada Diabetes
  - Nikotin dapat meningkatkan resistensi insulin
  - Merokok memperburuk kontrol gula darah
  - Meningkatkan risiko komplikasi diabetes
  - Memperlambat penyembuhan luka pada diabetesi

• Manfaat Berhenti Merokok
  - Menurunkan risiko penyakit jantung dan stroke
  - Meningkatkan sirkulasi darah
  - Memperbaiki fungsi paru-paru
  - Mengurangi risiko infeksi
  - Meningkatkan kualitas hidup secara keseluruhan

• Tantangan yang Dihadapi
  - Efek kecanduan nikotin yang kuat
  - Gejala putus nikotin saat berhenti
  - Kebiasaan dan rutinitas yang terkait merokok
  - Tekanan sosial dan lingkungan

Dengan tekad yang kuat dan strategi yang tepat, Anda dapat berhasil berhenti merokok dan merasakan manfaatnya."""
            ),
            TipsSection(
                title = "Strategi Mengelola Stres dan Pemicu",
                content = """1. Mengelola Stres Tanpa Rokok
• Merokok sering dijadikan pelarian dari stres
• Nikotin memberikan efek relaksasi sementara
• Penelitian membuktikan merokok justru meningkatkan stres jangka panjang

Alternatif Mengatasi Stres:
• Berolahraga ringan seperti jalan santai atau yoga
• Mendapatkan terapi pijat untuk relaksasi
• Meditasi dan latihan pernapasan
• Mendengarkan musik yang menenangkan
• Melakukan hobi yang disukai

2. Menghindari Pemicu Kebiasaan Merokok
• Hindari situasi yang memicu keinginan merokok
• Jauhi minuman kopi atau alkohol jika memicu keinginan merokok
• Batasi berkumpul dengan sesama perokok sementara waktu
• Ubah rutinitas harian yang terkait dengan merokok

Pengganti Kebiasaan Setelah Makan:
• Mengunyah permen karet tanpa gula
• Menggosok gigi langsung setelah makan
• Minum air putih atau teh herbal
• Berjalan-jalan sebentar untuk membantu pencernaan"""
            ),
            TipsSection(
                title = "Pola Hidup Sehat untuk Mendukung Berhenti Merokok",
                content = """1. Mengonsumsi Makanan Sehat
• Nikotin mengurangi sensitivitas indra perasa dan penciuman
• Perokok aktif sering mengalami penurunan nafsu makan
• Makanan sehat membantu mengurangi hasrat merokok

Makanan yang Direkomendasikan:
• Sayuran hijau dan buah-buahan segar
• Makanan kaya antioksidan untuk detoksifikasi
• Protein berkualitas untuk memperbaiki jaringan
• Makanan kaya vitamin C untuk meningkatkan imunitas
• Air putih yang cukup untuk mengeluarkan racun

2. Berolahraga Secara Rutin
• Olahraga mengurangi kecanduan nikotin secara alami
• Membantu mengalihkan keinginan merokok
• Meningkatkan produksi endorfin untuk perasaan bahagia
• Memperbaiki kondisi fisik yang rusak akibat merokok

Jenis Olahraga yang Disarankan:
• Jalan santai atau jogging ringan
• Berenang untuk melatih paru-paru
• Bersepeda untuk meningkatkan sirkulasi
• Yoga untuk mengatasi stres dan kecemasan

3. Membersihkan Lingkungan
• Cuci pakaian, sprei, dan karpet yang berbau rokok
• Bersihkan rumah dan mobil dari aroma rokok
• Gunakan pengharum ruangan alami
• Buang semua peralatan merokok (asbak, korek, dll)"""
            ),
            TipsSection(
                title = "Dukungan Sosial dan Terapi Profesional",
                content = """1. Melibatkan Keluarga dan Teman
• Beri tahu keluarga dan kerabat dekat tentang niat berhenti merokok
• Minta dukungan dan pengertian dari orang-orang terdekat
• Bergabung dengan komunitas atau support group
• Hindari lingkungan yang mendukung kebiasaan merokok

Tips Mendapatkan Dukungan:
• Jelaskan alasan dan tujuan berhenti merokok
• Minta bantuan untuk mengingatkan komitmen Anda
• Ajak keluarga untuk menjalani gaya hidup sehat bersama
• Rayakan pencapaian milestone berhenti merokok

2. Terapi Pengganti Nikotin (NRT)
• Membantu mengatasi gejala putus nikotin
• Meredakan rasa frustrasi dan kecemasan
• Tersedia dalam berbagai bentuk

Jenis NRT yang Tersedia:
• Permen karet nikotin
• Tablet hisap atau lozenge
• Koyo nikotin (patch)
• Inhaler nikotin
• Spray hidung nikotin

*Penting: Konsultasi dengan dokter sebelum menggunakan NRT*

3. Terapi Perilaku dan Konseling
• Konseling dengan psikolog atau konselor bersertifikat
• Mengidentifikasi faktor pemicu kebiasaan merokok
• Menemukan strategi personal yang sesuai kondisi
• Terapi kognitif perilaku untuk mengubah pola pikir

4. Terapi Alternatif
• Hipnoterapi untuk mengubah pola pikir bawah sadar
• Akupunktur untuk mengurangi kecanduan
• Terapi herbal (dengan pengawasan dokter)

5. Bantuan Medis
• Obat-obatan seperti bupropion dan varenicline
• Hanya boleh dikonsumsi dengan resep dokter
• Pemantauan efek samping oleh tenaga medis"""
            )
        ),
        readMoreLinks = listOf(
            ReadMoreLink(
                title = "9 Cara Berhenti Merokok yang Ampuh",
                url = "https://www.alodokter.com/bahas-satu-satu-cara-berhenti-merokok",
            ),
        )
    ),
    "hypertension" to TipsDetailData(
        title = "Cara Mengatasi dan Mencegah Hipertensi",
        image = R.drawable.hypertension,
        content = listOf(
            TipsSection(
                title = "Memahami Hipertensi dan Risikonya",
                content = """Hipertensi adalah kondisi ketika tekanan darah sistolik ≥ 140 mmHg dan/atau diastolik ≥ 90 mmHg pada orang dewasa:

• Fakta Penting Hipertensi
  - Prevalensi hipertensi di Indonesia mencapai 34,1% pada populasi dewasa (Riskesdas 2018)
  - 46% orang dewasa tidak menyadari kondisi hipertensi mereka
  - Hipertensi masih menjadi penyebab utama kematian dini di seluruh dunia
  - Sering disebut "pembunuh senyap" karena tidak menimbulkan gejala

• Hubungan Hipertensi dengan Diabetes
  - Penderita diabetes memiliki risiko 2x lipat terkena hipertensi
  - Hipertensi memperburuk komplikasi diabetes
  - Keduanya meningkatkan risiko penyakit jantung dan stroke
  - Kontrol kedua kondisi sangat penting untuk kesehatan jangka panjang

• Komplikasi Serius yang Dapat Terjadi
  - Penyakit jantung koroner dan serangan jantung
  - Stroke dan gangguan pembuluh darah otak
  - Gagal ginjal kronis
  - Atrial fibrilasi (gangguan irama jantung)
  - Kebutaan akibat kerusakan pembuluh darah mata
  - Kematian mendadak

Deteksi dini dan pencegahan adalah kunci utama menghindari komplikasi berbahaya."""
            ),
            TipsSection(
                title = "Faktor Risiko dan Pencegahan",
                content = """1. Faktor Risiko yang Dapat Dicegah
• Kebiasaan Buruk yang Harus Dihindari
  - Kebiasaan merokok dan paparan asap rokok
  - Konsumsi alkohol berlebihan
  - Kurang aktivitas fisik atau malas bergerak
  - Pola makan tidak sehat dengan garam berlebihan
  - Stres berkepanjangan tanpa manajemen yang baik

• Kondisi Kesehatan yang Perlu Dikelola
  - Diabetes mellitus
  - Kelebihan berat badan atau obesitas
  - Kolesterol tinggi
  - Pola tidur yang tidak teratur

2. Faktor Risiko yang Tidak Dapat Dicegah
• Faktor Genetik dan Demografis
  - Riwayat keluarga dengan hipertensi
  - Usia di atas 65 tahun
  - Jenis kelamin (pria lebih berisiko di usia muda)
  - Ras atau etnis tertentu

• Kondisi Medis Penyerta
  - Penyakit ginjal kronis
  - Gangguan hormon
  - Sleep apnea
  - Penyakit autoimun tertentu

*Penting: Meskipun beberapa faktor tidak dapat dicegah, modifikasi gaya hidup tetap sangat efektif mengurangi risiko hingga 15%*"""
            ),
            TipsSection(
                title = "Olahraga dan Aktivitas Fisik untuk Hipertensi",
                content = """1. Panduan Olahraga yang Direkomendasikan
• Target Aktivitas Fisik
  - Minimal 30 menit setiap hari
  - Atau total 150 menit per minggu
  - Intensitas sedang hingga ringan
  - Konsistensi lebih penting daripada intensitas tinggi

• Jenis Olahraga yang Efektif
  - Senam aerobik untuk meningkatkan sirkulasi
  - Jalan cepat atau jogging ringan
  - Bersepeda untuk melatih jantung
  - Berenang untuk olahraga low-impact
  - Yoga untuk relaksasi dan mengurangi stres

2. Tips Aman Berolahraga dengan Hipertensi
• Persiapan Sebelum Olahraga
  - Periksa tekanan darah sebelum berolahraga
  - Konsultasi dengan dokter untuk jenis olahraga yang sesuai
  - Mulai dengan intensitas ringan, tingkatkan bertahap
  - Siapkan air minum yang cukup

• Tanda Bahaya Saat Berolahraga
  - Nyeri dada atau sesak napas berlebihan
  - Pusing atau mual yang tidak normal
  - Detak jantung tidak teratur
  - Sakit kepala hebat mendadak
  - *Segera hentikan olahraga dan konsultasi dokter*

3. Aktivitas Fisik Sehari-hari
• Cara Meningkatkan Aktivitas Harian
  - Gunakan tangga daripada lift
  - Parkir kendaraan lebih jauh dari tujuan
  - Berjalan kaki saat berbelanja
  - Lakukan pekerjaan rumah secara aktif
  - Berdiri dan bergerak setiap 30 menit jika bekerja di kantor"""
            ),
            TipsSection(
                title = "Pola Makan dan Gaya Hidup Sehat",
                content = """1. Panduan Diet untuk Hipertensi
• Pembatasan Garam (Natrium)
  - Maksimal 1 sendok teh (5 gram) per hari
  - Hindari makanan olahan tinggi natrium
  - Batasi makanan cepat saji dan makanan kaleng
  - Gunakan rempah alami sebagai pengganti garam
  - Baca label nutrisi pada kemasan makanan

• Makanan yang Direkomendasikan
  - Buah-buahan segar kaya kalium (pisang, jeruk, alpukat)
  - Sayuran hijau dan berwarna-warni
  - Ikan laut kaya omega-3 (salmon, tuna, makarel)
  - Kacang-kacangan dan biji-bijian utuh
  - Produk susu rendah lemak

• Minuman Sehat
  - Air putih minimal 8 gelas per hari
  - Kopi tanpa gula (maksimal 2 cangkir/hari)
  - Teh hijau atau teh hitam tanpa gula
  - Jus buah segar tanpa tambahan gula

2. Menjaga Berat Badan Ideal
• Strategi Penurunan Berat Badan
  - Tetapkan target realistis (0,5-1 kg per minggu)
  - Kombinasikan diet sehat dengan olahraga teratur
  - Hindari diet ekstrem atau crash diet
  - Konsultasi dengan ahli gizi untuk menu personal

3. Manajemen Stres dan Gaya Hidup
• Teknik Mengelola Stres
  - Latihan pernapasan dalam dan meditasi
  - Yoga atau tai chi untuk relaksasi
  - Tidur yang cukup (7-8 jam per malam)
  - Hobi yang menyenangkan dan menenangkan
  - Dukungan sosial dari keluarga dan teman

• Kebiasaan yang Harus Dihindari
  - Berhenti merokok sepenuhnya
  - Hindari konsumsi alkohol
  - Batasi kafein berlebihan
  - Kelola waktu untuk mengurangi stres kerja

4. Monitoring dan Kontrol Rutin
• Pemeriksaan Mandiri di Rumah
  - Gunakan tensimeter digital yang akurat
  - Ukur tekanan darah di waktu yang sama setiap hari
  - Catat hasil dalam buku diary atau aplikasi
  - Laporkan hasil ke dokter secara berkala

• Kontrol Medis Berkala
  - Kunjungi dokter setiap 3-6 bulan
  - Pemeriksaan lab lengkap secara berkala
  - Evaluasi fungsi ginjal dan jantung
  - Konsultasi penyesuaian obat jika diperlukan"""
            )
        ),
        readMoreLinks = listOf(
            ReadMoreLink(
                title = "Cara Mengatasi Hipertensi - Kementerian Kesehatan RI",
                url = "https://ayosehat.kemkes.go.id/cara-mengatasi-hipertensi",
            )
        )
    ),
    "cholesterol" to TipsDetailData(
        title = "Cara Alami Menurunkan Kolesterol Tinggi",
        image = R.drawable.cholesterol,
        content = listOf(
            TipsSection(
                title = "Memahami Kolesterol dan Risikonya",
                content = """Kolesterol tinggi merupakan salah satu faktor risiko utama penyakit kardiovaskular, terutama pada penderita diabetes:

• Jenis-jenis Kolesterol
  - LDL (kolesterol jahat) - menyebabkan penyumbatan pembuluh darah
  - HDL (kolesterol baik) - membantu membersihkan LDL dari darah
  - Trigliserida - lemak dalam darah yang juga perlu dikontrol
  - Total kolesterol - kombinasi semua jenis kolesterol

• Hubungan Kolesterol dengan Diabetes
  - Penderita diabetes 2-4x lebih berisiko memiliki kolesterol tinggi
  - Kadar gula darah tinggi dapat memperburuk profil lipid
  - Resistensi insulin meningkatkan produksi kolesterol jahat
  - Keduanya meningkatkan risiko penyakit jantung secara eksponensial

• Risiko Komplikasi
  - Penyakit jantung koroner dan serangan jantung
  - Stroke akibat penyumbatan pembuluh darah otak
  - Penyakit arteri perifer
  - Gangguan sirkulasi dan penyembuhan luka

• Target Kolesterol untuk Diabetesi
  - Total kolesterol: < 200 mg/dL
  - LDL: < 100 mg/dL (< 70 mg/dL untuk risiko tinggi)
  - HDL: > 40 mg/dL (pria), > 50 mg/dL (wanita)
  - Trigliserida: < 150 mg/dL

Kontrol kolesterol yang baik sama pentingnya dengan kontrol gula darah untuk mencegah komplikasi diabetes."""
            ),
            TipsSection(
                title = "Menjaga Berat Badan dan Olahraga Teratur",
                content = """1. Pentingnya Menjaga Berat Badan Ideal
• Dampak Obesitas pada Kolesterol
  - Kelebihan berat badan meningkatkan produksi kolesterol jahat (LDL)
  - Menurunkan kadar kolesterol baik (HDL)
  - Meningkatkan resistensi insulin yang memperburuk profil lipid
  - Penelitian Alexander B. Leichtle menunjukkan penurunan berat badan jangka panjang memperbaiki metabolisme kolesterol

• Strategi Penurunan Berat Badan
  - Target penurunan 5-10% dari berat badan awal
  - Defisit kalori 500-750 kalori per hari
  - Kombinasi diet sehat dengan olahraga teratur
  - Pantau progres secara berkala

2. Program Olahraga untuk Menurunkan Kolesterol
• Panduan Olahraga yang Efektif
  - Minimal 30 menit per hari, 5 kali seminggu
  - Total 150 menit aktivitas sedang per minggu
  - Kombinasi latihan aerobik dan kekuatan otot
  - Konsistensi lebih penting daripada intensitas tinggi

• Jenis Olahraga yang Direkomendasikan
  - Jalan cepat untuk meningkatkan HDL
  - Berenang untuk latihan kardio low-impact
  - Bersepeda jarak pendek untuk pemula
  - Jogging ringan untuk meningkatkan metabolisme
  - Senam aerobik untuk variasi dan kesenangan

• Manfaat Olahraga untuk Kolesterol
  - Menurunkan kadar LDL (kolesterol jahat)
  - Meningkatkan kadar HDL (kolesterol baik)
  - Menurunkan trigliserida
  - Memperbaiki sirkulasi darah
  - Meningkatkan sensitivitas insulin

*Penting: Konsultasi dengan dokter sebelum memulai program olahraga, terutama jika memiliki kondisi medis tertentu*"""
            ),
            TipsSection(
                title = "Mengelola Stres dan Menghindari Kebiasaan Buruk",
                content = """1. Manajemen Stres untuk Kolesterol Sehat
• Dampak Stres pada Kolesterol
  - Stres berkepanjangan meningkatkan kadar LDL dan trigliserida
  - Tubuh melepaskan kortisol dan adrenalin saat stres
  - Kortisol meningkatkan gula darah dan produksi trigliserida
  - Stres kronis memperburuk resistensi insulin

• Teknik Mengelola Stres
  - Latihan pernapasan dalam dan meditasi
  - Yoga dan tai chi untuk relaksasi
  - Olahraga teratur sebagai pelepas stres
  - Hobi yang menyenangkan dan menenangkan
  - Dukungan sosial dari keluarga dan teman
  - Tidur yang cukup (7-8 jam per malam)

2. Berhenti Merokok untuk Kolesterol Sehat
• Dampak Rokok pada Kolesterol
  - Nikotin menghalangi perjalanan kolesterol ke hati untuk difiltrasi
  - Meningkatkan kadar LDL (kolesterol jahat)
  - Menurunkan kadar HDL (kolesterol baik)
  - Menyebabkan penyumbatan pembuluh darah
  - Meningkatkan risiko penyakit kardiovaskular secara drastis

• Manfaat Berhenti Merokok
  - HDL meningkat dalam 3 bulan setelah berhenti
  - LDL menurun secara bertahap
  - Sirkulasi darah membaik
  - Risiko serangan jantung menurun 50% dalam 1 tahun

3. Mengurangi Konsumsi Alkohol
• Dampak Alkohol pada Kolesterol
  - Konsumsi berlebihan meningkatkan LDL dan trigliserida
  - Meningkatkan tekanan darah
  - Memperburuk kontrol gula darah pada diabetesi
  - Menambah kalori yang menyebabkan kenaikan berat badan

• Batasan Konsumsi yang Aman
  - Pria: maksimal 2 gelas per hari
  - Wanita: maksimal 1 gelas per hari
  - Lebih baik dihindari sama sekali untuk diabetesi
  - Pilih hari-hari bebas alkohol dalam seminggu"""
            ),
            TipsSection(
                title = "Pola Makan Sehat untuk Kolesterol Normal",
                content = """1. Membatasi Makanan Pemicu Kolesterol Tinggi
• Makanan Tinggi Gula yang Harus Dihindari
  - Minuman manis dan soft drink
  - Kue, biskuit, dan permen
  - Es krim dan dessert manis
  - Makanan olahan dengan gula tambahan
  - Buah kaleng dengan sirup manis

*Dampak: Gula berlebihan meningkatkan LDL dan menurunkan HDL*

• Makanan Tinggi Lemak Jenuh dan Trans
  - Makanan cepat saji dan gorengan
  - Daging berlemak dan kulit ayam
  - Produk susu full-fat
  - Mentega dan margarin
  - Makanan olahan dan kemasan

2. Meningkatkan Asupan Serat dan Makanan Sehat
• Manfaat Serat untuk Kolesterol
  - Serat mengikat lemak dan mengurangi penyerapan kolesterol di usus
  - Menurunkan kadar kolesterol darah secara alami
  - Membantu mengontrol gula darah
  - Memberikan rasa kenyang lebih lama

• Sumber Serat yang Direkomendasikan
  - Sayuran hijau (bayam, brokoli, kangkung)
  - Buah-buahan segar (apel, pir, jeruk)
  - Biji-bijian utuh (oatmeal, beras merah, quinoa)
  - Kacang-kacangan (kacang merah, kacang hijau)
  - Polong-polongan (tempe, tahu, edamame)

3. Makanan Penurun Kolesterol Alami
• Sayuran dan Buah-buahan
  - Sayuran hijau kaya antioksidan
  - Pisang untuk kalium dan serat
  - Apel dengan kulit untuk pektin
  - Alpukat untuk lemak sehat

• Protein Sehat
  - Ikan berlemak (salmon, tuna, makarel) - kaya omega-3
  - Kacang kedelai dan produk olahannya
  - Kacang-kacangan (almond, walnut)
  - Daging tanpa lemak dan ayam tanpa kulit

• Biji-bijian dan Karbohidrat Sehat
  - Beras merah sebagai pengganti nasi putih
  - Oatmeal untuk sarapan kaya serat
  - Quinoa sebagai sumber protein nabati

• Rempah dan Bumbu Alami
  - Jahe untuk meningkatkan HDL
  - Bawang putih untuk menurunkan LDL
  - Kunyit sebagai antiinflamasi
  - Kayu manis untuk kontrol gula darah

4. Tips Praktis Pola Makan Sehat
• Perencanaan Menu Harian
  - Sarapan: Oatmeal dengan buah segar
  - Makan siang: Nasi merah + ikan + sayuran
  - Makan malam: Salad dengan protein tanpa lemak
  - Snack: Kacang-kacangan atau buah

• Cara Memasak yang Sehat
  - Kukus, rebus, atau panggang daripada menggoreng
  - Gunakan minyak zaitun atau minyak canola
  - Hindari santan kental dan minyak kelapa sawit
  - Gunakan rempah alami sebagai bumbu

5. Pemeriksaan Kolesterol Rutin
• Frekuensi Pemeriksaan
  - Setiap 3-6 bulan untuk diabetesi
  - Setiap tahun untuk orang sehat di atas 20 tahun
  - Lebih sering jika memiliki riwayat keluarga

• Persiapan Sebelum Tes
  - Puasa 9-12 jam sebelum pemeriksaan
  - Hindari olahraga berat 24 jam sebelumnya
  - Informasikan obat yang sedang dikonsumsi
  - Catat hasil dan konsultasikan dengan dokter"""
            )
        ),
        readMoreLinks = listOf(
            ReadMoreLink(
                title = "9 Natural Ways to Lower High Cholesterol Effectively",
                url = "https://www.siloamhospitals.com/en/informasi-siloam/artikel/9-effective-and-natural-ways-to-lower-cholesterol-levels-",
            )
        )
    )
)