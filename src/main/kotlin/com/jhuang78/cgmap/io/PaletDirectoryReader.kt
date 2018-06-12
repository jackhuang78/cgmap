package com.jhuang78.cgmap.io

import com.jhuang78.cgmap.common.toUint
import java.awt.Color
import java.nio.channels.FileChannel
import java.nio.channels.FileChannel.MapMode.READ_ONLY
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import java.util.stream.Collectors

const val PALET_FILE_SIZE = 708L
const val PALET_NUM_COLORS = 256

/**
 * A class to read palet.cgp files from a directory into Palet.
 */
class PaletDirectoryReader(private val directory: Path) {

	fun read(paletFile: Path): Palet {

		val buffer = FileChannel.open(directory.resolve(paletFile),
				StandardOpenOption.READ).map(READ_ONLY, 0, PALET_FILE_SIZE);

		return Palet(colors = List(PALET_NUM_COLORS) {
			when (it) {
				0 -> Color(0x00, 0x00, 0x00)
				1 -> Color(0x00, 0x00, 0x80)
				2 -> Color(0x00, 0x80, 0x00)
				3 -> Color(0x00, 0x80, 0x80)
				4 -> Color(0x80, 0x00, 0x00)
				5 -> Color(0x80, 0x00, 0x80)
				6 -> Color(0x80, 0x80, 0x00)
				7 -> Color(0xC0, 0xC0, 0xC0)
				8 -> Color(0xC0, 0xDC, 0xC0)
				9 -> Color(0xF0, 0xCA, 0xA6)
				10 -> Color(0x00, 0x00, 0xDF)
				11 -> Color(0x00, 0x5F, 0xFF)
				12 -> Color(0xA0, 0xFF, 0xFF)
				13 -> Color(0xD2, 0x5F, 0x00)
				14 -> Color(0xFF, 0xD2, 0x50)
				15 -> Color(0x28, 0xE1, 0x28)
				in 16..239 -> {
					val b = buffer.get().toUint()
					val g = buffer.get().toUint()
					val r = buffer.get().toUint()
					Color(r, g, b)
				}
				240 -> Color(0x96, 0xC3, 0xF5)
				241 -> Color(0x5F, 0xA0, 0x1E)
				242 -> Color(0x46, 0x7D, 0xC3)
				243 -> Color(0x1E, 0x55, 0x9B)
				244 -> Color(0x37, 0x41, 0x46)
				245 -> Color(0x1E, 0x23, 0x28)
				246 -> Color(0xF0, 0xFB, 0xFF)
				247 -> Color(0xA5, 0x6E, 0x3A)
				248 -> Color(0x80, 0x80, 0x80)
				249 -> Color(0x00, 0x00, 0xFF)
				250 -> Color(0x00, 0xFF, 0x00)
				251 -> Color(0x00, 0xFF, 0xFF)
				252 -> Color(0xFF, 0x00, 0x00)
				253 -> Color(0xFF, 0x80, 0xFF)
				254 -> Color(0xFF, 0xFF, 0x00)
				255 -> Color(0xFF, 0xFF, 0xFF)

				else -> throw AssertionError()
			}
		})
	}

	/**
	 * Lists the palet.cgp files under the directory.
	 */
	fun palets(): List<Path> {
		return Files.list(directory).map {
			directory.relativize(it)
		}.collect(Collectors.toList())
	}
}