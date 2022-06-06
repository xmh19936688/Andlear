package com.xmh.andlear

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.xmh.andlear.databinding.ActivityMainBinding
import java.lang.Exception
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {
    companion object {
        private const val REQUEST_EXTERNAL_STORAGE = 1
        private val PERMISSIONS_STORAGE = arrayOf(
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"
        )
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnClear.setOnClickListener {
            if (!verifyStoragePermission(this)) {
                return@setOnClickListener
            }

            binding.btnClear.isEnabled = false
            thread {
                clear()

                binding.btnClear.post {
                    AlertDialog.Builder(this@MainActivity)
                        .setMessage("Android clear finish!")
                        .setPositiveButton("确定") { dia: DialogInterface, i: Int ->
                            dia.dismiss()
                        }
                        .create()
                        .show()
                    binding.btnClear.isEnabled = true
                }
            }

        }
    }

    private fun verifyStoragePermission(activity: Activity): Boolean {
        try {
            val permisssion = ActivityCompat.checkSelfPermission(
                activity,
                "android.permission.WRITE_EXTERNAL_STORAGE"
            )
            if (permisssion != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
                ) //弹出权限申请对话框
            } else {
                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    private fun clear() {
        cmds.forEach {
            Runtime.getRuntime().exec(it)
        }
        cmds.forEach {
            Runtime.getRuntime().exec(it.replace("/0/", "/10/"))
        }
    }
}