package com.jhuang78.cgmap.io

import com.jhuang78.cgmap.graphics.paintGraphic
import java.awt.image.BufferedImage

class PaintedGraphic(graphic: Graphic, info: GraphicInfo, palet: Palet) {
	val name = "${info.mapNo}_${info.graphicNo}.png"
	val image by lazy {
		val image = BufferedImage(info.imageWidth, info.imageHeight,
				BufferedImage.TYPE_INT_ARGB)
		paintGraphic(image.createGraphics(), info, graphic, palet)
		image
	}
}