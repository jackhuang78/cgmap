//package com.jhuang78.cgmap.graphics
//
//import com.google.common.io.Resources
//import com.google.common.truth.Truth.assertThat
//import org.jetbrains.spek.api.Spek
//import org.jetbrains.spek.api.dsl.*
//import java.awt.Color
//import java.nio.file.Path
//import java.nio.file.Paths
//import kotlin.test.assertFailsWith
//
//fun getResource(path: String): Path {
//    return Paths.get(Resources.getResource(path).path)
//}
//
//class FileReaderTest : Spek({
//
//    describe("an InfoFileReader") {
//        given("a valid input file") {
//            val path = getResource("GraphicInfo_66_small.bin")
//            val reader = InfoFileReader(path)
//            it("should know number of entries") {
//                assertThat(reader.size).isEqualTo(2)
//            }
//            it("should read valid entries") {
//                reader.read(0).validate()
//                reader.read(1).validate()
//            }
//            it("should read entries correctly") {
//                val info0 = reader.read(0);
//                assertThat(info0.graphicNo).isEqualTo(0);
//                assertThat(info0.address).isEqualTo(0x0000);
//                assertThat(info0.dataLength).isEqualTo(0x01a8);
//                assertThat(info0.offsetX).isEqualTo(-32);
//                assertThat(info0.offsetY).isEqualTo(-24);
//                assertThat(info0.imageWidth).isEqualTo(64);
//                assertThat(info0.imageHeight).isEqualTo(47);
//                assertThat(info0.occupyEast).isEqualTo(1);
//                assertThat(info0.occupySouth).isEqualTo(1);
//                assertThat(info0.mark).isEqualTo(1);
//                assertThat(info0.mapNo).isEqualTo(0x03e7);
//
//                val info1 = reader.read(1);
//                assertThat(info1.graphicNo).isEqualTo(1);
//                assertThat(info1.address).isEqualTo(0x01a8);
//                assertThat(info1.dataLength).isEqualTo(0x0012);
//                assertThat(info1.offsetX).isEqualTo(-32);
//                assertThat(info1.offsetY).isEqualTo(-24);
//                assertThat(info1.imageWidth).isEqualTo(64);
//                assertThat(info1.imageHeight).isEqualTo(48);
//                assertThat(info1.occupyEast).isEqualTo(1);
//                assertThat(info1.occupySouth).isEqualTo(1);
//                assertThat(info1.mark).isEqualTo(0);
//                assertThat(info1.mapNo).isEqualTo(0x0012);
//            }
//            it("should fail to read non-existing entry") {
//                assertFailsWith<IndexOutOfBoundsException> {
//                    reader.read(2)
//                }
//            }
//        }
//
//        given("an invalid input") {
//            arrayOf(".", "random.bin").forEach {
//                context("path=${it}") {
//                    it("should fail to read an entry") {
//                        assertFailsWith<Exception> {
//                            InfoFileReader(getResource(it)).read(0)
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    describe("an DataFileReader") {
//        given("an valid input file") {
//            val path = getResource("Graphic_66_0_424.bin")
//            val reader = DataFileReader(path)
//
//            it("should read entry correctly") {
//                val data = reader.read(0, 424)
//                data.validate()
//
//                assertThat(data.version).isEqualTo(1)
//                assertThat(data.width).isEqualTo(0x00000040)
//                assertThat(data.height).isEqualTo(0x0000002f)
//                assertThat(data.dataLength).isEqualTo(424)
//            }
//        }
//    }
//
//    describe("a PaletDirectoryReader") {
//        given("an valid directory") {
//            val dir = getResource("palet")
//            val reader = PaletFileReader(dir)
//
//            given("an valid file") {
//                val file = Paths.get("palet_00.cgp")
//                val palet = reader.read(file)
//                it("should read base colors") {
//                    assertThat(palet.colors[0]).isEqualTo(Color(0x00, 0x00, 0x00))
//                    assertThat(palet.colors[15]).isEqualTo(Color(0x28, 0xE1, 0x28))
//                    assertThat(palet.colors[240]).isEqualTo(Color(0x96, 0xC3, 0xF5))
//                    assertThat(palet.colors[255]).isEqualTo(Color(0xFF, 0xFF, 0xFF))
//                }
//                it("should read custom colors") {
//                    assertThat(palet.colors[16]).isEqualTo(Color(0xA4, 0xF7, 0x85))
//                    assertThat(palet.colors[17]).isEqualTo(Color(0x96, 0xE2, 0x7A))
//                    assertThat(palet.colors[32]).isEqualTo(Color(0xB9, 0x52, 0x41))
//                    assertThat(palet.colors[64]).isEqualTo(Color(0xBE, 0x8A, 0xEC))
//                    assertThat(palet.colors[128]).isEqualTo(Color(0xA0, 0xC8, 0xF6))
//                    assertThat(palet.colors[239]).isEqualTo(Color(0x92, 0x53, 0x4D))
//                }
//
//            }
//        }
//    }
//
//    describe("MapFileReader") {
//        given("an valid file") {
//            it("should read header correctly") {
//                val reader = MapFileReader(getResource("map/0/100.dat"))
//                assertThat(reader.width).isEqualTo(0x0348)
//                assertThat(reader.height).isEqualTo(0x0262)
//            }
//        }
//    }
//})