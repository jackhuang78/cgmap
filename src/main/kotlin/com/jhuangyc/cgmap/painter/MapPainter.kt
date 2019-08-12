package com.jhuangyc.cgmap.painter

import com.jhuangyc.cgmap.entity.Map
import java.awt.Color
import java.awt.Graphics2D
import kotlin.math.PI

class MapPainter(
		private val map: Map
) {

	fun paint(g: Graphics2D, center: Map.Point, dimension: Map.Point) {
		val origin = center - (dimension / 2)


		val width = 20
		val height = 10

		for(de in 0 until dimension.east) {
			for(ds in 0 until dimension.south) {
				val current = origin + Map.Point(de, ds)

				g.color = Color.BLACK
				g.drawRect(current.east * width, current.south * height, width, height)
			}
		}
	}


//	fun Map.Point.rotate(degree: Double, about: Map.Point) {
//		val ROOT_2_OVER_2 = Math.sqrt(2.0)/2
//		val p1 = this - about
//		return Map.Point(p1.east * ROOT_2_OVER_2, p1.south * ROOT_2_OVER_2,
//				p1.east * -ROOT_2_OVER_2, p1.south * ROOT_2_OVER_2)
//	}
}