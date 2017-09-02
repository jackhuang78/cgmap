package com.jhuang78.cgmap.graphics

import com.google.common.base.Preconditions.checkElementIndex
import com.google.common.base.Preconditions.checkState
import com.google.common.base.Preconditions.checkArgument
import com.google.common.primitives.Longs
import java.nio.ByteOrder
import java.nio.channels.FileChannel
import java.nio.file.Path
import java.nio.file.StandardOpenOption

data class Info(
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
    public fun validate(): Info {
        checkState(graphicNo >= 0, "Expect non-negative graphicNo, but got ${graphicNo}.")
        checkState(address >= 0, "Expect non-negative address, but got ${address}.")
        checkState(dataLength >= 0, "Expect non-negative dataLength, but got ${dataLength}.")
        checkState(imageWidth >= 0, "Expect non-negative imageWidth, but got ${imageWidth}.")
        checkState(imageHeight >= 0, "Expect non-negative imageHeight, but got ${imageHeight}.")
        checkState(occupyEast >= 0, "Expect non-negative occupyEast, but got ${occupyEast}.")
        checkState(occupySouth >= 0, "Expect non-negative occupySouth, but got ${occupySouth}.")
        checkState(mark == 0 || mark == 1, "Expect mark to be either 0 or 1, but got ${mark}.")
        checkState(mapNo >= 0, "Expect non-negative mapNo, but got ${mapNo}.")
        return this
    }
}

const val INFO_ENTRY_SIZE = 40L

class InfoFileReader(val path: Path) {
    val fc: FileChannel
    val size: Int

    init {
        checkArgument(path.toFile().isFile(), "Path ${path} does not point to a file.")
        fc = FileChannel.open(path, StandardOpenOption.READ);
        size = (fc.size() / INFO_ENTRY_SIZE).toInt()
    }

    public fun read(entryNo: Int): Info {
        checkElementIndex(entryNo, size)

        val buffer = fc
                .map(FileChannel.MapMode.READ_ONLY, entryNo * INFO_ENTRY_SIZE, INFO_ENTRY_SIZE)
                .order(ByteOrder.LITTLE_ENDIAN)

        return Info(
                graphicNo = buffer.getInt(),
                address = buffer.getInt(),
                dataLength = buffer.getInt(),
                offsetX = buffer.getInt(),
                offsetY = buffer.getInt(),
                imageWidth = buffer.getInt(),
                imageHeight = buffer.getInt(),
                occupyEast = buffer.get().toInt(),
                occupySouth = buffer.get().toInt(),
                mark = buffer.get().toInt(),
                unknown = Longs.fromBytes(
                        buffer.get(),
                        buffer.get(),
                        buffer.get(),
                        buffer.get(),
                        buffer.get(),
                        0.toByte(),
                        0.toByte(),
                        0.toByte()
                ),
                mapNo = buffer.getInt()
        ).validate()
    }

}