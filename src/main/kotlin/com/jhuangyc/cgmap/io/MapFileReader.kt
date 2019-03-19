package com.jhuangyc.cgmap.io

import com.google.common.primitives.Ints
import com.jhuangyc.cgmap.entity.Map
import com.jhuangyc.cgmap.util.toHex
import com.jhuangyc.cgmap.util.toUint
import mu.KotlinLogging
import java.nio.ByteOrder.LITTLE_ENDIAN
import java.nio.channels.FileChannel
import java.nio.channels.FileChannel.MapMode.READ_ONLY
import java.nio.file.Path
import java.nio.file.StandardOpenOption.READ

class MapFileReader(private val path: Path) {
	companion object {
		// 'M', 'A', 'P'
		private val MAGIC = 0x004D4150
	}

	private val logger = KotlinLogging.logger {}

	fun read(): Map {
		FileChannel.open(path, READ).use { file ->
			val buffer = file
					.map(READ_ONLY, 0, file.size())
					.order(LITTLE_ENDIAN)

			val magic = Ints.fromBytes(0, buffer.get(), buffer.get(), buffer.get())
			if (magic != MAGIC) {
				logger.warn { "Got unexpected magic ${magic}. Data is likely corrupted." }
			}

			// Skip the next 9 bytes
			repeat(9) { buffer.get() }

			val eastLength = buffer.getInt()
			val southLength = buffer.getInt()
			val size = eastLength * southLength
			val floors = List(size) { buffer.getShort().toUint() }
			val entities = List(size) { buffer.getShort().toUint() }
			val attributes = List(size) {
				when (buffer.getShort().toUint()) {
					0x0000 -> Map.Attribute.VOID
					0xC000 -> Map.Attribute.FLOOR
					0xC00A -> Map.Attribute.WRAP
					0xC100 -> Map.Attribute.SOLID
					0xC003, 0x4000 -> Map.Attribute.UNKNOWN
					else -> {
						logger.warn { "Unexpected attribute ${it.toHex()}" }
						Map.Attribute.UNKNOWN
					}
				}
			}

			val map = Map(
					magic = magic,
					eastLength = eastLength,
					southLength = southLength,
					floors = floors,
					entities = entities,
					attributes = attributes)

			check(map.magic == MAGIC)
			check(map.eastLength > 0)
			check(map.southLength > 0)
			check(map.size > 0)

			return map
		}
	}
}


const val VALID_MAP_MAGIC = 0x004D4150  // 'M', 'A', 'P'

private val logger = KotlinLogging.logger {}

@Deprecated("Use MapFileReader().read() instead")
fun readMapFile(path: Path): Map {
	FileChannel.open(path, READ).use {
		val buffer = it.map(READ_ONLY, 0, it.size()).order(LITTLE_ENDIAN)

		val magic = Ints.fromBytes(0, buffer.get(), buffer.get(), buffer.get())
		if (magic != VALID_MAP_MAGIC) {
			logger.warn { "Got unexpected magic ${magic}. Data is likely corrupted." }
		}

		repeat(9) { buffer.get() }
		val eastLength = buffer.getInt()
		val southLength = buffer.getInt()
		val size = eastLength * southLength
		val floors = (0 until size).map { buffer.getShort().toUint() }
		val entities = (0 until size).map { buffer.getShort().toUint() }
		val attributes = (0 until size)
				.map { buffer.getShort() }
				.map {
					when (it.toUint()) {
						0x0000 -> Map.Attribute.VOID
						0xC000 -> Map.Attribute.FLOOR
						0xC00A -> Map.Attribute.WRAP
						0xC100 -> Map.Attribute.SOLID
						0xC003, 0x4000 -> Map.Attribute.UNKNOWN
						else -> {
							logger.warn { "Unexpected attribute ${it.toHex()}" }
							Map.Attribute.UNKNOWN
						}
					}
				}

		return Map(
				magic = magic,
				eastLength = eastLength,
				southLength = southLength,
				floors = floors,
				entities = entities,
				attributes = attributes)
	}
}


fun Map.validate(): Map {
	check(magic == VALID_MAP_MAGIC)
	check(eastLength > 0)
	check(southLength > 0)
	check(size > 0)
	return this
}