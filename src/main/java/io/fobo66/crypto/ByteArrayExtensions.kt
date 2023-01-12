@file:Suppress("MagicNumber") // too many of them
package io.fobo66.crypto

fun ByteArray.extractBit(pos: Int): Int {
    val posByte = pos / 8
    val posBit = pos % 8
    val tmpB = this[posByte]
    return tmpB.toInt() shr 8 - (posBit + 1) and 0x0001
}

fun ByteArray.setBit(pos: Int, value: Int) {
    val posByte = pos / 8
    val posBit = pos % 8
    var tmpB = this[posByte]
    tmpB = (0xFF7F shr posBit and tmpB.toInt() and 0x00FF).toByte()
    val newByte = (value shl 8 - (posBit + 1) or tmpB.toInt()).toByte()
    this[posByte] = newByte
}

fun ByteArray.xorBytes(another: ByteArray): ByteArray {
    val out = ByteArray(this.size)
    for (i in this.indices) {
        out[i] = (this[i].toInt() xor another[i].toInt()).toByte()
    }
    return out
}

fun ByteArray.rotateLeft(len: Int, pas: Int): ByteArray {
    val nrBytes = (len - 1) / 8 + 1
    val out = ByteArray(nrBytes)
    for (i in 0 until len) {
        val value = this.extractBit((i + pas) % len)
        this.setBit(i, value)
    }
    return out
}

fun ByteArray.extractBits(pos: Int, n: Int): ByteArray {
    val numOfBytes = (n - 1) / 8 + 1
    val out = ByteArray(numOfBytes)
    for (i in 0 until n) {
        val value = this.extractBit(pos + i)
        this.setBit(i, value)
    }
    return out
}
