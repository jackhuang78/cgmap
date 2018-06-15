package com.jhuang78.cgmap.io

import java.nio.file.Path
import javax.imageio.ImageIO

fun writePaintedGraphic(file: Path, paintedGraphic: PaintedGraphic) {
	ImageIO.write(paintedGraphic.image, "png", file.toFile())
}

