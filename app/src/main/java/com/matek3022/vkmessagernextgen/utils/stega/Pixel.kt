package com.matek3022.vkmessagernextgen.utils.stega

data class Pixel(var red: Int, var green: Int, var blue: Int) {
    fun compare(pixel: Pixel) = Math.abs(red - pixel.red) + Math.abs(green - pixel.green) + Math.abs(blue - pixel.blue)
    fun maxDiff(pixel: Pixel): Int{
        val a1 = Math.abs(red - pixel.red)
        val a2 = Math.abs(green - pixel.green)
        val a3 = Math.abs(blue - pixel.blue)
        return Math.max(a1, Math.max(a2, a3))
    }

}