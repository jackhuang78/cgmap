package com.jhuang78.cgmap.graphics

import java.awt.Color
import java.awt.Graphics2D
import java.awt.Point


fun paintGraphic(g: Graphics2D, info: GraphicInfo, data: GraphicData, palet: GraphicPalet, zoom: Double = 1.0, origin: Point = Point(0, 0)) {
    val decoder = if (data.isCompressed())
        CompressedDataDecoder(data.data)
    else
        UncompressedDataDecoder(data.data)

    var dx = 0
    var dy = 0
    for (colorPoint in decoder) {
        g.color = palet.get(colorPoint)
        g.fillRect(
                Math.round((dx * zoom) + origin.x).toInt(),
                Math.round(((info.imageHeight - dy) * zoom) + origin.y).toInt(),
                Math.max(Math.round(zoom), 1).toInt(),
                Math.max(Math.round(zoom), 1).toInt());
        dx++
        if (dx >= info.imageWidth) {
            dx = 0
            dy++
        }

    }
}

fun paintMap(
        g: Graphics2D,
        view: MapView,
        dataFileReader: DataFileReader,
        palet: GraphicPalet,
        grid: Boolean = false,
        tileNo: Boolean = false,
        zoom: Double = 1.0) {

    var dx = 64
    var dy = 47 + view.width * 47 / 2
    var i = 0
    var j = 0

    for ((ground, item, mark, border, loc) in view.tiles) {


        val dxx = dx
        val dyy = dy
        dx -= 32
        dy -= 23

        if (ground.mapNo !in setOf(0, 999, 100)) {
            val data = dataFileReader.read(ground.address, ground.dataLength)
            paintGraphic(g, ground, data, palet,
                    origin = Point(
                            Math.round((dxx + ground.offsetX) * zoom).toInt(),
                            Math.round((dyy + ground.offsetY) * zoom).toInt()),
                    zoom = zoom)
        } else {
            g.color = Color.BLACK
            g.fillPolygon(
                    arrayOf(dx, dx + 32, dx + 64, dx + 32, dx).toIntArray(),
                    arrayOf(dy + 23, dy, dy + 23, dy + 47, dy + 23).toIntArray(),
                    5)
        }


        //g.drawString("${ground.graphicNo} (${loc.x},${loc.y})", dx+32, dy+23)

        dx += 64
        i++

        if (i >= view.width) {
            i = 0
            j++
            dx = 64 + (j * 64 / 2)
            dy = (47 + view.width * 47 / 2) + (j * 47 / 2)

        }
    }


    dx = 64
    dy = 47 + view.width * 47 / 2
    i = 0
    j = 0
    for ((ground, item, mark, border, loc) in view.tiles) {


        val dxx = dx
        val dyy = dy
        dx -= 32
        dy -= 23

        if (item.mapNo !in setOf(0, 999, 100)) {
            val data = dataFileReader.read(item.address, item.dataLength)
            paintGraphic(g, item, data, palet,
                    origin = Point(
                            Math.round((dxx + item.offsetX) * zoom).toInt(),
                            Math.round((dyy + item.offsetY) * zoom).toInt()),
                    zoom = zoom)
        }


        dx += 64
        i++

        if (i >= view.width) {
            i = 0
            j++
            dx = 64 + (j * 64 / 2)
            dy = (47 + view.width * 47 / 2) + (j * 47 / 2)

        }
    }


    dx = 64
    dy = 47 + view.width * 47 / 2
    i = 0
    j = 0
    for ((ground, item, mark, border, loc) in view.tiles) {


        val dxx = dx
        val dyy = dy
        dx -= 32
        dy -= 23

        if (grid) {
            g.color = if (border) Color.BLACK else Color.RED
            g.drawPolygon(
                    arrayOf(dx, dx + 32, dx + 64, dx + 32, dx).toIntArray(),
                    arrayOf(dy + 23, dy, dy + 23, dy + 47, dy + 23).toIntArray(),
                    5)
        }
        if (tileNo) {
            g.color = Color.BLACK
            g.drawString("${ground.graphicNo}", dx + 32, dy + 23)
        }


        dx += 64
        i++

        if (i >= view.width) {
            i = 0
            j++
            dx = 64 + (j * 64 / 2)
            dy = (47 + view.width * 47 / 2) + (j * 47 / 2)

        }
    }


}
