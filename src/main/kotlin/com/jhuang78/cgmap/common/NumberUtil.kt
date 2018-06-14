package com.jhuang78.cgmap.common

import com.google.common.primitives.UnsignedBytes
import java.nio.ByteBuffer

/**
 * Unsigned-extends a short to an int.
 */
fun Short.toUint() = (this.toInt() and 0x0000FFFF)

/**
 * Unsigned-extends a byte to an int.
 */
fun Byte.toUint() =  (this.toInt() and 0x000000FF)

/**
 * Converts a byte into hex string format
 */
fun Byte.toHex() = "%02x".format(this)

/**
 *
 */
fun Byte.bits(msb: Int, lsb: Int)
		= (this.toUint() shr lsb) and (0x000000FF ushr (7 - msb + lsb))

/**
 * Converts an int into hex string format
 */
fun Int.toHex() = "%04x".format(this)

/**
 * Converts a ByteBuffer into a 2D memory map representation
 */
fun ByteBuffer.illustrate(): String {
	val sb = StringBuilder()
	for (i in 0 until this.capacity()) {
		if (i % 16 == 0) {
			sb.append("${i.toHex()}: ")
		}
		sb.append("${this[i].toHex()} ")
		if (i % 16 == 15) {
			sb.appendln()
		}
	}
	sb.appendln()
	return sb.toString()
}

fun ByteBuffer.getOrZero(idx: Int): Byte {
	return if (this.capacity() < idx) this[idx] else 0
}