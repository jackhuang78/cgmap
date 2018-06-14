package com.jhuang78.cgmap.io

import com.google.common.base.Preconditions.checkElementIndex
import com.google.common.primitives.Longs
import com.jhuang78.cgmap.io.GraphicInfo.MapMarker.FLOOR
import com.jhuang78.cgmap.io.GraphicInfo.MapMarker.OBSTACLE
import java.nio.ByteOrder.LITTLE_ENDIAN
import java.nio.channels.FileChannel
import java.nio.channels.FileChannel.MapMode.READ_ONLY
import java.nio.file.Path
import java.nio.file.StandardOpenOption

/**
 * Size of a GraphicInfo entry in GraphicInfo.bin file.
 */
const val GRAPHIC_INFO_ENTRY_SIZE = 40L

/**
 * A class to read GraphicInfo from a GraphicInfo.bin file.
 */
class GraphicInfoFileReader(path: Path) : AutoCloseable {

	/**
	 * An open channel to the GraphicInfo.bin file. This is keep open until
	 * {@link #close} is invoked.
	 */
	private val fileChannel = FileChannel.open(path, StandardOpenOption.READ)


	/**
	 * The number of GraphicInfo in this file.
	 */
	val numberOfEntries = (fileChannel.size() / GRAPHIC_INFO_ENTRY_SIZE).toInt()

	/**
	 * Reads a specific GraphicInfo from the file.
	 */
	fun read(entryNo: Int): GraphicInfo {
		checkElementIndex(entryNo, numberOfEntries)

		val buffer = fileChannel.map(READ_ONLY, entryNo * GRAPHIC_INFO_ENTRY_SIZE,
				GRAPHIC_INFO_ENTRY_SIZE).order(LITTLE_ENDIAN)

		return GraphicInfo(graphicNo = buffer.getInt(), address = buffer.getInt(),
				dataLength = buffer.getInt(), offsetX = buffer.getInt(),
				offsetY = buffer.getInt(), imageWidth = buffer.getInt(),
				imageHeight = buffer.getInt(), occupyEast = buffer.get().toInt(),
				occupySouth = buffer.get().toInt(), // TODO: for 7480 to 7498, mapMarker is 45 for unknown reason
				mapMarker = if (buffer.get().toInt() == 0) OBSTACLE else FLOOR,
				unknown = {
					val b1 = buffer.get()
					val b2 = buffer.get()
					val b3 = buffer.get()
					val b4 = buffer.get()
					val b5 = buffer.get()
					Longs.fromBytes(0, 0, 0, b5, b4, b3, b2, b1)
				}(), mapNo = buffer.getInt())
	}

	override fun close() {
		fileChannel.close()
	}
}

/**
 * Validates a GraphicInfo.
 */
fun GraphicInfo.validate(): GraphicInfo {
	listOf(Pair(graphicNo, "GraphicNo"), Pair(address, "Address"),
			Pair(dataLength, "DataLength"), Pair(imageWidth, "ImageWidth"),
			Pair(imageHeight, "ImageHeight"), Pair(occupyEast, "OccupyEast"),
			Pair(occupySouth, "OccupySouth"), Pair(mapNo, "MapNo")).forEach {
		check(it.first >= 0, {
			"Expect non-negative value for ${it.second}, but got ${it.first}"
		})
	}

	return this
}

