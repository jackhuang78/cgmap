package com.jhuangyc.cgmap.io

import com.google.common.truth.Truth.assertThat
import com.google.common.truth.Truth.assertWithMessage
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import java.nio.ByteBuffer
import java.util.*

class GraphicDataDecoderTest : Spek({
	describe("a GraphicDataDecoder") {
		val r1 = Random(0)
		val r2 = Random(0)

		data class TestCase(
				val name: String,
				val data: List<Int?>,
				val expected: List<Int?>
		)

		listOf(
				TestCase(name = "01",
						data = listOf(0x01, 0x23),
						expected = listOf(0x23)),
				TestCase(name = "02",
						data = listOf(0x02, 0x34, 0x56),
						expected = listOf(0x34, 0x56)),
				TestCase(name = "0F",
						data = listOf(0x0F).appendRandom(0x0F, r1),
						expected = listOf<Int>().appendRandom(0x0F, r2)),
				TestCase(name = "10 01",
						data = listOf(0x10, 0x01, 0x23),
						expected = listOf(0x23)),
				TestCase(name = "11 12",
						data = listOf(0x11, 0x23).appendRandom(0x123, r1),
						expected = listOf<Int>().appendRandom(0x123, r2)),
				TestCase(name = "1F FF",
						data = listOf(0x1F, 0xFF).appendRandom(0xFFF, r1),
						expected = listOf<Int>().appendRandom(0xFFF, r2)),
				TestCase(name = "20 00 01",
						data = listOf(0x20, 0x00, 0x01, 0x23),
						expected = listOf(0x23)),
				TestCase(name = "21 12 34",
						data = listOf(0x21, 0x23, 0x45).appendRandom(0x12345, r1),
						expected = listOf<Int>().appendRandom(0x12345, r2)),
				TestCase(name = "2F FF FF",
						data = listOf(0x2F, 0xFF, 0xFF).appendRandom(0xFFFFF, r1),
						expected = listOf<Int>().appendRandom(0xFFFFF, r2)),
				TestCase(name = "81",
						data = listOf(0x81, 0x23),
						expected = listOf(0x23)),
				TestCase(name = "82",
						data = listOf(0x82, 0x23),
						expected = listOf(0x23, 0x23)),
				TestCase(name = "8F",
						data = listOf(0x8F, 0x23),
						expected = listOf<Int>().appendRepeated(0xF, 0x23)),
				TestCase(name = "90 01",
						data = listOf(0x90, 0x23, 0x01),
						expected = listOf(0x23)),
				TestCase(name = "90 12",
						data = listOf(0x91, 0x45, 0x23),
						expected = listOf<Int>().appendRepeated(0x123, 0x45)),
				TestCase(name = "9F FF",
						data = listOf(0x9F, 0x45, 0xFF),
						expected = listOf<Int>().appendRepeated(0xFFF, 0x45)),
				TestCase(name = "A0 00 01",
						data = listOf(0xA0, 0x23, 0x00, 0x01),
						expected = listOf(0x23)),
				TestCase(name = "A0 12 34",
						data = listOf(0xA1, 0x67, 0x23, 0x45),
						expected = listOf<Int>().appendRepeated(0x12345, 0x67)),
				TestCase(name = "AF FF FF",
						data = listOf(0xAF, 0x67, 0xFF, 0xFF),
						expected = listOf<Int>().appendRepeated(0xFFFFF, 0x67)),
				TestCase(name = "C1",
						data = listOf(0xC1),
						expected = listOf(null)),
				TestCase(name = "C2",
						data = listOf(0xC2),
						expected = listOf(null, null)),
				TestCase(name = "CF",
						data = listOf(0xCF),
						expected = listOf<Int>().appendRepeated(0xF, null)),
				TestCase(name = "D0 01",
						data = listOf(0xD0, 0x01),
						expected = listOf(null)),
				TestCase(name = "D1 23",
						data = listOf(0xD1, 0x23),
						expected = listOf<Int>().appendRepeated(0x123, null)),
				TestCase(name = "DF FF",
						data = listOf(0xDF, 0xFF),
						expected = listOf<Int>().appendRepeated(0xFFF, null)),
				TestCase(name = "E0 00 01",
						data = listOf(0xE0, 0x00, 0x01),
						expected = listOf(null)),
				TestCase(name = "E1 23 45",
						data = listOf(0xE1, 0x23, 0x45),
						expected = listOf<Int>().appendRepeated(0x12345, null)),
				TestCase(name = "EF FF FF",
						data = listOf(0xEF, 0xFF, 0xFF),
						expected = listOf<Int>().appendRepeated(0xFFFFF, null)),
				TestCase(name = "Mixed",
						data = listOf<Int>()
								.append(0x01).appendRandom(0x1, r1)
								.append(0x12, 0x02).appendRandom(0x202, r1)
								.append(0x23, 0x03, 0x03).appendRandom(0x30303, r1)
								.append(0x89, 0x09)
								.append(0x9A, 0x0A, 0x0B)
								.append(0xAB, 0x0B, 0x0C, 0x0D)
								.append(0xCD)
								.append(0xDE, 0x0E)
								.append(0xEF, 0x0F, 0x10),
						expected = listOf<Int>()
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

			describe("case ${name}") {
				val decoder = GraphicDataDecoder(data.toByteBuffer())

				it("should decode data correctly") {
					val expectedIterator = expected.iterator()
					val actual = decoder.iterator()

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