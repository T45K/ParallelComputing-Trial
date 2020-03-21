package io.github.t45k.pct.thread

import io.github.t45k.pct.sumAccumulation

/**
 * java.lang.Thread
 *
 * 実装した Runnable を引数にとる
 * Thread#run で実行開始
 * Thread#join で終了待ちが可能
 * 論理コア数以上のスレッドを確保できる
 *
 * 実行するだけ，引数と返り値はない
 */
class ThreadMain {
    /**
     * 計算自体はマルチスレッドだが，実行が線形な例
     * イメージとしては
     * 1 -> 1
     *        2 -> 2
     *               3 -> 3
     *                      ...
     */
    fun iterative() {
        // Kotlin のラムダ式は () -> {} じゃなくて Interface {} らしい
        // SAM変換
        (10_000_000..10_000_010)
                .map {
                    val runnable = Runnable {
                        val result = sumAccumulation(it)
                        println("${Thread.currentThread().name} $it $result")
                    }
                    val thread = Thread(runnable)
                    thread
                }
                .forEach {
                    // main スレッドが先に終わってしまう可能性もある
                    // join すると終了を待つ
                    it.start() // 返り値はvoid
                    it.join() // 実行待ち
                }
        // it.join を使うタイミングの問題
    }

    /**
     * 計算も実行も並列
     * 1 -> 1
     * 2 -> 2
     * 3 -> 3
     * ...
     */
    fun parallel() {
        // マルチで動かして，各スレッドで終了を待つ
        // マルチで動かすので，メモリが枯渇する可能性が高い
        (10_000_000..10_000_010)
                .map {
                    val runnable = Runnable {
                        val result = sumAccumulation(it)
                        println("${Thread.currentThread().name} $it $result")
                    }
                    val thread = Thread(runnable)
                    thread
                }
                .onEach { it.start() }
                .forEach { it.join() }
    }

    /**
     * スレッドの確保のされ方の確認
     *
     * 予想してた挙動: 3秒ごとに8スレッドずつ表示される
     * 3秒で全てのスレッドが表示される
     */
    fun checkHowGetThread() {
        (1_000_000_000..1_000_000_100)
                .map {
                    val runnable = Runnable {
                        println(Thread.currentThread().name)
                        Thread.sleep(3_000)
                    }
                    val thread = Thread(runnable)
                    thread
                }
                .onEach { it.start() }
                .forEach { it.join() }
    }

    /*
    実スレッド以上のスレッドを new しても確保するっぽい
    なので，アホみたいなスレッド数を確保しようとすると簡単にメモリ不足になる
    間違いなく非効率
     */
}
