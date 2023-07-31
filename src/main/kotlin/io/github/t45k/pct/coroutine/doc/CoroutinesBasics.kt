package io.github.t45k.pct.coroutine.doc

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking { // this: CoroutineScope
    launch { // launch a new coroutine and continue
        delay(1000L) // non-blocking delay for 1 second (default time unit is ms)
        println("World! from " + Thread.currentThread().name) // print after delay
    }
    println("Hello from " + Thread.currentThread().name) // main coroutine continues while a previous one is delayed
}
