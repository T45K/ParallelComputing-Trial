package io.github.t45k.pct.coroutine.doc

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class CoroutinesBasics {
    fun main() = runBlocking {
        repeat(50_000) { // launch a lot of coroutines
            launch {
                delay(5000L)
                print(".")
            }
        }
    }
}
