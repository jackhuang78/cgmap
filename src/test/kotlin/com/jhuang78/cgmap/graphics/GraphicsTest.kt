package com.jhuang78.cgmap.graphics

import com.google.common.truth.Truth.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import kotlin.test.assertFailsWith

class GraphicsTest : Spek({
    describe("Info") {
        describe("an valid Info object") {
            val info = Info()
            it("passes validation") {
                info.validate()
            }
        }
        describe("An invalid Info object") {
            val info = Info(graphicNo=-1)
            it("fails validation") {
                assertFailsWith<IllegalStateException> {
                    info.validate()
                }
            }
        }
    }

})