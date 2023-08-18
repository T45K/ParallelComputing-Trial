package io.github.t45k.pct.coroutine.doc

import kotlinx.coroutines.delay
import kotlin.system.measureTimeMillis

suspend fun main() {
    val time = measureTimeMillis {
        val one = doSomethingUsefulOne()
        val two = doSomethingUsefulTwo()
        println("The answer is ${one + two}")
    }

    println(time)
}

suspend fun doSomethingUsefulOne(): Int {
    delay(1_000)
    return 13
}

suspend fun doSomethingUsefulTwo(): Int {
    delay(1_000)
    return 29
}
