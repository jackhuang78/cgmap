package com.jhuang78.cgmap.entity;

data class Map(
		/**
		 * Magic number that marks the beginning of a map file
		 */
		val magic: Int,

		/**
		 * Number of tiles in the east direction (lower left)
		 */
		val eastLength: Int,

		/**
		 * Number of tiles in the south direction (lower right)
		 */
		val southLength: Int,

		/**
		 * The number of tiles in this map.
		 */
		val size: Int,

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
}
