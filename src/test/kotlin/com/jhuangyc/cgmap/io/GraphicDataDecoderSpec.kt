package com.jhuangyc.cgmap.io

import com.google.common.truth.Truth.assertThat
import com.google.common.truth.Truth.assertWithMessage
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import java.nio.ByteBuffer
import kotlin.random.nextUBytes

@ExperimentalUnsignedTypes
object GraphicDataDecoderSpec : Spek({

	fun UByteArray.toNullable() = Array<UByte?>(size) { get(it) }
	operator fun UByteArray.plus(that: UByteArray) =
			UByteArray(this.size + that.size) {
				if (it < this.size) this[it] else that[it - this.size]
			}


	val inputRng = kotlin.random.Random(0)
	val outputRng = kotlin.random.Random(0)

	listOf(
			arrayOf("01",
					ubyteArrayOf(0x01u, 0x23u),
					arrayOf<UByte?>(0x23u)),
			arrayOf("02",
					ubyteArrayOf(0x02u, 0x34u, 0x56u),
					arrayOf<UByte?>(0x34u, 0x56u)),
			arrayOf("0F",
					ubyteArrayOf(0x0Fu) + inputRng.nextUBytes(0x0F),
					outputRng.nextUBytes(0x0F).toNullable()),
			arrayOf("10 01",
					ubyteArrayOf(0x10u, 0x01u, 0x23u),
					arrayOf<UByte?>(0x23u)),
			arrayOf("11 12",
					ubyteArrayOf(0x11u, 0x23u) + inputRng.nextUBytes(0x123),
					outputRng.nextUBytes(0x123).toNullable()),
			arrayOf("1F FF",
					ubyteArrayOf(0x1Fu, 0xFFu) + inputRng.nextUBytes(0xFFF),
					outputRng.nextUBytes(0xFFF).toNullable()),
			arrayOf("20 00 01",
					ubyteArrayOf(0x20u, 0x00u, 0x01u, 0x23u),
					arrayOf<UByte?>(0x23u)),
			arrayOf("21 12 34",
					ubyteArrayOf(0x21u, 0x23u, 0x45u) + inputRng.nextUBytes(0x12345),
					outputRng.nextUBytes(0x12345).toNullable()),
			arrayOf("2F FF FF",
					ubyteArrayOf(0x2Fu, 0xFFu, 0xFFu) + inputRng.nextUBytes(0xFFFFF),
					outputRng.nextUBytes(0xFFFFF).toNullable()),
			arrayOf("81",
					ubyteArrayOf(0x81u, 0x23u),
					arrayOf<UByte?>(0x23u)),
			arrayOf("82",
					ubyteArrayOf(0x82u, 0x23u),
					arrayOf<UByte?>(0x23u, 0x23u)),
			arrayOf("8F",
					ubyteArrayOf(0x8Fu, 0x23u),
					Array<UByte?>(0xF) { 0x23u }),
			arrayOf("90 01",
					ubyteArrayOf(0x90u, 0x23u, 0x01u),
					arrayOf<UByte?>(0x23u)),
			arrayOf("90 12",
					ubyteArrayOf(0x91u, 0x45u, 0x23u),
					Array<UByte?>(0x123) { 0x45u }),
			arrayOf("9F FF",
					ubyteArrayOf(0x9Fu, 0x45u, 0xFFu),
					Array<UByte?>(0xFFF) { 0x45u }),
			arrayOf("A0 00 01",
					ubyteArrayOf(0xA0u, 0x23u, 0x00u, 0x01u),
					arrayOf<UByte?>(0x23u)),
			arrayOf("A0 12 34",
					ubyteArrayOf(0xA1u, 0x67u, 0x23u, 0x45u),
					Array<UByte?>(0x12345) { 0x67u }),
			arrayOf("AF FF FF",
					ubyteArrayOf(0xAFu, 0x67u, 0xFFu, 0xFFu),
					Array<UByte?>(0xFFFFF) { 0x67u }),
			arrayOf("C1",
					ubyteArrayOf(0xC1u),
					arrayOf<UByte?>(null)),
			arrayOf("C2",
					ubyteArrayOf(0xC2u),
					arrayOf<UByte?>(null, null)),
			arrayOf("CF",
					ubyteArrayOf(0xCFu),
					Array<UByte?>(0xF) { null }),
			arrayOf("D0 01",
					ubyteArrayOf(0xD0u, 0x01u),
					arrayOf<UByte?>(null)),
			arrayOf("D1 23",
					ubyteArrayOf(0xD1u, 0x23u),
					Array<UByte?>(0x123) { null }),
			arrayOf("DF FF",
					ubyteArrayOf(0xDFu, 0xFFu),
					Array<UByte?>(0xFFF) { null }),
			arrayOf("E0 00 01",
					ubyteArrayOf(0xE0u, 0x00u, 0x01u),
					arrayOf<UByte?>(null)),
			arrayOf("E1 23 45",
					ubyteArrayOf(0xE1u, 0x23u, 0x45u),
					Array<UByte?>(0x12345) { null }),
			arrayOf("EF FF FF",
					ubyteArrayOf(0xEFu, 0xFFu, 0xFFu),
					Array<UByte?>(0xFFFFF) { null }),
			arrayOf("Mixed",
					ubyteArrayOf(0x01u) + inputRng.nextUBytes(0x1) +
							ubyteArrayOf(0x12u, 0x02u) + inputRng.nextUBytes(0x202) +
							ubyteArrayOf(0x23u, 0x03u, 0x03u) + inputRng.nextUBytes(0x30303) +
							ubyteArrayOf(0x89u, 0x09u) +
							ubyteArrayOf(0x9Au, 0x0Au, 0x0Bu) +
							ubyteArrayOf(0xABu, 0x0Bu, 0x0Cu, 0x0Du) +
							ubyteArrayOf(0xCDu) +
							ubyteArrayOf(0xDEu, 0x0Eu) +
							ubyteArrayOf(0xEFu, 0x0Fu, 0x10u),
					outputRng.nextUBytes(0x1).toNullable() +
							outputRng.nextUBytes(0x202).toNullable() +
							outputRng.nextUBytes(0x30303).toNullable() +
							Array<UByte?>(0x09) { 0x09u } +
							Array<UByte?>(0xA0B) { 0x0Au } +
							Array<UByte?>(0xB0C0D) { 0x0Bu } +
							Array<UByte?>(0xD) { null } +
							Array<UByte?>(0xE0E) { null } +
							Array<UByte?>(0xF0F10) { null })

	).forEach { (name, data, expected) ->
		given("a decoder with data ${name}") {
			val bytes = ByteBuffer.wrap((data as UByteArray).asByteArray())
			val decoder = GraphicDataDecoder(bytes)

			on("iterator()") {
				val expectedIterator = (expected as Array<UByte?>).iterator()
				val actual = decoder.iterator()

				it("should iterate and decode data correctly") {
					var iteration = 0
					while (expectedIterator.hasNext()) {
						assertWithMessage("${iteration}").that(actual.hasNext()).isTrue()
						assertWithMessage("${iteration}").that(
								actual.next()?.toUByte()).isEqualTo(
								expectedIterator.next())
						iteration++
					}
					assertThat(actual.hasNext()).isFalse()
				}
			}
		}
	}
})


