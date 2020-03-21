package io.github.t45k.pct.future

import kotlin.test.Test

internal class FutureMainTest {
    @Test
    fun test1() {
        val main = FutureMain()
        main.singleFuture()
    }

    @Test
    fun test2() {
        val main = FutureMain()
        main.singleFutureWithNoReturnValue()
    }

    @Test
    fun test3() {
        val main = FutureMain()
        main.multiFuture()
    }

    @Test
    fun test4() {
        val main = FutureMain()
        main.variousThreadPool()
    }
}