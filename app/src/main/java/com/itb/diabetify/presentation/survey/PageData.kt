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

object SurveyData {
    val questions = listOf(
        SurveyQuestion(
            id = "pregnancy",
            category = "Riwayat Kehamilan",
            questionText = "Apakah Anda pernah melahirkan bayi dengan berat 4 kg atau lebih?",
            questionType = SurveyQuestionType.Selection,
            options = listOf(
                SurveyOption("yes", "Pernah"),
                SurveyOption("no", "Tidak"),
                SurveyOption("never", "Saya tidak pernah melahirkan")
            )
        ),
        SurveyQuestion(
            id = "smoking_status",
            category = "Kebiasaan Merokok",
            questionText = "Apa status merokok Anda saat ini?",
            questionType = SurveyQuestionType.Selection,
            options = listOf(
                SurveyOption("never", "Tidak Pernah"),
                SurveyOption("quit", "Sudah Berhenti"),
                SurveyOption("active", "Masih Merokok")
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
            id = "hypertension",
            category = "Kesehatan",
            questionText = "Apakah Anda pernah didiagnosis memiliki tekanan darah tinggi (hipertensi)?",
            questionType = SurveyQuestionType.Selection,
            options = listOf(
                SurveyOption("yes", "Ya"),
                SurveyOption("no", "Tidak")
            )
        ),
        SurveyQuestion(
            id = "moderate_activity",
            category = "Aktivitas Fisik Mingguan",
            questionText = "Dalam seminggu terakhir, berapa menit total Anda melakukan aktivitas fisik tingkat sedang?",
            questionType = SurveyQuestionType.Numeric,
            numericUnit = "menit",
            additionalInfo = "Contoh: jalan cepat, menyapu, berkebun"
        ),
        SurveyQuestion(
            id = "intense_activity",
            category = "Aktivitas Fisik Mingguan",
            questionText = "Dalam seminggu terakhir, berapa menit total Anda melakukan aktivitas fisik tingkat berat?",
            questionType = SurveyQuestionType.Numeric,
            numericUnit = "menit",
            additionalInfo = "Contoh: naik tangga cepat, mengangkat barang berat"
        )
    )
}