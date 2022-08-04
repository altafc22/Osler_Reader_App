package com.altafrazzque.oslerreaderapp.utilities


import android.os.Environment
import com.altafrazzque.oslerreaderapp.MainApp
import java.io.File

object Constants {
    const val BASE_TIMEOUT = 15L

    val portalUserList = "https://gist.githubusercontent.com/altafc22/8d3ae42989532514bcbd042bdf7ee6d6/raw/5568c450cbc72e54134de2756adeeede14eb9eaf/PortalUserList.txt"

    //val dir = "Osler files"
    val appDir = "${Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_DOCUMENTS).path}/Osler files"
    val deviceUserList = "DeviceUserList.txt"
    val localPortalUserList = "PortalUserList.txt"
    val deviceUserListUpdated = "DeviceUserListUpdated.txt"
}