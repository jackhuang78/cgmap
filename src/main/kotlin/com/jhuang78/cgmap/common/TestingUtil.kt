package com.jhuang78.cgmap.common

import com.google.common.io.Resources
import java.nio.file.Paths

fun getPathForResource(resource: String) =
		Paths.get(Resources.getResource(resource).path)
