package com.visualeap.aliforreddit.presentation.util

import java.util.*
import java.util.concurrent.TimeUnit

fun formatTimestamp(time: Long): String {
    if (time < 0) return UNKNOWN

    val createdInMillis = TimeUnit.SECONDS.toMillis(time)
    val now = System.currentTimeMillis()
    val timeSpan = now - createdInMillis
    return when {
        timeSpan < MINUTE_IN_MILLIS -> JUST_NOW
        timeSpan < HOUR_IN_MILLIS -> "${timeSpan / MINUTE_IN_MILLIS}$MINUTE_ABBREVIATION"
        timeSpan < DAY_IN_MILLIS -> "${timeSpan / HOUR_IN_MILLIS}$HOUR_ABBREVIATION"
        timeSpan < MONTH_IN_MILLIS -> "${timeSpan / DAY_IN_MILLIS}$DAY_ABBREVIATION"
        timeSpan < YEAR_IN_MILLIS -> "${timeSpan / MONTH_IN_MILLIS}$MONTH_ABBREVIATION"
        timeSpan > YEAR_IN_MILLIS -> "${timeSpan / YEAR_IN_MILLIS}$YEAR_ABBREVIATION"
        else -> UNKNOWN
    }
}

fun formatCount(num: Int): String {
    if (num >= THOUSAND) {
        return String.format(Locale.ENGLISH, "%.1f", num / THOUSAND) + THOUSAND_ABBREVIATION
    }
    return num.toString()
}

private const val THOUSAND = 1000f
private const val THOUSAND_ABBREVIATION = "k"

private const val UNKNOWN = "unknown"
private const val JUST_NOW = "just now"
private const val MINUTE_ABBREVIATION = "m"
private const val HOUR_ABBREVIATION = "h"
private const val DAY_ABBREVIATION = "d"
private const val MONTH_ABBREVIATION = "mo"
private const val YEAR_ABBREVIATION = "y"

private const val MINUTE_IN_MILLIS: Long = 60000
private const val HOUR_IN_MILLIS: Long = 3600000
private const val DAY_IN_MILLIS: Long = 86400000
private const val MONTH_IN_MILLIS: Long = 2592000000
private const val YEAR_IN_MILLIS: Long = 31104000000
