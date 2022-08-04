package com.altafrazzque.oslerreaderapp.utilities

import android.content.Context
import android.util.Log
import com.altafrazzque.oslerreaderapp.model.User
import com.altafrazzque.oslerreaderapp.model.UserStatus
import java.io.*
import java.util.*


object FileManager {
    private val TAG = FileManager::class.simpleName
    fun copyAssetsToStorage(fileName:String, context: Context) {
        val assetManager = context.assets
        var files: Array<String>? = null
        try {
            files = assetManager.list(fileName)
        } catch (e: IOException) {
            Log.e(TAG,"Failed to get asset file list. ${e.message}")
        }

        for (filename in files!!) {
            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null
            try {
                inputStream = assetManager.open("$filename")
                val outFile = File(Constants.appDir + "/", filename)

                outputStream = FileOutputStream(outFile)
                copyFile(inputStream, outputStream)
                inputStream.close()
                inputStream = null
                outputStream.flush()
                outputStream.close()
                outputStream = null
            } catch (e: IOException) {
                Log.e(TAG,"Failed to copy asset file list. ${e.message}")
            }
        }
    }

    @Throws(IOException::class)
    private fun copyFile(inputStream: InputStream, outputStream: OutputStream) {
        val buffer = ByteArray(1024)
        var read: Int
        while (inputStream.read(buffer).also { read = it } != -1) {
            outputStream.write(buffer, 0, read)
        }
    }

    fun readFile(path: String): ArrayList<User> {
        val userList: java.util.ArrayList<User> = java.util.ArrayList()
        try {
            val myObj = File(path)
            val myReader = Scanner(myObj)
            while (myReader.hasNextLine()) {
                val data: String = myReader.nextLine()
                if (data.isNotEmpty()) {
                    val user: User? = AppUtils.getUser(data)
                    if(user!=null)
                        userList.add(user)
                }
            }
            myReader.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        return userList
    }

    fun readUpdatedFile(): ArrayList<User> {
        val userList: java.util.ArrayList<User> = java.util.ArrayList()
        try {
            val myObj = File(Constants.appDir+File.separator+Constants.deviceUserListUpdated)
            val myReader = Scanner(myObj)
            while (myReader.hasNextLine()) {
                val data: String = myReader.nextLine()
                if (data.isNotEmpty()) {


                    val parts = data.split("\t").toTypedArray()
                    val uuid = parts[0]
                    val deviceId = parts[1]
                    val auth = parts[2]
                    val userType = parts[3]
                    val training = parts[4]

                    val user = User(
                        uuid = uuid,
                        deviceId = deviceId,
                        userStatus = UserStatus(
                            authorisation = auth,
                            isAdmin = userType,
                            training = training
                        ))

                    userList.add(user)
                }
            }
            myReader.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        return userList

    }

    //create file in device to store updated users
    fun updateUsers(deviceUsers: ArrayList<User>) {
        Log.e(TAG,"Update status in file")
        try {
            val file = File(Constants.appDir+File.separator+Constants.deviceUserListUpdated)
            if (!file.exists()) file.createNewFile()
            val writer = PrintWriter(file.absolutePath, "UTF-8")
            for (user in deviceUsers) {
                var type = ""
                var authorzation = ""
                var training = ""
                authorzation = if (user.userStatus.authorisation == "1") {
                    "Authorised\t"
                } else {
                    "Disabled\t"
                }
                type = if (user.userStatus.isAdmin == "1") {
                    "Admin\t"
                } else {
                    "Operator\t"
                }
                training = if (user.userStatus.training == "1") {
                    "Trained"
                } else {
                    "Untrained"
                }
                writer.println(user.uuid + "\t" + user.deviceId + "\t" + authorzation + type + "\t" + training)
            }
            writer.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun getDeviceUserList(): ArrayList<User>{
        return  readFile(Constants.appDir+File.separator+Constants.deviceUserList)
    }

}