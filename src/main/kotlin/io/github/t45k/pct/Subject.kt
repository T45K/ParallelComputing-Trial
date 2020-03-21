package io.github.t45k.pct

import java.util.ArrayList
import java.util.stream.Collectors
import kotlin.math.sqrt

// エラトステネスの篩
// 省計算量
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

fun sumAccumulation(number: Int): Long {
    var sum = 0L
    for (i in 1..number) {
        sum += i
    }
    return sum
}

