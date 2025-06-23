package dev.antoine.moduloestudiante.locale

import java.text.NumberFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*
import kotlin.math.pow
import kotlin.math.roundToInt

private val locale = Locale.getDefault()
private val lang = locale.displayLanguage
private val country = locale.displayCountry
private val LocaleES = Locale.forLanguageTag("es, ES")


fun Double.toLocalNumber(): String {
    return NumberFormat.getNumberInstance(Locale.getDefault()).format(this)
}

fun Double.round(decimals: Int): Double {
    val factor = 10.0.pow(decimals)
    return (this * factor).roundToInt() / factor
}