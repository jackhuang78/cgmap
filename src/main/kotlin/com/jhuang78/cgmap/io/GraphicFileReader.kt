package com.jhuang78.cgmap.io

import com.google.common.base.Preconditions.checkElementIndex
import com.jhuang78.cgmap.common.toUint
import com.jhuang78.cgmap.entity.Graphic
import com.jhuang78.cgmap.entity.Graphic.Version.ENCODED
import com.jhuang78.cgmap.entity.Graphic.Version.RAW
import mu.KotlinLogging
import java.nio.ByteOrder.LITTLE_ENDIAN
import java.nio.channels.FileChannel
import java.nio.channels.FileChannel.MapMode.READ_ONLY
import java.nio.file.Path
import java.nio.file.StandardOpenOption

/**
 * Logger for this file.
 */
private val logger = KotlinLogging.logger {}

/**
 * Size of the header part of the entry (i.e. everything except the actual
 * data).
 */
private const val GRAPHIC_ENTRY_HEADER_SIZE = 16

/**
 * The magic value to mapMarker the beginning of each Graphic entry.
 * The value is "RD" in bytes.
 * Ints.fromBytes(0, 0, 'D'.toByte(), 'R'.toByte())
 */
private const val GRAPHIC_ENTRY_MAGIC = 0x4452

/**
 * A class to read Graphic from Graphic.bin file.
 */
class GraphicFileReader(val path: Path) : AutoCloseable {

	private val fileChannel = FileChannel.open(path, StandardOpenOption.READ)

	/**
	 * Reads an Graphic from file.
	 */
	fun read(position: Int, size: Int): Graphic {
		checkElementIndex(position, fileChannel.size().toInt())

		logger.info("Reading ${size} bytes from ${path} at position ${position}")

		val buffer = fileChannel.map(READ_ONLY, position.toLong(),
				size.toLong()).order(LITTLE_ENDIAN);

		val graphic = Graphic(
				magic = buffer.getShort().toUint(),
				version = if (buffer.get().toUint() == 0) RAW else ENCODED,
				unknown = buffer.get().toUint(), width = buffer.getInt(),
				height = buffer.getInt(),
				dataLength = buffer.getInt(), // Read the rest of hte buffer
				data = buffer.slice().asReadOnlyBuffer())

		try {
			graphic.validate()
		} catch (e: IllegalStateException) {
			logger.error(e, { "The read Graphic is probably corrupted" })
		}

		return graphic
	}

	override fun close() {
		fileChannel.close()
	}
}

/**
 * Validates that all values in this Graphic are valid.
 */
fun Graphic.validate(): Graphic {
	check(magic == GRAPHIC_ENTRY_MAGIC, {
		"Expect magic to be ${GRAPHIC_ENTRY_MAGIC} (\"RD\"), but got ${magic}"
	})
	check(width > 0, { "Expect width to be positive, but got ${width}" });
	check(height > 0, { "Expect height to be positive, but got ${height}" });
	check(dataLength == GRAPHIC_ENTRY_HEADER_SIZE + data.capacity(),
			{ "Expect data length to be ${GRAPHIC_ENTRY_HEADER_SIZE}(header) + ${data.capacity()}(data) = ${GRAPHIC_ENTRY_HEADER_SIZE + data.capacity()}, but got ${dataLength}." })

	//TODO 7476, (?)

	return this
}





