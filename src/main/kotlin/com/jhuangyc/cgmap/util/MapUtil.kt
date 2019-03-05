package com.jhuangyc.cgmap.util

import java.awt.Dimension
import java.awt.Point

fun Point.east() = this.x
fun Point.south() = this.y

fun Dimension.eastLength() = this.width
fun Dimension.southLength() = this.height


fun Point.minus(p: Point) = Point(this.x - p.x, this.y - p.y)