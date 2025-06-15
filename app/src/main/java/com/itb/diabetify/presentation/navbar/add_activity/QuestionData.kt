package com.itb.diabetify.presentation.navbar.add_activity

enum class DataInputQuestionType {
    Numeric,
    Selection,
    Hypertension
}

data class InputOption(
    val id: String,
    val label: String
)

data class DataInputQuestion(
    val id: String,
    val category: String,
    val questionText: String,
    val questionType: DataInputQuestionType,
    val numericUnit: String? = null,
    val options: List<InputOption> = emptyList()
)

val questions = listOf(
    DataInputQuestion(
        id = "weight",
        category = "Berat Badan",
        questionText = "Berapa berat badan Anda saat ini?",
        questionType = DataInputQuestionType.Numeric,
        numericUnit = "KG",
    ),
    DataInputQuestion(
        id = "height",
        category = "Tinggi Badan",
        questionText = "Berapa tinggi badan Anda saat ini?",
        questionType = DataInputQuestionType.Numeric,
        numericUnit = "CM",
    ),
    DataInputQuestion(
        id = "birth",
        category = "Riwayat Kehamilan",
        questionText = "Apakah Anda melahirkan bayi dengan berat 4 kg atau lebih?",
        questionType = DataInputQuestionType.Selection,
        options = listOf(
            InputOption("yes", "Pernah"),
            InputOption("no", "Tidak")
        )
    ),
    DataInputQuestion(
        id = "hypertension",
        category = "Hipertensi",
        questionText = "Apakah Anda mengetahui nilai tekanan darah Anda?",
        questionType = DataInputQuestionType.Hypertension,
        options = listOf(
            InputOption("yes", "Ya"),
            InputOption("no", "Tidak")
        )
    ),
    DataInputQuestion(
        id = "cigarette",
        category = "Kebiasaan Merokok",
        questionText = "Berapa batang rokok yang Anda konsumsi hari ini?",
        questionType = DataInputQuestionType.Numeric,
        numericUnit = "batang"
    ),
    DataInputQuestion(
        id = "activity",
        category = "Aktivitas Fisik",
        questionText = "Berapa menit total Anda melakukan aktivitas fisik hari ini?",
        questionType = DataInputQuestionType.Numeric,
        numericUnit = "menit"
    ),
    DataInputQuestion(
        id = "cholesterol",
        category = "Kolesterol",
        questionText = "Apakah Anda memiliki kolesterol tinggi?",
        questionType = DataInputQuestionType.Selection,
        options = listOf(
            InputOption("yes", "Ya"),
            InputOption("no", "Tidak")
        )
    ),
    DataInputQuestion(
        id = "bloodline",
        category = "Riwayat Keluarga",
        questionText = "Apakah ayah atau ibu Anda meninggal dunia akibat komplikasi diabetes?",
        questionType = DataInputQuestionType.Selection,
        options = listOf(
            InputOption("yes", "Ya"),
            InputOption("no", "Tidak")
        )
    )
)