package com.jhuang78.cgmap.entity

import java.awt.Color

/**
 * Represent a Palet from its palet.cgp file.
 */
data class Palet(
		/**
		 * The colors contained in this Palet.
		 */
		val colors: List<Color>
)
