package tech.tosee.bluetoothdemo.utils

import kotlin.experimental.and


class AudioUtil {

    companion object {

        val FOUR_MIC_TYPE = 1
        val SIX_WITH_MIC_TYPE = 2;
        fun culateVolume(buffer: ByteArray): Int {
            var sumVolume = 0.0
            var avg = 0.0
            var volume = 0.0
            var i = 0
            while (i < buffer.size) {
                val v1: Int = buffer[i].and(0xff.toByte()).toInt()
                val v2: Int = buffer[i + 1]. and (0xff.toByte()).toInt()
                var temp = v1 + (v2 shl 8)
                if (temp >= 0x8000) {
                    temp = 0xffff - temp
                }
                sumVolume += Math.abs(temp).toDouble()
                i += 2
            }
            avg = sumVolume / buffer.size / 2
            volume = Math.log10(1 + avg) * 24
            return (if(volume < 0) 0.0 else volume).toInt()
        }

        fun doubleCulateVolume(buffer: ByteArray,type: Int): Array<String> {
            var sumVolume = 0.0
            var volume = 0.0
            var sumVolume1 = 0.0
            var volume1 = 0.0
            var sumVolume2 = 0.0
            var volume2 = 0.0
            var sumVolume3 = 0.0
            var volume3 = 0.0

            val size = buffer.size / 8
            var i = 0
            while (i < buffer.size) {
                val v1: Int = buffer[i].and(0xff.toByte()).toInt()
                val v2: Int = buffer[i + 1].and(0xff.toByte()).toInt()
                var temp = v1 + (v2 shl 8)
                if (temp >= 0x8000) {
                    temp = 0xffff - temp
                }

                val v3: Int = buffer[i + 2].and(0xff.toByte()).toInt()
                val v4: Int = buffer[i + 3].and(0xff.toByte()).toInt()
                var temp1 = v3 + (v4 shl 8)
                if (temp1 >= 0x8000) {
                    temp1 = 0xffff - temp1
                }

                val v5: Int = buffer[i + 4].and(0xff.toByte()).toInt()
                val v6: Int = buffer[i + 5].and(0xff.toByte()).toInt()
                var temp2 = v5 + (v6 shl 8)
                if (temp2 >= 0x8000) {
                    temp2 = 0xffff - temp2
                }

                val v7: Int = buffer[i + 6].and(0xff.toByte()).toInt()
                val v8: Int = buffer[i + 7].and(0xff.toByte()).toInt()
                var temp3 = v7 + (v8 shl 8)
                if (temp3 >= 0x8000) {
                    temp3 = 0xffff - temp3
                }

                sumVolume += Math.abs(temp).toDouble()
                sumVolume1 += Math.abs(temp1).toDouble()
                sumVolume2 += Math.abs(temp2).toDouble()
                sumVolume3 += Math.abs(temp3).toDouble()
                when(type){
                    FOUR_MIC_TYPE -> i += 8
                    SIX_WITH_MIC_TYPE -> i += 12
                }
            }

            volume = Math.log10(1 + sumVolume / size) * 24
            volume1 = Math.log10(1 + sumVolume1 / size) * 24
            volume2 = Math.log10(1 + sumVolume2 / size) * 24
            volume3 = Math.log10(1 + sumVolume3 / size) * 24
            volume = if(volume < 0) 0.0 else volume
            volume1 = if(volume1 < 0) 0.0 else volume1
            volume2 = if(volume2 < 0) 0.0 else volume2
            volume3 = if(volume3 < 0) 0.0 else volume3

            val v0 = if(volume1.toInt() < 10) ("0" + volume1.toInt()) else volume1.toInt().toString()
            val v1 = if(volume.toInt() < 10) ("0" + volume.toInt()) else volume.toInt().toString()
            val v2 = if(volume3.toInt() < 10) ("0" + volume3.toInt()) else volume3.toInt().toString()
            val v3 = if(volume2.toInt() < 10) ("0" + volume2.toInt()) else volume2.toInt().toString()

            return arrayOf(v0, v1 , v2, v3 )
        }




    }


}