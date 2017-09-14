package com.jhuang78.cgmap.graphics

import com.google.common.base.Preconditions.checkState
import com.google.common.primitives.Ints
import java.awt.Color
import java.nio.ByteBuffer

val INFO_VALID_MARK = setOf(0, 1)
data class GraphicInfo(
        val graphicNo: Int = 0,
        val address: Int = 0,
        val dataLength: Int = 0,
        val offsetX: Int = 0,
        val offsetY: Int = 0,
        val imageWidth: Int = 0,
        val imageHeight: Int = 0,
        val occupyEast: Int = 0,
        val occupySouth: Int = 0,
        val mark: Int = 0,
        val unknown: Long = 0L,
        val mapNo: Int = 0
) {
    fun validate(): GraphicInfo {
        checkState(graphicNo >= 0, "Expect non-negative graphicNo, but got ${graphicNo}.")
        checkState(address >= 0, "Expect non-negative address, but got ${address}.")
        checkState(dataLength >= 0, "Expect non-negative dataLength, but got ${dataLength}.")
        checkState(imageWidth >= 0, "Expect non-negative imageWidth, but got ${imageWidth}.")
        checkState(imageHeight >= 0, "Expect non-negative imageHeight, but got ${imageHeight}.")
        checkState(occupyEast >= 0, "Expect non-negative occupyEast, but got ${occupyEast}.")
        checkState(occupySouth >= 0, "Expect non-negative occupySouth, but got ${occupySouth}.")
        checkState(mark in INFO_VALID_MARK, "Expect mark to be one of ${INFO_VALID_MARK}, but got ${mark}.")
        checkState(mapNo >= 0, "Expect non-negative mapNo, but got ${mapNo}.")
        return this
    }
}

val DATA_VALID_MAGIC = Ints.fromBytes(0, 0, 'D'.toByte(), 'R'.toByte())
val DATA_VALID_VERSIONS = setOf(0, 1)
val DATA_HEADER_SIZE = 16
data class GraphicData(
        val magic: Int = 0,
        val version: Int = 0,
        val unknown: Int = 0,
        val width: Int = 0,
        val height: Int = 0,
        val dataLength: Int = 0,
        val data: ByteBuffer = ByteBuffer.allocate(0)
) {
    fun validate(): GraphicData {
        checkState(magic == DATA_VALID_MAGIC, "Expect magic to be ${DATA_VALID_MAGIC}, but got ${magic}.")
        checkState(version in DATA_VALID_VERSIONS, "Expect version to be one of ${DATA_VALID_VERSIONS}, but got ${version}.");
        checkState(width > 0, "Expect width to be positive, but got ${width}.");
        checkState(height > 0, "Expect height to be positive, but got ${height}.");
        checkState(dataLength == DATA_HEADER_SIZE + data.capacity(),
                "Expect data length to be ${DATA_HEADER_SIZE}(header) + ${data.capacity()}(data) = ${DATA_HEADER_SIZE + data.capacity()}, but got ${dataLength}.")
        return this
    }

    fun isCompressed(): Boolean {
        return (version % 2 == 1)
    }
}

val GRAPHIC_PALET_TRANSPARENT = Color(0,0,0,0)
data class GraphicPalet(
        val colors: Array<Color> = arrayOf()
) {
    fun get(colorPoint: Int): Color {
        return if(colorPoint >= 0)
            colors[colorPoint]
        else
            GRAPHIC_PALET_TRANSPARENT
    }
}
