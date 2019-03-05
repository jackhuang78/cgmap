package com.jhuangyc.cgmap.io

import com.jhuangyc.cgmap.util.asIterable
import com.jhuangyc.cgmap.util.bits
import com.jhuangyc.cgmap.util.toUint
import java.nio.ByteBuffer

/**
 * A class to decode the data portion of a Graphic into color points in the Palet.
 */
class GraphicDataDecoder(val data: ByteBuffer) : Iterable<Int?> {

	/**
	 * Identifier for the different types of section
	 */
	enum class Section {

		/**
		 * Section with starting byte = 0, 1, 2, or 3.
		 */
		S0,

		/**
		 * Section with starting byte = 8, 9, A, or B.
		 */
		S2,

		/**
		 * Section with starting byte = C, D, E, or F.
		 */
		S3,
	}

	/**
	 * The state of the decoder FSN
	 */
	data class State(
			/**
			 * The type of current section
			 */
			val section: Section? = null,

			/**
			 * The starting index of this section
			 */
			val startIdx: Int = 0,

			/**
			 * The number of iterations spent in the current section
			 */
			val iteration: Int = 0,

			/**
			 * Number of bytes used to specify the number of colors
			 */
			val numColorSize: Int = 0,

			/**
			 * Number of colors contained in this section
			 */
			val numColors: Int = 0)

	override fun iterator(): Iterator<Int?> {
		return object : Iterator<Int?> {

			// Initialize state
			var state = State(section = null, startIdx = 0)

			override fun hasNext(): Boolean {
				return (state.startIdx < data.capacity())
			}

			override fun next(): Int? {
				check(state.startIdx < data.capacity())
				val (output, nextState) = processState(state)
				state = nextState
				return output
			}
		}
	}

	private fun processState(state: State): Pair<Int?, State> {
		if (state.section == null) {
			val (section, numColorSize, numColors) = parseHeader(state)
			return processState(state.copy(
					section = section,
					numColorSize = numColorSize,
					numColors = numColors,
					iteration = 0))
		}

		return Pair(getColor(state), getNextState(state))
	}

	private fun parseHeader(state: State): Triple<Section, Int, Int> {
		// Up to the first four bytes are the section header
		// Using MutableList as a queue

		val byteIterator = data.asIterable(start = state.startIdx).iterator()

		// Extract info from byte 0
		val byte0 = byteIterator.next()
		val sectionName = byte0.bits(7, 6)
		val numColorSize = byte0.bits(5, 4)
		var numColors = byte0.bits(3, 0)

		val section = when (sectionName) {
			0 -> Section.S0
			2 -> Section.S2
			3 -> Section.S3
			else -> error("Invalid section preferredName ${sectionName}")
		}

		// When in Section 2, skip byte 1 is not part of color size
		if (section == Section.S2) {
			byteIterator.next()
		}

		// Calculate number of colors in this section
		check(numColorSize < 3)
		for (i in 0 until numColorSize) {
			numColors = (numColors shl 8) + byteIterator.next().toUint()
		}

		return Triple(section, numColorSize, numColors)
	}

	private fun getColor(state: State): Int? {
		return when (state.section!!) {
			Section.S0 -> data[state.startIdx + 1 + state.numColorSize + state.iteration].toUint()
			Section.S2 -> data[state.startIdx + 1].toUint()
			Section.S3 -> null
		}
	}

	private fun getNextState(state: State): State {
		checkNotNull(state.section)

		if (state.iteration + 1 < state.numColors) {
			return state.copy(iteration = state.iteration + 1)
		}

		val nextStartIdx = when (state.section) {
			Section.S0 -> state.startIdx + 1 + state.numColorSize + state.numColors
			Section.S2 -> state.startIdx + 1 + 1 + state.numColorSize
			Section.S3 -> state.startIdx + 1 + state.numColorSize
		}

		return State(section = null, startIdx = nextStartIdx)
	}
}