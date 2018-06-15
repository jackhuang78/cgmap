package com.jhuang78.cgmap.entity

import java.nio.ByteBuffer

/**
 * A Graphic contains the data (raw or encoded) of a piece of graphic on the
 * map.
 */
data class Graphic(
		/**
		 * A magic number to mapMarker the beginning of a Graphic in the Graphic.bin
		 * file.
		 */
		val magic: Int = 0,

		/**
		 * Indicates whether the data is raw or encoded
		 */
		val version: Version = Version.RAW,

		/**
		 * Unused data.
		 */
		val unknown: Int = 0,

		/**
		 * The width of the Graphic when rendered.
		 */
		val width: Int = 0,

		/**
		 * The height of the Graphic when rendered.
		 */
		val height: Int = 0,

		/**
		 * The number of bytes (raw or encoded) this Graphic contains.
		 */
		val dataLength: Int = 0,

		/**
		 * The raw or encoded graphic data.
		 */
		val data: ByteBuffer = ByteBuffer.allocate(0)) {

	/**
	 * Data version.
	 */
	enum class Version {
		RAW, ENCODED
	}

}