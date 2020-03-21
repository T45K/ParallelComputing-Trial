package io.github.t45k.pct.future

import io.github.t45k.pct.sieveOfEratosthenes
import io.github.t45k.pct.sumAccumulation
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.Future

/**
 * java.util.concurrent.Future と java.util.concurrent.ExecutorService
 *
 * Futureは非同期計算の結果を返す
 * 計算だけしたい場合はジェネリクスにワイルドカード(<>)を利用する
 *
 * ExecutorService#submit に計算したい Runnable または Callable<T> を渡す
 * Callable は返り値を指定できる Runnable っぽい
 *
 * ExecutorService#submit された瞬間から計算は始まる
 * Future#get で実行結果を返すまで待つ
 *
 * 引数は与えられない
 */
class FutureMain {
    fun singleFuture() {
        // 確保するスレッドに制約をかけられるっぽい．キャッシュ済み，一定数以下，etc.
        val executor = Executors.newSingleThreadExecutor()
        val future: Future<List<Int>> = executor.submit(Callable {
            println(Thread.currentThread())
            val list = sieveOfEratosthenes(10_000_000)
            println(list.size)
            list
        }) // submit された時点で実行は開始している
        Thread.sleep(3000)
        println("start")
        val list: List<Int> = future.get() // 実行が終わるまで待つ
        println("end")
        println(list.size)
    }

    fun singleFutureWithNoReturnValue() {
        val executor = Executors.newSingleThreadExecutor()
        // Runnable の時はSAM変換で下みたいに書けるっぽい．よく分からん
        val future: Future<*> = executor.submit {
            println(Thread.currentThread())
            val list = sieveOfEratosthenes(10_000_000)
            println(list.size)
        } // submit された時点で実行は開始している
        Thread.sleep(3000)
        println("start")
        future.get() // 実行が終わるまで待つ
        println("end")
    }

    fun multiFuture() {
        val value = (1_000_000_000..1_000_000_100)
                .map {
                    Executors.newSingleThreadExecutor()
                            .submit(Callable {
                                println("${Thread.currentThread()} $it")
                                sumAccumulation(it)
                            })
                }
                .map { it.get() }
                .sum()
        println(value)
    }

    fun variousThreadPool() {
        // スレッドをキャッシュして，使いまわせそうなら使いまわす
        val cachedThreadPool = Executors.newCachedThreadPool()
        println((1_000_000_000..1_000_000_100)
                .map {
                    cachedThreadPool
                            .submit(Callable {
                                println("${Thread.currentThread()} $it")
                                sumAccumulation(it)
                            })
                }
                .map { it.get() }
                .sum())

        // 指定した数のスレッドを確保して，それを使い回す
        val fixedThreadPool = Executors.newFixedThreadPool(10)
        println((1_000_000_000..1_000_000_100)
                .map {
                    fixedThreadPool
                            .submit(Callable {
                                println("${Thread.currentThread()} $it")
                                sumAccumulation(it)
                            })
                }
                .map { it.get() }
                .sum())
    }
}