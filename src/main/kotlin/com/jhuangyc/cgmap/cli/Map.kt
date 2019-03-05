package com.jhuangyc.cgmap.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.defaultLazy
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.path
import com.jhuangyc.cgmap.io.readMapFile
import com.jhuangyc.cgmap.util.toHex
import mu.KotlinLogging
import java.nio.file.Path
import java.nio.file.Paths

object Map : CliktCommand(help = "Show info for a Map entity") {
	private val logger = KotlinLogging.logger {}

	private val mapFile: Path by argument(
			help = "The Map file (.dat)")
			.path()

	private val dataDir: Path by option(
			help = "The directory where all data files reside")
			.path(exists = true, fileOkay = false)
			.default(Paths.get(".", "data"))

	private val outputDir: Path by option(
			help = "The directory where outputs are written to")
			.path(exists = true, fileOkay = false)
			.default(Paths.get(".", "out"))

	private val paintMapToFile: Path? by option(
			help = "If specified, paint the Map and output to this file")
			.path(folderOkay = false)

	private val graphicInfoFile: Path by option(
			help = "The GraphicInfo file (.bin)")
			.path(exists = true, folderOkay = false)
			.defaultLazy {
				dataDir.resolve("GraphicInfo_66.bin")
			}

	private val graphicFile: Path by option(
			help = "The Graphic file (.bin)")
			.path(exists = true, folderOkay = false)
			.defaultLazy {
				dataDir.resolve("Graphic_66.bin")
			}

	private val paletFile: Path by option(
			help = "The Palet file (.cgp) to use for painting the graphic")
			.path(exists = true, folderOkay = false)
			.defaultLazy { dataDir.resolve("palet/palet_00.cgp") }


	override fun run() {
		val map = readMapFile(dataDir.resolve(mapFile))

		echo("File ${mapFile}:")
		echo("  Magic = 0x${map.magic.toHex()}")
		echo("  Size = ${map.size}")
		echo("  EastLength = ${map.eastLength}")
		echo("  SouthLength = ${map.southLength}")


	}
}