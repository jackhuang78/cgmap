package com.jhuangyc.cgmap.entity

class MapView(
		val viewWidth: Int,
		val viewHeight: Int
) {

	fun tilesCenteredAt(viewCenter: MapLocation) = sequence {

		val viewOrigin = viewCenter.copy()
				.moveSouthEast(-viewWidth / 2)
				.moveSouthWest(-viewHeight / 2)
				.moveSouth(-1)

		val rowCursor = viewOrigin.copy()
		var colCursor: MapLocation

		repeat(viewHeight) {
			colCursor = rowCursor.copy()
			repeat(viewWidth + 1) {
				yield(colCursor.copy())
				colCursor.moveSouthEast(1)
			}
			rowCursor.moveSouth(1)

			colCursor = rowCursor.copy()
			repeat(viewWidth) {
				yield(colCursor.copy())
				colCursor.moveSouthEast(1)
			}
			rowCursor.moveEast(-1)
		}

		colCursor = rowCursor.copy()
		repeat(viewWidth + 1) {
			yield(colCursor.copy())
			colCursor.moveSouthEast(1)
		}
		rowCursor.moveSouth(1)
	}
}
