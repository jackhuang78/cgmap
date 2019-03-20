package com.jhuangyc.cgmap.io

import com.google.common.truth.Truth.assertThat
import com.google.common.truth.Truth.assertWithMessage
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import java.nio.ByteBuffer
import java.util.*

object GraphicDataDecoderSpec : Spek({
	val r1 = Random(0)
	val r2 = Random(0)

	listOf(
			arrayOf("01",
					listOf(0x01, 0x23),
					listOf(0x23)),
			arrayOf("02",
					listOf(0x02, 0x34, 0x56),
					listOf(0x34, 0x56)),
			arrayOf("0F",
					listOf(0x0F).appendRandom(0x0F, r1),
					listOf<Int>().appendRandom(0x0F, r2)),
			arrayOf("10 01",
					listOf(0x10, 0x01, 0x23),
					listOf(0x23)),
			arrayOf("11 12",
					listOf(0x11, 0x23).appendRandom(0x123, r1),
					listOf<Int>().appendRandom(0x123, r2)),
			arrayOf("1F FF",
					listOf(0x1F, 0xFF).appendRandom(0xFFF, r1),
					listOf<Int>().appendRandom(0xFFF, r2)),
			arrayOf("20 00 01",
					listOf(0x20, 0x00, 0x01, 0x23),
					listOf(0x23)),
			arrayOf("21 12 34",
					listOf(0x21, 0x23, 0x45).appendRandom(0x12345, r1),
					listOf<Int>().appendRandom(0x12345, r2)),
			arrayOf("2F FF FF",
					listOf(0x2F, 0xFF, 0xFF).appendRandom(0xFFFFF, r1),
					listOf<Int>().appendRandom(0xFFFFF, r2)),
			arrayOf("81",
					listOf(0x81, 0x23),
					listOf(0x23)),
			arrayOf("82",
					listOf(0x82, 0x23),
					listOf(0x23, 0x23)),
			arrayOf("8F",
					listOf(0x8F, 0x23),
					listOf<Int>().appendRepeated(0xF, 0x23)),
			arrayOf("90 01",
					listOf(0x90, 0x23, 0x01),
					listOf(0x23)),
			arrayOf("90 12",
					listOf(0x91, 0x45, 0x23),
					listOf<Int>().appendRepeated(0x123, 0x45)),
			arrayOf("9F FF",
					listOf(0x9F, 0x45, 0xFF),
					listOf<Int>().appendRepeated(0xFFF, 0x45)),
			arrayOf("A0 00 01",
					listOf(0xA0, 0x23, 0x00, 0x01),
					listOf(0x23)),
			arrayOf("A0 12 34",
					listOf(0xA1, 0x67, 0x23, 0x45),
					listOf<Int>().appendRepeated(0x12345, 0x67)),
			arrayOf("AF FF FF",
					listOf(0xAF, 0x67, 0xFF, 0xFF),
					listOf<Int>().appendRepeated(0xFFFFF, 0x67)),
			arrayOf("C1",
					listOf(0xC1),
					listOf(null)),
			arrayOf("C2",
					listOf(0xC2),
					listOf(null, null)),
			arrayOf("CF",
					listOf(0xCF),
					listOf<Int>().appendRepeated(0xF, null)),
			arrayOf("D0 01",
					listOf(0xD0, 0x01),
					listOf(null)),
			arrayOf("D1 23",
					listOf(0xD1, 0x23),
					listOf<Int>().appendRepeated(0x123, null)),
			arrayOf("DF FF",
					listOf(0xDF, 0xFF),
					listOf<Int>().appendRepeated(0xFFF, null)),
			arrayOf("E0 00 01",
					listOf(0xE0, 0x00, 0x01),
					listOf(null)),
			arrayOf("E1 23 45",
					listOf(0xE1, 0x23, 0x45),
					listOf<Int>().appendRepeated(0x12345, null)),
			arrayOf("EF FF FF",
					listOf(0xEF, 0xFF, 0xFF),
					listOf<Int>().appendRepeated(0xFFFFF, null)),
			arrayOf("Mixed",
					listOf<Int>()
							.append(0x01).appendRandom(0x1, r1)
							.append(0x12, 0x02).appendRandom(0x202, r1)
							.append(0x23, 0x03, 0x03).appendRandom(0x30303, r1)
							.append(0x89, 0x09)
							.append(0x9A, 0x0A, 0x0B)
							.append(0xAB, 0x0B, 0x0C, 0x0D)
							.append(0xCD)
							.append(0xDE, 0x0E)
							.append(0xEF, 0x0F, 0x10),
					listOf<Int>()
							.appendRandom(0x1, r2)
							.appendRandom(0x202, r2)
							.appendRandom(0x30303, r2)
							.appendRepeated(0x09, 0x09)
							.appendRepeated(0xA0B, 0x0A)
							.appendRepeated(0xB0C0D, 0x0B)
							.appendRepeated(0xD, null)
							.appendRepeated(0xE0E, null)
							.appendRepeated(0xF0F10, null))

	).forEach { (name, data, expected) ->
		given("a decoder with data ${name}") {
			val decoder = GraphicDataDecoder((data as List<Int?>).toByteBuffer())

			on("iterator()") {
				val expectedIterator = (expected as List<Int?>).iterator()
				val actual = decoder.iterator()

				it("should iterate and decode data correctly") {
					var iteration = 0
					while (expectedIterator.hasNext()) {
						assertWithMessage("${iteration}").that(actual.hasNext()).isTrue()
						assertWithMessage("${iteration}").that(actual.next()).isEqualTo(
								expectedIterator.next())
						iteration++
					}
					assertThat(actual.hasNext()).isFalse()
				}
			}
		}
	}
})

fun List<Int?>.toByteBuffer() = ByteBuffer.wrap(
		this.map { it!!.toByte() }.toByteArray()
)

fun List<Int?>.appendRandom(n: Int, r: Random): List<Int?> {
	val ml = this.toMutableList()
	ml.addAll((1..n).map { r.nextInt(256) })
	return ml
}

fun List<Int?>.appendRepeated(n: Int, v: Int?): List<Int?> {
	val ml = this.toMutableList()
	ml.addAll((1..n).map { v })
	return ml
}

fun List<Int?>.append(vararg vs: Int?): List<Int?> {
	val ml = this.toMutableList()
	ml.addAll(vs.toList())
	return ml
}