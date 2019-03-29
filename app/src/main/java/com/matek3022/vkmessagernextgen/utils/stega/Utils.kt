package com.matek3022.vkmessagernextgen.utils.stega

import android.content.res.Resources
import android.graphics.Bitmap
import java.util.*
import kotlin.collections.ArrayList

/**
 * if true then 1
 * if false then 0
 */

const val seed = 4L

const val TEXT_ID = "text"

fun Bitmap.codeText(text: String): Bitmap {
    val bitArray = ArrayList<Boolean>()
    val byteArray = text.toByteArray()
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
    val pixels = getPixels()
    pixels.inToPixels(bitArray)
    setPixels(pixels, 1)
    return this
}

@Throws(Resources.NotFoundException::class)
fun Bitmap.getText(): String {
    val pixels = getPixels()
    val bits = pixels.fromPixels(TEXT_ID)
    val byteArray = booleanArrayToByteArray(bits)
    return String(byteArray)
}

fun ArrayList<ArrayList<Int>>.inToPixels(bitsArray: ArrayList<Boolean>) {
    val n = this.size
    val m = this[0].size
    var matrYColor = Array(8){ DoubleArray(8) }
    val bitsCount = bitsArray.size
    val random = Random(seed)
    val blockNcount = n / 8
    val blockMcount = m / 8
    val maxBlocks = blockNcount * blockMcount
    val randomIndexes = IntArray(maxBlocks) { i -> i }.toMutableList()
    randomIndexes.shuffle(random)

    var bitsInput = 0

    for (i in 0 until blockNcount) {
        for (j in 0 until blockMcount) {

            if (bitsInput < bitsCount) {
                val currIndex = i * blockMcount + j
                val findingIndex = randomIndexes.indexOf(currIndex)
                if (findingIndex != -1) {
                    for (ik in 0..7) {
                        for (jk in 0..7) {
                            matrYColor[ik][jk] = getY(this[i * 8 + ik][j * 8 + jk])
                        }
                    }
                    matrYColor = dct(matrYColor)
                    val bit = bitsArray[bitsInput]
                    if (bit) {
                        matrYColor[7][7] = 100.0
                    } else {
                        matrYColor[7][7] = -100.0
                    }
                    bitsInput++
                    matrYColor = idct(matrYColor)
                    for (ik in 0..7) {
                        for (jk in 0..7) {
                            this[i * 8 + ik][j * 8 + jk] = toRGB(
                                matrYColor[ik][jk],
                                getCb(this[i * 8 + ik][j * 8 + jk]),
                                getCr(this[i * 8 + ik][j * 8 + jk])
                            )
                        }
                    }
                }
            }
        }
    }
}

@Throws(Resources.NotFoundException::class)
fun ArrayList<ArrayList<Int>>.fromPixels(id: String): List<Boolean> {
    val n = this.size
    val m = this[0].size
    var matrYColor = Array(8){ DoubleArray(8) }
    val random = Random(seed)
    val blockNcount = n / 8
    val blockMcount = m / 8
    var bitsCount = blockMcount * blockNcount / 4
    val maxBlocks = blockNcount * blockMcount
    val randomIndexes = IntArray(maxBlocks) { i -> i }.toMutableList()
    randomIndexes.shuffle(random)
    val res = ArrayList<Boolean>()

    var bitsRead = 0

    val idBytes = id.toByteArray()

    for (i in 0 until blockNcount) {
        for (j in 0 until blockMcount) {
            if (bitsRead < bitsCount) {
                if (bitsRead < bitsCount) {
                    val currIndex = i * blockMcount + j
                    val findingIndex = randomIndexes.indexOf(currIndex)
                    if (findingIndex != -1) {
                        for (ik in 0..7) {
                            for (jk in 0..7) {
                                matrYColor[ik][jk] = getY(this[i * 8 + ik][j * 8 + jk])
                            }
                        }
                        matrYColor = dct(matrYColor)
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

fun Bitmap.getPixels(): ArrayList<ArrayList<Int>> {
    val pixels = ArrayList<ArrayList<Int>>()
    for (i in 0 until width) {
        pixels.add(ArrayList())
        for (j in 0 until height) {
            val bitmapPixel = this.getPixel(i, j)
            pixels[i].add(
                bitmapPixel
//                Pixel(
//                    Color.red(bitmapPixel),
//                    Color.green(bitmapPixel),
//                    Color.blue(bitmapPixel)
//                )
            )
        }
    }
    return pixels
}

fun Bitmap.setPixels(pixels: ArrayList<ArrayList<Int>>, corr: Int = 256) {
    for (i in 0 until width) {
        for (j in 0 until height) {
            val pixel = pixels[i][j]
            this.setPixel(i, j, pixel/*Color.rgb(pixel.red / corr, pixel.green / corr, pixel.blue / corr)*/)
        }
    }
}

private val Q = intArrayOf(
    16, 11, 10, 16, 24, 40, 51, 61,
    12, 12, 14, 19, 26, 58, 60, 55,
    14, 13, 16, 24, 40, 57, 69, 56,
    14, 17, 22, 29, 51, 87, 80, 62,
    18, 22, 37, 56, 68, 109, 103, 77,
    24, 35, 55, 64, 81, 104, 113, 92,
    49, 64, 78, 87, 103, 121, 120, 101,
    72, 92, 95, 95, 112, 100, 103, 99
)

fun DoubleArray.devideArray(x: Int) {
    this.forEachIndexed { index, fl ->
        this[index] = fl / x
    }
}

fun toQuantiz(arr: DoubleArray) {
    arr.forEachIndexed { index, fl ->
        arr[index] = Math.round(fl / Q[index]).toDouble()
    }
}

fun fromQuantiz(arr: DoubleArray) {
    arr.forEachIndexed { index, fl ->
        arr[index] = Math.round(fl * Q[index]).toDouble()
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
    var mse = 0L
    val pixels1 = bitmap1.getPixels()
    val pixels2 = bitmap2.getPixels()
    pixels1.forEachIndexed { index1, arrayList ->
        arrayList.forEachIndexed { index2, pixel ->
            //            mse += Math.abs((pixel.blue - pixels2[index1][index2].blue) * (pixel.blue - pixels2[index1][index2].blue))
        }
    }
    mse /= pixels1.size * pixels1[0].size
    return 10 * Math.log10(255.0 * 255 / mse)
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