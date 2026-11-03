package com.example.data.model

enum class QuestionCategory(val id: String) {
    LAW("law"),
    SAFETY("safety"),
    NAVIGATION("navigation"),
    EQUIPMENT("equipment"),
    MOCK_EXAM("mock_exam");

    fun title(lang: AppLanguage): String = when (this) {
        LAW -> when (lang) {
            AppLanguage.EN -> "Traffic Legislation"
            AppLanguage.SV -> "Trafiklagstiftning"
            AppLanguage.AR -> "تشريعات المرور"
        }
        SAFETY -> when (lang) {
            AppLanguage.EN -> "Safety & Behavior"
            AppLanguage.SV -> "Säkerhet & Beteende"
            AppLanguage.AR -> "السلامة والسلوك"
        }
        NAVIGATION -> when (lang) {
            AppLanguage.EN -> "Map & Navigation"
            AppLanguage.SV -> "Karta & Navigering"
            AppLanguage.AR -> "الخريطة والملاحة"
        }
        EQUIPMENT -> when (lang) {
            AppLanguage.EN -> "Taximeter & Equipment"
            AppLanguage.SV -> "Taxameter & Utrustning"
            AppLanguage.AR -> "عداد التاكسي والتجهيزات"
        }
        MOCK_EXAM -> when (lang) {
            AppLanguage.EN -> "Official Mock Exam"
            AppLanguage.SV -> "Officiellt Övningsprov"
            AppLanguage.AR -> "اختبار تجريبي رسمي"
        }
    }

    fun description(lang: AppLanguage): String = when (this) {
        LAW -> when (lang) {
            AppLanguage.EN -> "Swedish road laws, commercial rules & permits"
            AppLanguage.SV -> "Svenska väglagar, yrkestrafik och tillstånd"
            AppLanguage.AR -> "قوانين السير السويدية وتراخيص النقل التجاري"
        }
        SAFETY -> when (lang) {
            AppLanguage.EN -> "Passenger care, child seats & accident protocol"
            AppLanguage.SV -> "Passagerarvård, barnstolar och olyckor"
            AppLanguage.AR -> "رعاية الركاب ومقاعد الأطفال وإجراءات الحوادث"
        }
        NAVIGATION -> when (lang) {
            AppLanguage.EN -> "Swedish address systems, routes & environment zones"
            AppLanguage.SV -> "Adressystem, ruttplanering och miljözoner"
            AppLanguage.AR -> "أنظمة العناوين وتخطيط المسارات والمناطق البيئية"
        }
        EQUIPMENT -> when (lang) {
            AppLanguage.EN -> "Taximeter calibration, yellow plates & receipts"
            AppLanguage.SV -> "Taxameterkalibrering, gula skyltar och kvitton"
            AppLanguage.AR -> "معايرة العداد واللوحات الصفراء وإيصالات الركاب"
        }
        MOCK_EXAM -> when (lang) {
            AppLanguage.EN -> "Complete test simulation across all domains"
            AppLanguage.SV -> "Komplett provsimulering över alla områden"
            AppLanguage.AR -> "محاكاة شاملة للاختبار عبر جميع المجالات"
        }
    }
}
