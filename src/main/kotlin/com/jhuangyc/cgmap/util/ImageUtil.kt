package com.jhuangyc.cgmap.util

import java.awt.Dimension
import java.awt.image.BufferedImage
import java.nio.file.Path
import javax.imageio.ImageIO

fun BufferedImage.dimension() = Dimension(this.width, this.height)

fun BufferedImage.saveTo(path: Path) =
		ImageIO.write(this, "png", path.toFile())