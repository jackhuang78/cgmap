package com.jhuang78.cgmap.io

import com.google.common.truth.Truth.assertThat
import com.jhuang78.cgmap.common.getPathForResource
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertFailsWith

class GraphicFileReaderTest : Spek({

	describe("an GraphicFileReader") {
		given("an valid input file") {
			val reader = GraphicFileReader(getPathForResource("Graphic_66_0_424.bin"))

			given("successfully read a entry") {
				val graphic = reader.read(0, 424)

				it("should be a valid entry") {
					graphic.validate()
				}

				it("should read entry value correctly") {
					assertThat(graphic.version).isEqualTo(Graphic.Version.ENCODED)
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