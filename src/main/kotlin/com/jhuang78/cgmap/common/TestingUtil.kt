package com.jhuang78.cgmap.common

import com.google.common.io.Resources
import java.nio.file.Path
import java.nio.file.Paths

/**
 * Get the path under resource.
 */
fun Path.underResource() = Paths.get(
		Resources.getResource(this.toString()).path)

