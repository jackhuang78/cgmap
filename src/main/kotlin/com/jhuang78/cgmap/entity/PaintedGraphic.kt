package com.jhuang78.cgmap.entity

import com.jhuang78.cgmap.painter.paintGraphic
import java.awt.Dimension
import java.awt.image.BufferedImage

class PaintedGraphic(graphic: Graphic, info: GraphicInfo, palet: Palet) {
	val preferredName = "${info.mapNo}_${info.graphicNo}.png"
	val preferredSize = Dimension(info.imageWidth, info.imageHeight)
	val image by lazy {
		val image = BufferedImage(info.imageWidth, info.imageHeight,
				BufferedImage.TYPE_INT_ARGB)
		paintGraphic(image.createGraphics(), info,
				graphic, palet)
		image
	}
}