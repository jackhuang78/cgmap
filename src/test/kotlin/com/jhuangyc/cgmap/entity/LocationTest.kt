package com.jhuangyc.cgmap.entity
//
//import com.google.common.truth.Truth.assertThat
//import org.jetbrains.spek.api.Spek
//import org.jetbrains.spek.api.dsl.describe
//import org.jetbrains.spek.api.dsl.it
//import org.jetbrains.spek.api.dsl.xdescribe
//
//// TODO: clean up this class
//class LocationTest : Spek({
//
//	data class Case(
//			val initialEast: Int = 0,
//			val initialSouth: Int = 0,
//			val eastLength: Int = 9,
//			val southLength: Int = 9,
//			val delta: Int = 0,
//			val expectedEast: Int = initialEast,
//			val expectedSouth: Int = initialSouth,
//			val expectedTileNumber: Int? = 0
//	)
//
//	xdescribe("MapLocation") {
//		describe("moveEast()") {
//			listOf(
//					Case(delta = 0, expectedEast = 0, expectedTileNumber = 0),
//					Case(delta = 1, expectedEast = 1, expectedTileNumber = 1),
//					Case(delta = -1, expectedEast = -1, expectedTileNumber = null),
//					Case(eastLength = 9, delta = 8, expectedEast = 8,
//							expectedTileNumber = 8),
//					Case(eastLength = 9, delta = 9, expectedEast = 9,
//							expectedTileNumber = null)
//			).forEach {
//				it("${it}") {
//					val location = MapLocation(east = it.initialEast,
//							south = it.initialSouth)
//					location.moveEast(it.delta)
//					assertThat(location.east).isEqualTo(it.expectedEast)
//					assertThat(location.south).isEqualTo(it.expectedSouth)
//					assertThat(
//							location.getTileNumber(
//									MapSize(it.eastLength, it.southLength))).isEqualTo(
//							it.expectedTileNumber)
//				}
//			}
//		}
//		describe("moveSouth()") {
//			listOf(
//					Case(delta = 0, expectedSouth = 0, expectedTileNumber = 0),
//					Case(eastLength = 9, delta = 1, expectedSouth = 1,
//							expectedTileNumber = 9),
//					Case(eastLength = 9, delta = -1, expectedSouth = -1,
//							expectedTileNumber = null),
//					Case(eastLength = 9, southLength = 9, delta = 8, expectedSouth = 8,
//							expectedTileNumber = 72),
//					Case(eastLength = 9, southLength = 9, delta = 9, expectedSouth = 9,
//							expectedTileNumber = null)
//			).forEach {
//				it("${it}") {
//					val location = MapLocation(east = it.initialEast,
//							south = it.initialSouth)
//					location.moveSouth(it.delta)
//					assertThat(location.east).isEqualTo(it.expectedEast)
//					assertThat(location.south).isEqualTo(it.expectedSouth)
//					assertThat(
//							location.getTileNumber(
//									MapSize(it.eastLength, it.southLength))).isEqualTo(
//							it.expectedTileNumber)
//				}
//			}
//		}
//	}
//})