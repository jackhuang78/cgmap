package com.jhuang78.cgmap.util

import java.nio.ByteBuffer

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

/**
 * Provides a view of the ByteBuffer in the form of Iterable
 */
fun ByteBuffer.asIterable(start: Int = 0,
		end: Int = this.capacity()): Iterable<Byte> {

	return object : Iterable<Byte> {

		override fun iterator(): Iterator<Byte> {

			return object : Iterator<Byte> {
				var current = start

				override fun hasNext(): Boolean {
					return (current < end)
				}

				override fun next(): Byte {
					val byte = this@asIterable[current]
					current++
					return byte
				}
			}
		}
	}
}