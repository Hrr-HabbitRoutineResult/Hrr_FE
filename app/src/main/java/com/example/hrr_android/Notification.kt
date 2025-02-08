package com.example.hrr_android

data class Notification(
    val type: NotificationType,
    val dynamicValues: Map<String, String>,
    val timeLeft: TimeLeft?,
    val time: String,
    val coverImg: Int
) {
    // title 템플릿 변환
    fun getFormattedTitle(): String {
        return formatTemplate(type.titleTemplate, dynamicValues)
    }

    // content 템플릿 변환
    fun getFormattedContent(): String {
        var formattedText = type.contentTemplate?.let { formatTemplate(it, dynamicValues) } ?: ""
        // time_left를 직접 치환
        timeLeft?.let { formattedText = formattedText.replace("{time_left}", it.displayText) }
        return formattedText
    }

    // 템플릿 변환 함수
    private fun formatTemplate(template: String, values: Map<String, String>): String {
        var formattedText = template
        values.forEach { (key, value) ->
            formattedText = formattedText.replace("{$key}", value)
        }
        return formattedText
    }
}
