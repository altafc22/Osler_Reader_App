package com.altafrazzque.oslerreaderapp.ui

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.altafrazzque.oslerreaderapp.MainApp
import com.altafrazzque.oslerreaderapp.R
import com.altafrazzque.oslerreaderapp.adpater.UserListAdapterListener
import com.altafrazzque.oslerreaderapp.adpater.UserStatusListAdapter
import com.altafrazzque.oslerreaderapp.base.BaseActivity
import com.altafrazzque.oslerreaderapp.databinding.ActivityMainBinding
import com.altafrazzque.oslerreaderapp.model.User
import com.altafrazzque.oslerreaderapp.utilities.AppStatus
import com.altafrazzque.oslerreaderapp.utilities.Constants
import com.altafrazzque.oslerreaderapp.utilities.FileManager
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.*
import java.net.URL
import java.net.URLConnection
import java.util.*
import java.util.stream.Collectors


class MainActivity : BaseActivity(), UserListAdapterListener {

    private val TAG = MainActivity::class.simpleName
    private lateinit var mainBinding: ActivityMainBinding

    private var deviceUserList = ArrayList<User>()
    private var portalUserList = ArrayList<User>()
    private var updatedUserList = ArrayList<User>()

    private lateinit var adapter : UserStatusListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        setViews()

        checkPermission()
    }

    private fun setViews(){
        adapter = UserStatusListAdapter(updatedUserList, this)
        mainBinding.rvItems.adapter = adapter
        loadUpdatedData()
    }

    private fun loadUpdatedData(){
        val updatedList = FileManager.readUpdatedFile()
        adapter.clearData()
        adapter.addData(updatedList)
    }
    private fun checkPermission(){
        val permissions = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val rationale = "Please provide Storage permission so that you can ..."
        val options = Permissions.Options()
            .setRationaleDialogTitle("Info")
            .setSettingsDialogTitle("Warning")

        Permissions.check(this /*context*/, permissions, rationale, options,
            object : PermissionHandler() {
                override fun onGranted() {
                    mainApp.createAppDir()
                    checkFiles()

                    getPortalUserList()
                    FileManager.getDeviceUserList()
                }

                override fun onDenied(context: Context, deniedPermissions: ArrayList<String>) {
                    // permission denied, block the feature.
                    showSnackBar("Please allow storage permissions ")
                }
            }
        )

    }
    private fun checkFiles(){
        val deviceUserFile = File(Constants.appDir+File.separator+Constants.deviceUserList)
        if(!deviceUserFile.exists()){
            FileManager.copyAssetsToStorage(Constants.deviceUserList,this)
            Log.e(TAG,"deviceUserFile copied to external storage")
        } else {
            Log.e(TAG,"deviceUserFile already exist in external storage")
        }
        val portalUserFile = File(Constants.appDir+File.separator+Constants.portalUserList)
        if(!deviceUserFile.exists()){
            FileManager.copyAssetsToStorage(Constants.portalUserList,this)
            Log.e(TAG,"portalUserFile copied to external storage")
        } else {
            Log.e(TAG,"portalUserFile already exist in external storage")
        }
    }

    private fun showErrorView(visible:Boolean){
        if(visible)
            mainBinding.noInternetView.visibility = View.VISIBLE
        else
            mainBinding.noInternetView.visibility = View.GONE
    }

    private fun showLoadingView(visible:Boolean){
       runOnUiThread {
           if(visible)
               mainBinding.loadingLayout.visibility = View.VISIBLE
           else
               mainBinding.loadingLayout.visibility = View.GONE
       }
    }

    fun getPortalUserList() {
        val isOnline = AppStatus.getInstance(MainApp.getContext()).isOnline
        if(isOnline){
            showLoadingView(true)
            downloadPortalUserFile() //download latest portal file from api
        } else {
           updateDetails()
        }
    }

    private fun downloadPortalUserFile() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val url = Constants.portalUserList
                val filePath = Constants.appDir+File.separator+Constants.localPortalUserList

                val oldFile = File(filePath)
                if(oldFile.exists())
                    oldFile.delete()

                val outputFile = File(filePath)

                val u = URL(url)
                val conn: URLConnection = u.openConnection()
                val contentLength: Int = conn.contentLength
                val stream = DataInputStream(u.openStream())
                val buffer = ByteArray(contentLength)
                stream.readFully(buffer)
                stream.close()
                val fos = DataOutputStream(FileOutputStream(outputFile))
                fos.write(buffer)
                fos.flush()
                fos.close()

                Log.e(TAG,"PORTAL FILE DOWNLOADED")
                portalUserList = FileManager.readFile(filePath)
                updateDetails()

            } catch (e: FileNotFoundException) {
              Log.e(TAG,"Exception: ${e.message}")
            } catch (e: IOException) {
                Log.e(TAG,"Exception: ${e.message}")
            }
            finally {
                showLoadingView(false)
            }
        }
    }

    //update latest changes and create updated user list file
    private fun updateDetails(){
        portalUserList = FileManager.readFile(Constants.appDir + File.separator + Constants.localPortalUserList)
        deviceUserList = FileManager.readFile(Constants.appDir + File.separator + Constants.deviceUserList)
        if(portalUserList.isEmpty())

        updatedUserList.clear()

        for (user in deviceUserList) {
            val updatedUser: User? = getUpdatedData(user)
            if (updatedUser != null) updatedUserList.add(updatedUser)
        }
        FileManager.updateUsers(updatedUserList)

        runOnUiThread { loadUpdatedData() }
    }

    //get updated status for user by UUID and device id
    private fun getUpdatedData(user: User): User? {
        //find portal users based on UUID
        val usersFromPortal: List<User> = portalUserList.stream().filter { item ->
            item.uuid == user.uuid
        }.collect(Collectors.toList())
        var updatedUser: User? = null

        //check user status is available in portal list or not
        if (!usersFromPortal.isEmpty()) {
            for (portalUser in usersFromPortal) {
                //verify device id to update correct user status
                if (portalUser.deviceId === user.deviceId) {
                    updatedUser = portalUser
                    break
                }
            }
        } else { //get current status from device user list
            //User fromDevice = deviceUsers.stream().filter(deviceUser -> user.uuid.equals(user.uuid)).findAny().orElse(null);
            val usersFromDevice: List<User> = deviceUserList.stream().filter { item ->
                item.uuid == user.uuid
            }.collect(Collectors.toList())
            if (usersFromDevice.isNotEmpty()) {
                updatedUser = usersFromDevice[usersFromDevice.size - 1] //getting current status from device list
            }
        }
        return updatedUser
    }

    override fun onItemClick(user: User) {
        Log.e(TAG, "Item clicked ${user.uuid}")
        val dialog = Dialog(this, R.style.DialogStyle)
        dialog.setContentView(R.layout.dialog_details)
        val btnClose = dialog.findViewById<LinearLayout>(R.id.btnClose)
        val tvUUID = dialog.findViewById<TextView>(R.id.tvUuid)
        val tvUser = dialog.findViewById<TextView>(R.id.tvUserType)
        val tvDeviceId = dialog.findViewById<TextView>(R.id.tvDeviceId)
        val tvAuthStatus = dialog.findViewById<TextView>(R.id.tvAuthorised)
        val tvTrainingStatus = dialog.findViewById<TextView>(R.id.tvTraining)

        tvUUID.text = user.uuid
        tvUser.text = if (user.userStatus.isAdmin == "1") "Admin" else "Operator"
        tvDeviceId.text = user.deviceId
        tvAuthStatus.text =  if (user.userStatus.authorisation == "1") "Authorized" else "Disabled"
        tvTrainingStatus.text = if (user.userStatus.training == "1") "Trained" else "Untrained"

        btnClose.setOnClickListener{ dialog.dismiss() }
        dialog.show()
    }
}