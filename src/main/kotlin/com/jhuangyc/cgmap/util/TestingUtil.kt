package com.jhuangyc.cgmap.util

import com.google.common.io.Resources
import java.nio.file.Path
import java.nio.file.Paths

/**
 * Get the path under resource.
 */
fun Path.fromResources() = Paths.get(
		Resources.getResource(this.toString()).path)

