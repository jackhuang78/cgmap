package com.jhuang78.cgmap.graphics

import java.awt.Graphics2D

fun paintGraphic(g: Graphics2D, info: GraphicInfo, data: GraphicData, palet: GraphicPalet, zoom: Int = 1) {
    val decoder = if(data.isCompressed())
        CompressedDataDecoder(data.data)
    else
        UncompressedDataDecoder(data.data)

    var dx = 0
    var dy = 0
    for(colorPoint in decoder) {
        g.color = palet.get(colorPoint)
        g.fillRect(dx * zoom, (info.imageHeight - dy) * zoom, zoom, zoom);
        dx++
        if(dx >= info.imageWidth) {
            dx = 0
            dy++
        }
    }
}
