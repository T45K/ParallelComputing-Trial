package io.github.t45k.pct.future

import io.github.t45k.pct.sieveOfEratosthenes
import java.util.concurrent.CompletableFuture

/**
 * supply -> apply -> accept(consume)
 * 末尾に async を付けたり付けなかったりすることでスレッドの指定が可能
 *
 * 非同期を直列につないで実行できる
 * thenApply でデータを加工することもできる
 * Rxみたいに大量のデータを捌くわけではない
 * 使い方としては
 * "メインでデータ生成 -> UIスレッドに投げる(非同期) -> ... -> get() でメインで待ち合わせ"みたいな
 *
 * 待ち合わせは get() を使う
 */
class CompletableFutureMain {
    fun apply() {
        // 上手くいく例
        // apply は main スレッド
        CompletableFuture.supplyAsync { 1_000_000 }
                .thenApply {
                    println(Thread.currentThread().name)
                    sieveOfEratosthenes(it)
                }
                .whenComplete { ret, _ -> println(ret) }
    }

    /**
     * thenAccept(Async) のチェインも可能
     */
    fun applyAsync1() {
        // 上手くいかない例
        // acceptAsync
        CompletableFuture.supplyAsync { 1_000_000 }
                .thenApplyAsync {
                    println(Thread.currentThread().name)
                    sieveOfEratosthenes(it)
                }
                .thenAcceptAsync { // apply と accept は別スレッド
                    println(Thread.currentThread().name)
                    println(it)
                }
    }

    /**
     * get() を使えば上手くブロッキングしてくれる
     */
    fun applyAsync2() {
        // 多分上手くいく例
        val list: List<Int> = CompletableFuture.supplyAsync { 1_000_000 }
                .thenApplyAsync {
                    println(Thread.currentThread().name)
                    sieveOfEratosthenes(it)
                }
                .get() // get() でブロッキング

        println(list)

        // 上手くいく例
        CompletableFuture.supplyAsync { 1_000_000 }
                .thenApplyAsync {
                    println(Thread.currentThread().name)
                    sieveOfEratosthenes(it)
                }
                .thenAcceptAsync { // apply と accept は別スレッド
                    println(Thread.currentThread().name)
                    println(it)
                }
                .get() // ブロッキング
    }

    /**
     * 期待している動作: whenComplete が上手いことブロッキングしてくれる
     * 結果: してくれない
     *
     * どうも例外を捕まえるためのメソッドっぽい
     *
     * やっぱり get() をつける
     */
    fun whenComplete() {
        CompletableFuture.supplyAsync { 1_000_000 }
                .thenApplyAsync {
                    println(Thread.currentThread().name)
                    sieveOfEratosthenes(it)
                }
                .thenAcceptAsync { // apply と accept は別スレッド
                    println(Thread.currentThread().name)
                    println(it)
                }
                .whenComplete { _, _ -> println("done") }
                .get()
    }
}
