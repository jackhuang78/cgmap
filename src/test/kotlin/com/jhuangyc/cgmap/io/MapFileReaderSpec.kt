package com.jhuangyc.cgmap.io

import com.google.common.truth.Truth.assertThat
import com.jhuangyc.cgmap.util.fromResources
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import java.nio.file.Paths

object MapFileReaderSpec : Spek({

	given("a reader") {
		val file = Paths.get("map", "0", "100.dat").fromResources()
		val reader = MapFileReader(file)

		on("read()") {
			val map = reader.read()

			it("should read header correctly") {
				assertThat(map.eastLength).isEqualTo(0x0348)
				assertThat(map.southLength).isEqualTo(0x0262)

				// TODO: check the rest of the header
			}
		}
	}
})