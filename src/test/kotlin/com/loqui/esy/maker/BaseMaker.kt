package com.loqui.esy.maker

import com.fasterxml.jackson.databind.ObjectMapper
import com.loqui.esy.data.view.ErrorView
import com.loqui.esy.data.view.ResponseView
import com.loqui.esy.data.wrapper.EsyError
import java.util.*

val objectMapper = ObjectMapper()

fun getPrefix(i: Int?): String {
    if (i != null) {
        return "$i-"
    }
    return ""
}

fun <T> toResponse(data: T): ResponseView<T> {
    return ResponseView(
        true,
        data
    )
}

fun toResponse(error: EsyError): ResponseView<ErrorView> {
    return ResponseView(
        false,
        ErrorView(error.code, error.message)
    )
}


fun <T> toJSON(obj: T): String {
    return objectMapper.writeValueAsString(obj)
}

fun toUUID(uuid: String): UUID {
    if (uuid.length == 36) {
        val ch1 = uuid[8]
        val ch2 = uuid[13]
        val ch3 = uuid[18]
        val ch4 = uuid[23]
        if (ch1 == '-' && ch2 == '-' && ch3 == '-' && ch4 == '-') {
            val msb1 = uuidParse4Nibbles(uuid, 0)
            val msb2 = uuidParse4Nibbles(uuid, 4)
            val msb3 = uuidParse4Nibbles(uuid, 9)
            val msb4 = uuidParse4Nibbles(uuid, 14)
            val lsb1 = uuidParse4Nibbles(uuid, 19)
            val lsb2 = uuidParse4Nibbles(uuid, 24)
            val lsb3 = uuidParse4Nibbles(uuid, 28)
            val lsb4 = uuidParse4Nibbles(uuid, 32)
            if (msb1 or msb2 or msb3 or msb4 or lsb1 or lsb2 or lsb3 or lsb4 >= 0) {
                return UUID(
                    msb1 shl 48 or (msb2 shl 32) or (msb3 shl 16) or msb4,
                    lsb1 shl 48 or (lsb2 shl 32) or (lsb3 shl 16) or lsb4
                )
            }
        }
    }
    throw IllegalArgumentException("Invalid UUID")
}

fun uuidParse4Nibbles(uuid: String, pos: Int): Long {
    val ns = ByteArray(256)
    ns['0'.code] = 0
    ns['1'.code] = 1
    ns['2'.code] = 2
    ns['3'.code] = 3
    ns['4'.code] = 4
    ns['5'.code] = 5
    ns['6'.code] = 6
    ns['7'.code] = 7
    ns['8'.code] = 8
    ns['9'.code] = 9
    ns['A'.code] = 10
    ns['B'.code] = 11
    ns['C'.code] = 12
    ns['D'.code] = 13
    ns['E'.code] = 14
    ns['F'.code] = 15
    ns['a'.code] = 10
    ns['b'.code] = 11
    ns['c'.code] = 12
    ns['d'.code] = 13
    ns['e'.code] = 14
    ns['f'.code] = 15
    val ch1 = uuid[pos]
    val ch2 = uuid[pos + 1]
    val ch3 = uuid[pos + 2]
    val ch4 = uuid[pos + 3]
    return if (ch1.code or ch2.code or ch3.code or ch4.code > 0xff) -1 else (ns[ch1.code].toInt() shl 12 or (ns[ch2.code].toInt() shl 8) or (ns[ch3.code].toInt() shl 4) or ns[ch4.code].toInt()).toLong()
}
