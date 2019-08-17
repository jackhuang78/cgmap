package com.jhuangyc.cgmap.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.path
import com.jhuangyc.cgmap.entity.Map
import com.jhuangyc.cgmap.io.MapFileReader
import com.jhuangyc.cgmap.painter.MapPainter
import com.jhuangyc.cgmap.util.toHex
import mu.KotlinLogging
import java.awt.Graphics
import java.awt.Graphics2D
import java.nio.file.Path
import java.nio.file.Paths
import javax.swing.JFrame
import javax.swing.JPanel

object MapCommand :
		CliktCommand(name = "map", help = "Show info for a MapCommand entity") {
	private val logger = KotlinLogging.logger {}

	private val mapFile: Path by argument(
			name = "<map_file>",
			help = "The MapCommand file (.dat)")
			.path(exists = true, folderOkay = false)

	private val paint: Boolean by option(
			"-p",
			help = "Paint the GraphicCommand to screen or file")
			.flag()

	private val graphicInfoFile: Path by option(
			"--graphic_info_file",
			help = "The GraphicInfo file (.bin)")
			.path(exists = true, folderOkay = false)
			.default(Paths.get("data", "GraphicInfo_66.bin"))

	private val graphicFile: Path by option(
			"--graphic_file",
			help = "The GraphicCommand file (.bin)")
			.path(exists = true, folderOkay = false)
			.default(Paths.get("data", "Graphic_66.bin"))

	private val paletFile: Path by option(
			"--palet_file",
			help = "The Palet file (.cgp) to use for painting the graphic")
			.path(exists = true, folderOkay = false)
			.default(Paths.get("data", "palet", "palet_00.cgp"))


	override fun run() {
		val map = MapFileReader(mapFile).read()

		echo("File ${mapFile}:")
		echo("  Magic = 0x${map.magic.toHex()}")
		echo("  Dimension = ${map.dimension}")


		val painter = MapPainter(map)

		JFrame("CGMap GraphicCommand").let { frame ->
			frame.add(object : JPanel() {
				override fun paint(g: Graphics?) {
					painter.paint(g as Graphics2D, Map.Point(0, 0),
							Map.Point(10, 10))
				}

				//				override fun getPreferredSize(): Dimension {
				//					return paintedGraphic.dimension()
				//				}
			})
			frame.pack()
			frame.isVisible = true
			frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
		}


	}
}