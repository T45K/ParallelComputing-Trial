package io.github.t45k.pct.coroutine

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class Main {
    fun main() {
        println("main: " + Thread.currentThread().name) // main
        runBlocking {
            println("main (runBlocking): " + Thread.currentThread().name) // main
            launch { foo() }
        }
    }

    /**
     * とりあえずsuspend fun
     */
    private suspend fun foo() {
        println("foo: " + Thread.currentThread().name) // main
        bar()
    }

    private suspend fun bar() {
        println("bar: " + Thread.currentThread().name) // main
        withContext(Dispatchers.IO) {
            println("bar (IO context): " + Thread.currentThread().name) // DefaultDispatcher-worker-1
        }
    }
}
