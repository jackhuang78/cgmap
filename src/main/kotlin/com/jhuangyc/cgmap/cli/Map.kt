package com.jhuangyc.cgmap.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.path
import com.jhuangyc.cgmap.io.MapFileReader
import com.jhuangyc.cgmap.util.toHex
import mu.KotlinLogging
import java.nio.file.Path
import java.nio.file.Paths

object Map : CliktCommand(help = "Show info for a Map entity") {
	private val logger = KotlinLogging.logger {}

	private val mapFile: Path by argument(
			name = "<map_file>",
			help = "The Map file (.dat)")
			.path(exists = true, folderOkay = false)

	private val paint: Boolean by option(
			"-p",
			help = "Paint the Graphic to screen or file")
			.flag()

	private val graphicInfoFile: Path by option(
			help = "The GraphicInfo file (.bin)")
			.path(exists = true, folderOkay = false)
			.default(Paths.get("data", "GraphicInfo_66.bin"))

	private val graphicFile: Path by option(
			help = "The Graphic file (.bin)")
			.path(exists = true, folderOkay = false)
			.default(Paths.get("data", "Graphic_66.bin"))

	private val paletFile: Path by option(
			help = "The Palet file (.cgp) to use for painting the graphic")
			.path(exists = true, folderOkay = false)
			.default(Paths.get("data", "palet", "palet_00.cgp"))


	override fun run() {
		val map = MapFileReader(mapFile).read()

		echo("File ${mapFile}:")
		echo("  Magic = 0x${map.magic.toHex()}")
		echo("  Size = ${map.size}")
		echo("  EastLength = ${map.eastLength}")
		echo("  SouthLength = ${map.southLength}")


	}
}