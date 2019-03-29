package com.matek3022.vkmessagernextgen.utils.stega

import org.apache.commons.math3.linear.MatrixUtils

/**
 * @author matek3022 (semenovmm@altarix.ru)
 *         on 29.03.19.
 */

val dctmtx = arrayOf(
    doubleArrayOf(0.353553, 0.353553, 0.353553, 0.353553, 0.353553, 0.353553, 0.353553, 0.353553),
    doubleArrayOf(0.490393, 0.415735, 0.277785, 0.097545, -0.097545, -0.277785, -0.415735, -0.490393),
    doubleArrayOf(0.461940, 0.191342, -0.191342, -0.461940, -0.461940, -0.191342, 0.191342, 0.461940),
    doubleArrayOf(0.415735, -0.097545, -0.490393, -0.277785, 0.277785, 0.490393, 0.097545, -0.415735),
    doubleArrayOf(0.353553, -0.353553, -0.353553, 0.353553, 0.353553, -0.353553, -0.353553, 0.353553),
    doubleArrayOf(0.277785, -0.490393, 0.097545, 0.415735, -0.415735, -0.097545, 0.490393, -0.277785),
    doubleArrayOf(0.191342, -0.461940, 0.461940, -0.191342, -0.191342, 0.461940, -0.461940, 0.191342),
    doubleArrayOf(0.097545, -0.277785, 0.415735, -0.490393, 0.490393, -0.415735, 0.277785, -0.097545)
)

fun dct(value: Array<DoubleArray>): Array<DoubleArray> {
    val dctmtxMatr = MatrixUtils.createRealMatrix(dctmtx)
    val valueMatr = MatrixUtils.createRealMatrix(value)
    return dctmtxMatr.multiply(valueMatr).multiply(dctmtxMatr.transpose()).data
}

fun idct(value: Array<DoubleArray>): Array<DoubleArray> {
    val dctmtxMatr = MatrixUtils.createRealMatrix(dctmtx)
    val valueMatr = MatrixUtils.createRealMatrix(value)
    return dctmtxMatr.transpose().multiply(valueMatr).multiply(dctmtxMatr).data
}