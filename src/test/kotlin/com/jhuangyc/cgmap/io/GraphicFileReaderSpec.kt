package com.jhuangyc.cgmap.io

import com.google.common.truth.Truth.assertThat
import com.jhuangyc.cgmap.entity.Graphic
import com.jhuangyc.cgmap.util.fromResources
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import java.io.IOException
import java.nio.file.Paths
import kotlin.test.assertFailsWith

object GraphicFileReaderSpec : Spek({

	given("a reader with valid file") {
		val validGraphicFile = Paths.get("Graphic_66_0_424.bin").fromResources()
		val reader = GraphicFileReader(validGraphicFile)

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
			val thrown = assertFailsWith<Throwable> {
				reader.read(424, 1)
			}
			it("should fail with IOException") {
				assertThat(thrown).isInstanceOf(IOException::class.java)
			}
		}

	}
})