package io.github.t45k.pct.coroutine.doc

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

fun main() = runBlocking {
    val time = measureTimeMillis {
        val one = async { doSomethingUsefulOne() }
        val two = async { doSomethingUsefulTwo() }
        println("The answer is ${one.await() + two.await()}")
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
