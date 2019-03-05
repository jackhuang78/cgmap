package com.jhuangyc.cgmap.graphics.deprecated

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertFailsWith

class GraphicsTest : Spek({
    describe("an GraphicInfo") {
        given("that is valid") {
            val info = GraphicInfo()
            it("should pass validation") {
                info.validate()
            }
        }
        given("that is invalid") {
            val info = GraphicInfo(
								graphicNo = -1)
            it("should fail validation") {
                assertFailsWith<IllegalStateException> {
                    info.validate()
                }
            }
        }
    }
})