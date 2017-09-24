package org.hiucimon.utility

internal fun Byte.Value() : Int {
    val i = this.toInt()
    val byte2 = i and 0x0f
    val byte1 = i shr 4 and 0x0f
    return byte1*16+byte2
}

fun Dump(data:ArrayList<Int>,start:Int=0,linesize:Int=32) :Int{
    var work=data
    var offset=start
    val SPACES="                                                                       "
    while (work.size>0) {
        val prefix="${offset.toHexString(6)} "
//        print("${offset.toHexString(6)} ")
        val line=work.take(linesize)
//        for (i in 0..line.size-1) {
//            work.remove(0)
//        }
        val hexpart=StringBuilder()
        line.forEach {
            hexpart.append("${it.toHexString(2)}")
//            print("${it.toHexString(2)}")
        }
//        print(" ")
        val charpart=StringBuilder()
        line.forEach {
            val tmp=EBCBIC[it]!!.toChar()
            val tmp2=if (tmp.isPrint()) tmp else '.'
            charpart.append("${tmp2}")
//            print("${tmp2}")
        }
        hexpart.append(SPACES.take(linesize*2-hexpart.length))
        charpart.append(SPACES.take(linesize-charpart.length))
        println("${prefix} *${hexpart}* *${charpart}*")
        offset+=linesize
        val tmp=work.drop<Int>(linesize)
        work= if (tmp.isEmpty()) arrayListOf<Int>() else tmp as ArrayList<Int>
    }
    return start+data.size
}

fun Int.toHexString(size:Int=99999):String {
    return java.lang.Integer.toHexString(this).takeLast(size).padStart(size,'0')
}

fun Byte.toHexString(size:Int=99999):String {
    return this.toInt().toHexString(size)
}

fun String.Hex():Int {
    var res=0
    val digits= hashMapOf<Char,Int>('0' to 0,'1' to 1,'2' to 2,
            '3' to 3,'4' to 4,'5' to 5,'6' to 6,'7' to 7,'8' to 8,
            '9' to 9,'a' to 10,'b' to 11,'c' to 12,'d' to 13,'e' to 14,'f' to 15)
    val data=this.toLowerCase().toCharArray()
    data.forEach {
        val tmp=digits[it.toChar()]
        if (digits.containsKey(it.toChar())) {
            res = res shl 4 or tmp!!
        }
    }
    return res
}

fun genHexConst() {
    val digits=arrayListOf('0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f')
    for (i in digits) {
        for (j in digits) {
            print("const val X${i}${j} = 0x00${i}${j}.toInt()\n")
        }
    }
}

inline fun Char.isPrint():Boolean =
        if ((this.toInt()<0x20)or(this.toInt()>0x7e)) false else true

