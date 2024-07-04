package com.call2owner.custom.cropImage
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.net.Uri
import android.os.AsyncTask
import com.call2owner.custom.cropImage.BitmapUtils.BitmapSampled
import com.call2owner.custom.cropImage.CropImageView.RequestSizeOptions
import java.lang.ref.WeakReference

internal class BitmapCroppingWorkerTask : AsyncTask<Void?, Void?, BitmapCroppingWorkerTask.Result?> {
    private val mCropImageViewReference: WeakReference<CropImageView>
    private val mBitmap: Bitmap?
    val uri: Uri?
    private val mContext: Context
    private val mCropPoints: FloatArray
    private val mDegreesRotated: Int
    private val mOrgWidth: Int
    private val mOrgHeight: Int
    private val mFixAspectRatio: Boolean
    private val mAspectRatioX: Int
    private val mAspectRatioY: Int
    private val mReqWidth: Int
    private val mReqHeight: Int
    private val mFlipHorizontally: Boolean
    private val mFlipVertically: Boolean
    private val mReqSizeOptions: RequestSizeOptions
    private val mSaveUri: Uri?
    private val mSaveCompressFormat: CompressFormat
    private val mSaveCompressQuality: Int

    // endregion
    constructor(cropImageView: CropImageView, bitmap: Bitmap?, cropPoints: FloatArray, degreesRotated: Int, fixAspectRatio: Boolean, aspectRatioX: Int, aspectRatioY: Int, reqWidth: Int, reqHeight: Int, flipHorizontally: Boolean, flipVertically: Boolean, options: RequestSizeOptions, saveUri: Uri?, saveCompressFormat: CompressFormat, saveCompressQuality: Int) {
        mCropImageViewReference = WeakReference(cropImageView)
        mContext = cropImageView.context
        mBitmap = bitmap
        mCropPoints = cropPoints
        uri = null
        mDegreesRotated = degreesRotated
        mFixAspectRatio = fixAspectRatio
        mAspectRatioX = aspectRatioX
        mAspectRatioY = aspectRatioY
        mReqWidth = reqWidth
        mReqHeight = reqHeight
        mFlipHorizontally = flipHorizontally
        mFlipVertically = flipVertically
        mReqSizeOptions = options
        mSaveUri = saveUri
        mSaveCompressFormat = saveCompressFormat
        mSaveCompressQuality = saveCompressQuality
        mOrgWidth = 0
        mOrgHeight = 0
    }

    constructor(
        cropImageView: CropImageView,
        uri: Uri?,
        cropPoints: FloatArray,
        degreesRotated: Int,
        orgWidth: Int,
        orgHeight: Int,
        fixAspectRatio: Boolean,
        aspectRatioX: Int,
        aspectRatioY: Int,
        reqWidth: Int,
        reqHeight: Int,
        flipHorizontally: Boolean,
        flipVertically: Boolean,
        options: RequestSizeOptions,
        saveUri: Uri?,
        saveCompressFormat: CompressFormat,
        saveCompressQuality: Int
    ) {
        mCropImageViewReference = WeakReference(cropImageView)
        mContext = cropImageView.context
        this.uri = uri
        mCropPoints = cropPoints
        mDegreesRotated = degreesRotated
        mFixAspectRatio = fixAspectRatio
        mAspectRatioX = aspectRatioX
        mAspectRatioY = aspectRatioY
        mOrgWidth = orgWidth
        mOrgHeight = orgHeight
        mReqWidth = reqWidth
        mReqHeight = reqHeight
        mFlipHorizontally = flipHorizontally
        mFlipVertically = flipVertically
        mReqSizeOptions = options
        mSaveUri = saveUri
        mSaveCompressFormat = saveCompressFormat
        mSaveCompressQuality = saveCompressQuality
        mBitmap = null
    }
    override fun doInBackground(vararg params: Void?): Result? {
        return try {
            if (!isCancelled) {
                val bitmapSampled: BitmapSampled
                bitmapSampled = if (uri != null) {
                    BitmapUtils.cropBitmap(
                        mContext,
                        uri,
                        mCropPoints,
                        mDegreesRotated,
                        mOrgWidth,
                        mOrgHeight,
                        mFixAspectRatio,
                        mAspectRatioX,
                        mAspectRatioY,
                        mReqWidth,
                        mReqHeight,
                        mFlipHorizontally,
                        mFlipVertically
                    )
                } else if (mBitmap != null) {
                    BitmapUtils.cropBitmapObjectHandleOOM(
                        mBitmap,
                        mCropPoints,
                        mDegreesRotated,
                        mFixAspectRatio,
                        mAspectRatioX,
                        mAspectRatioY,
                        mFlipHorizontally,
                        mFlipVertically
                    )
                } else {
                    return Result(null as Bitmap?, 1)
                }
                val bitmap = BitmapUtils.resizeBitmap(
                    bitmapSampled.bitmap,
                    mReqWidth,
                    mReqHeight,
                    mReqSizeOptions
                )
                return if (mSaveUri == null) {
                    Result(bitmap, bitmapSampled.sampleSize)
                } else {
                    BitmapUtils.writeBitmapToUri(
                        mContext, bitmap, mSaveUri, mSaveCompressFormat, mSaveCompressQuality
                    )
                    bitmap?.recycle()
                    Result(mSaveUri, bitmapSampled.sampleSize)
                }
            }
            null
        } catch (e: Exception) {
            Result(e, mSaveUri != null)
        }
    }
    override fun onPostExecute(result: Result?) {
        if (result != null) {
            var completeCalled = false
            if (!isCancelled) {
                val cropImageView = mCropImageViewReference.get()
                if (cropImageView != null) {
                    completeCalled = true
                    cropImageView.onImageCroppingAsyncComplete(result)
                }
            }
            if (!completeCalled && result.bitmap != null) {
                // fast release of unused bitmap
                result.bitmap.recycle()
            }
        }
    }

    internal class Result {
        @JvmField
        val bitmap: Bitmap?
        @JvmField
        val uri: Uri?
        @JvmField
        val error: Exception?
        val isSave: Boolean
        @JvmField
        val sampleSize: Int

        constructor(bitmap: Bitmap?, sampleSize: Int) {
            this.bitmap = bitmap
            uri = null
            error = null
            isSave = false
            this.sampleSize = sampleSize
        }

        constructor(uri: Uri?, sampleSize: Int) {
            bitmap = null
            this.uri = uri
            error = null
            isSave = true
            this.sampleSize = sampleSize
        }

        constructor(error: Exception?, isSave: Boolean) {
            bitmap = null
            uri = null
            this.error = error
            this.isSave = isSave
            sampleSize = 1
        }
    } // endregion
}