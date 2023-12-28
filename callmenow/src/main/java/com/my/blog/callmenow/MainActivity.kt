package com.my.blog.callmenow

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.telephony.TelephonyManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.my.blog.callmenow.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private val launcher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                callPhone()
            } else {
                Toast.makeText(this, "deny", Toast.LENGTH_SHORT).show()
            }
        }

    private fun callPhone() {
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:13086092815")
//        intent.setPackage("com.android.incallui")

        startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.button.setOnClickListener {
            checkCallPhonePermission()
        }

        binding.stop.setOnClickListener {
            handUp()
        }

    }

    private fun checkCallPhonePermission() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            callPhone()
            return
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
            AlertDialog.Builder(this).setTitle("Call Phone").setMessage("Request Permission")
                .setPositiveButton("yes", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        launcher.launch(Manifest.permission.CALL_PHONE)
                    }
                }).setNegativeButton("No",null).show()
            return
        }
        launcher.launch(Manifest.permission.CALL_PHONE)
    }

    private fun handUp(){
        try {
            // 创建一个电话应用实例
            val telephonyManager = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
            // 获取ITelephony接口
            val telephonyClass = Class.forName(telephonyManager.javaClass.name)
            val endCallMethod = telephonyClass.getMethod("endCall")
            // 调用挂断电话方法
            endCallMethod.invoke(telephonyManager)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}