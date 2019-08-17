package com.jhuangyc.cgmap.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.findObject
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.options.counted
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.versionOption
import com.github.ajalt.clikt.parameters.types.path
import com.google.common.io.Resources
import mu.KotlinLogging
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.core.config.Configurator
import java.nio.file.Path
import java.nio.file.Paths

/**
 * Top-level CLI command.
 */
object MainCommand : CliktCommand(name = "cgmap") {
	private val logger = KotlinLogging.logger {}

	//region CLI arguments and flags
	private val verbose: Int by option(
		"-v",
		help = "Increase verbosity"
	).counted()

	private val graphicInfoFile: Path by option(
		"--graphic_info_file", help = "The GraphicInfo file (.bin)")
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
	//endregion

	data class FileParams(
		var graphicInfoFile: Path,
		var graphicFile: Path,
		var paletFile: Path
	)

	val fileParams by findObject {
		FileParams(
			graphicInfoFile = graphicInfoFile,
			graphicFile = graphicFile,
			paletFile = paletFile)
	}

	init {
		subcommands(GraphicCommand, MapCommand)
		versionOption("1.0")
	}

	override fun run() {
		//region Modifying logging level according to the specified verbosity
		when (verbose) {
			1 -> Configurator.setRootLevel(Level.INFO)
			2 -> Configurator.setRootLevel(Level.DEBUG)
			in 3..Int.MAX_VALUE -> Configurator.setRootLevel(Level.TRACE)
		}
		//endregion

		fileParams.graphicInfoFile = graphicInfoFile
		fileParams.graphicFile = graphicFile
		fileParams.paletFile = paletFile

		//region Displaying banner
		logger.info("Run MainCommand command")
		echo(Resources.getResource("banner.txt").readText())
		//endregion
	}
}

