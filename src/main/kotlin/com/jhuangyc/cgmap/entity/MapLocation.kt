package com.jhuangyc.cgmap.entity

@Deprecated("Use Map.Point instead")
data class MapLocation(
		var east: Int = 0,
		var south: Int = 0) {

	fun moveEast(delta: Int): MapLocation {
		east += delta
		return this
	}

	fun moveSouth(delta: Int): MapLocation {
		south += delta
		return this
	}

	fun moveSouthEast(delta: Int): MapLocation {
		east += delta
		south += delta
		return this
	}

	fun moveSouthWest(delta: Int): MapLocation {
		east -= delta
		south += delta
		return this
	}

	fun getTileNumber(mapSize: MapSize): Int? {
		return when {
			east !in (0 until mapSize.eastLength) -> null
			south !in (0 until mapSize.southLength) -> null
			else -> south * mapSize.eastLength + east + 1
		}
	}

	fun getTileNumber(mapEastLength: Int, mapSouthLength: Int) = getTileNumber(
			MapSize(mapEastLength, mapSouthLength))

	fun minus(other: MapLocation): MapLocation {
		return MapLocation(east - other.east, south - other.south)
	}

	override fun toString(): String {
		return "MapLocation(${east},${south})"
	}
}