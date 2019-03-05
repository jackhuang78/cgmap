package com.jhuangyc.cgmap.graphics.deprecated

import com.google.common.truth.Truth.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import java.nio.ByteBuffer
import java.util.*

class DecoderTest : Spek({
  describe("CompressedDataDecoder") {
    val r1 = Random(0)
    val r2 = Random(0)

    listOf(
        arrayOf("01", 0, 2,
            listOf(0x01, 0x23),
            listOf(0x23)),
        arrayOf("02", 0, 3,
            listOf(0x02, 0x34, 0x56),
            listOf(0x34, 0x56)),
        arrayOf("0F", 0, 1 + 0x0F,
            listOf(0x0F).appendRandom(0x0F, r1),
            listOf<Int>().appendRandom(0x0F, r2)),
        arrayOf("10 01", 0, 3,
            listOf(0x10, 0x01, 0x23),
            listOf(0x23)),
        arrayOf("11 12", 0, 2 + 0x123,
            listOf(0x11, 0x23).appendRandom(0x123, r1),
            listOf<Int>().appendRandom(0x123, r2)),
        arrayOf("1F FF", 0, 2 + 0xFFF,
            listOf(0x1F, 0xFF).appendRandom(0xFFF, r1),
            listOf<Int>().appendRandom(0xFFF, r2)),
        arrayOf("20 00 01", 0, 4,
            listOf(0x20, 0x00, 0x01, 0x23),
            listOf(0x23)),
        arrayOf("21 12 34", 0, 3 + 0x12345,
            listOf(0x21, 0x23, 0x45).appendRandom(0x12345, r1),
            listOf<Int>().appendRandom(0x12345, r2)),
        arrayOf("2F FF FF", 0, 3 + 0xFFFFF,
            listOf(0x2F, 0xFF, 0xFF).appendRandom(0xFFFFF, r1),
            listOf<Int>().appendRandom(0xFFFFF, r2)),
        arrayOf("81", 0, 2,
            listOf(0x81, 0x23),
            listOf(0x23)),
        arrayOf("82", 0, 2,
            listOf(0x82, 0x23),
            listOf(0x23, 0x23)),
        arrayOf("8F", 0, 2,
            listOf(0x8F, 0x23),
            listOf<Int>().appendRepeated(0xF, 0x23)),
        arrayOf("90 01", 0, 3,
            listOf(0x90, 0x23, 0x01),
            listOf(0x23)),
        arrayOf("90 12", 0, 3,
            listOf(0x91, 0x45, 0x23),
            listOf<Int>().appendRepeated(0x123, 0x45)),
        arrayOf("9F FF", 0, 3,
            listOf(0x9F, 0x45, 0xFF),
            listOf<Int>().appendRepeated(0xFFF, 0x45)),
        arrayOf("A0 00 01", 0, 4,
            listOf(0xA0, 0x23, 0x00, 0x01),
            listOf(0x23)),
        arrayOf("A0 12 34", 0, 4,
            listOf(0xA1, 0x67, 0x23, 0x45),
            listOf<Int>().appendRepeated(0x12345, 0x67)),
        arrayOf("AF FF FF", 0, 4,
            listOf(0xAF, 0x67, 0xFF, 0xFF),
            listOf<Int>().appendRepeated(0xFFFFF, 0x67)),
        arrayOf("C1", 0, 1,
            listOf(0xC1),
            listOf(-1)),
        arrayOf("C2", 0, 1,
            listOf(0xC2),
            listOf(-1, -1)),
        arrayOf("CF", 0, 1,
            listOf(0xCF),
            listOf<Int>().appendRepeated(0xF, -1)),
        arrayOf("D0 01", 0, 2,
            listOf(0xD0, 0x01),
            listOf(-1)),
        arrayOf("D1 23", 0, 2,
            listOf(0xD1, 0x23),
            listOf<Int>().appendRepeated(0x123, -1)),
        arrayOf("DF FF", 0, 2,
            listOf(0xDF, 0xFF),
            listOf<Int>().appendRepeated(0xFFF, -1)),
        arrayOf("E0 00 01", 0, 3,
            listOf(0xE0, 0x00, 0x01),
            listOf(-1)),
        arrayOf("E1 23 45", 0, 3,
            listOf(0xE1, 0x23, 0x45),
            listOf<Int>().appendRepeated(0x12345, -1)),
        arrayOf("EF FF FF", 0, 3,
            listOf(0xEF, 0xFF, 0xFF),
            listOf<Int>().appendRepeated(0xFFFFF, -1)),
        arrayOf("Mixed", 0, 1 + 0x1 + 2 + 0x202 + 3 + 0x30303 + 2 + 3 + 4 + 1 + 2 + 3,
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
                .appendRepeated(0xD, -1)
                .appendRepeated(0xE0E, -1)
                .appendRepeated(0xF0F10, -1))
    ).forEach { (name, start, length, data, output) ->

      describe("case ${name}") {
        val decoder = CompressedDataDecoder(
						(data as List<Int>).toByteBuffer())

        it("should decode data correctly") {
          val expected = (output as List<Int>).iterator()
          val actual = decoder.iterator()
          while (expected.hasNext()) {
            assertThat(actual.hasNext()).isTrue()
            assertThat(actual.next()).isEqualTo(expected.next())
          }
          assertThat(actual.hasNext()).isFalse()
        }
      }
    }
  }
})

fun List<Int>.toByteBuffer() = ByteBuffer.wrap(
    this.map { it.toByte() }.toByteArray()
)

fun List<Int>.appendRandom(n: Int, r: Random): List<Int> {
  val ml = this.toMutableList()
  ml.addAll((1..n).map { r.nextInt(256) })
  return ml
}

fun List<Int>.appendRepeated(n: Int, v: Int): List<Int> {
  val ml = if (this is MutableList<Int>) this else this.toMutableList()
  ml.addAll((1..n).map { v })
  return ml
}

fun List<Int>.append(vararg vs: Int): List<Int> {
  val ml = if (this is MutableList<Int>) this else this.toMutableList()
  ml.addAll(vs.toList())
  return ml
}