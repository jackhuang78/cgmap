package com.jhuang78.cgmap.graphics

data class Info(
        val graphicNo : Int = 0,
        val address : Int = 0,
        val dataLength : Int = 0,
        val offsetX : Int = 0,
        val offsetY : Int = 0,
        val imageWidth : Int = 0,
        val imageHeight: Int = 0,
        val occupyEast: Int = 0,
        val occupySouth: Int = 0,
        val mark: Int = 0,
        val unknown: Long = 0L,
        val mapNo: Int = 0
)

fun Info.validate() : Info {
    fun checkNonNegative(name : String, value : Int) {
        check(value >= 0) {"Expect non-negative ${name}, but got ${value}"}
    }

    check(graphicNo >= 0) {"Expect non-negative graphicNo, but got ${graphicNo}."}
    check(address >= 0) {"Expect non-negative address, but got ${address}."}
    check(dataLength >= 0) {"Expect non-negative dataLength, but got ${dataLength}."}
    check(offsetX >= 0) {"Expect non-negative offsetX, but got ${offsetX}."}
    check(offsetY >= 0) {"Expect non-negative offsetY, but got ${offsetY}."}
    check(imageWidth >= 0) {"Expect non-negative imageWidth, but got ${imageWidth}."}
    check(imageHeight >= 0) {"Expect non-negative imageHeight, but got ${imageHeight}."}
    check(occupyEast >= 0) {"Expect non-negative occupyEast, but got ${occupyEast}."}
    check(occupySouth >= 0) {"Expect non-negative occupySouth, but got ${occupySouth}."}
    check(mark == 0 || mark == 1) {"Expect mark to be either 0 or 1, but got ${mark}."}
    check(mapNo >= 0) {"Expect non-negative mapNo, but got ${mapNo}."}

    return this
}