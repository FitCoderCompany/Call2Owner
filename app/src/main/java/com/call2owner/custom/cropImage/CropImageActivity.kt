package com.call2owner.custom.cropImage

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ImageDecoder
import android.graphics.Paint
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.format.DateFormat
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.call2owner.R
import com.call2owner.databinding.CropImageActivityBinding
import com.call2owner.databinding.DialogChooseAppBinding
import com.call2owner.ui.BaseActivity
import com.call2owner.utils.MyUtil
import com.call2owner.utils.MyUtil.getProviderName
import com.call2owner.utils.MyUtil.log
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.Date


open class CropImageActivity : BaseActivity(), CropImageView.OnSetImageUriCompleteListener,
    CropImageView.OnCropImageCompleteListener {
    private lateinit var binding: CropImageActivityBinding
    private var mCropImageUri: Uri? = null
    private var mOptions: CropImageOptions? = null
    private var imageFilePath = ""


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CropImageActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        initClicks()
        getAddressFromLocation()
    }

    private fun initView() {
        val bundle = intent.getBundleExtra(CropImage.CROP_IMAGE_EXTRA_BUNDLE)
        mCropImageUri = bundle?.getParcelable(CropImage.CROP_IMAGE_EXTRA_SOURCE)
        mOptions = bundle?.getParcelable(CropImage.CROP_IMAGE_EXTRA_OPTIONS)

        showOptionsDialog()
    }

    private fun showOptionsDialog() {
        val pickerDialog = AlertDialog.Builder(context)
        val pickerBinding = DialogChooseAppBinding.inflate(layoutInflater)
        pickerDialog.setView(pickerBinding.root)
        pickerDialog.setCancelable(false)
        val pickerAlert = pickerDialog.create()
        pickerBinding.apply {
            lytCameraPick.setOnClickListener {
                pickerAlert.dismiss()
                getCameraPermission()
            }
            lytGalleryPick.setOnClickListener {
                pickerAlert.dismiss()
                openGallery()
            }

            cancel.setOnClickListener {
                pickerAlert.dismiss()
                setResultCancel()
            }
        }
        pickerAlert.show()
    }

    private fun initClicks() {
        binding.apply {
            save.setOnClickListener { cropImage() }
            rotate.setOnClickListener {
                rotateImage(mOptions?.rotationDegrees ?: 90)
            }
            horizontalFlip.setOnClickListener {
                cropImageView.flipImageHorizontally()
            }
            verticalFlip.setOnClickListener {
                cropImageView.flipImageVertically()
            }
            selectImage.setOnClickListener {
                showOptionsDialog()
            }
        }
    }

    private var cameraLaunch =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                try {
                    binding.cropImageView.setImageBitmap(BitmapFactory.decodeFile(imageFilePath))
                } catch (e: Exception) {
                    e.printStackTrace()
                    showErrorSnackBar(getString(R.string.something_wrong))
                }
            }
        }

    private var cameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                openCamera()
            } else {
                showOptionsDialog()
                showErrorSnackBar(msg = "Unable to open camera due to insufficient permission", btnText = getString(R.string.setting)) {/* openPermissionSetting(context) */}
            }
        }

    private var galleryLaunch =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                if (result.data != null) {
                    try {
                        if (result.data!!.data.toString().contains("video", true)) {
                            showErrorSnackBar("Video Not Supported")
                        } else {
                            val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                ImageDecoder.decodeBitmap(
                                    ImageDecoder.createSource(
                                        contentResolver,
                                        result.data?.data!!
                                    )
                                )
                            } else {
                                MediaStore.Images.Media.getBitmap(
                                    contentResolver,
                                    result.data!!.data
                                )
                            }
                            binding.cropImageView.setImageBitmap(bitmap)
                        }
                    } catch (e: Exception) {
                        e.log()
                    }
                } else {
                    showErrorSnackBar()
                }
            }
        }

    private fun openCamera() {
        val imageFile = File.createTempFile(
            "sahaz",
            ".jpg",
            getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        )
        val capturedImageUri = FileProvider.getUriForFile(
            context,
            getProviderName(),
            imageFile
        )
        imageFilePath = imageFile.absolutePath

        val intent = Intent()
        intent.action = MediaStore.ACTION_IMAGE_CAPTURE
        intent.putExtra(MediaStore.EXTRA_OUTPUT, capturedImageUri)
        cameraLaunch.launch(intent)
    }

    private fun openGallery() {
        if (ActivityResultContracts.PickVisualMedia.isPhotoPickerAvailable(context)) {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        } else {
            galleryLaunch.launch(
                Intent(
                    Intent.ACTION_GET_CONTENT,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
            )
        }
    }

    @Suppress("DEPRECATION")
    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver, uri))
                } else {
                    MediaStore.Images.Media.getBitmap(contentResolver, uri)
                }
                binding.cropImageView.setImageBitmap(bitmap)
            } else {
                showErrorSnackBar()

            }
        }

    private fun getCameraPermission() {
        if (checkPermission()) {
            openCamera()
        } else {
            requestPermission()
        }
    }

    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this@CropImageActivity,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
//        Intent(context, PermissionActivity::class.java).also {
//            cameraPermissionLauncher.launch(it)
//        }
    }

    override fun onStart() {
        super.onStart()
        binding.cropImageView.setOnSetImageUriCompleteListener(this)
        binding.cropImageView.setOnCropImageCompleteListener(this)
    }

    override fun onStop() {
        super.onStop()
        binding.cropImageView.setOnSetImageUriCompleteListener(null)
        binding.cropImageView.setOnCropImageCompleteListener(null)
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        @Suppress("DEPRECATION")
        super.onBackPressed()
        setResultCancel()
    }

    override fun onSetImageUriComplete(view: CropImageView, uri: Uri, error: Exception) {
        setResult(null, error, 1)
    }

    override fun onCropImageComplete(view: CropImageView, result: CropImageView.CropResult) {
        setResult(result.uri, result.error, result.sampleSize)
    }

    private fun cropImage() {
        if (mOptions!!.noOutputImage) {
            setResult(null, null, 1)
        } else {
            val outputUri = outputUri
            binding.cropImageView.saveCroppedImageAsync(
                outputUri,
                mOptions!!.outputCompressFormat,
                mOptions!!.outputCompressQuality,
                mOptions!!.outputRequestWidth,
                mOptions!!.outputRequestHeight,
                mOptions!!.outputRequestSizeOptions
            )
        }
    }

    private fun rotateImage(degrees: Int) {
        binding.cropImageView.rotateImage(degrees)
    }

    private val outputUri: Uri?
        get() {
            var outputUri = mOptions!!.outputUri
            if (outputUri == null || outputUri == Uri.EMPTY) {
                outputUri = try {
                    val ext =
                        if (mOptions!!.outputCompressFormat == Bitmap.CompressFormat.JPEG) ".jpg" else if (mOptions!!.outputCompressFormat == Bitmap.CompressFormat.PNG) ".png" else ".webp"
                    Uri.fromFile(File.createTempFile("cropped", ext, cacheDir))
                } catch (e: IOException) {
                    throw RuntimeException("Failed to create temp file for output image", e)
                }
            }
            return outputUri
        }

    private fun getAddressFromLocation() {
//        val geocoder = Geocoder(context)
//        MyUtil.i("Lat: ${secureManager.latitude}, lang: ${secureManager.longitude}")
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            geocoder.getFromLocation(
//                secureManager.latitude.toDouble(),
//                secureManager.longitude.toDouble(),
//                1,
//                object : Geocoder.GeocodeListener {
//                    @SuppressLint("SetTextI18n")
//                    override fun onGeocode(addresses: MutableList<Address>) {
//                        if (addresses.isNotEmpty()) {
//                            val address = addresses[0]
//                            val addressString = address.getAddressLine(0)
//                            dataManager.addressByLocation =
//                                "${secureManager.latitude},${secureManager.longitude}, $addressString"
//                            binding.locationInfo.text = dataManager.addressByLocation
//                        }
//                    }
//
//                    override fun onError(errorMessage: String?) {
//                        super.onError(errorMessage)
//                        MyUtil.e(errorMessage ?: "not found")
//                    }
//                })
//        } else {
//            try {
//                @Suppress("DEPRECATION") val addresses = geocoder.getFromLocation(
//                    secureManager.latitude.toDouble(),
//                    secureManager.longitude.toDouble(),
//                    1
//                )
//                if (addresses!!.isNotEmpty()) {
//                    val address = addresses[0]
//                    val addressString = address.getAddressLine(0)
//                    dataManager.addressByLocation =
//                        "${secureManager.latitude},${secureManager.longitude}, $addressString"
//                    binding.locationInfo.text = dataManager.addressByLocation
//                }
//            } catch (e: Exception) {
//                MyUtil.i(e)
//            }
//        }
    }

    private fun setResult(uri: Uri?, error: Exception?, sampleSize: Int) {
        setResult(RESULT_OK, getResultIntent(uri, error, sampleSize))
        finish()
    }

    private fun setResultCancel() {
        setResult(RESULT_CANCELED)
        finish()
    }

    private fun getResultIntent(uri: Uri?, error: Exception?, sampleSize: Int): Intent {
        val result = CropImage.ActivityResult(
            binding.cropImageView.imageUri,
            uri,
            error,
            binding.cropImageView.cropPoints,
            binding.cropImageView.cropRect,
            binding.cropImageView.rotatedDegrees,
            binding.cropImageView.wholeImageRect,
            sampleSize
        )
        val intent = Intent()
        intent.putExtras(getIntent())
        intent.putExtra(CropImage.CROP_IMAGE_EXTRA_RESULT, result)
        return intent
    }

//    private fun getCombinedImageUri(imageUri: Uri?): Uri? {
//        return try {
//            val bitmap1 = BitmapFactory.decodeFile(File(imageUri?.path ?: "").absolutePath)
//            val bitmap2 = getAddressBitmap(dataManager.addressByLocation)
//
//            val width = bitmap1.width
//            val height = bitmap1.height + bitmap2.height
//
//            var mergedBitmap = Bitmap.createBitmap(width, height, bitmap1.config)
//            val canvas = Canvas(mergedBitmap)
//
//            canvas.drawBitmap(bitmap1, 0f, 0f, null)
//            canvas.drawBitmap(bitmap2, 0f, bitmap1.height.toFloat(), null)
//            val format = DateFormat.format("MM-dd-yyyy_hh:mm:ss", Date())
//            val mainDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//            mainDir?.mkdirs()
//            val path = "$mainDir/TrendOceans-$format.jpeg"
////            val fileOutputStream = FileOutputStream(imageFile)
////            if((mergedBitmap.byteCount/1024)>499){
////                compressImage(mergedBitmap,50,fileOutputStream)
////            }
////            fileOutputStream.flush()
////            fileOutputStream.close()
//            mergedBitmap = compressBitmap(mergedBitmap, 199)
//            val imageFile = File(path)
//            val fileOutputStream = FileOutputStream(imageFile)
//            mergedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
//            fileOutputStream.flush()
//            fileOutputStream.close()
//
//            imageFile.toUri()
//        } catch (e: Exception) {
//            e.printStackTrace()
//            imageUri
//        }
//    }

    private fun compressBitmap(bitmap: Bitmap, targetSizeKB: Int): Bitmap {
        try {
            val maxDimension = maxOf(bitmap.width, bitmap.height)
            var scaleFactor = 1.0f
            val quality = 100
            val outputStream = ByteArrayOutputStream()

            while (true) {
                // Scale the bitmap
                val scaledBitmap = Bitmap.createScaledBitmap(
                    bitmap,
                    (bitmap.width / scaleFactor).toInt(),
                    (bitmap.height / scaleFactor).toInt(),
                    true
                )

                // Compress and check the file size
                outputStream.reset()
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
                val currentSizeKB = outputStream.toByteArray().size / 1024

                if (currentSizeKB <= targetSizeKB || maxDimension / scaleFactor < 10) {
                    return scaledBitmap
                } else {
                    // Decrease quality and increase scale factor to reduce file size
//                    quality -= 2
                    scaleFactor += 0.1f
                    scaledBitmap.recycle()
                }
            }
        } catch (e: Exception) {
            e.log()
            return bitmap
        }
    }

    private fun getAddressBitmap(text: String): Bitmap {
        val width = binding.cropImageView.cropRect.width()
        val height = width / 4

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.BLACK)
        val paint = Paint()
        paint.color = Color.WHITE
        val textSize = width / 24

        paint.textSize = textSize.toFloat()

        val texts = splitString(text.replace(", ", ","), 2)
        for (i in texts.indices) {
            val textX1 = (width - paint.measureText(texts[i])) / 2
            val textY1 = (height / 2 + textSize * i - (height / 6.7)).toFloat()
            canvas.drawText(texts[i], textX1, textY1, paint)
        }
        return bitmap
    }

    private fun splitString(input: String, wpl: Int): List<String> {
        val substrings = input.split(",")
        val result = mutableListOf<String>()
        for (i in substrings.indices step wpl) {
            val group = substrings.subList(i, minOf(i + wpl, substrings.size))
            result.add(group.joinToString(", "))
        }
        return result
    }


}