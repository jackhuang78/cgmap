package com.jhuangyc.cgmap.io

import com.jhuangyc.cgmap.entity.Graphic
import com.jhuangyc.cgmap.util.fromResources
import org.spekframework.spek2.Spek
import strikt.api.expectCatching
import strikt.api.expectThat
import strikt.assertions.failed
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import java.io.IOException
import java.nio.file.Paths

object GraphicFileReaderSpec : Spek({

	group("Given a valid input file") {
		val validFile = Paths.get("Graphic_66_0_424.bin").fromResources()
		val reader = GraphicFileReader(validFile)

		test("Should read an entry within the file length") {
			val graphic = reader.read(0, 424)

			expectThat(graphic) {
				get { version }.isEqualTo(Graphic.Version.ENCODED)
				get { width }.isEqualTo(0x00000040)
				get { height }.isEqualTo(0x0000002f)
				get { dataLength }.isEqualTo(424)
			}
		}

		test("Should fail to read an entry beyond the file length") {
			expectCatching { reader.read(424, 1) }
				.failed()
				.isA<IOException>()
		}
	}

	test("Should fail to read from an invalid input file") {
		val invalidFile = Paths.get("random.bin").fromResources()
		val reader = GraphicFileReader(invalidFile)

		expectCatching { reader.read(0, 16) }
			.failed()
			.isA<IllegalStateException>()
	}
})