package dev.numberonedroid.scheduler.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DateUtil {

    companion object {
        fun localDateTimeToString(localDateTime: LocalDateTime): String {
            return localDateTime.format(
                DateTimeFormatter.ofPattern("MM월 dd일 HH:mm")
            )
        }
    }

}