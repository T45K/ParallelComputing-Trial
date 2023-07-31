package io.github.t45k.pct.coroutine.doc

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

// Sequentially executes doWorld followed by "Done"
fun main() = runBlocking {
    doWorld()
    println("Done")
}

// Concurrently executes both sections
suspend fun doWorld() = coroutineScope { // this: CoroutineScope
    launch { // ここの launch と
        delay(2000L)
        println("World 2")
    }
    launch { // ここの launch は並行に走る
        delay(1000L)
        println("World 1")
    }
    println("Hello")
}
