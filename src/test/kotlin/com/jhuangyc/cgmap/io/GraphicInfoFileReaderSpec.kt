package com.jhuangyc.cgmap.io

import com.jhuangyc.cgmap.entity.GraphicInfo
import com.jhuangyc.cgmap.util.fromResources
import org.spekframework.spek2.Spek
import strikt.api.expectCatching
import strikt.api.expectThat
import strikt.assertions.failed
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import java.io.IOException
import java.nio.file.Paths

object GraphicInfoFileReaderSpec : Spek({

	group("Given a file containing 2 entries") {
		val validFile = Paths.get("GraphicInfo_66_small.bin").fromResources()
		val reader = GraphicInfoFileReader(validFile)

		test("Should know the correct number of entries") {
			expectThat(reader.numEntries).isEqualTo(2)
		}

		test("Should read the first entry") {
			val info = reader.read(0)

			expectThat(info) {
				get { graphicNo }.isEqualTo(0)
				get { address }.isEqualTo(0x0000)
				get { dataLength }.isEqualTo(0x01a8)
				get { offsetX }.isEqualTo(-32)
				get { offsetY }.isEqualTo(-24)
				get { imageWidth }.isEqualTo(64)
				get { imageHeight }.isEqualTo(47)
				get { occupyEast }.isEqualTo(1)
				get { occupySouth }.isEqualTo(1)
				get { mapMarker }.isEqualTo(GraphicInfo.MapMarker.FLOOR)
				get { graphicId }.isEqualTo(0x03e7)
			}
		}

		test("Should read the second entry") {
			val info = reader.read(1)

			expectThat(info) {
				get { graphicNo }.isEqualTo(1)
				get { address }.isEqualTo(0x01a8)
				get { dataLength }.isEqualTo(0x0012)
				get { offsetX }.isEqualTo(-32)
				get { offsetY }.isEqualTo(-24)
				get { imageWidth }.isEqualTo(64)
				get { imageHeight }.isEqualTo(48)
				get { occupyEast }.isEqualTo(1)
				get { occupySouth }.isEqualTo(1)
				get { mapMarker }.isEqualTo(GraphicInfo.MapMarker.OBSTACLE)
				get { graphicId }.isEqualTo(0x0012)
			}
		}

		test("Should fail to read the third entry") {
			expectCatching { reader.read(2) }
				.failed()
				.isA<IOException>()
		}

	}

	test("Should fail to read an invalid input file") {
		val invalidFile = Paths.get("random.bin").fromResources()
		val reader = GraphicInfoFileReader(invalidFile)

		expectCatching { reader.read(0) }
			.failed()
			.isA<IllegalStateException>()
	}

	test("Should fail to read a directory") {
		val invalidFile = Paths.get(".").fromResources()
		val reader = GraphicInfoFileReader(invalidFile)

		expectCatching { reader.read(0) }
			.failed()
			.isA<IOException>()
	}
})