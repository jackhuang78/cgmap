@file:JvmName("GraphicInfoFileViewer")

package com.jhuangyc.cgmap.tool

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int
import com.jhuangyc.cgmap.entity.PaintedGraphic
import com.jhuangyc.cgmap.io.GraphicFileReader
import com.jhuangyc.cgmap.io.GraphicInfoFileReader
import com.jhuangyc.cgmap.io.readPaletFile
import com.jhuangyc.cgmap.io.writePaintedGraphic
import com.jhuangyc.cgmap.util.toHexTable
import mu.KotlinLogging
import java.awt.Graphics
import java.nio.file.Paths
import javax.swing.JFrame
import javax.swing.JFrame.EXIT_ON_CLOSE
import javax.swing.JPanel

private val logger = KotlinLogging.logger {}

/**
 * A runnable class to display content of GraphicInfo.bin files.
 */
fun main(args: Array<String>) = object : CliktCommand() {
	val graphicInfoFile: String by argument(
			help = "The GraphicInfo file (e.g. GraphicInfo.bin)")
	val graphicFile: String by argument(
			help = "The Graphic file (e.g. Graphic.bin)")
	val entry: Int by argument(help = "The entry to display").int()
	val paint: Boolean by option(
			help = "Map the Graphic into an image file").flag()
	val paletFile: String by option(
			help = "The Palet file (e.g. palet/palet_00.cgp)"
	).default("data/palet/palet_00.cgp")

	override fun run() {
		println("========= GraphicInfoFileViewer ==========")
		println()
		println("GraphicInfo file: ${graphicInfoFile}")
		println("Graphic file: ${graphicFile}")
		println()

		// Read and display GraphicInfo
		val graphicInfo = GraphicInfoFileReader(Paths.get(graphicInfoFile)).use {
			it.read(entry)
		}
		println(graphicInfo.toString().replace(",", "\n"))
		println()

		// Read and display Graphic
		val graphic = GraphicFileReader(Paths.get(graphicFile)).use {
			it.read(graphicInfo.address, graphicInfo.dataLength)
		}
		println(graphic.toString().replace(",", "\n"))
		println()

		println("Graphic.data")
		println(graphic.data.toHexTable())

		if (paint) {
			println("Painting Graphic")
			val palet = readPaletFile(Paths.get(paletFile))
			val paintedGraphic = PaintedGraphic(graphic,
					graphicInfo, palet)

			writePaintedGraphic(Paths.get("out", paintedGraphic.preferredName),
					paintedGraphic)

			JFrame("GraphicFileViewer").let {
				it.add(object : JPanel() {
					override fun paint(g: Graphics?) {
						g!!.drawImage(paintedGraphic.image, 0, 0, null)
					}
				}).let { it.preferredSize = paintedGraphic.preferredSize }
				it.pack()
				it.isVisible = true
				it.defaultCloseOperation = EXIT_ON_CLOSE
			}
		}
	}
}.main(if (args.isNotEmpty()) args else
// hardcoded arguments for convenience
	arrayOf("data/GraphicInfo_66.bin", "data/Graphic_66.bin", "0", "--paint"))
