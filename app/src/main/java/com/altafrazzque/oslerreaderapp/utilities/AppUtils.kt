package com.altafrazzque.oslerreaderapp.utilities

import com.altafrazzque.oslerreaderapp.model.User
import com.altafrazzque.oslerreaderapp.model.UserStatus
import java.io.*


object AppUtils {

    fun hexToBin(hex: String): String? {
        val i = hex.toInt(16)
        var bin = Integer.toBinaryString(i)
        while (bin.length < 8) {
            bin = "0$bin"
        }
        return bin
    }

    fun getUser(record: String): User {
        val parts = record.split("\t").toTypedArray()
        val uuid = parts[0]
        val deviceId = parts[1]
        val status = getUserStatus(parts[2])
        return User(uuid, deviceId, status)
    }
    private fun getUserStatus(hexString: String): UserStatus {
        val binary = hexToBin(hexString.replace("0x", ""))
        val authorisation = "" + binary!![0] //bit 7
        val training = "" + binary[1] //bit 6
        val isAdmin = "" + binary[2] //bit 5
        return UserStatus(authorisation, training, isAdmin)
    }



}
