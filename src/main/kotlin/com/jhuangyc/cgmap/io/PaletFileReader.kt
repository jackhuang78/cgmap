package com.jhuangyc.cgmap.io

import com.jhuangyc.cgmap.entity.Palet
import com.jhuangyc.cgmap.util.getUByte
import java.awt.Color
import java.nio.channels.FileChannel
import java.nio.file.Path
import java.nio.file.StandardOpenOption

class PaletFileReader(private val file: Path) {

	companion object {
		val PALET_FILE_SIZE = 708L
		val PALET_NUM_COLORS = 256
		val PRESET_COLORS_0_15 = listOf(
				Color(0x00, 0x00, 0x00),
				Color(0x00, 0x00, 0x80),
				Color(0x00, 0x80, 0x00),
				Color(0x00, 0x80, 0x80),
				Color(0x80, 0x00, 0x00),
				Color(0x80, 0x00, 0x80),
				Color(0x80, 0x80, 0x00),
				Color(0xC0, 0xC0, 0xC0),
				Color(0xC0, 0xDC, 0xC0),
				Color(0xF0, 0xCA, 0xA6),
				Color(0x00, 0x00, 0xDF),
				Color(0x00, 0x5F, 0xFF),
				Color(0xA0, 0xFF, 0xFF),
				Color(0xD2, 0x5F, 0x00),
				Color(0xFF, 0xD2, 0x50),
				Color(0x28, 0xE1, 0x28)
		)
		val PRESET_COLORS_240_255 = listOf(
				Color(0x96, 0xC3, 0xF5),
				Color(0x5F, 0xA0, 0x1E),
				Color(0x46, 0x7D, 0xC3),
				Color(0x1E, 0x55, 0x9B),
				Color(0x37, 0x41, 0x46),
				Color(0x1E, 0x23, 0x28),
				Color(0xF0, 0xFB, 0xFF),
				Color(0xA5, 0x6E, 0x3A),
				Color(0x80, 0x80, 0x80),
				Color(0x00, 0x00, 0xFF),
				Color(0x00, 0xFF, 0x00),
				Color(0x00, 0xFF, 0xFF),
				Color(0xFF, 0x00, 0x00),
				Color(0xFF, 0x80, 0xFF),
				Color(0xFF, 0xFF, 0x00),
				Color(0xFF, 0xFF, 0xFF)
		)
	}

	fun read(): Palet {
		FileChannel.open(file, StandardOpenOption.READ).use {
			val buffer = it.map(FileChannel.MapMode.READ_ONLY, 0, PALET_FILE_SIZE)

			val colors = List(PALET_NUM_COLORS) { colorIndex ->
				when (colorIndex) {
					in 0..15 -> PRESET_COLORS_0_15[colorIndex]
					in 16..239 -> {
						val b = buffer.getUByte().toInt()
						val g = buffer.getUByte().toInt()
						val r = buffer.getUByte().toInt()
						Color(r, g, b)
					}
					in 240..255 -> PRESET_COLORS_240_255[colorIndex - 240]
					else -> error(AssertionError())
				}
			}

			return Palet(colors = colors)
		}
	}

}