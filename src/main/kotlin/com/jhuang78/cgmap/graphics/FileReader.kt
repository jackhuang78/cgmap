//package com.jhuang78.cgmap.graphics
//
//import com.google.common.base.Preconditions.checkElementIndex
//import com.google.common.base.Preconditions.checkArgument
//import com.google.common.primitives.Ints
//import com.google.common.primitives.Longs
//import java.awt.Color
//import java.awt.Point
//import java.nio.ByteOrder
//import java.nio.channels.FileChannel
//import java.nio.file.Files
//import java.nio.file.Path
//import java.nio.file.StandardOpenOption
//import kotlin.stream
//import java.nio.channels.FileChannel.MapMode
//
//
//
//fun Short.toUint() = (this.toInt() and 0x0000FFFF)
//fun Byte.toUint()  = (this.toInt() and 0x000000FF)
//
//val INFO_ENTRY_SIZE = 40L
//class InfoFileReader(val path: Path): AutoCloseable {
//    private val fc = FileChannel.open(path, StandardOpenOption.READ);
//    val size = (fc.size() / INFO_ENTRY_SIZE).toInt()
//    private val mapNoToEntryNo = {
//        val map = mutableMapOf<Int, Int>()
//        for(i in 0..size-1) {
//            val info = read(i)
//            // TODO: there shouldn't be any duplicated
//            if(info.mapNo !in map) {
//                map.put(info.mapNo, i)
//            }
//        }
//        map.toMap()
//    }()
//
//    fun readMapEntry(mapNo: Int): GraphicInfo {
//        val entryNo = mapNoToEntryNo[mapNo] ?: 0
//        return read(checkNotNull(entryNo))
//    }
//
//    fun read(entryNo: Int): GraphicInfo {
//        checkElementIndex(entryNo, size)
//        val buffer = fc
//                .map(FileChannel.MapMode.READ_ONLY, entryNo * INFO_ENTRY_SIZE, INFO_ENTRY_SIZE)
//                .order(ByteOrder.LITTLE_ENDIAN)
//
//        return GraphicInfo(
//                graphicNo = buffer.getInt(),
//                address = buffer.getInt(),
//                dataLength = buffer.getInt(),
//                offsetX = buffer.getInt(),
//                offsetY = buffer.getInt(),
//                imageWidth = buffer.getInt(),
//                imageHeight = buffer.getInt(),
//                occupyEast = buffer.get().toInt(),
//                occupySouth = buffer.get().toInt(),
//                mark = buffer.get().toInt(),
//                unknown =  {
//                    val b1 = buffer.get()
//                    val b2 = buffer.get()
//                    val b3 = buffer.get()
//                    val b4 = buffer.get()
//                    val b5 = buffer.get()
//                    Longs.fromBytes(0, 0, 0, b5, b4, b3, b2, b1)
//                }(),
//                mapNo = buffer.getInt()
//            )//.validate()
//
//    }
//
//    override fun close() {
//        fc.close()
//    }
//}
//
//class DataFileReader(val path: Path) : AutoCloseable {
//    private val fc = FileChannel.open(path, StandardOpenOption.READ)
//    private var prevPos = -1
//    private var prevData = GraphicData()
//
//    fun read(position: Int, size: Int): GraphicData {
//        checkElementIndex(position, fc.size().toInt())
//
//        if(position == prevPos) {
//            return prevData
//        }
//
//        val buffer = fc
//                .map(FileChannel.MapMode.READ_ONLY, position.toLong(), size.toLong())
//                .order(ByteOrder.LITTLE_ENDIAN);
//        val data = GraphicData(
//                magic = buffer.getShort().toUint(),
//                version = buffer.get().toUint(),
//                unknown = buffer.get().toUint(),
//                width = buffer.getInt(),
//                height = buffer.getInt(),
//                dataLength = buffer.getInt(),
//                data = buffer.slice().asReadOnlyBuffer()
//        ).validate()
//
//        prevData = data
//        prevPos = position
//
//        return data
//    }
//
//    override fun close() {
//        fc.close()
//    }
//}
//
//val PALET_FILE_SIZE = 708L
//val PALET_NUM_COLORS = 256
//val COLORS_BASE = Array(PALET_NUM_COLORS) {
//    when(it) {
//        0    -> Color(0x00, 0x00, 0x00)
//        1    -> Color(0x00, 0x00, 0x80)
//        2    -> Color(0x00, 0x80, 0x00)
//        3    -> Color(0x00, 0x80, 0x80)
//        4    -> Color(0x80, 0x00, 0x00)
//        5    -> Color(0x80, 0x00, 0x80)
//        6    -> Color(0x80, 0x80, 0x00)
//        7    -> Color(0xC0, 0xC0, 0xC0)
//        8    -> Color(0xC0, 0xDC, 0xC0)
//        9    -> Color(0xF0, 0xCA, 0xA6)
//        10   -> Color(0x00, 0x00, 0xDF)
//        11   -> Color(0x00, 0x5F, 0xFF)
//        12   -> Color(0xA0, 0xFF, 0xFF)
//        13   -> Color(0xD2, 0x5F, 0x00)
//        14   -> Color(0xFF, 0xD2, 0x50)
//        15   -> Color(0x28, 0xE1, 0x28)
//        240  -> Color(0x96, 0xC3, 0xF5)
//        241  -> Color(0x5F, 0xA0, 0x1E)
//        242  -> Color(0x46, 0x7D, 0xC3)
//        243  -> Color(0x1E, 0x55, 0x9B)
//        244  -> Color(0x37, 0x41, 0x46)
//        245  -> Color(0x1E, 0x23, 0x28)
//        246  -> Color(0xF0, 0xFB, 0xFF)
//        247  -> Color(0xA5, 0x6E, 0x3A)
//        248  -> Color(0x80, 0x80, 0x80)
//        249  -> Color(0x00, 0x00, 0xFF)
//        250  -> Color(0x00, 0xFF, 0x00)
//        251  -> Color(0x00, 0xFF, 0xFF)
//        252  -> Color(0xFF, 0x00, 0x00)
//        253  -> Color(0xFF, 0x80, 0xFF)
//        254  -> Color(0xFF, 0xFF, 0x00)
//        255  -> Color(0xFF, 0xFF, 0xFF)
//        else -> Color(0xEE, 0xEE, 0xEE)
//    }
//}
//class PaletFileReader(val dir: Path) {
//    fun read(file: Path): GraphicPalet {
//        FileChannel.open(dir.resolve(file), StandardOpenOption.READ).use {
//            checkArgument(it.size() == PALET_FILE_SIZE,
//                    "Expect palet file to have numberOfEntries ${PALET_FILE_SIZE}, but got ${it.size()}")
//
//            val buffer = it.map(MapMode.READ_ONLY, 0, PALET_FILE_SIZE)
//            return GraphicPalet(colors = Array(PALET_NUM_COLORS) {
//                when(it) {
//                    in 0..15 -> COLORS_BASE[it]
//                    in 16..239 -> {
//                        val b = buffer.get().toUint()
//                        val g = buffer.get().toUint()
//                        val r = buffer.get().toUint()
//                        Color(r, g, b)
//                    }
//                    in 240..255 -> COLORS_BASE[it]
//                    else -> throw IllegalStateException("Unexpected color index ${it}.")
//                }
//            })
//        }
//    }
//
//    fun palets(): List<Path> {
//        return Files.list(dir).map {
//            dir.relativize(it)
//        }.toList()
//    }
//}
//
//val MAP_FILE_HEADER = 20
//val MAP_VALID_MAGIC = Ints.fromBytes(0, 'P'.toByte(), 'A'.toByte(), 'M'.toByte())
//class MapFileReader(val path: Path): AutoCloseable {
//    private val fc = FileChannel.open(path, StandardOpenOption.READ)
//    private val buf = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size())
//            .order(ByteOrder.LITTLE_ENDIAN);
//    val magic = {
//        val magic = Ints.fromBytes(0, buf.get(2), buf.get(1), buf.get(0))
//        checkArgument(magic == MAP_VALID_MAGIC, "Expect magic to be ${MAP_VALID_MAGIC}, but got ${magic}")
//        magic
//    }
//    val width = buf.getInt(12)
//    val height = buf.getInt(16)
//    val size = width * height
//
//    fun read(east: Int, south: Int): MapTile {
//        val groundIdx = MAP_FILE_HEADER + (south * width + east) * 2
//        val itemIdx = groundIdx + size*2
//        val markIdx = itemIdx + size*2
//        return MapTile(
//                ground = buf.getShort(groundIdx).toUint(),
//                item = buf.getShort(itemIdx).toUint(),
//                mark = buf.getShort(markIdx).toUint()
//        )
//    }
//
//    fun readView(east: Int, south: Int, width: Int, height: Int, infoFileReader: InfoFileReader): MapView {
//
//        val tiles = mutableListOf<MapTileInfo>()
//        for(j in 0..height-1) {
//            for(i in 0..width-1) {
//                val tile = read(east + i, south + j)
//                tiles.add(MapTileInfo(
//                        ground = infoFileReader.readMapEntry(tile.ground),
//                        item = infoFileReader.readMapEntry(tile.item),
//                        mark = tile.mark,
//                        border = (south + j == 0 || south +j == this.height - 1 || east + i == 0 || east + i == this.width - 1),
//                        loc = Point(east+i, south+j))
//                )
//            }
//        }
//
//        return MapView(
//                width = width,
//                height = height,
//                tiles = tiles.toList()
//        )
//
//    }
//
//    override fun close() {
//        fc.close()
//    }
//}
//
//
//
//
