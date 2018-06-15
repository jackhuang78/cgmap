package com.jhuang78.cgmap.graphics

import com.jhuang78.cgmap.common.asIterable
import com.jhuang78.cgmap.common.toUint
import com.jhuang78.cgmap.entity.Graphic
import com.jhuang78.cgmap.io.GraphicDataDecoder
import com.jhuang78.cgmap.entity.GraphicInfo
import com.jhuang78.cgmap.entity.Palet
import java.awt.Graphics2D
import java.awt.Point

fun paintGraphic(g: Graphics2D, info: GraphicInfo,
								 graphic: Graphic, palet: Palet,
								 origin: Point = Point(0, 0)) {

	val colorIterator = when (graphic.version) {
		Graphic.Version.RAW -> graphic.data.asIterable().map { it.toUint() }
		Graphic.Version.ENCODED -> GraphicDataDecoder(graphic.data)
	}.iterator()


	for (y in info.imageHeight - 1 downTo 0) {
		for (x in 0 until info.imageWidth) {
			val colorPoint = colorIterator.next()
			if (colorPoint != null) {
				g.color = palet.colors[colorPoint]
				g.fillRect(origin.x + x, origin.y + y, 1, 1)
			}
		}
	}
}
