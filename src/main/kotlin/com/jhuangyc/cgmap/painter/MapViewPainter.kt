package com.jhuangyc.cgmap.painter

import com.jhuangyc.cgmap.entity.Map
import com.jhuangyc.cgmap.entity.MapLocation
import com.jhuangyc.cgmap.entity.MapView
import com.jhuangyc.cgmap.entity.Palet
import com.jhuangyc.cgmap.io.GraphicFileReader
import com.jhuangyc.cgmap.io.GraphicInfoFileReader
import java.awt.Color
import java.awt.Dimension
import java.awt.Graphics2D
import java.awt.Point

val TILE_DIMENSION = Dimension(64, 47)

class MapViewPainter(
		private val mapView: MapView,
		val map: Map,
		val palet: Palet,
		val graphicFileReader: GraphicFileReader,
		val graphicInfoFileReader: GraphicInfoFileReader,
		var drawOutline: Boolean = false,
		var drawTileNumber: Boolean = false
) {

	fun paint(g: Graphics2D, centerTile: MapLocation) {
		val centerTileOrigin = Point(
				(mapView.viewWidth / 2) * TILE_DIMENSION.width,
				(mapView.viewHeight / 2) * TILE_DIMENSION.height)

		mapView.tilesCenteredAt(centerTile).forEach {
			val tileOrigin = centerTileOrigin.tileMove(it.minus(centerTile))
			drawTile(g, it, true, tileOrigin)
		}

		mapView.tilesCenteredAt(centerTile).forEach {
			val tileOrigin = centerTileOrigin.tileMove(it.minus(centerTile))
			drawTile(g, it, false, tileOrigin)
		}
	}

	private fun drawTile(g: Graphics2D, tile: MapLocation,
			drawFloor: Boolean,
			tileOrigin: Point) {

		val tileNumber = tile.getTileNumber(map.dimension.east, map.dimension.south)

		if (tileNumber != null) {
			val graphicMapNumber = if (drawFloor)
				map.floors[tileNumber] else map.entities[tileNumber]

			if (graphicMapNumber !in setOf(0, 2, 999, 100)) {
				val graphicInfo = graphicInfoFileReader.readByGraphicId(
						graphicMapNumber)
				val graphic = graphicFileReader.read(graphicInfo.address,
						graphicInfo.dataLength)

				GraphicPainter(graphicInfo, graphic, palet, tileOrigin).paint(g)
			} else {
				println("$tileNumber, $graphicMapNumber")
			}


			if (drawTileNumber) {
				g.color = Color.black
				g.drawString(
						"${graphicMapNumber}",
						//"[${tile.getTileNumber(map.eastLength, map.southLength)}],${graphicMapNumber}",
						tileOrigin.x + TILE_DIMENSION.width / 2,
						tileOrigin.y + TILE_DIMENSION.height / 2)
			}
		}


		if (drawOutline) {
			g.color = Color.red
			g.drawPolygon(
					intArrayOf(
							tileOrigin.x,
							tileOrigin.x + TILE_DIMENSION.width / 2,
							tileOrigin.x + TILE_DIMENSION.width,
							tileOrigin.x + TILE_DIMENSION.width / 2,
							tileOrigin.x
					),
					intArrayOf(
							tileOrigin.y + TILE_DIMENSION.height / 2,
							tileOrigin.y,
							tileOrigin.y + TILE_DIMENSION.height / 2,
							tileOrigin.y + TILE_DIMENSION.height,
							tileOrigin.y + TILE_DIMENSION.height / 2
					), 5)
		}


	}

	fun Point.tileMove(delta: MapLocation): Point {
		return Point(
				x + (delta.south + delta.east) * TILE_DIMENSION.width / 2,
				y + (delta.south - delta.east) * TILE_DIMENSION.height / 2)
	}
}