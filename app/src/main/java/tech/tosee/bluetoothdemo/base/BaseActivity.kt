package tech.tosee.bluetoothdemo.base

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import tech.tosee.bluetoothdemo.R
import tech.tosee.bluetoothdemo.utils.PermissionUtil

/*
 * @author: geh
 * @version: V100R001C01
 * @date: 2021/7/13
 */open class BaseActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)

    }


    private val PERMISSION_CODE = 1000

    open fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            PERMISSION_CODE
        )
    }

    open fun requestMicPermission() {

        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.RECORD_AUDIO),
            PERMISSION_CODE)

    }

    open fun requestAllPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf( Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO,Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for (i in permissions.indices) {
            val permission = permissions[i]!!
            val result = grantResults[i]
            if (permission == Manifest.permission.RECORD_AUDIO) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    goSetting(false)
                } else {
                    microphonePermissionGenerated()
                }
            }

        }

    }

    protected open fun microphonePermissionGenerated() {

    }

    private fun goSetting(camera: Boolean) {
        var alertDialog = AlertDialog.Builder(this)
            .setMessage("必须的权限没有得到授权，功能将无法使用 请重新授权!")
            .setNegativeButton(
                "不"
            ) { dialog, which ->
                dialog.dismiss()
                finish()
            }
            .setPositiveButton(
                "好的"
            ) { dialog, which ->
                PermissionUtil.gotoPermission(baseContext)

//                requestPermission(camera)
            }.create()
        alertDialog.show()

        var button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
        button.setTextColor(ContextCompat.getColor(this, R.color.purple_200))
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
            .setTextColor(ContextCompat.getColor(this, R.color.purple_200))

    }

}