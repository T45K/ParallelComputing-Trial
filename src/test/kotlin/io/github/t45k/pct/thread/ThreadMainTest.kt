package io.github.t45k.pct.thread

import kotlin.test.Test

internal class ThreadMainTest {
    @Test
    fun test1() {
        val main = ThreadMain()
        main.iterative()
    }

    @Test
    fun test2() {
        val main = ThreadMain()
        main.parallel()
    }

    @Test
    fun test3() {
        val main = ThreadMain()
        main.checkHowGetThread()
    }
}