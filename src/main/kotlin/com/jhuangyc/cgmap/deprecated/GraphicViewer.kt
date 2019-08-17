//package com.jhuangyc.cgmap
//
//import com.jhuang78.cgmap.graphics.*
//import java.awt.*
//import java.nio.file.Path
//import java.nio.file.Paths
//import javax.swing.*
//
//fun main(args: Array<String>) {
//    print("GraphicViewer")
//
//    val infoFileReader = InfoFileReader(Paths.get("data/GraphicInfo_66.bin"))
//    val dataFileReader = DataFileReader(Paths.get("data/Graphic_66.bin"))
//    val paletFileReader = PaletFileReader(Paths.get("data/pal"))
//
//    var info = infoFileReader.read(0)
//    var data = dataFileReader.read(info.address, info.dataLength)
//    var palet = paletFileReader.read(Paths.get("palet_00.cgp"))
//
//    val infoTextArea = JTextArea()
//    infoTextArea.isEditable = false
//    infoTextArea.lineWrap = true
//    infoTextArea.columns = 20
//    val infoPanel = JPanel(BorderLayout())
//    infoPanel.add(infoTextArea)
//
//    val imagePanel = object: JPanel() {
//        override fun paint(g: GraphicCommand?) {
//            super.paint(g)
//            infoTextArea.text = "${info}\n\n${data}".replace(",", "\n")
//            paintGraphic(g as Graphics2D, info, data, palet, zoom=2.0)
//        }
//    }
//    imagePanel.preferredSize = Dimension(640, 480)
//
//    val graphicSelectSpinner = JSpinner(SpinnerNumberModel(0, 0, infoFileReader.numEntries - 1, 1))
//    graphicSelectSpinner.addChangeListener {
//        info = infoFileReader.read((it.source as JSpinner).value as Int)
//        data = dataFileReader.read(info.address, info.dataLength)
//        imagePanel.repaint()
//    }
//    val paletSelectSpinner = JSpinner(SpinnerListModel(paletFileReader.palets()))
//    paletSelectSpinner.addChangeListener {
//        palet = paletFileReader.read((it.source as JSpinner).value as Path)
//        imagePanel.repaint()
//    }
//
//
//    val controlPanel = JPanel(GridLayout(1, 3, 5, 5))
//    controlPanel.add(graphicSelectSpinner)
//    controlPanel.add(paletSelectSpinner)
//
//
//
//    val frame = JFrame("GraphicViewer")
//    frame.add(imagePanel, BorderLayout.CENTER)
//    frame.add(controlPanel, BorderLayout.SOUTH)
//    frame.add(infoPanel, BorderLayout.EAST)
//    frame.pack()
//    frame.isVisible = true
//    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
//
//
//
//
//
//
//}
//
