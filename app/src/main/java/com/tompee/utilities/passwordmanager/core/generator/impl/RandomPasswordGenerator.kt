package com.tompee.utilities.passwordmanager.core.generator.impl

import com.tompee.utilities.passwordmanager.core.generator.PasswordGenerator
import java.security.SecureRandom

class RandomPasswordGenerator : PasswordGenerator {

    companion object {
        private val numbers = arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")
        private val smallCaps = arrayOf("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z")
        private val allCaps = smallCaps.map { it.toUpperCase() }.toTypedArray()
        private val symbols = arrayOf("@", "%", "+", "\\", "/", "'", "!", "#", "$", "^", "?", ":", ".", "(", ")", "{", "}", "[", "]", "~", "-", "_", ",")
    }

    override fun generatePassword(length: Int): String {
        // Only works for length >= 8
        val random = SecureRandom.getInstanceStrong()
        val lowerBound = 1 // at least 1 for each
        val list = mutableListOf<String>()

        val symbolsCount = random.nextInt((length / 4) - lowerBound) + lowerBound
        for (i in 1..symbolsCount) {
            list.add(symbols[random.nextInt(symbols.size - 1)])
        }

        val numbersCount = random.nextInt((length / 4) - lowerBound) + lowerBound
        for (i in 1..numbersCount) {
            list.add(numbers[random.nextInt(numbers.size - 1)])
        }

        val allCapsCount = random.nextInt(((length - symbolsCount - numbersCount) / 2) - lowerBound) + lowerBound
        for (i in 1..allCapsCount) {
            list.add(allCaps[random.nextInt(allCaps.size - 1)])
        }

        val smallCapsCount = length - allCapsCount - symbolsCount - numbersCount
        for (i in 1..smallCapsCount) {
            list.add(smallCaps[random.nextInt(smallCaps.size - 1)])
        }

        list.shuffle(random)
        return list.joinToString(separator = "") { it }
    }
}