package io.github.t45k.pct.coroutine.doc

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking { // this: CoroutineScope
    doWorld()
}

private suspend fun doWorld() = coroutineScope {
    launch {
        delay(1000L)
        println("World! from " + Thread.currentThread().name)
    }
    println("Hello from " + Thread.currentThread().name)
}
