package com.jhuang78.cgmap.graphics

import com.google.common.base.Preconditions.checkElementIndex
import com.google.common.base.Preconditions.checkArgument
import com.google.common.primitives.Longs
import java.nio.ByteOrder
import java.nio.channels.FileChannel
import java.nio.file.Path
import java.nio.file.StandardOpenOption

fun Short.toUint() = (this.toInt() and 0x0000FFFF)
fun Byte.toUint()  = (this.toInt() and 0x000000FF)
val INFO_ENTRY_SIZE = 40L

class InfoFileReader(val path: Path) {
    val fc: FileChannel
    val size: Int

    init {
        checkArgument(path.toFile().isFile(), "Path ${path} does not point to a file.")
        fc = FileChannel.open(path, StandardOpenOption.READ);
        size = (fc.size() / INFO_ENTRY_SIZE).toInt()
    }

    public fun read(entryNo: Int): GraphicInfo {
        checkElementIndex(entryNo, size)
        val buffer = fc
                .map(FileChannel.MapMode.READ_ONLY, entryNo * INFO_ENTRY_SIZE, INFO_ENTRY_SIZE)
                .order(ByteOrder.LITTLE_ENDIAN)

        return GraphicInfo(
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
                //FIXME: order of bytes is wrong
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

class DataFileReader(val path: Path) {
    val fc: FileChannel

    init {
        checkArgument(path.toFile().isFile(),
                "Path ${path} does not point to a file.")
        fc = FileChannel.open(path, StandardOpenOption.READ)
    }

    public fun read(position: Int, size: Int): GraphicData {
        checkElementIndex(position, fc.size().toInt())

        val buffer = fc
                .map(FileChannel.MapMode.READ_ONLY, position.toLong(), size.toLong())
                .order(ByteOrder.LITTLE_ENDIAN);
        return GraphicData(
                magic = buffer.getShort().toUint(),
                version = buffer.get().toUint(),
                unknown = buffer.get().toUint(),
                width = buffer.getInt(),
                height = buffer.getInt(),
                dataLength = buffer.getInt(),
                data = buffer.slice().asReadOnlyBuffer()
        ).validate()
    }
}


