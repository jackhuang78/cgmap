@file:JvmName("GraphicInfoFileViewer")

package com.jhuang78.cgmap.tool

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.types.int
import com.jhuang78.cgmap.common.illustrate
import com.jhuang78.cgmap.io.GraphicFileReader
import com.jhuang78.cgmap.io.GraphicInfoFileReader
import mu.KotlinLogging
import java.nio.file.Paths

/**
 * Logger for this file.
 */
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
		println(graphic.data.illustrate())
	}
}.main(if (args.isNotEmpty()) args else
// hardcoded arguments for convenience
	arrayOf("data/GraphicInfo_66.bin", "data/Graphic_66.bin", "2"))
