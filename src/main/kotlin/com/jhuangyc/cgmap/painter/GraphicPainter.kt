package com.jhuangyc.cgmap.painter

import com.jhuangyc.cgmap.entity.Graphic
import com.jhuangyc.cgmap.entity.GraphicInfo
import com.jhuangyc.cgmap.entity.Palet
import com.jhuangyc.cgmap.io.GraphicDataDecoder
import com.jhuangyc.cgmap.util.asIterable
import java.awt.Graphics2D
import java.awt.Point
import java.awt.image.BufferedImage

@ExperimentalUnsignedTypes
class GraphicPainter(
		private val info: GraphicInfo,
		private val graphic: Graphic,
		private val palet: Palet,
		private val origin: Point = Point(0, 0)) {

	fun paint(): BufferedImage {
		val image = BufferedImage(
				info.imageWidth,
				info.imageHeight,
				BufferedImage.TYPE_INT_ARGB)
		paint(image.createGraphics())
		return image
	}

	fun paint(g: Graphics2D) {
		val colorIterator = when (graphic.version) {
			Graphic.Version.RAW -> graphic.data.asIterable().map { it.toUByte() }
			Graphic.Version.ENCODED -> GraphicDataDecoder(graphic.data)
		}.iterator()

		for (y in info.imageHeight - 1 downTo 0) {
			for (x in 0 until info.imageWidth) {
				val colorPoint = colorIterator.next()
				if (colorPoint != null) {
					g.color = palet.colors[colorPoint.toInt()]
					g.fillRect(origin.x + x, origin.y + y, 1, 1)
				}
			}
		}
	}
}