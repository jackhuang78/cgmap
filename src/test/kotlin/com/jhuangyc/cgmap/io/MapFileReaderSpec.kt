package com.jhuangyc.cgmap.io

import com.jhuangyc.cgmap.util.fromResources
import org.spekframework.spek2.Spek
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import java.nio.file.Paths

object MapFileReaderSpec : Spek({

	test("Should read the map header") {
		val file = Paths.get("map", "0", "100.dat").fromResources()
		val reader = MapFileReader(file)

		val map = reader.read()

		expectThat(map) {
			get { dimension }.and {
				get { east }.isEqualTo(0x0348)
				get { south }.isEqualTo(0x0262)

				// TODO: check the rest of the header
			}
		}
	}
})