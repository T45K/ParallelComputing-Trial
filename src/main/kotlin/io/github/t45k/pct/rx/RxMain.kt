package io.github.t45k.pct.rx

import io.github.t45k.pct.sumAccumulation
import io.github.t45k.pct.sumAccumulation2
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class RxMain {
    /**
     * 直列実行の例
     * 通常のIterable#mapが全て処理してからなのに対して
     * map -> subscribe -> map -> subscribe -> ...の繰り返し
     */
    fun basic() {
        // 普通の実行
        // 全てに対して map を実行してから forEach を実行する
        (10_000_000..10_000_010).map {
                    println(it)
                    it to sumAccumulation(it)
                }
                .forEach { println(it) }

        // Rx
        Observable.fromIterable(10_000_000..10_000_010)
                .map {
                    println(it)
                    it to sumAccumulation(it)
                }
                .subscribe { println(it) }

        // シーケンシャル実行
        // Rx と同じ挙動
        // Java の Stream API と同じ
        (10_000_000..10_000_010).asSequence().map {
                    println(it)
                    it to sumAccumulation(it)
                }
                .forEach { println(it) }
    }

    /**
     * Rx の特徴として，subscribe されるまで全ての処理が実行されない
     * つまり，subscribe が実行されて初めてデータが生成される(遅延化)
     */
    fun checkSubscribe() {
        val declarationTiming = System.currentTimeMillis()
        val observable: Observable<Pair<Int, Long>> = Observable.fromIterable(10_000_000..10_000_010)
                .map { it to System.currentTimeMillis() }

        Thread.sleep(3000)

        // データの生成は subscribe のタイミングなので，Observable を生成したタイミングから3秒が過ぎている
        observable.subscribe { println("$it ${System.currentTimeMillis() - declarationTiming}") }

        // blockingGetも多分そんな感じ
        val declarationTiming2 = System.currentTimeMillis()
        val observable2: Observable<Pair<Int, Long>> = Observable.fromIterable(10_000_000..10_000_010)
                .map { it to System.currentTimeMillis() }

        Thread.sleep(3000)

        observable2.toList().blockingGet()
                .map { it.second - declarationTiming2 }
                .forEach { println(it) }

        /*
        Rx ではなく Kotlin の話だが，以下2つは異なるので注意
        Observable.just(declarationTiming) // declarationTiming を宣言したタイミングの時間
        Observable.just{System.currentTimeMillis()} // subscribe を実行したタイミング(遅延処理)
        */
    }

    /**
     * 並列実行
     *
     * 前提: 別スレッドで処理する時は宣言時に実行が開始するわけではない
     * やっぱり別スレッドでも原則的に subscribe してから生成が開始される
     */
    fun parallel() {
        // 何も起きない
        // main スレッドが先に終了するので
        val decTiming: Long = System.currentTimeMillis()
        val observable: Observable<Pair<Int, Long>> = Observable.fromIterable(10_000_000..10_000_010)
                .subscribeOn(Schedulers.newThread())
                .map { it to System.currentTimeMillis() }

        Thread.sleep(3000)
        observable.subscribe { println("${Thread.currentThread().name} $it ${it.second - decTiming}") }
        Thread.sleep(1000) // 強引なブロッキング
        println(Thread.currentThread().name)
    }

    /**
     * Future 的な使い方を考えるとこう？
     * やっぱりブロッキング
     */
    fun parallel2() {
        val decTiming: Long = System.currentTimeMillis()
        val list: List<Pair<Int, Long>> = Observable.fromIterable(10_000_000..10_000_010)
                .subscribeOn(Schedulers.newThread())
                .map { it to System.currentTimeMillis() }
                .toList()
                .blockingGet()

        list.forEach { println("${it.second - decTiming}") }
    }

    /**
     * やりたいこと: 分散処理
     *
     * a1, a2, ..., an に対して
     * ai -> bi の map とかを各スレッドでやって，結果をリアクティブに消費
     */
    fun distribute() {
        /*
        直列
        Observable.fromIterable(500..1000 step 50)
                .map { it to sumAccumulation2(it) }
                .subscribe { println(it) }
        */

        // 何か上手く動いてそう？
        // 初めにスレッドを割り振るから，非効率になってそう(スレッド1に1000と200を振ってる．実行終了が早いスレッド8とかに振った方が良い)
        Observable.fromIterable(1000 downTo 0 step 100)
                .flatMap {
                    Observable.just(it)
                            .observeOn(Schedulers.computation())
                            .map {
                                sumAccumulation2(it)
                                it to Thread.currentThread().name
                            }
                }
                .blockingSubscribe { println(it) }
    }
    /*
    重い処理を別スレッドでやりたいとかだとFutureの方が良い？
    Completable#andThen とかもあるらしい
     */
}
