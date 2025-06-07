package com.itb.diabetify.presentation.survey

sealed class SurveyQuestionType {
    object Selection : SurveyQuestionType()
    object Numeric : SurveyQuestionType()
}

data class SurveyOption(
    val id: String,
    val text: String
)

data class SurveyQuestion(
    val id: String,
    val category: String,
    val questionText: String,
    val questionType: SurveyQuestionType,
    val options: List<SurveyOption> = emptyList(),
    val numericUnit: String = "",
    val additionalInfo: String = ""
)

data class SurveyState(
    val currentPageIndex: Int = 0,
    val answers: Map<String, String> = emptyMap(),
    val showSnackbar: Boolean = false,
    val snackbarMessage: String = "",
    val isComplete: Boolean = false
)

val questions = listOf(
    SurveyQuestion(
        id = "weight",
        category = "Berat Badan",
        questionText = "Berapa berat badan Anda saat ini?",
        questionType = SurveyQuestionType.Numeric,
        numericUnit = "KG",
    ),
    SurveyQuestion(
        id = "height",
        category = "Tinggi Badan",
        questionText = "Berapa tinggi badan Anda saat ini?",
        questionType = SurveyQuestionType.Numeric,
        numericUnit = "CM",
    ),
    SurveyQuestion(
        id = "pregnancy",
        category = "Riwayat Kehamilan",
        questionText = "Apakah Anda pernah melahirkan bayi dengan berat 4 kg atau lebih?",
        questionType = SurveyQuestionType.Selection,
        options = listOf(
            SurveyOption("true", "Pernah"),
            SurveyOption("false", "Tidak")
        )
    ),
    SurveyQuestion(
        id = "smoking_status",
        category = "Kebiasaan Merokok",
        questionText = "Apa status merokok Anda saat ini?",
        questionType = SurveyQuestionType.Selection,
        options = listOf(
            SurveyOption("0", "Tidak Pernah"),
            SurveyOption("1", "Sudah Berhenti"),
            SurveyOption("2", "Masih Merokok")
        )
    ),
    SurveyQuestion(
        id = "smoking_age",
        category = "Kebiasaan Merokok",
        questionText = "Sejak usia berapa Anda mulai merokok?",
        questionType = SurveyQuestionType.Numeric,
        numericUnit = "tahun"
    ),
    SurveyQuestion(
        id = "smoking_amount",
        category = "Kebiasaan Merokok",
        questionText = "Berapa batang rokok yang Anda konsumsi per hari (rata-rata)?",
        questionType = SurveyQuestionType.Numeric,
        numericUnit = "batang"
    ),
    SurveyQuestion(
        id = "bp_unknown",
        category = "Kesehatan",
        questionText = "Apakah Anda mengetahui nilai tekanan darah Anda?",
        questionType = SurveyQuestionType.Selection,
        options = listOf(
            SurveyOption("yes", "Ya"),
            SurveyOption("no", "Tidak")
        )
    ),
    SurveyQuestion(
        id = "systolic",
        category = "Kesehatan",
        questionText = "Berapa nilai tekanan darah sistolik Anda?",
        questionType = SurveyQuestionType.Numeric,
        numericUnit = "mmHg",
        additionalInfo = "Nilai tekanan darah yang lebih tinggi"
    ),
    SurveyQuestion(
        id = "diastolic",
        category = "Kesehatan",
        questionText = "Berapa nilai tekanan darah diastolik Anda?",
        questionType = SurveyQuestionType.Numeric,
        numericUnit = "mmHg",
        additionalInfo = "Nilai tekanan darah yang lebih rendah"
    ),
    SurveyQuestion(
        id = "hypertension",
        category = "Kesehatan",
        questionText = "Apakah Anda pernah didiagnosis memiliki tekanan darah tinggi (hipertensi)?",
        questionType = SurveyQuestionType.Selection,
        options = listOf(
            SurveyOption("true", "Ya"),
            SurveyOption("false", "Tidak")
        )
    ),
    SurveyQuestion(
        id = "activity",
        category = "Aktivitas Fisik Mingguan",
        questionText = "Dalam seminggu terakhir, berapa menit total Anda melakukan aktivitas fisik?",
        questionType = SurveyQuestionType.Numeric,
        numericUnit = "menit",
        additionalInfo = "Contoh: jalan cepat, menyapu, berkebun"
    ),
)