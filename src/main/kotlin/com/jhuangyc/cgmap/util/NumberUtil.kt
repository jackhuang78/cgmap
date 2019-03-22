package com.jhuangyc.cgmap.util

/**
 * Converts a byte into hex string format
 */
fun Byte.toHex() = "%02x".format(this)

/**
 * Converts an int into hex string format
 */
fun Int.toHex() = "%04x".format(this)

/**
 * Extract bits from a byte.
 */
fun Byte.bits(msb: Int, lsb: Int) =
		((this.toUInt() shr lsb) and (0xFFu shr (7 - msb + lsb))).toInt()



