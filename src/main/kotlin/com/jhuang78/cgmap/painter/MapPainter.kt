package com.jhuang78.cgmap.painter

import com.jhuang78.cgmap.entity.Map
import com.jhuang78.cgmap.io.GraphicFileReader
import com.jhuang78.cgmap.io.GraphicInfoFileReader
import java.awt.Graphics2D
import java.awt.Point


fun paintMap(g: Graphics2D,
		origin: Point,
		map: Map,
		centerTile: Int,
		side: Int,
		graphicInfoFileReader: GraphicInfoFileReader,
		graphicFileReader: GraphicFileReader) {


	// Start drawing from the bottom
	val bottomTile = centerTile + map.eastLength - 1


}
