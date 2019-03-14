package com.jhuangyc.cgmap.io

import com.google.common.truth.Truth.assertThat
import com.jhuangyc.cgmap.entity.GraphicInfo
import com.jhuangyc.cgmap.util.fromResources
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import java.io.IOException
import java.nio.file.Paths
import kotlin.test.assertFails


object GraphicInfoFileReaderSpec : Spek({

	given("a reader with file containing 2 entries") {
		val validFile = Paths.get("GraphicInfo_66_small.bin").fromResources()
		val reader = GraphicInfoFileReader(validFile)

		on("numEntries()") {
			val numEntries = reader.numEntries

			it("should be 2") {
				assertThat(numEntries).isEqualTo(2)
			}
		}

		on("read(0)") {
			val info = reader.read(0)

			it("should read 1st entry correctly") {
				assertThat(info.graphicNo).isEqualTo(0)
				assertThat(info.address).isEqualTo(0x0000)
				assertThat(info.dataLength).isEqualTo(0x01a8)
				assertThat(info.offsetX).isEqualTo(-32)
				assertThat(info.offsetY).isEqualTo(-24)
				assertThat(info.imageWidth).isEqualTo(64)
				assertThat(info.imageHeight).isEqualTo(47)
				assertThat(info.occupyEast).isEqualTo(1)
				assertThat(info.occupySouth).isEqualTo(1)
				assertThat(info.mapMarker).isEqualTo(
						GraphicInfo.MapMarker.FLOOR)
				assertThat(info.graphicId).isEqualTo(0x03e7)
			}
		}

		on("read(1)") {
			val info = reader.read(1)

			it("should read 2nd entry correctly") {
				assertThat(info.graphicNo).isEqualTo(1)
				assertThat(info.address).isEqualTo(0x01a8)
				assertThat(info.dataLength).isEqualTo(0x0012)
				assertThat(info.offsetX).isEqualTo(-32)
				assertThat(info.offsetY).isEqualTo(-24)
				assertThat(info.imageWidth).isEqualTo(64)
				assertThat(info.imageHeight).isEqualTo(48)
				assertThat(info.occupyEast).isEqualTo(1)
				assertThat(info.occupySouth).isEqualTo(1)
				assertThat(info.mapMarker).isEqualTo(
						GraphicInfo.MapMarker.OBSTACLE)
				assertThat(info.graphicId).isEqualTo(0x0012)
			}
		}

		on("read(2)") {
			val thrown = assertFails { reader.read(2) }

			it("should fail") {
				assertThat(thrown).isInstanceOf(IOException::class.java)
			}
		}

		given("a reader with invalid input file") {
			val invalidFile = Paths.get("random.bin").fromResources()
			val thrown = assertFails { GraphicInfoFileReader(invalidFile) }

			it("should fail") {
				assertThat(thrown).isInstanceOf(IllegalStateException::class.java)
			}
		}

		given("a reader with directory as input file") {
			val directory = Paths.get(".").fromResources()
			val thrown = assertFails { GraphicInfoFileReader(directory) }

			it("should fail") {
				assertThat(thrown).isInstanceOf(IOException::class.java)
			}
		}


	}
})