package com.jhuangyc.cgmap.entity

import java.nio.ByteBuffer

/**
 * A GraphicCommand contains the data (raw or encoded) of a piece of graphic on the
 * map.
 */
data class Graphic(
		/**
		 * A magic number to mapMarker the beginning of a GraphicCommand in the GraphicCommand.bin
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
		 * The width of the GraphicCommand when rendered.
		 */
		val width: Int = 0,

		/**
		 * The height of the GraphicCommand when rendered.
		 */
		val height: Int = 0,

		/**
		 * The number of bytes (raw or encoded) this GraphicCommand contains.
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