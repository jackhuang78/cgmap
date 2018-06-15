package com.jhuang78.cgmap.util

/**
 * Unsigned-extends a short to an int.
 */
fun Short.toUint() = (this.toInt() and 0x0000FFFF)

/**
 * Unsigned-extends a byte to an int.
 */
fun Byte.toUint() = (this.toInt() and 0x000000FF)

/**
 * Converts a byte into hex string format
 */
fun Byte.toHex() = "%02x".format(this)

/**
 * Converts a short into hex string format
 */
fun Short.toHex() = "%04x".format(this)

/**
 * Extracts bits from byte.
 */
fun Byte.bits(msb: Int, lsb: Int)
		= (this.toUint() shr lsb) and (0x000000FF ushr (7 - msb + lsb))


