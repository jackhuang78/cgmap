package com.jhuangyc.cgmap.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.options.counted
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.versionOption
import com.google.common.io.Resources
import mu.KotlinLogging
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.core.config.Configurator

/**
 * Top-level CLI command.
 */
object CGMap : CliktCommand() {
	private val logger = KotlinLogging.logger {}

	//region CLI arguments and flags
	private val verbose: Int by option("-v").counted()
	//endregion

	init {
		subcommands(Graphic, Map)
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

		//region Displaying banner
		logger.info("Run CGMap command")
		echo(Resources.getResource("banner.txt").readText())
		//endregion
	}
}

