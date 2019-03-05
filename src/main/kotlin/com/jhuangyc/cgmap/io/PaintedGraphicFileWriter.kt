package com.jhuangyc.cgmap.io

import com.jhuangyc.cgmap.entity.PaintedGraphic
import java.nio.file.Path
import javax.imageio.ImageIO

fun writePaintedGraphic(file: Path, paintedGraphic: PaintedGraphic) {
	ImageIO.write(paintedGraphic.image, "png", file.toFile())
}

