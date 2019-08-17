package com.jhuangyc.cgmap.entity

/**
 * A GraphicInfo contains the metadata of a GraphicCommand.
 *
 * It includes information like the location of the GraphicCommand in the GraphicCommand.bin
 * file and how the GraphicCommand should be rendered.
 *
 * Also {@see https://cgsword.com/filesystem_graphicmap.htm#graphicinfo}.
 */
data class GraphicInfo(
		/**
		 * A serial number assigned to this GraphicCommand.
		 */
		val graphicNo: Int = 0,

		/**
		 * The starting location of the GraphicCommand in the Grpahic.bin file.
		 */
		val address: Int = 0,

		/**
		 * The number of bytes occupied by the GraphicCommand in the Grpahic.bin file.
		 */
		val dataLength: Int = 0,

		/**
		 * The horizontal offset of this GraphicCommand when rendered.
		 */
		val offsetX: Int = 0,

		/**
		 * The vertical offset of this GraphicCommand when rendered.
		 */
		val offsetY: Int = 0,

		/**
		 * The width of this GraphicCommand when rendered.
		 */
		val imageWidth: Int = 0,

		/**
		 * The height of this GraphicCommand when rendered.
		 */
		val imageHeight: Int = 0,

		/**
		 * The number of tiles this GraphicCommand occupies in the east direction
		 * (e.g. upper right) when rendered.
		 */
		val occupyEast: Int = 0,

		/**
		 * The number of tiles this GraphicCommand occupies in the south direction
		 * (e.g. lower right) when rendered.
		 */
		val occupySouth: Int = 0,

		/**
		 * MapCommand property of this GraphicCommand.
		 */
		val mapMarker: MapMarker = MapMarker.OBSTACLE,

		/**
		 * Some unknown value not being used.
		 */
		val unknown: Long = 0L,

		/**
		 * The ID of this graphic
		 */
		val graphicId: Int = 0
) {

	/**
	 * Possible map property of the GraphicCommand.
	 */
	enum class MapMarker {
		OBSTACLE, FLOOR
	}
}




