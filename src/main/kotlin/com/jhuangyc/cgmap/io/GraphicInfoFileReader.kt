package com.jhuangyc.cgmap.io

import com.google.common.primitives.Longs
import com.jhuangyc.cgmap.entity.GraphicInfo
import com.jhuangyc.cgmap.entity.GraphicInfo.MapMarker.FLOOR
import com.jhuangyc.cgmap.entity.GraphicInfo.MapMarker.OBSTACLE
import mu.KotlinLogging
import java.nio.ByteOrder.LITTLE_ENDIAN
import java.nio.channels.FileChannel
import java.nio.channels.FileChannel.MapMode.READ_ONLY
import java.nio.file.Path
import java.nio.file.StandardOpenOption


/**
 * A class to read GraphicInfo from a GraphicInfo.bin file.
 */
class GraphicInfoFileReader(private val path: Path) : AutoCloseable {
	companion object {
		private val ENTRY_SIZE_BYTE = 40
	}

	private val logger = KotlinLogging.logger {}

	private val fileChannel by lazy {
		FileChannel.open(path, StandardOpenOption.READ)
	}

	val numEntries by lazy {
		(fileChannel.size() / ENTRY_SIZE_BYTE).toInt()
	}

	private val graphicIdToFileIdxMap by lazy {
		logger.debug("Building GraphicId to FileIdx mapping")
		(0 until numEntries)
				.map { read(it).graphicId to it }
				.toMap()
	}

	fun readByGraphicId(graphicId: Int): GraphicInfo {
		logger.debug("Read graphicInfo by GraphicId ${graphicId}")

		val fileIdx = graphicIdToFileIdxMap[graphicId]
				?: throw IllegalArgumentException("Unknown graphic ID: ${graphicId}")

		return read(fileIdx)
	}

	fun read(fileIdx: Int): GraphicInfo {
		logger.debug("Read GraphicInfo from ${path} at index ${fileIdx}")

		val buffer = fileChannel
				.map(READ_ONLY,
						(fileIdx * ENTRY_SIZE_BYTE).toLong(),
						ENTRY_SIZE_BYTE.toLong())
				.order(LITTLE_ENDIAN)

		val graphicInfo = GraphicInfo(
				graphicNo = buffer.getInt(),
				address = buffer.getInt(),
				dataLength = buffer.getInt(),
				offsetX = buffer.getInt(),
				offsetY = buffer.getInt(),
				imageWidth = buffer.getInt(),
				imageHeight = buffer.getInt(),
				occupyEast = buffer.get().toInt(),
				// TODO: for 7480 to 7498, mapMarker is 45 for unknown reason
				occupySouth = buffer.get().toInt(),
				mapMarker = if (buffer.get().toInt() == 0) OBSTACLE else FLOOR,
				unknown = {
					val b1 = buffer.get()
					val b2 = buffer.get()
					val b3 = buffer.get()
					val b4 = buffer.get()
					val b5 = buffer.get()
					Longs.fromBytes(0, 0, 0, b5, b4, b3, b2, b1)
				}(),
				graphicId = buffer.getInt())

		listOf(
				graphicInfo.graphicNo to "GraphicNo",
				graphicInfo.address to "Address",
				graphicInfo.dataLength to "DataLength",
				graphicInfo.imageWidth to "ImageWidth",
				graphicInfo.imageHeight to "ImageHeight",
				graphicInfo.occupyEast to "OccupyEast",
				graphicInfo.occupySouth to "OccupySouth",
				graphicInfo.graphicId to "MapNo"
		).forEach {
			check(it.first >= 0) {
				"Expect non-negative value for ${it.second}, but got ${it.first}"
			}
		}

		return graphicInfo
	}

	override fun close() {
		fileChannel.close()
	}
}

