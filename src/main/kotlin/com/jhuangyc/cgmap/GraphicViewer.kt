package com.jhuangyc.cgmap

import com.jhuang78.cgmap.graphics.*
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Graphics
import java.awt.Graphics2D
import java.nio.file.Paths
import javax.swing.JFrame
import javax.swing.JPanel

fun main(args: Array<String>) {
    print("GraphicViewer")

    

    val infoFileReader = InfoFileReader(Paths.get("data/GraphicInfo_66.bin"))
    val dataFileReader = DataFileReader(Paths.get("data/Graphic_66.bin"))
    val paletFileReader = PaletFileReader(Paths.get("data/pal"))

    val info = infoFileReader.read(123)
    val data = dataFileReader.read(info.address, info.dataLength)
    val palet = paletFileReader.read(Paths.get("palet_00.cgp"))

    val panel = object: JPanel() {
        override fun paint(g: Graphics?) {
            paintGraphic(g as Graphics2D, info, data, palet, zoom=2)
        }

        override fun getPreferredSize(): Dimension {
            return Dimension(640, 400)
        }
    }
    val frame = JFrame("GraphicViewer")
    frame.add(panel, BorderLayout.CENTER)
    frame.pack()
    frame.isVisible = true
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE






}

