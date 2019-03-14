package com.jhuangyc.cgmap.io

import com.google.common.truth.Truth.assertThat
import com.jhuangyc.cgmap.entity.Graphic
import com.jhuangyc.cgmap.util.fromResources
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import java.io.IOException
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import java.nio.file.Paths
import kotlin.test.assertFails

object GraphicFileReaderSpec : Spek({

	given("a reader with valid file") {
		val validFile = Paths.get("Graphic_66_0_424.bin").fromResources()
		val reader = GraphicFileReader(validFile)

		on("read() an entry within file length") {
			val graphic = reader.read(0, 424)

			it("should read entry value correctly") {
				assertThat(graphic.version).isEqualTo(Graphic.Version.ENCODED)
				assertThat(graphic.width).isEqualTo(0x00000040)
				assertThat(graphic.height).isEqualTo(0x0000002f)
				assertThat(graphic.dataLength).isEqualTo(424)
			}
		}

		on("read() an entry beyond file length") {
			val thrown = assertFails {
				reader.read(424, 1)
			}
			it("should fail") {
				assertThat(thrown).isInstanceOf(IOException::class.java)
			}
		}
	}

	given("a reader with invalid file") {
		val invalidFile = Paths.get("random.bin").fromResources()
		val reader = GraphicFileReader(invalidFile)

		on("read()") {
			val thrown = assertFails {
				reader.read(0, 16)
			}

			it("should fail") {
				assertThat(thrown).isInstanceOf(IllegalStateException::class.java)
			}
		}
	}
})