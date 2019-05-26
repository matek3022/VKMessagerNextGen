package com.matek3022.vkmessagernextgen.utils.stega

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Color
import com.matek3022.vkmessagernextgen.utils.CryptUtils
import java.util.*
import kotlin.collections.ArrayList

/**
 * if true then 1
 * if false then 0
 */

const val seed = 4L

const val TEXT_ID = "text"

fun Bitmap.codeText(text: String, localKey: String = ""): Bitmap {
    val bitArray = ArrayList<Boolean>()
    val byteArray = CryptUtils.cryptString(text, localKey)
    val byteSizeArr = Utils.intToByteArray(byteArray.size)
    TEXT_ID.toByteArray().forEach {
        bitArray.addAll(getBits(it).toList())
    }
    byteSizeArr.forEach {
        bitArray.addAll(getBits(it).toList())
    }
    byteArray.forEach {
        bitArray.addAll(getBits(it).toList())
    }
//    val pixels = getPixels()
    this.inToPixels(bitArray)
//    setPixels(pixels, 1)
    return this
}

@Throws(Resources.NotFoundException::class)
fun Bitmap.getText(localKey: String = ""): String {
//    val pixels = getPixels()
    val bits = this.fromPixels(TEXT_ID)
    val byteArray = booleanArrayToByteArray(bits)
    return CryptUtils.decryptString(byteArray, localKey)
}

fun Bitmap.inToPixels(bitsArray: ArrayList<Boolean>) {
    val yCount = this.height
    val xCount = this.width
    val bitsCount = bitsArray.size
    val random = Random(seed)
    val blockYcount = yCount / 8
    val blockXcount = xCount / 8
    val maxBlocks = blockYcount * blockXcount
    val randomIndexes = IntArray(maxBlocks) { i -> i }.toMutableList()
    randomIndexes.shuffle(random)
    var bitsInput = 0
    for (yBlockIndex in 0 until blockYcount) {
        for (xBlockIndex in 0 until blockXcount) {
            if (bitsInput < bitsCount) {
                val currIndex = yBlockIndex * blockXcount + xBlockIndex
                val findingIndex = randomIndexes.indexOf(currIndex)
                if (findingIndex != -1) {
                    var matrYColor = Array(8) { DoubleArray(8) }
                    var matrCrColor = Array(8) { DoubleArray(8) }
                    var matrCbColor = Array(8) { DoubleArray(8) }
                    for (xk in 0..7) {
                        for (yk in 0..7) {
                            val oldColor = this.getPixel(xBlockIndex * 8 + xk, yBlockIndex * 8 + yk)
                            matrYColor[xk][yk] = getY(oldColor) - 128
                            matrCrColor[xk][yk] = getCr(oldColor)
                            matrCbColor[xk][yk] = getCb(oldColor)
                        }
                    }
                    matrYColor = dct(matrYColor)
                    val bit = bitsArray[bitsInput]
//                    toQuantiz(matrYColor)
                    if (bit) {
                        /*if (Math.round(matrYColor[7][7]).toInt() % 2 != 1)*/ matrYColor[7][7] = 10.0
                    } else {
                        /*if (Math.round(matrYColor[7][7]).toInt() % 2 == 1) */matrYColor[7][7] = -10.0
                    }
                    bitsInput++
//                    fromQuantiz(matrYColor)
                    matrYColor = idct(matrYColor)
                    for (xk in 0..7) {
                        for (yk in 0..7) {
                            val newColor = toRGB(
                                matrYColor[xk][yk] + 128,
                                matrCbColor[xk][yk],
                                matrCrColor[xk][yk]
                            )
                            this.setPixel(xBlockIndex * 8 + xk, yBlockIndex * 8 + yk, newColor)
                        }
                    }
                }
            }
        }
    }
}

@Throws(Resources.NotFoundException::class)
fun Bitmap.fromPixels(id: String): List<Boolean> {
    val yCount = this.height
    val xCount = this.width
    val random = Random(seed)
    val blockYcount = yCount / 8
    val blockXcount = xCount / 8
    var bitsCount = blockXcount * blockYcount / 4
    val maxBlocks = blockYcount * blockXcount
    val randomIndexes = IntArray(maxBlocks) { i -> i }.toMutableList()
    randomIndexes.shuffle(random)
    val res = ArrayList<Boolean>()

    var bitsRead = 0

    val idBytes = id.toByteArray()

    for (yBlockIndex in 0 until blockYcount) {
        for (xBlockIndex in 0 until blockXcount) {
            if (bitsRead < bitsCount) {
                if (bitsRead < bitsCount) {
                    val currIndex = yBlockIndex * blockXcount + xBlockIndex
                    val findingIndex = randomIndexes.indexOf(currIndex)
                    if (findingIndex != -1) {
                        var matrYColor = Array(8) { DoubleArray(8) }
                        for (xk in 0..7) {
                            for (yk in 0..7) {
                                matrYColor[xk][yk] = getY(this.getPixel(xBlockIndex * 8 + xk, yBlockIndex * 8 + yk)) - 128
                            }
                        }
                        matrYColor = dct(matrYColor)
//                        toQuantiz(matrYColor)
                        res.add(matrYColor[7][7] > 0)
                        bitsRead++

                        if (bitsRead == idBytes.size * 8) {
                            val outId = String(booleanArrayToByteArray(res.toMutableList()))
                            if (!outId.contains(id)) {
                                throw Resources.NotFoundException()
                            }
                        } else if (bitsRead == idBytes.size * 8 + 4 * 8) {
                            val countBoolean = ArrayList<Boolean>()
                            res.forEachIndexed { index, b ->
                                if (index >= idBytes.size * 8 && index < idBytes.size * 8 + 4 * 8) {
                                    countBoolean.add(b)
                                }
                            }
                            val countByte = countBoolean.toByteArray()
                            val count = Utils.byteArrayToInt(countByte)
                            bitsCount = idBytes.size * 8 + 4 * 8 + count * 8
                            res.clear()
                        }
                    }
                }
            }
        }
    }
    return res
}

fun Bitmap.getPixels(): ArrayList<ArrayList<Pixel>> {
    val pixels = ArrayList<ArrayList<Pixel>>()
    for (i in 0 until width) {
        pixels.add(ArrayList())
        for (j in 0 until height) {
            val bitmapPixel = this.getPixel(i, j)
            pixels[i].add(
                Pixel(
                    Color.red(bitmapPixel),
                    Color.green(bitmapPixel),
                    Color.blue(bitmapPixel)
                )
            )
        }
    }
    return pixels
}

private val Q = arrayOf(
    intArrayOf(16, 11, 10, 16, 24, 40, 51, 61),
    intArrayOf(12, 12, 14, 19, 26, 58, 60, 55),
    intArrayOf(14, 13, 16, 24, 40, 57, 69, 56),
    intArrayOf(14, 17, 22, 29, 51, 87, 80, 62),
    intArrayOf(18, 22, 37, 56, 68, 109, 103, 77),
    intArrayOf(24, 35, 55, 64, 81, 104, 113, 92),
    intArrayOf(49, 64, 78, 87, 103, 121, 120, 101),
    intArrayOf(72, 92, 95, 98, 112, 100, 103, 99)
)

fun DoubleArray.devideArray(x: Int) {
    this.forEachIndexed { index, fl ->
        this[index] = fl / x
    }
}

fun toQuantiz(arr: Array<DoubleArray>) {
    arr.forEachIndexed { index1, arr1 ->
        arr1.forEachIndexed { index, d ->
            arr[index1][index] = Math.round(d / Q[index1][index]).toDouble()
        }
    }
}

fun fromQuantiz(arr: Array<DoubleArray>) {
    arr.forEachIndexed { index1, arr1 ->
        arr1.forEachIndexed { index, d ->
            arr[index1][index] = d * Q[index1][index]
        }
    }
}

fun getBits(byte: Byte): BooleanArray {
    val currByte = byte.toInt() + 128
    val res = BooleanArray(8)
    val bit0 = currByte / 128
    val bit1 = currByte % 128 / 64
    val bit2 = currByte % 128 % 64 / 32
    val bit3 = currByte % 128 % 64 % 32 / 16
    val bit4 = currByte % 128 % 64 % 32 % 16 / 8
    val bit5 = currByte % 128 % 64 % 32 % 16 % 8 / 4
    val bit6 = currByte % 128 % 64 % 32 % 16 % 8 % 4 / 2
    val bit7 = currByte % 128 % 64 % 32 % 16 % 8 % 4 % 2
    res[0] = bit0 > 0
    res[1] = bit1 > 0
    res[2] = bit2 > 0
    res[3] = bit3 > 0
    res[4] = bit4 > 0
    res[5] = bit5 > 0
    res[6] = bit6 > 0
    res[7] = bit7 > 0
    return res
}

fun getByte(arr: BooleanArray): Byte {
    var currInt = 0
    currInt += 128 * if (arr[0]) 1 else 0
    currInt += 64 * if (arr[1]) 1 else 0
    currInt += 32 * if (arr[2]) 1 else 0
    currInt += 16 * if (arr[3]) 1 else 0
    currInt += 8 * if (arr[4]) 1 else 0
    currInt += 4 * if (arr[5]) 1 else 0
    currInt += 2 * if (arr[6]) 1 else 0
    currInt += 1 * if (arr[7]) 1 else 0
    val byte = currInt - 128
    return byte.toByte()
}

fun ArrayList<Boolean>.toByteArray(): ByteArray {
    val byteArray = ByteArray(this.size / 8)
    for (i in 0 until this.size / 8) {
        byteArray[i] = getByte(BooleanArray(8) {
            this[i * 8 + it]
        })
    }
    return byteArray
}

fun booleanArrayToByteArray(booll: List<Boolean>): ByteArray {
    val byteArray = ByteArray(booll.size / 8)
    for (i in 0 until booll.size / 8) {
        byteArray[i] = getByte(BooleanArray(8) {
            booll[i * 8 + it]
        })
    }
    return byteArray
}

fun computePsnr(bitmap1: Bitmap, bitmap2: Bitmap): Double {
    var mse = 0.0
    val width = bitmap1.width
    val height = bitmap1.height
    for (x in 0 until width) {
        for (y in 0 until height) {
            var err = 0.0
            err += Math.abs((Color.blue(bitmap1.getPixel(x, y)) - Color.blue(bitmap2.getPixel(x, y))) * (Color.blue(bitmap1.getPixel(x, y)) - Color.blue(bitmap2.getPixel(x, y))))
            err += Math.abs((Color.red(bitmap1.getPixel(x, y)) - Color.red(bitmap2.getPixel(x, y))) * (Color.red(bitmap1.getPixel(x, y)) - Color.red(bitmap2.getPixel(x, y))))
            err += Math.abs((Color.green(bitmap1.getPixel(x, y)) - Color.green(bitmap2.getPixel(x, y))) * (Color.green(bitmap1.getPixel(x, y)) - Color.green(bitmap2.getPixel(x, y))))
            mse += err / 3
        }
    }
    mse /= width * height// * 3
    return 10 * Math.log10(255.0 * 255 / mse)
}

fun computeSF(i: Bitmap, iw: Bitmap): Double {
    var sum1 = 0.0
    var sum2 = 0.0
    val width = i.width
    val height = i.height
    for (x in 0 until width) {
        for (y in 0 until height) {
            sum1 += (Color.blue(i.getPixel(x, y))*Color.blue(iw.getPixel(x, y)) + Color.red(i.getPixel(x, y))*Color.red(iw.getPixel(x, y)) + Color.green(i.getPixel(x, y))*Color.green(iw.getPixel(x, y))) / 3.0
            sum2 += (Color.blue(iw.getPixel(x, y))*Color.blue(iw.getPixel(x, y)) + Color.red(iw.getPixel(x, y))*Color.red(iw.getPixel(x, y)) + Color.green(iw.getPixel(x, y))*Color.green(iw.getPixel(x, y))) / 3.0
        }
    }
    return sum1/sum2
}

fun generateTextToPercentage(bitmap: Bitmap, percentage: Int): String {
    val blockNcount = bitmap.height / 8
    val blockMcount = bitmap.width / 8
    val maxTextSize = blockMcount * blockNcount - (TEXT_ID.toByteArray().size * 8 + 4 * 8)
    val p = percentage / 100.0
    val byteArray = ByteArray((p * maxTextSize / 8).toInt())
    val random = Random()
    random.nextBytes(byteArray)
    return String(byteArray)
}