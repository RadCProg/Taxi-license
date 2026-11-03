package com.example

import com.example.data.database.DefaultQuestions
import com.example.data.model.AppLanguage
import com.example.data.model.LocalizedStrings
import com.example.data.model.QuestionCategory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun testDefaultLanguageIsEnglish() {
        val defaultLang = AppLanguage.fromCode("en")
        assertEquals(AppLanguage.EN, defaultLang)
        assertFalse(defaultLang.isRtl)
    }

    @Test
    fun testArabicLanguageIsRtl() {
        val arabic = AppLanguage.AR
        assertTrue(arabic.isRtl)
        assertEquals("ar", arabic.code)
    }

    @Test
    fun testLocalizedStringsKeysExistAcrossAllLanguages() {
        val testKeys = listOf(
            "app_title", "nav_home", "nav_stats", "nav_review", "nav_settings",
            "home_welcome", "start_test", "start_mock_exam", "results_title"
        )
        for (key in testKeys) {
            val enText = LocalizedStrings.get(key, AppLanguage.EN)
            val svText = LocalizedStrings.get(key, AppLanguage.SV)
            val arText = LocalizedStrings.get(key, AppLanguage.AR)

            assertNotNull(enText)
            assertNotNull(svText)
            assertNotNull(arText)
            assertFalse(enText.isEmpty())
            assertFalse(svText.isEmpty())
            assertFalse(arText.isEmpty())
        }
    }

    @Test
    fun testQuestionCategoriesHaveTitlesInAllLanguages() {
        for (cat in QuestionCategory.entries) {
            val enTitle = cat.title(AppLanguage.EN)
            val svTitle = cat.title(AppLanguage.SV)
            val arTitle = cat.title(AppLanguage.AR)

            assertNotNull(enTitle)
            assertNotNull(svTitle)
            assertNotNull(arTitle)
        }
    }

    @Test
    fun testDefaultQuestionsHaveValidOptionsAndCorrectIndices() {
        assertTrue(DefaultQuestions.list.isNotEmpty())
        for (q in DefaultQuestions.list) {
            val enOptions = q.getOptions(AppLanguage.EN)
            val svOptions = q.getOptions(AppLanguage.SV)
            val arOptions = q.getOptions(AppLanguage.AR)

            assertEquals(4, enOptions.size)
            assertEquals(4, svOptions.size)
            assertEquals(4, arOptions.size)

            assertTrue(q.correctOptionIndex in 0..3)
            assertFalse(q.getQuestion(AppLanguage.EN).isEmpty())
            assertFalse(q.getQuestion(AppLanguage.SV).isEmpty())
            assertFalse(q.getQuestion(AppLanguage.AR).isEmpty())
            assertFalse(q.getExplanation(AppLanguage.EN).isEmpty())
        }
    }

    @Test
    fun testPassingScoreBenchmark() {
        val total = 16
        val passingScore = 12 // 75%
        val failingScore = 11 // 68%

        val passingPct = (passingScore * 100) / total
        val failingPct = (failingScore * 100) / total

        assertTrue(passingPct >= 75)
        assertFalse(failingPct >= 75)
    }
}
