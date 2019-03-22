package com.jhuangyc.cgmap.util

/**
 * Converts a byte into hex string format
 */
fun Byte.toHex() = "%02x".format(this)

/**
 * Converts a short into hex string format
 */
fun Short.toHex() = "%04x".format(this)


/**
 *
 */
fun Byte.bits(msb: Int,
		lsb: Int) = (this.toUByte().toInt() shr lsb) and (0x000000FF ushr (7 - msb + lsb))

/**
 * Converts an int into hex string format
 */
fun Int.toHex() = "%04x".format(this)
