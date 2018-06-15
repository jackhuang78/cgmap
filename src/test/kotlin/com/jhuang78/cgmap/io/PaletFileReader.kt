package com.jhuang78.cgmap.io

import com.google.common.truth.Truth.assertThat
import com.jhuang78.cgmap.util.underResource
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import java.awt.Color
import java.nio.file.Paths

class PaletFileReader : Spek({

	describe("readPaletFile()") {

		given("an valid palet file") {
			val palet = readPaletFile(
					Paths.get("palet", "palet_00.cgp").underResource())

			it("should read base colors") {
				assertThat(palet.colors[0]).isEqualTo(Color(0x00, 0x00, 0x00))
				assertThat(palet.colors[15]).isEqualTo(Color(0x28, 0xE1, 0x28))
				assertThat(palet.colors[240]).isEqualTo(Color(0x96, 0xC3, 0xF5))
				assertThat(palet.colors[255]).isEqualTo(Color(0xFF, 0xFF, 0xFF))
			}
			it("should read custom colors") {
				assertThat(palet.colors[16]).isEqualTo(Color(0xA4, 0xF7, 0x85))
				assertThat(palet.colors[17]).isEqualTo(Color(0x96, 0xE2, 0x7A))
				assertThat(palet.colors[32]).isEqualTo(Color(0xB9, 0x52, 0x41))
				assertThat(palet.colors[64]).isEqualTo(Color(0xBE, 0x8A, 0xEC))
				assertThat(palet.colors[128]).isEqualTo(Color(0xA0, 0xC8, 0xF6))
				assertThat(palet.colors[239]).isEqualTo(Color(0x92, 0x53, 0x4D))
			}
		}
	}
})