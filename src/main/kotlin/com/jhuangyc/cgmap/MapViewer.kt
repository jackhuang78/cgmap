package com.jhuangyc.cgmap

import com.jhuang78.cgmap.graphics.*
import java.awt.*
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.nio.file.Paths
import javax.swing.*

fun main(args: Array<String>) {
    val infoFileReader = InfoFileReader(Paths.get("data/GraphicInfo_66.bin"))
    val dataFileReader = DataFileReader(Paths.get("data/Graphic_66.bin"))
    val paletFileReader = PaletFileReader(Paths.get("data/pal"))
    val mapFileReader = MapFileReader(Paths.get("data/map/0/100.dat"))

    var east = mapFileReader.width / 2
    var south = mapFileReader.height / 2
    var width = 13 * 2 + 1
    var height = 13 * 2 + 1
    var palet = paletFileReader.read(Paths.get("palet_00.cgp"))
    var grid = false
    var tileNo = false
    var zoom = 1.0

    val imagePanel = object : JPanel() {
        override fun paint(g: Graphics?) {
            super.paint(g)
            val mapView = mapFileReader.readView(east, south, width, height, infoFileReader)
            paintMap(g as Graphics2D, mapView, dataFileReader, palet, grid = grid, tileNo = tileNo, zoom = zoom)
        }
    }
    imagePanel.preferredSize = Dimension(800, 700)
    imagePanel.isFocusable = true
    imagePanel.requestFocusInWindow()
    imagePanel.addKeyListener(object : KeyAdapter() {
        override fun keyPressed(e: KeyEvent) {
            when (e.keyCode) {
                KeyEvent.VK_UP -> {
                    east = minOf(east + 1, mapFileReader.width - width)
                    south = maxOf(south - 1, 0)
                }
                KeyEvent.VK_RIGHT -> {
                    east = minOf(east + 1, mapFileReader.width - width)
                    south = minOf(south + 1, mapFileReader.height - height)
                }
                KeyEvent.VK_DOWN -> {
                    east = maxOf(east - 1, 0)
                    south = minOf(south + 1, mapFileReader.height - height)
                }
                KeyEvent.VK_LEFT -> {
                    east = maxOf(east - 1, 0)
                    south = maxOf(south - 1, 0)
                }
            }
            imagePanel.repaint()
        }
    })
    imagePanel.addMouseListener(object : MouseAdapter() {
        override fun mouseClicked(e: MouseEvent) {
            imagePanel.requestFocusInWindow()
        }
    })

    val gridCheckBox = JCheckBox("Grid")
    gridCheckBox.addActionListener {
        grid = gridCheckBox.isSelected
        imagePanel.repaint()
    }
    val tileNoCheckBox = JCheckBox("TileNo")
    tileNoCheckBox.addActionListener() {
        tileNo = tileNoCheckBox.isSelected
        imagePanel.repaint()
    }
    val zoomSpinner = JSpinner(SpinnerNumberModel(1.0, 0.0, 2.0, 0.1))
    zoomSpinner.addChangeListener {
        zoom = zoomSpinner.value as Double
        imagePanel.repaint()
    }
    val widthSpinner = JSpinner(SpinnerNumberModel(11, 3, 1000, 1))
    widthSpinner.addChangeListener {
        width = widthSpinner.value as Int
        height = widthSpinner.value as Int
        imagePanel.repaint()
    }
    val controlPanel = JPanel(GridLayout(2, 4, 5, 5))
    controlPanel.add(gridCheckBox)
    controlPanel.add(tileNoCheckBox)
    controlPanel.add(zoomSpinner)
    controlPanel.add(widthSpinner)


    val frame = JFrame("MapViewer")
    frame.add(imagePanel, BorderLayout.CENTER)
    frame.add(controlPanel, BorderLayout.SOUTH)
    frame.pack()
    frame.isVisible = true
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
}