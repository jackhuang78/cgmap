package com.jhuang78.cgmap.io

import com.google.common.truth.Truth.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

class MapNavigatorTest : Spek({

	describe("MapNavigator()") {

		data class Case(
				val center: Int,
				val upLeft: Int,
				val upRight: Int,
				val downRight: Int,
				val downLeft: Int
		)

		describe("3x4 map") {
			val navigator = MapNavigator(
					location = 0,
					mapEastLength = 4,
					mapSouthLength = 3)

			listOf(
					Case(center = 0,
							upLeft = 0, upRight = 1, downRight = 4, downLeft = 0),
					Case(center = 1,
							upLeft = 1, upRight = 2, downRight = 5, downLeft = 0),
					Case(center = 2,
							upLeft = 2, upRight = 3, downRight = 6, downLeft = 1),
					Case(center = 3,
							upLeft = 3, upRight = 3, downRight = 7, downLeft = 2),
					Case(center = 4,
							upLeft = 0, upRight = 5, downRight = 8, downLeft = 4),
					Case(center = 5,
							upLeft = 1, upRight = 6, downRight = 9, downLeft = 4),
					Case(center = 6,
							upLeft = 2, upRight = 7, downRight = 10, downLeft = 5),
					Case(center = 7,
							upLeft = 3, upRight = 7, downRight = 11, downLeft = 6),
					Case(center = 8,
							upLeft = 4, upRight = 9, downRight = 8, downLeft = 8),
					Case(center = 9,
							upLeft = 5, upRight = 10, downRight = 9, downLeft = 8),
					Case(center = 10,
							upLeft = 6, upRight = 11, downRight = 10, downLeft = 9),
					Case(center = 11,
							upLeft = 7, upRight = 11, downRight = 11, downLeft = 10)

			).forEach {
				describe("${it}") {

					it("should move to up left correctly") {
						assertThat(
								navigator.copy(location = it.center).tryMoveUpLeft().location)
								.isEqualTo(it.upLeft)
					}
					it("should move to up right correctly") {
						assertThat(
								navigator.copy(location = it.center).tryMoveUpRight().location)
								.isEqualTo(it.upRight)
					}
					it("should move to down right correctly") {
						assertThat(navigator.copy(
								location = it.center).tryMoveDownRight().location)
								.isEqualTo(it.downRight)
					}
					it("should move to down left correctly") {
						assertThat(
								navigator.copy(location = it.center).tryMoveDownLeft().location)
								.isEqualTo(it.downLeft)
					}
				}
			}
		}
	}


})