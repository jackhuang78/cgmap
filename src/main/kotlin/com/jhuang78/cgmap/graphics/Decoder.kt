package com.jhuang78.cgmap.graphics

import com.google.common.base.Preconditions.checkState
import com.google.common.primitives.Ints
import com.jhuang78.cgmap.util.toUint
import java.nio.ByteBuffer

fun ByteBuffer.getDataByteAt(idx: Int): Byte {
  return if (idx < capacity()) get(idx) else 0
}

class UncompressedDataDecoder(val data: ByteBuffer) : Iterable<Int> {
  override fun iterator(): Iterator<Int> {
    return object : Iterator<Int> {
      var idx = 0
      override fun hasNext(): Boolean {
        return (idx < data.capacity())
      }

      override fun next(): Int {
        val v = data.get(idx).toUint()
        idx++
        return v
      }
    }
  }
}

class CompressedDataDecoder(val data: ByteBuffer) : Iterable<Int> {
  enum class State {
    SN, S0, S1, S2, S8, S9, SA, SC, SD, SE
  }

  data class Input(
      val state: State,
      val count: Int,
      val idx: Int,
      val colorPoint: Int,
      val b0: Byte,
      val b0h: Byte,
      val b0l: Byte,
      val b1: Byte,
      val b2: Byte,
      val b3: Byte
  )

  data class Output(
      val state: State,
      val idx: Int,
      val colorPoint: Int,
      val count: Int
  )

  override fun iterator(): Iterator<Int> {

    return object : Iterator<Int> {
      var input = Input(
          state = State.SN,
          count = 0,
          idx = 0,
          colorPoint = 0,
          b0 = 0,
          b0h = 0,
          b0l = 0,
          b1 = 0,
          b2 = 0,
          b3 = 0
      )
      var output = Output(
          state = State.SN,
          idx = 0,
          colorPoint = 0,
          count = 0
      )

      override fun hasNext(): Boolean {
        return (output.count > 0) || (output.idx < data.capacity())
      }

      override fun next(): Int {
        input = {
          val b0 = if (output.idx + 0 < data.capacity()) data.get(output.idx + 0) else 0
          Input(
              state = output.state,
              idx = output.idx,
              colorPoint = output.colorPoint,
              count = output.count,
              b0h = ((b0.toInt() shr 4) and 0x0F).toByte(),
              b0l = ((b0.toInt() shr 0) and 0x0F).toByte(),
              b0 = b0.toByte(),
              b1 = if (output.idx + 1 < data.capacity()) data.get(output.idx + 1) else 0,
              b2 = if (output.idx + 2 < data.capacity()) data.get(output.idx + 2) else 0,
              b3 = if (output.idx + 3 < data.capacity()) data.get(output.idx + 3) else 0
          )
        }()

        output = when (input.state) {
          State.SN -> {
            checkState(input.count == 0, "Expect count = 0 when in ${input.state}, but got ${input.count}.")
            when (input.b0h) {
              0x0.toByte() -> {
                val count = Ints.fromBytes(0, 0, 0, input.b0l)
                val state = if (count == 1) State.SN else State.S0
                Output(state, input.idx + 2, input.b1.toUint(), count - 1)
              }
              0x1.toByte() -> {
                val count = Ints.fromBytes(0, 0, input.b0l, input.b1)
                val state = if (count == 1) State.SN else State.S1
                Output(state, input.idx + 3, input.b2.toUint(), count - 1)
              }
              0x2.toByte() -> {
                val count = Ints.fromBytes(0, input.b0l, input.b1, input.b2)
                val state = if (count == 1) State.SN else State.S2
                Output(state, input.idx + 4, input.b3.toUint(), count - 1)
              }
              0x8.toByte() -> {
                val count = Ints.fromBytes(0, 0, 0, input.b0l)
                val state = if (count == 1) State.SN else State.S8
                Output(state, input.idx + 2, input.b1.toUint(), count - 1)
              }
              0x9.toByte() -> {
                val count = Ints.fromBytes(0, 0, input.b0l, input.b2)
                val state = if (count == 1) State.SN else State.S9
                Output(state, input.idx + 3, input.b1.toUint(), count - 1)
              }
              0xA.toByte() -> {
                val count = Ints.fromBytes(0, input.b0l, input.b2, input.b3)
                val state = if (count == 1) State.SN else State.SA
                Output(state, input.idx + 4, input.b1.toUint(), count - 1)
              }
              0xC.toByte() -> {
                val count = Ints.fromBytes(0, 0, 0, input.b0l)
                val state = if (count == 1) State.SN else State.SC
                Output(state, input.idx + 1, -1, count - 1)
              }
              0xD.toByte() -> {
                val count = Ints.fromBytes(0, 0, input.b0l, input.b1)
                val state = if (count == 1) State.SN else State.SD
                Output(state, input.idx + 2, -1, count - 1)
              }
              0xE.toByte() -> {
                val count = Ints.fromBytes(0, input.b0l, input.b1, input.b2)
                val state = if (count == 1) State.SN else State.SE
                Output(state, input.idx + 3, -1, count - 1)
              }
              else -> {
                throw IllegalStateException("Unexpected high nibble of b0: ${input.b0h}.")
              }
            }
          }

          in State.S0..State.S2 -> Output(
              state = if (input.count > 1) input.state else State.SN,
              idx = input.idx + 1,
              colorPoint = data.get(input.idx).toUint(),
              count = input.count - 1
          )

          in State.S8..State.SE -> Output(
              state = if (input.count > 1) input.state else State.SN,
              idx = input.idx,
              colorPoint = input.colorPoint,
              count = input.count - 1
          )

          else -> throw IllegalStateException("Unexpected state ${input.state}.")
        }

        return output.colorPoint
      }
    }
  }
}