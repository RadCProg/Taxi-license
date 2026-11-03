package com.example.data.model

enum class AppLanguage(val code: String, val displayName: String, val shortBadge: String, val isRtl: Boolean) {
    EN("en", "English", "EN", false),
    SV("sv", "Svenska", "SV", false),
    AR("ar", "العربية", "عربي", true);

    companion object {
        fun fromCode(code: String): AppLanguage {
            return entries.firstOrNull { it.code.equals(code, ignoreCase = true) } ?: EN
        }
    }
}
