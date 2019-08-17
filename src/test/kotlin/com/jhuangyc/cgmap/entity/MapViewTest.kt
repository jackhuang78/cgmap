package com.jhuangyc.cgmap.entity
//
//import com.google.common.truth.Truth.assertThat
//import org.jetbrains.spek.api.Spek
//import org.jetbrains.spek.api.dsl.describe
//import org.jetbrains.spek.api.dsl.it
//
//class MapViewTest : Spek({
//	describe("MapView") {
//		describe("tilesCenteredAt()") {
//			it("should produce the correct tile sequence") {
//				val tileItor = MapView(5, 5).tilesCenteredAt(
//						MapLocation(0, 0)).iterator()
//
//				assertThat(tileItor.next()).isEqualTo(MapLocation(0, -5))
//				assertThat(tileItor.next()).isEqualTo(MapLocation(1, -4))
//				assertThat(tileItor.next()).isEqualTo(MapLocation(2, -3))
//				assertThat(tileItor.next()).isEqualTo(MapLocation(3, -2))
//				assertThat(tileItor.next()).isEqualTo(MapLocation(4, -1))
//				assertThat(tileItor.next()).isEqualTo(MapLocation(5, 0))
//
//				assertThat(tileItor.next()).isEqualTo(MapLocation(0, -4))
//				assertThat(tileItor.next()).isEqualTo(MapLocation(1, -3))
//				assertThat(tileItor.next()).isEqualTo(MapLocation(2, -2))
//				assertThat(tileItor.next()).isEqualTo(MapLocation(3, -1))
//				assertThat(tileItor.next()).isEqualTo(MapLocation(4, -0))
//
//				assertThat(tileItor.next()).isEqualTo(MapLocation(-1, -4))
//				assertThat(tileItor.next()).isEqualTo(MapLocation(0, -3))
//				assertThat(tileItor.next()).isEqualTo(MapLocation(1, -2))
//				assertThat(tileItor.next()).isEqualTo(MapLocation(2, -1))
//				assertThat(tileItor.next()).isEqualTo(MapLocation(3, -0))
//				assertThat(tileItor.next()).isEqualTo(MapLocation(4, 1))
//
//				repeat(5) { tileItor.next()}
//				repeat(6) { tileItor.next()}
//				repeat(5) { tileItor.next()}
//				repeat(6) { tileItor.next()}
//				repeat(5) { tileItor.next()}
//				repeat(6) { tileItor.next()}
//
//				assertThat(tileItor.next()).isEqualTo(MapLocation(-4, 0))
//				assertThat(tileItor.next()).isEqualTo(MapLocation(-3, 1))
//				assertThat(tileItor.next()).isEqualTo(MapLocation(-2, 2))
//				assertThat(tileItor.next()).isEqualTo(MapLocation(-1, 3))
//				assertThat(tileItor.next()).isEqualTo(MapLocation(0, 4))
//
//				assertThat(tileItor.next()).isEqualTo(MapLocation(-5, 0))
//				assertThat(tileItor.next()).isEqualTo(MapLocation(-4, 1))
//				assertThat(tileItor.next()).isEqualTo(MapLocation(-3, 2))
//				assertThat(tileItor.next()).isEqualTo(MapLocation(-2, 3))
//				assertThat(tileItor.next()).isEqualTo(MapLocation(-1, 4))
//				assertThat(tileItor.next()).isEqualTo(MapLocation(0, 5))
//
//				assertThat(tileItor.hasNext()).isFalse()
//			}
//		}
//	}
//})