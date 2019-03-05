package com.jhuangyc.cgmap.io

import com.google.common.truth.Truth.assertThat
import com.jhuang78.cgmap.util.underResource
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import java.nio.file.Paths

class MapFileReaderTest : Spek({

	describe("readMapFile()") {
		given("an valid file") {
			val map = readMapFile(Paths.get("map", "0", "100.dat").underResource())
			it("should read header correctly") {
				assertThat(map.eastLength).isEqualTo(0x0348)
				assertThat(map.southLength).isEqualTo(0x0262)
				// TODO: check the rest of the header
			}
			it("should past validation") {
				map.validate()
			}
		}
	}
})