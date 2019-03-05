package com.jhuangyc.cgmap.io

import com.jhuangyc.cgmap.entity.PaintedGraphic
import java.nio.file.Path
import javax.imageio.ImageIO

@Deprecated("Replace with BufferedImage.saveTo(file)")
fun writePaintedGraphic(file: Path, paintedGraphic: PaintedGraphic) {
	ImageIO.write(paintedGraphic.image, "png", file.toFile())
}

