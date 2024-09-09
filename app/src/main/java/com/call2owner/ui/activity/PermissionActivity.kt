package com.call2owner.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.call2owner.R
import com.call2owner.databinding.ActivityPermissionBinding
import com.call2owner.utils.MyUtil.showSimpleAlertDialog
import com.call2owner.utils.MyUtil.toast
import java.io.Serializable

class PermissionActivity : AppCompatActivity() {
    lateinit var binding: ActivityPermissionBinding
    private var selectedPermissionType: PermissionModel? = null
    lateinit var context: Context
    val map = HashMap<PermissionType, PermissionModel>()
    var isBluetooth=false

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPermissionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        context = this
        initView()
        initClicks()

        onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (selectedPermissionType == map[PermissionType.LOCATION])
                    showMandatoryInfoDialog()
            }
        })
    }



    private fun initView() {
        window.statusBarColor = ContextCompat.getColor(context, R.color.md_theme_background)
        map[PermissionType.LOCATION] = PermissionModel(
            getString(R.string.permission_required),
            getString(R.string.location_permission_message),
            R.raw.location,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        map[PermissionType.CAMERA] = PermissionModel(
            getString(R.string.permission_required),
            getString(R.string.camera_permission_message),
            R.raw.camera,
            Manifest.permission.CAMERA
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            map[PermissionType.NOTIFICATION] = PermissionModel(
                getString(R.string.get_notified),
                getString(R.string.notified_details),
                R.raw.json_notification,
                Manifest.permission.POST_NOTIFICATIONS
            )
        }
        map[PermissionType.CONTACT] = PermissionModel(
            getString(R.string.permission_required),
            getString(R.string.permission_contact_message),
            R.raw.contact_permission,
            Manifest.permission.READ_CONTACTS
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            map[PermissionType.NEARBY] = PermissionModel(
                getString(R.string.permission_required),
                getString(R.string.permission_nearby_message),
                R.raw.bluetooth,
                Manifest.permission.BLUETOOTH_CONNECT
            )
        }

        val permissionType: PermissionType =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent.getSerializableExtra("type", PermissionType::class.java)!!
            } else {
                @Suppress("DEPRECATION")
                intent.getSerializableExtra("type") as PermissionType
            }

        if(permissionType== PermissionType.NEARBY){
            isBluetooth=true
        }

        setValues(map[permissionType])

    }

    private fun setValues(model: PermissionModel?) {
        if (model != null) {
            selectedPermissionType = model
            binding.apply {
                permissionTitle.text = model.title
                permissionDesc.text = model.description
                permissionLottie.setAnimation(model.lottieIcon)
                permissionTitle.text = model.title
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun initClicks() {
        binding.apply {
            skipBtn.setOnClickListener {
                if (selectedPermissionType == map[PermissionType.LOCATION])
                    showMandatoryInfoDialog()
                else
                    showSimpleAlertDialog(
                        getString(R.string.permission_skipped),
                        getString(R.string.location_msg_if_dont_allow),
                        true,
                        ::higherFun
                    )
            }

            allowBtn.setOnClickListener {
                if(isBluetooth){
                    requestMultiplePermissionLauncher.launch( arrayOf(
                        Manifest.permission.BLUETOOTH,
                        Manifest.permission.BLUETOOTH_ADMIN,
                        Manifest.permission.BLUETOOTH_SCAN,
                        Manifest.permission.BLUETOOTH_CONNECT,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ))

                }else{
                    requestPermissionLauncher.launch(selectedPermissionType?.permission!!)
                }
            }
        }
    }

    private fun higherFun() {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                setResult(Activity.RESULT_OK)
            } else {
                toast(getString(R.string.permission_denied_msg))
                setResult(Activity.RESULT_CANCELED)
            }
            finish()
        }

    private val requestMultiplePermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val allPermissionsGranted = permissions.all { it.value }

            if (allPermissionsGranted) {
                setResult(Activity.RESULT_OK)
            } else {
                toast(getString(R.string.permission_denied_msg))
                setResult(Activity.RESULT_CANCELED)
            }

            finish()
        }



    enum class PermissionType : Serializable {
        LOCATION,
        CAMERA,
        NOTIFICATION,
        CONTACT,
        NEARBY
    }

    data class PermissionModel(
        val title: String,
        val description: String,
        val lottieIcon: Int,
        val permission: String
    )

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        if (selectedPermissionType == map[PermissionType.LOCATION])
            showMandatoryInfoDialog()
    }

    private fun showMandatoryInfoDialog() {
        val al = AlertDialog.Builder(context)
        al.setTitle(getString(R.string.mandatory_permission))
            .setMessage(getString(R.string.for_security_reason_you_are_unable_to_use_the_app))
            .setCancelable(true)
            .setNegativeButton(getString(R.string.allow_permission)) { d, _ ->
                d.dismiss()
                requestPermissionLauncher.launch(selectedPermissionType?.permission!!)
            }
            .setPositiveButton(R.string.close_app) { d, _ ->
                d.dismiss()
                finishAffinity()
            }.show()
    }
}