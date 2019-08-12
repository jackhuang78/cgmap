package com.jhuangyc.cgmap.io

import com.jhuangyc.cgmap.util.fromResources
import org.spekframework.spek2.Spek
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import java.awt.Color
import java.nio.file.Paths

object PaletFileReaderSpec : Spek({

	group("Given a palet file") {
		val paletFile = Paths.get("palet", "palet_00.cgp").fromResources()
		val reader = PaletFileReader(paletFile)

		listOf(
			arrayOf(0, Color(0x00, 0x00, 0x00)),
			arrayOf(15, Color(0x28, 0xE1, 0x28)),
			arrayOf(240, Color(0x96, 0xC3, 0xF5)),
			arrayOf(255, Color(0xFF, 0xFF, 0xFF))
		).forEach { (colorIdx, expectedColor) ->

			test("Should read base color $colorIdx") {
				val palet = reader.read()

				expectThat(palet.colors[colorIdx as Int]).isEqualTo(
					expectedColor as Color)
			}
		}

		listOf(
			arrayOf(16, Color(0xA4, 0xF7, 0x85)),
			arrayOf(17, Color(0x96, 0xE2, 0x7A)),
			arrayOf(32, Color(0xB9, 0x52, 0x41)),
			arrayOf(64, Color(0xBE, 0x8A, 0xEC)),
			arrayOf(128, Color(0xA0, 0xC8, 0xF6)),
			arrayOf(239, Color(0x92, 0x53, 0x4D))
		).forEach { (colorIdx, expectedColor) ->

			test("Should read custom color $colorIdx") {
				val palet = reader.read()

				expectThat(palet.colors[colorIdx as Int])
					.isEqualTo(expectedColor as Color)
			}
		}
	}
})