package io.github.t45k.pct.coroutine

import io.reactivex.rxkotlin.toFlowable
import io.reactivex.schedulers.Schedulers
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.Path
import kotlin.io.path.bufferedWriter
import kotlin.io.path.isDirectory
import kotlin.io.path.readText
import kotlin.io.path.walk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.nio.file.Path

@OptIn(ExperimentalPathApi::class)
object FileIOTrial {
    private const val numOfThreads = 4
    private val outputPath = Path("output")
    private val inputPath = Path(".")

    fun sequential() {
        outputPath.bufferedWriter().use { bw ->
            inputPath.walk()
                .filterNot { it.isDirectory() }
                .map {
                    Thread.sleep(10)
                    println("read: ${Thread.currentThread()}")
                    File(it, it.readText())
                }
                .map {
                    println("calc: ${Thread.currentThread()}")
                    FileLine(it.path, it.contents.split("\n").size)
                }
                .forEach {
                    println("write: ${Thread.currentThread()}")
                    bw.write("${it.path} ${it.line}\n")
                }
        }
    }

    fun rx() {
        outputPath.bufferedWriter().use { bw ->
            inputPath.walk()
                .toFlowable()
                .filter { !it.isDirectory() }
                .parallel(numOfThreads)
                .runOn(Schedulers.io())
                .map {
                    Thread.sleep(10)
                    println("read: ${Thread.currentThread()}")
                    File(it, it.readText())
                }
                .runOn(Schedulers.computation())
                .map {
                    println("calc: ${Thread.currentThread()}")
                    FileLine(it.path, it.contents.split("\n").size)
                }
                .runOn(Schedulers.io())
                .sequential()
                .blockingSubscribe {
                    println("write: ${Thread.currentThread()}")
                    bw.write("${it.path} ${it.line}\n")
                }
        }
    }

    fun coroutine() {
        runBlocking {
            coroutineScope {
                outputPath.bufferedWriter().use { bw ->
                    inputPath.walk()
                        .asFlow()
                        .flatMapMerge(4) { file ->
                            flow {
                                val contents = withContext(Dispatchers.IO) {
                                    delay(10)
                                    println("read: ${Thread.currentThread()}")
                                    file.readText()
                                }
                                emit(File(file, contents))
                            }
                        }
                        .map {
                            println("calc: ${Thread.currentThread()}")
                            FileLine(it.path, it.contents.split("\n").size)
                        }
                        .collect {
                            withContext(Dispatchers.IO) {
                                println("write: ${Thread.currentThread()}")
                                bw.write("${it.path} ${it.line}\n")
                            }
                        }
                }
            }
        }
    }

    data class File(val path: Path, val contents: String)
    data class FileLine(val path: Path, val line: Int)
}