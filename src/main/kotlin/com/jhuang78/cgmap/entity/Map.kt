package com.jhuang78.cgmap.entity;

data class Map(
		val magic: Int,
		val eastLength: Int,
		val southLength: Int,
		val size: Int,
		val floors: List<Int>,
		val entities: List<Int>,
		val attributes: List<Attribute>
) {
	enum class Attribute {
		UNKNOWN, VOID, FLOOR, WRAP, SOLID,
	}
}
