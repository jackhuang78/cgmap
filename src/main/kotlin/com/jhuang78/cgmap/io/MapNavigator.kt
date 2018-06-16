package com.jhuang78.cgmap.io

data class MapNavigator(
		var location: Int,
		val mapEastLength: Int,
		val mapSouthLength: Int) {

	fun atUpperLeftEdge() =
			(location < mapEastLength)

	fun atLowerLeftEdge() =
			((location % mapEastLength) == 0)

	fun atUpperRightEdge() =
			(((location + 1) % mapEastLength) == 0)

	fun atLowerRightEdge() =
			(location >= (mapEastLength * (mapSouthLength - 1)))


	fun moveUpLeft() =
			if (atUpperLeftEdge()) {
				false
			} else {
				location = location - mapEastLength
				true
			}

	fun moveUpRight() =
			if (atUpperRightEdge()) {
				false
			} else {
				location = location + 1
				true
			}


	fun moveDownLeft() =
			if (atLowerLeftEdge()) {
				false
			} else {
				location = location - 1
				true
			}

	fun moveDownRight() =
			if (atLowerRightEdge()) {
				false
			} else {
				location = location + mapEastLength
				true
			}

	fun tryMoveUpLeft(): MapNavigator {
		moveUpLeft()
		return this
	}

	fun tryMoveUpRight(): MapNavigator {
		moveUpRight()
		return this
	}

	fun tryMoveDownRight(): MapNavigator {
		moveDownRight()
		return this
	}

	fun tryMoveDownLeft(): MapNavigator {
		moveDownLeft()
		return this
	}
}