package com.jhuangyc.cgmap.io

import com.google.common.truth.Truth.assertThat
import com.jhuang78.cgmap.util.underResource
import com.jhuang78.cgmap.entity.GraphicInfo
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import java.io.IOException
import java.nio.file.Paths
import kotlin.test.assertFailsWith


class GraphicInfoFileReaderTest : Spek({

	describe("GraphicInfoFileReader()") {
		given("a valid input file") {
			val reader = GraphicInfoFileReader(
					Paths.get("GraphicInfo_66_small.bin").underResource())

			it("should know number of entries") {
				assertThat(reader.numberOfEntries).isEqualTo(2)
			}

			it("should read valid entries") {
				reader.read(0).validate()
				reader.read(1).validate()
			}

			it("should read entries correctly") {
				val info0 = reader.read(0);
				assertThat(info0.graphicNo).isEqualTo(0);
				assertThat(info0.address).isEqualTo(0x0000);
				assertThat(info0.dataLength).isEqualTo(0x01a8);
				assertThat(info0.offsetX).isEqualTo(-32);
				assertThat(info0.offsetY).isEqualTo(-24);
				assertThat(info0.imageWidth).isEqualTo(64);
				assertThat(info0.imageHeight).isEqualTo(47);
				assertThat(info0.occupyEast).isEqualTo(1);
				assertThat(info0.occupySouth).isEqualTo(1);
				assertThat(info0.mapMarker).isEqualTo(
						GraphicInfo.MapMarker.FLOOR);
				assertThat(info0.mapNo).isEqualTo(0x03e7);

				val info1 = reader.read(1);
				assertThat(info1.graphicNo).isEqualTo(1);
				assertThat(info1.address).isEqualTo(0x01a8);
				assertThat(info1.dataLength).isEqualTo(0x0012);
				assertThat(info1.offsetX).isEqualTo(-32);
				assertThat(info1.offsetY).isEqualTo(-24);
				assertThat(info1.imageWidth).isEqualTo(64);
				assertThat(info1.imageHeight).isEqualTo(48);
				assertThat(info1.occupyEast).isEqualTo(1);
				assertThat(info1.occupySouth).isEqualTo(1);
				assertThat(info1.mapMarker).isEqualTo(
						GraphicInfo.MapMarker.OBSTACLE);
				assertThat(info1.mapNo).isEqualTo(0x0012);
			}

			it("should fail to read non-existing entry") {
				assertFailsWith<IndexOutOfBoundsException> {
					reader.read(2)
				}
			}
		}

		given("a directory as input file") {
			val reader = GraphicInfoFileReader(Paths.get(".").underResource())

			it("should fail to read an entry") {
				assertFailsWith<IOException> {
					reader.read(0)
				}
			}
		}

		given("an invalid input file") {
			val reader = GraphicInfoFileReader(
					Paths.get("random.bin").underResource())

			it("should fail to read an valid entry") {
				assertFailsWith<IllegalStateException> {
					reader.read(0).validate()
				}
			}
		}
	}
})