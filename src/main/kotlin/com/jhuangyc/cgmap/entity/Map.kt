package com.jhuangyc.cgmap.entity;

data class Map(
		/**
		 * Magic number that marks the beginning of a map file
		 */
		val magic: Int,

		/** Dimension of the map. */
		val dimension: Size,

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

	/** Represents the size of a rectangular region on a [Map]. */
	data class Size(
			/** Length along lower left */
			val east: Int,
			/** Length along lower right */
			val south: Int
	)
}
