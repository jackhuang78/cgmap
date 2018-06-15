package com.jhuang78.cgmap.io

import com.google.common.truth.Truth.assertThat
import com.jhuang78.cgmap.common.underResource
import com.jhuang78.cgmap.entity.Graphic
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import java.nio.file.Paths
import kotlin.test.assertFailsWith

class GraphicFileReaderTest : Spek({

	describe("GraphicFileReader()") {
		given("an valid input file") {
			val reader = GraphicFileReader(
					Paths.get("Graphic_66_0_424.bin").underResource())

			given("successfully read a entry") {
				val graphic = reader.read(0, 424)

				it("should be a valid entry") {
					graphic.validate()
				}

				it("should read entry value correctly") {
					assertThat(graphic.version).isEqualTo(
							Graphic.Version.ENCODED)
					assertThat(graphic.width).isEqualTo(0x00000040)
					assertThat(graphic.height).isEqualTo(0x0000002f)
					assertThat(graphic.dataLength).isEqualTo(424)
				}
			}


			it("should not be able to read past file length") {
				assertFailsWith<IndexOutOfBoundsException> {
					reader.read(424, 1)
				}
			}
		}
	}
})