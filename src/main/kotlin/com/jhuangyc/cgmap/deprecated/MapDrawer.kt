//package com.jhuangyc.cgmap
//
//import com.jhuang78.cgmap.graphics.*
//import java.awt.Graphics2D
//import java.awt.image.BufferedImage
//import java.nio.file.Paths
//import javax.imageio.ImageIO
//
//fun main(args: Array<String>) {
//    val graphicId = "30011"
//
//    val infoFileReader = InfoFileReader(Paths.get("data/GraphicInfo_66.bin"))
//    val dataFileReader = DataFileReader(Paths.get("data/Graphic_66.bin"))
//    val paletFileReader = PaletFileReader(Paths.get("data/pal"))
//    val mapFileReader = MapFileReader(Paths.get("data/map/0/${graphicId}.dat"))
//
//    val width = mapFileReader.width
//    val height = mapFileReader.height
//    println("${width}, ${height}")
//    val side = Math.max(width, height) + 1
//    val image = BufferedImage(side*64, side*47, BufferedImage.TYPE_INT_ARGB)
//    val mapView = mapFileReader.readView(0, 0, width, height, infoFileReader)
//    var palet = paletFileReader.read(Paths.get("palet_00.cgp"))
//    paintMap(image.createGraphics(), mapView, dataFileReader, palet, zoom = 1.0)
//    ImageIO.write(image, "png", Paths.get("${graphicId}.png").toFile())
//
//
//}