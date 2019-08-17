package com.jhuangyc.cgmap.graphics.deprecated

import org.spekframework.spek2.Spek
import strikt.api.expectCatching
import strikt.assertions.failed
import strikt.assertions.isA
import strikt.assertions.succeeded

object GraphicsSpec : Spek({
	test("A valid GraphicInfo should pass validation") {
		val validInfo = GraphicInfo()

		expectCatching { validInfo.validate() }
			.succeeded()
	}

	test("An invalid GraphicInfo should fail validation") {
		val invalidInfo = GraphicInfo(graphicNo = -1)

		expectCatching { invalidInfo.validate() }
			.failed()
			.isA<IllegalStateException>()
	}
})
