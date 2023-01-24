package com.example.textrecoginition.ui.scanner

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.viewModels
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import com.example.textrecoginition.R
import com.example.textrecoginition.databinding.ActivityScannerBinding
import com.example.textrecoginition.domain.Resource
import com.example.textrecoginition.extension.logError
import com.example.textrecoginition.extension.navigateTo
import com.example.textrecoginition.extension.showOrHide
import com.example.textrecoginition.extension.showToast
import com.example.textrecoginition.ui.base.BaseActivity
import com.example.textrecoginition.ui.detail.DetailActivity
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@AndroidEntryPoint
class ScannerActivity : BaseActivity<ActivityScannerBinding>() {
    private val viewModel: ScannerViewModel by viewModels()
    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var recognizer: TextRecognizer
    private lateinit var crashlytics: FirebaseCrashlytics

    override fun getViewBinding(): ActivityScannerBinding {
        return ActivityScannerBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showBackButton()
        cameraExecutor = Executors.newSingleThreadExecutor()
        recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        crashlytics = FirebaseCrashlytics.getInstance()
        checkPermission()
        initListener()
    }

    private fun checkPermission() {
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                showToast(getString(R.string.permission_not_granted))
                finish()
            }
        }
    }

    private fun initListener() {
        binding.imageCaptureButton.setOnClickListener { takePhoto() }
    }

    private fun takePhoto() {
        // Create time stamped name and MediaStore entry.
        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, MIME_TYPE)
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, FOLDER_NAME)
            }
        }

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(
                contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )
            .build()

        // Set up image capture listener, which is triggered after photo has
        // been taken
        setLoadingVisibility(true)
        setStatusText(getString(R.string.capture_image_status))
        imageCapture?.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    setLoadingVisibility(false)
                    crashlytics.recordException(exc)
                    showToast("Photo capture failed: ${exc.message}")
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    output.savedUri?.let {
                        analyzeText(it)
                        showToast("Photo capture succeeded: $it")
                    } ?: setLoadingVisibility(false)
                }
            }
        )
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            initImageCapture()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture
                )
            } catch (exc: Exception) {
                crashlytics.recordException(exc)
                logError(TAG, "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun initImageCapture() {
        imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .setTargetRotation(binding.viewFinder.display.rotation)
            .build()
    }

    private fun analyzeText(uri: Uri) {
        try {
            setStatusText(getString(R.string.analyze_text_status))
            val image = InputImage.fromFilePath(this, uri)
            recognizer.process(image)
                .addOnSuccessListener { visionText ->
                    uploadToServer(visionText)
                }
                .addOnFailureListener { e ->
                    crashlytics.recordException(e)
                    setLoadingVisibility(false)
                    logError(TAG, e.message.toString(), e)
                }
        } catch (e: IOException) {
            crashlytics.recordException(e)
            setLoadingVisibility(false)
            logError(TAG, e.message.toString(), e)
        }
    }

    private fun setLoadingVisibility(isShow: Boolean) {
        with(binding) {
            progressBar.showOrHide(isShow)
            textViewStatus.showOrHide(isShow)
            imageCaptureButton.showOrHide(!isShow)
        }
    }

    private fun setStatusText(text: String) {
        binding.textViewStatus.text = text
    }

    private fun uploadToServer(text: Text) {
        viewModel.postText(text).observe(this) { res ->
            when (res) {
                is Resource.Success -> {
                    setLoadingVisibility(false)
                    if (res.data != null) {
                        navigateTo(
                            DetailActivity::class.java,
                            bundleOf(DetailActivity.EXTRA_DATA to res.data)
                        )
                    }
                }
                is Resource.Error -> {
                    setLoadingVisibility(false)
                    showToast(res.message.toString())
                }
                is Resource.Loading -> {
                    setLoadingVisibility(true)
                    setStatusText(getString(R.string.upload_to_server_status))
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
        imageCapture = null
    }

    companion object {
        const val TAG = "CameraXApp"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val MIME_TYPE = "image/jpeg"
        private const val FOLDER_NAME = "Pictures/CameraX-Image"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS =
            mutableListOf(
                Manifest.permission.CAMERA
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }
}