@file:JvmName("MapViewer")

package com.jhuangyc.cgmap.tool

import com.github.ajalt.clikt.core.CliktCommand
import com.jhuangyc.cgmap.entity.MapLocation
import com.jhuangyc.cgmap.entity.MapView
import com.jhuangyc.cgmap.io.GraphicFileReader
import com.jhuangyc.cgmap.io.GraphicInfoFileReader
import com.jhuangyc.cgmap.io.PaletFileReader
import com.jhuangyc.cgmap.io.readMapFile
import com.jhuangyc.cgmap.painter.MapViewPainter
import com.jhuangyc.cgmap.painter.TILE_DIMENSION
import mu.KotlinLogging
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.nio.file.Paths
import javax.swing.JFrame
import javax.swing.JFrame.EXIT_ON_CLOSE
import javax.swing.JPanel


/**
 * Logger for this file.
 */
private val logger = KotlinLogging.logger {}

fun main(args: Array<String>) = object : CliktCommand() {

	override fun run() {
		println("========= MapViewer ==========")

		val mapView = MapView(9, 9)
		val mapViewCenter = MapLocation(50, 50)
		val mapViewPainter = MapViewPainter(
				mapView = mapView,
				map = readMapFile(Paths.get("data", "map", "0", "1000.dat")),
				palet = PaletFileReader(
						Paths.get("data", "palet", "palet_00.cgp")).read(),
				graphicFileReader = GraphicFileReader(
						Paths.get("data", "Graphic_66.bin")),
				graphicInfoFileReader = GraphicInfoFileReader(
						Paths.get("data", "GraphicInfo_66.bin"))
		)


		JFrame("MapViewer").let {
			val panel = object : JPanel() {
				override fun paint(g: Graphics?) {
					mapViewPainter.paint(g!! as Graphics2D, mapViewCenter)
				}

				override fun getPreferredSize(): Dimension {
					return Dimension(
							mapView.viewWidth * TILE_DIMENSION.width,
							mapView.viewHeight * TILE_DIMENSION.height)
				}
			}
			it.add(panel)
			it.addKeyListener(object : KeyAdapter() {

				override fun keyPressed(e: KeyEvent?) {
					if (e!!.keyChar !in setOf('q', 'w', 'e', 'd', 'c', 'x', 'z', 'a', '1',
									'2')) {
						return
					}

					when (e.keyChar) {
						'q' -> mapViewCenter.moveSouth(-1)
						'w' -> mapViewCenter.moveSouthWest(-1)
						'e' -> mapViewCenter.moveEast(1)
						'd' -> mapViewCenter.moveSouthEast(1)
						'c' -> mapViewCenter.moveSouth(1)
						'x' -> mapViewCenter.moveSouthWest(1)
						'z' -> mapViewCenter.moveEast(-1)
						'a' -> mapViewCenter.moveSouthEast(-1)
						'1' -> mapViewPainter.drawOutline = !mapViewPainter.drawOutline
						'2' -> mapViewPainter.drawTileNumber = !mapViewPainter.drawTileNumber
					}
					panel.repaint()
				}
			})
			it.pack()
			it.isVisible = true
			it.defaultCloseOperation = EXIT_ON_CLOSE

		}


	}
}.main(args)
