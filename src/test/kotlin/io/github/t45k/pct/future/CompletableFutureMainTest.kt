package io.github.t45k.pct.future

import kotlin.test.Test


internal class CompletableFutureMainTest {
    @Test
    fun test1() {
        val main = CompletableFutureMain()
        main.apply()
    }

    @Test
    fun test2() {
        val main = CompletableFutureMain()
        main.applyAsync1()
    }

    @Test
    fun test3() {
        val main = CompletableFutureMain()
        main.applyAsync2()
    }

    @Test
    fun test4() {
        val main = CompletableFutureMain()
        main.whenComplete()
    }
}
