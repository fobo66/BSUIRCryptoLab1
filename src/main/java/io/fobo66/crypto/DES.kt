@file:Suppress("MagicNumber") // too many of them
package io.fobo66.crypto

import java.util.*
import kotlin.experimental.xor

object DES {
    // initialization vector for CBC, CFB and OFB modes
    val initializationVector =
        byteArrayOf(11, 22, 33, 44, 55, 66, 77, 88)

    private val initialPermutationTable = intArrayOf(
        58, 50, 42, 34, 26, 18, 10, 2,
        60, 52, 44, 36, 28, 20, 12, 4,
        62, 54, 46, 38, 30, 22, 14, 6,
        64, 56, 48, 40, 32, 24, 16, 8,
        57, 49, 41, 33, 25, 17, 9, 1,
        59, 51, 43, 35, 27, 19, 11, 3,
        61, 53, 45, 37, 29, 21, 13, 5,
        63, 55, 47, 39, 31, 23, 15, 7
    )

    private val invertedInitialPermutationTable = intArrayOf(
        40, 8, 48, 16, 56, 24, 64, 32,
        39, 7, 47, 15, 55, 23, 63, 31,
        38, 6, 46, 14, 54, 22, 62, 30,
        37, 5, 45, 13, 53, 21, 61, 29,
        36, 4, 44, 12, 52, 20, 60, 28,
        35, 3, 43, 11, 51, 19, 59, 27,
        34, 2, 42, 10, 50, 18, 58, 26,
        33, 1, 41, 9, 49, 17, 57, 25
    )

    // Permutation P (in f(Feistel) function)
    private val permutation = intArrayOf(
        16, 7, 20, 21,
        29, 12, 28, 17,
        1, 15, 23, 26,
        5, 18, 31, 10,
        2, 8, 24, 14,
        32, 27, 3, 9,
        19, 13, 30, 6,
        22, 11, 4, 25
    )

    // initial key permutation 64 => 56 bit
    private val initialKeyPermutation = intArrayOf(
        57, 49, 41, 33, 25, 17, 9, 1, 58, 50, 42, 34, 26, 18, 10, 2, 59, 51, 43, 35, 27, 19,
        11, 3, 60, 52, 44, 36, 63, 55, 47, 39, 31, 23, 15, 7, 62, 54, 46, 38, 30, 22, 14, 6, 61, 53, 45, 37, 29, 21,
        13, 5, 28, 20, 12, 4
    )

    // key permutation at round i 56 => 48
    private val PC2 = intArrayOf(
        14, 17, 11, 24, 1, 5, 3, 28, 15, 6, 21, 10, 23, 19, 12, 4, 26, 8, 16, 7, 27, 20, 13, 2,
        41, 52, 31, 37, 47, 55, 30, 40, 51, 45, 33, 48, 44, 49, 39, 56, 34, 53, 46, 42, 50, 36, 29, 32
    )

    // key shift for each round
    private val keyShift = intArrayOf(1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1)

    // expansion permutation from function f
    private val expandTable = intArrayOf(
        32, 1, 2, 3, 4, 5, 4, 5, 6, 7, 8, 9, 8, 9, 10, 11, 12, 13, 12, 13, 14, 15, 16,
        17, 16, 17, 18, 19, 20, 21, 20, 21, 22, 23, 24, 25, 24, 25, 26, 27, 28, 29, 28, 29, 30, 31, 32, 1
    )

    // substitution boxes
    private val sboxes = arrayOf(
        intArrayOf(
            14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7,
            0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8,
            4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0,
            15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13
        ),

        intArrayOf(
            15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10,
            3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5,
            0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15,
            13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9
        ),

        intArrayOf(
            10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8,
            13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1,
            13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7,
            1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12
        ),

        intArrayOf(
            7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15,
            13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9,
            10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4,
            3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14
        ),

        intArrayOf(
            2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9,
            14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6,
            4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14,
            11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3
        ),

        intArrayOf(
            12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11,
            10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8,
            9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6,
            4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13
        ),

        intArrayOf(
            4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1,
            13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6,
            1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2,
            6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12
        ),

        intArrayOf(
            13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7,
            1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2,
            7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8,
            2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11
        )
    )

    fun encrypt(data: ByteArray, key: ByteArray, mode: DESMode): ByteArray {
        var feedback = ByteArray(BLOCK_SIZE)
        System.arraycopy(initializationVector, 0, feedback, 0, feedback.size)

        val subKeys = generateSubKeys(key)
        val result = ByteArray(data.size)
        var block = ByteArray(BLOCK_SIZE)
        var processedBlock = ByteArray(BLOCK_SIZE)
        var i = 0
        while (i < data.size) {
            if (i > 0 && i % BLOCK_SIZE == 0) {
                when (mode) {
                    DESMode.ECB -> processedBlock = processBlock(block, subKeys)
                    DESMode.CBC -> {
                        processedBlock = xorBytes(block, feedback)
                        processedBlock = processBlock(processedBlock, subKeys)
                        feedback = processedBlock.copyOfRange(0, BLOCK_SIZE)
                    }

                    DESMode.CFB -> {
                        processedBlock = processBlock(key, subKeys)
                        feedback = processedBlock.copyOfRange(0, BLOCK_SIZE)
                        processedBlock = xorBytes(processedBlock, block)
                    }

                    DESMode.OFB -> {
                        processedBlock = processBlock(feedback, subKeys)
                        feedback = processedBlock.copyOfRange(0, BLOCK_SIZE)
                        processedBlock = xorBytes(processedBlock, block)
                    }
                }
                System.arraycopy(processedBlock, 0, result, i - BLOCK_SIZE, block.size)
            }
            block[i % BLOCK_SIZE] = data[i]
            i++
        }
        if (block.size == BLOCK_SIZE) {
            block = processBlock(block, subKeys)
            System.arraycopy(block, 0, result, i - BLOCK_SIZE, block.size)
        }
        return result
    }

    fun decrypt(data: ByteArray, key: ByteArray, mode: DESMode): ByteArray {
        val result = ByteArray(data.size)
        var block = ByteArray(BLOCK_SIZE)
        var processedBlock = ByteArray(BLOCK_SIZE)
        var feedback = ByteArray(BLOCK_SIZE)
        System.arraycopy(initializationVector, 0, feedback, 0, feedback.size)
        val subKeys = generateSubKeys(key)
        for (i in 1..8) {
            val temp = subKeys[i]
            subKeys[i] = subKeys[17 - i]
            subKeys[17 - i] = temp
        }
        var i = 0
        while (i < data.size) {
            if (i > 0 && i % BLOCK_SIZE == 0) {
                when (mode) {
                    DESMode.ECB -> processedBlock = processBlock(block, subKeys)
                    DESMode.CBC -> {
                        processedBlock = processBlock(block, subKeys)
                        processedBlock = xorBytes(feedback, processedBlock)
                        feedback = block.copyOfRange(0, BLOCK_SIZE)
                    }

                    DESMode.CFB -> {
                        processedBlock = processBlock(key, subKeys)
                        feedback = processedBlock.copyOfRange(0, BLOCK_SIZE)
                        processedBlock = xorBytes(processedBlock, block)
                    }

                    DESMode.OFB -> {
                        processedBlock = processBlock(feedback, subKeys)
                        feedback = processedBlock.copyOfRange(0, BLOCK_SIZE)
                        processedBlock = xorBytes(processedBlock, block)
                    }
                }
                System.arraycopy(processedBlock, 0, result, i - BLOCK_SIZE, block.size)
            }
            block[i % BLOCK_SIZE] = data[i]
            i++
        }
        block = processBlock(block, subKeys)
        System.arraycopy(block, 0, result, i - BLOCK_SIZE, block.size)
        return result
    }

    private fun xorBytes(a: ByteArray, b: ByteArray): ByteArray {
        val out = ByteArray(a.size)
        for (i in a.indices) {
            out[i] = a[i] xor b[i]
        }
        return out
    }

    private fun processBlock(block: ByteArray, subKeys: Array<BitSet>): ByteArray {
        val blockBitSet = block.toBitSet()
        val mp = BitSet(64)
        for (i in 0..63) {
            mp[i] = blockBitSet[initialPermutationTable[i] - 1]
        }

        // split 'mp' in half and process the resulting series of 'l' and 'r
        val l = Array(17) { BitSet(32) }
        val r = Array(17) { BitSet(32) }
        for (i in 0..31) l[0][i] = mp[i]
        for (i in 0..31) r[0][i] = mp[i + 32]
        for (i in 1..16) {
            l[i] = r[i - 1]
            val fs = fFunc(r[i - 1], subKeys[i])
            l[i - 1].xor(fs)
            r[i] = l[i - 1]
        }

        // amalgamate r[16] and l[16] (in that order) into 'e'
        val e = BitSet(64)
        for (i in 0..31) e[i] = r[16][i]
        for (i in 32..63) e[i] = l[16][i - 32]

        // permute 'e' using table IP2
        val ep = BitSet(64)
        for (i in 0..63) ep[i] = e[invertedInitialPermutationTable[i] - 1]
        return ep.toByteArray()
    }

    private fun fFunc(r: BitSet, subKey: BitSet): BitSet {
        val er = BitSet(48)
        for (i in 0..47) er[i] = r[expandTable[i] - 1]

        er.xor(subKey)

        // process 'er' six bits at a time and store resulting four bits in 'sr'
        val sr = BitSet(32)
        for (i in 0..7) {
            val j = i * 6
            val b = IntArray(6)
            for (k in 0..5) b[k] = if (er[j + k]) 1 else 0
            val row = 2 * b[0] + b[5]
            val col = 8 * b[1] + 4 * b[2] + 2 * b[3] + b[4]
            var m = sboxes[i][row * 16 + col]   // apply table S
            var n = 1
            while (m > 0) {
                val p = m % 2
                sr[(i + 1) * 4 - n] = (p == 1)
                m /= 2
                n++
            }
        }

        // permute sr using table P
        val sp = BitSet(32)
        for (i in 0..31) sp[i] = sr[permutation[i] - 1]
        return sp
    }

    private fun generateSubKeys(key: ByteArray): Array<BitSet> {
        val keyBitSet = key.toBitSet()
        val kp = BitSet(56)
        for (i in 0..55) kp[i] = keyBitSet[initialKeyPermutation[i] - 1]

        // split 'kp' in half and process the resulting series of 'c' and 'd'
        val c = Array(17) { BitSet(56) }
        val d = Array(17) { BitSet(28) }
        for (i in 0..27) c[0][i] = kp[i]
        for (i in 0..27) d[0][i] = kp[i + 28]
        for (i in 1..16) {
            c[i - 1].shiftLeft(keyShift[i - 1], 28, c[i])
            d[i - 1].shiftLeft(keyShift[i - 1], 28, d[i])
        }

        // merge 'd' into 'c'
        for (i in 1..16) {
            for (j in 28..55) c[i][j] = d[i][j - 28]
        }

        // form the sub-keys and store them in 'ks'
        val ks = Array(17) { BitSet(48) }

        // permute 'c' using table PC2
        for (i in 1..16) {
            for (j in 0..47) ks[i][j] = c[i][PC2[j] - 1]
        }

        return ks
    }

    private fun ByteArray.toBitSet(): BitSet {
        return BitSet.valueOf(this)
    }

    private fun BitSet.shiftLeft(times: Int, len: Int, out: BitSet) {
        for (i in 0 until len) {
            out[i] = this[i]
        }

        repeat((1..times).count()) {
            val temp = out[0]
            for (i in 1 until len) out[i - 1] = out[i]
            out[len - 1] = temp
        }
    }


    private const val BLOCK_SIZE = 8
}
