package io.github.t45k.pct

import java.util.ArrayList
import java.util.stream.Collectors
import kotlin.math.sqrt

/**
 * エラトステネスの篩
 * 計算量はO(n log(log(n)))
 */
fun sieveOfEratosthenes(number: Int): List<Int> {
    val sqrt = sqrt(number.toDouble()).toInt()
    var numbers: List<Int> = (2..number).toList()
    val primeNumbers: MutableList<Int> = ArrayList()
    var condition: Int
    do {
        val prime = numbers[0]
        primeNumbers.add(prime)
        numbers = numbers.stream()
                .filter { i: Int -> i % prime != 0 }
                .collect(Collectors.toList())
        condition = prime
    } while (condition < sqrt)
    primeNumbers.addAll(numbers)
    return primeNumbers
}

/**
 * 累積和
 * 計算量は線形
 */
fun sumAccumulation(number: Int): Long = (1..number.toLong()).sum()

/**
 * 与えられた数字から3つ選んだ組み合わせの和の総数
 * O(n ^ 3)
 */
fun sumAccumulation2(number: Int): Long = (1..number.toLong())
        .map { i ->
            (i + 1..number.toLong())
                    .map { j ->
                        (j + 1..number.toLong())
                                .map { k -> i + j + k }
                                .sum()
                    }
                    .sum()
        }
        .sum()
