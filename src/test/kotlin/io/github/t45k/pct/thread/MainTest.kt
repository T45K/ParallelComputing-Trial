package io.github.t45k.pct.thread

import kotlin.test.Test

internal class MainTest {
    @Test
    fun test1() {
        val main = Main()
        main.iterative()
    }

    @Test
    fun test2() {
        val main = Main()
        main.parallel()
    }

    @Test
    fun test3() {
        val main = Main()
        main.checkHowGetThread()
    }
}