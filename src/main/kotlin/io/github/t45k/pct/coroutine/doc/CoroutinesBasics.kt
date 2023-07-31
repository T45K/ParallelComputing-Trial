package io.github.t45k.pct.coroutine.doc

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking { // this: CoroutineScope
    launch { doWorld() }
    println("Hello from " + Thread.currentThread().name)
}

private suspend fun doWorld() {
    delay(1000L)
    println("World! from " + Thread.currentThread().name)
}
