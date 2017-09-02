package com.jhuang78.cgmap.graphics

import com.google.common.truth.Truth.assertThat
import com.google.common.io.Resources
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import java.nio.file.Paths
import kotlin.test.assertFailsWith

class GraphicsTest : Spek({
    describe("an Info") {
        given("that is valid") {
            val info = Info()
            it("should pass validation") {
                info.validate()
            }
        }
        given("that is invalid") {
            val info = Info(graphicNo=-1)
            it("should fail validation") {
                assertFailsWith<IllegalStateException> {
                    info.validate()
                }
            }
        }
    }

    describe("an InfoFileReader") {
        given("a valid input file") {
            val path = Paths.get(Resources.getResource("GraphicInfo_66_small.bin").path)
            val reader = InfoFileReader(path)
            it("should know number of entries") {
                assertThat(reader.size).isEqualTo(2)
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
                assertThat(info0.mark).isEqualTo(1);
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
                assertThat(info1.mark).isEqualTo(0);
                assertThat(info1.mapNo).isEqualTo(0x0012);
            }
            it("should fail to read non-existing entry") {
                assertFailsWith<IndexOutOfBoundsException> {
                    reader.read(2)
                }
            }
        }

        given("an input that is a directory") {
            val path = Paths.get(Resources.getResource(".").path)
            it("should fail to instanciate") {
                assertFailsWith<IllegalArgumentException> {
                    InfoFileReader(path)
                }
            }
        }

        given("an input that is an invalid file") {
            val path = Paths.get(Resources.getResource("random.bin").path)
            it("should fail to read any entry") {
                assertFailsWith<IllegalStateException> {
                    InfoFileReader(path).read(0)
                }
            }
        }

    }

})