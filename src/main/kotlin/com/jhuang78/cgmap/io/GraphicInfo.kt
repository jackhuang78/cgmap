package com.jhuang78.cgmap.io

import com.google.common.base.Preconditions.checkState

/**
 * A GraphicInfo contains the metadata of a Graphic.
 *
 * It includes information like the location of the Graphic in the Graphic.bin
 * file and how the Graphic should be rendered.
 *
 * Also {@see https://cgsword.com/filesystem_graphicmap.htm#graphicinfo}.
 */
data class GraphicInfo(
		/**
		 * A serial number assigned to this Graphic.
		 */
		val graphicNo: Int = 0,

		/**
		 * The starting location of the Graphic in the Grpahic.bin file.
		 */
		val address: Int = 0,

		/**
		 * The number of bytes occupied by the Graphic in the Grpahic.bin file.
		 */
		val dataLength: Int = 0,

		/**
		 * The horizontal offset of this Graphic when rendered.
		 */
		val offsetX: Int = 0,

		/**
		 * The vertical offset of this Graphic when rendered.
		 */
		val offsetY: Int = 0,

		/**
		 * The width of this Graphic when rendered.
		 */
		val imageWidth: Int = 0,

		/**
		 * The height of this Graphic when rendered.
		 */
		val imageHeight: Int = 0,

		/**
		 * The number of tiles this Graphic occupies in the east direction
		 * (e.g. upper right) when rendered.
		 */
		val occupyEast: Int = 0,

		/**
		 * The number of tiles this Graphic occupies in the south direction
		 * (e.g. lower right) when rendered.
		 */
		val occupySouth: Int = 0,

		/**
		 * Map property of this Graphic.
		 */
		val mapMarker: MapMarker = MapMarker.OBSTACLE,

		/**
		 * Some unknown value not being used.
		 */
		val unknown: Long = 0L,

		/**
		 * The map this Graphic belongs to.
		 */
		val mapNo: Int = 0
) {

	/**
	 * Possible map property of the Graphic.
	 */
	enum class MapMarker {
		OBSTACLE, FLOOR
	}
}




