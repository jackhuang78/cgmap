package com.jhuangyc.cgmap.io

import com.jhuangyc.cgmap.entity.Graphic
import com.jhuangyc.cgmap.entity.Graphic.Version.ENCODED
import com.jhuangyc.cgmap.entity.Graphic.Version.RAW
import com.jhuangyc.cgmap.util.toUint
import mu.KotlinLogging
import java.nio.ByteOrder.LITTLE_ENDIAN
import java.nio.channels.FileChannel
import java.nio.channels.FileChannel.MapMode.READ_ONLY
import java.nio.file.Path
import java.nio.file.StandardOpenOption


/**
 * A class to read Graphic from Graphic.bin file.
 */
class GraphicFileReader(private val path: Path) : AutoCloseable {
	companion object {
		private val HEADER_SIZE = 16

		// Each entry begins with this two-byte magic value: 'RD'
		private val MAGIC = 0x4452

		//TODO 7476, (?)
	}

	private val logger = KotlinLogging.logger {}

	private val fileChannel by lazy {
		FileChannel.open(path, StandardOpenOption.READ)
	}

	fun read(position: Int, size: Int): Graphic {
		logger.debug("Reading ${size} bytes from ${path} at position ${position}")

		val buffer = fileChannel
				.map(READ_ONLY, position.toLong(), size.toLong())
				.order(LITTLE_ENDIAN)

		val graphic = Graphic(
				magic = buffer.getShort().toUint(),
				version = if (buffer.get().toUint() == 0) RAW else ENCODED,
				unknown = buffer.get().toUint(),
				width = buffer.getInt(),
				height = buffer.getInt(),
				dataLength = buffer.getInt(), // Read the rest of the buffer
				data = buffer.slice().asReadOnlyBuffer())

		check(graphic.magic == MAGIC) {
			"Expect magic to be ${MAGIC} (\"RD\"), but got ${graphic.magic}"
		}
		check(graphic.width > 0) {
			"Expect width to be positive, but got ${graphic.width}"
		}
		check(graphic.height > 0) {
			"Expect height to be positive, but got ${graphic.height}"
		}
		check(graphic.dataLength == (HEADER_SIZE + graphic.data.capacity())) {
			"""
				Expect data length to be
				${HEADER_SIZE}(header)
				+ ${graphic.data.capacity()}(data)
				= ${HEADER_SIZE + graphic.data.capacity()},
				but got $graphic.{dataLength}.
			""".trimIndent()
		}

		return graphic
	}

	override fun close() {
		fileChannel.close()
	}
}





