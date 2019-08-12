package com.jhuangyc.cgmap.entity;

data class Map(
		/**
		 * Magic number that marks the beginning of a map file
		 */
		val magic: Int,

		/**
		 *  Dimension of the map.
		 *  NOTE: this point is not on the map.
		 */
		val dimension: Point,

		/**
		 * The graphic numbers of tiles on this map
		 */
		val floors: List<Int>,

		/**
		 * The graphic numbers of entities on this map
		 */
		val entities: List<Int>,

		/**
		 * The tile attributes on this map
		 */
		val attributes: List<Attribute>
) {
	enum class Attribute {
		UNKNOWN, VOID, FLOOR, WRAP, SOLID,
	}

	/** Represents a location on a [Map]. */
	data class Point(
			/** The nth tile in the lower left direction. */
			val east: Int,
			/** The nth tile in the lower right direction. */
			val south: Int
	) {
		operator fun plus(delta: Point) = Point(east + delta.east,
				south + delta.south)

		operator fun minus(delta: Point) = Point(east - delta.east,
				south - delta.south)

		operator fun times(scale: Int) = Point(east * scale, south * scale)
		operator fun div(scale: Int) = Point(east / scale, south / scale)
	}

}
