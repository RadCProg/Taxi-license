package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.AppLanguage

@Entity(tableName = "questions")
data class QuestionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val category: String, // "law", "safety", "navigation", "equipment"
    val questionEn: String,
    val questionSv: String,
    val questionAr: String,
    val option1En: String,
    val option1Sv: String,
    val option1Ar: String,
    val option2En: String,
    val option2Sv: String,
    val option2Ar: String,
    val option3En: String,
    val option3Sv: String,
    val option3Ar: String,
    val option4En: String,
    val option4Sv: String,
    val option4Ar: String,
    val correctOptionIndex: Int, // 0 to 3
    val explanationEn: String,
    val explanationSv: String,
    val explanationAr: String,
    val isBookmarked: Boolean = false,
    val timesAnswered: Int = 0,
    val timesCorrect: Int = 0,
    val lastAnsweredCorrectly: Boolean? = null
) {
    fun getQuestion(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> questionEn
        AppLanguage.SV -> questionSv
        AppLanguage.AR -> questionAr
    }

    fun getExplanation(lang: AppLanguage): String = when (lang) {
        AppLanguage.EN -> explanationEn
        AppLanguage.SV -> explanationSv
        AppLanguage.AR -> explanationAr
    }

    fun getOptions(lang: AppLanguage): List<String> = when (lang) {
        AppLanguage.EN -> listOf(option1En, option2En, option3En, option4En)
        AppLanguage.SV -> listOf(option1Sv, option2Sv, option3Sv, option4Sv)
        AppLanguage.AR -> listOf(option1Ar, option2Ar, option3Ar, option4Ar)
    }
}
