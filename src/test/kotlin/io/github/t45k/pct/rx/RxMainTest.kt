package io.github.t45k.pct.rx

import kotlin.test.Test

internal class RxMainTest {
    @Test
    fun test1() {
        val main = RxMain()
        main.basic()
    }

    @Test
    fun test2() {
        val main = RxMain()
        main.checkSubscribe()
    }

    @Test
    fun test3() {
        val main = RxMain()
        main.parallel()
    }

    @Test
    fun test4() {
        val main = RxMain()
        main.parallel2()
    }

    @Test
    fun test5() {
        val main = RxMain()
        main.distribute()
    }
}
