package com.biogin.myapplication

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ImageCapture
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import android.widget.Toast
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.core.Preview
import androidx.camera.core.CameraSelector
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import com.biogin.myapplication.databinding.ActivityMainBinding
import com.biogin.myapplication.databinding.ActivityPhotoRegisterBinding
import com.google.firebase.storage.FirebaseStorage
import com.google.mlkit.vision.face.FaceDetectorOptions
import java.text.SimpleDateFormat
import java.util.Locale
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceContour
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceLandmark

class PhotoRegisterActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityPhotoRegisterBinding
    private var imageCapture: ImageCapture? = null
    private var videoCapture: VideoCapture<Recorder>? = null
    private var recording: Recording? = null
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var imageAnalysis : ImageAnalysis
    private var personId = 1
    private var storageRef = FirebaseStorage.getInstance().getReference()
    private lateinit var camera: CameraHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityPhotoRegisterBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        cameraExecutor = Executors.newSingleThreadExecutor()
        // Request camera permissions
        if (allPermissionsGranted()) {
            initCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        // Set up the listeners for take photo and video capture buttons
        viewBinding.imageCaptureButton.setOnClickListener { takePhoto() }
    }

    private fun initCamera() {
        camera = CameraHelper(this, cameraExecutor, viewBinding, viewBinding.viewFinder.surfaceProvider, viewBinding.graphicOverlayFinder)
        camera.startCamera()
    }

    private fun uploadPhotoToFirebase(photo: Uri?) {
        if (photo != null) {
            var imageRef = storageRef.child("images/${intent.getStringExtra("dni")}/${intent.getStringExtra("name") + "_" + intent.getStringExtra("surname")}")
            var uploadTask = imageRef.putFile(photo)
            uploadTask.addOnFailureListener {
                Log.e("Firebase", "Error al subir imagen")
            }.addOnSuccessListener {
                Log.e("Firebase", "Exito al subir imagen")
            }
        }
    }
    private fun takePhoto() {
        camera.takePhoto(TAG, FILENAME_FORMAT, this, intent, ::finish)
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
        private const val TAG = "CameraXApp"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS =
            mutableListOf (
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }
}