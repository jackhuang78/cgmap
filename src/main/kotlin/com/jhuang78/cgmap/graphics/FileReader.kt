package com.jhuang78.cgmap.graphics

import com.google.common.base.Preconditions.checkElementIndex
import com.google.common.base.Preconditions.checkArgument
import com.google.common.primitives.Longs
import java.awt.Color
import java.nio.ByteOrder
import java.nio.channels.FileChannel
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import kotlin.streams.toList
import java.nio.channels.FileChannel.MapMode



fun Short.toUint() = (this.toInt() and 0x0000FFFF)
fun Byte.toUint()  = (this.toInt() and 0x000000FF)

val INFO_ENTRY_SIZE = 40L
class InfoFileReader(val path: Path): AutoCloseable {
    val fc = FileChannel.open(path, StandardOpenOption.READ);
    val size = (fc.size() / INFO_ENTRY_SIZE).toInt()

    fun read(entryNo: Int): GraphicInfo {
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

    override fun close() {
        fc.close()
    }
}

class DataFileReader(val path: Path) : AutoCloseable {
    val fc = FileChannel.open(path, StandardOpenOption.READ)

    fun read(position: Int, size: Int): GraphicData {
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

    override fun close() {
        fc.close()
    }
}

val PALET_FILE_SIZE = 708L
val PALET_NUM_COLORS = 256
val COLORS_BASE = Array(PALET_NUM_COLORS) {
    when(it) {
        0    -> Color(0x00, 0x00, 0x00)
        1    -> Color(0x00, 0x00, 0x80)
        2    -> Color(0x00, 0x80, 0x00)
        3    -> Color(0x00, 0x80, 0x80)
        4    -> Color(0x80, 0x00, 0x00)
        5    -> Color(0x80, 0x00, 0x80)
        6    -> Color(0x80, 0x80, 0x00)
        7    -> Color(0xC0, 0xC0, 0xC0)
        8    -> Color(0xC0, 0xDC, 0xC0)
        9    -> Color(0xF0, 0xCA, 0xA6)
        10   -> Color(0x00, 0x00, 0xDF)
        11   -> Color(0x00, 0x5F, 0xFF)
        12   -> Color(0xA0, 0xFF, 0xFF)
        13   -> Color(0xD2, 0x5F, 0x00)
        14   -> Color(0xFF, 0xD2, 0x50)
        15   -> Color(0x28, 0xE1, 0x28)
        240  -> Color(0x96, 0xC3, 0xF5)
        241  -> Color(0x5F, 0xA0, 0x1E)
        242  -> Color(0x46, 0x7D, 0xC3)
        243  -> Color(0x1E, 0x55, 0x9B)
        244  -> Color(0x37, 0x41, 0x46)
        245  -> Color(0x1E, 0x23, 0x28)
        246  -> Color(0xF0, 0xFB, 0xFF)
        247  -> Color(0xA5, 0x6E, 0x3A)
        248  -> Color(0x80, 0x80, 0x80)
        249  -> Color(0x00, 0x00, 0xFF)
        250  -> Color(0x00, 0xFF, 0x00)
        251  -> Color(0x00, 0xFF, 0xFF)
        252  -> Color(0xFF, 0x00, 0x00)
        253  -> Color(0xFF, 0x80, 0xFF)
        254  -> Color(0xFF, 0xFF, 0x00)
        255  -> Color(0xFF, 0xFF, 0xFF)
        else -> Color(0xEE, 0xEE, 0xEE)
    }
}
class PaletFileReader(val dir: Path) {
    fun read(file: Path): GraphicPalet {
        FileChannel.open(dir.resolve(file), StandardOpenOption.READ).use {
            checkArgument(it.size() == PALET_FILE_SIZE,
                    "Expect palet file to have size ${PALET_FILE_SIZE}, but got ${it.size()}")

            val buffer = it.map(MapMode.READ_ONLY, 0, PALET_FILE_SIZE)
            return GraphicPalet(colors = Array(PALET_NUM_COLORS) {
                when(it) {
                    in 0..15 -> COLORS_BASE[it]
                    in 16..239 -> {
                        val b = buffer.get().toUint()
                        val g = buffer.get().toUint()
                        val r = buffer.get().toUint()
                        Color(r, g, b)
                    }
                    in 240..255 -> COLORS_BASE[it]
                    else -> throw IllegalStateException("Unexpected color index ${it}.")
                }
            })
        }
    }

}




