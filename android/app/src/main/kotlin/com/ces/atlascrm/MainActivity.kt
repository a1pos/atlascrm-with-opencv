package com.ces.atlascrm

import androidx.annotation.NonNull
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugins.GeneratedPluginRegistrant
import org.opencv.android.OpenCVLoader
import org.opencv.core.*
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc


class MainActivity : FlutterActivity() {

//    init {
//        if (OpenCVLoader.initDebug()) {
//            println("Successfully loaded OPENCV")
//        } else {
//            println("failed to load OPENCV")
//        }
//    }

    private val REQUEST_CODE = 101

    private val CHANNEL = "com.ces.atlascrm.channel"

    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        GeneratedPluginRegistrant.registerWith(flutterEngine)

        val METHOD_CHANNEL = MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL)

        METHOD_CHANNEL.setMethodCallHandler { methodCall, result ->
            when (methodCall.method.toUpperCase()) {
                "PROCESS_IMAGE" -> {
                    try {
//                        val argsArr = methodCall.arguments<ArrayList<*>>()
//
//                        val tempImagePath = argsArr[0].toString()
//                        val finalImagePath = argsArr[1].toString()
//
//                        val img = Imgcodecs.imread(tempImagePath)
//
//                        val imgGray = Mat()
//                        Imgproc.cvtColor(img, imgGray, Imgproc.COLOR_BGR2GRAY)
//
//                        Imgproc.threshold(imgGray, imgGray, 195.0, 255.0, Imgproc.THRESH_BINARY)
//
////                        Imgproc.GaussianBlur(imgGray, imgGray, Size(3.0,3.0), 0.0,0.0)
//
////                        Imgproc.Canny(imgGray, imgGray, 255.0 / 3.0, 255.0)
//
//                        val contours = ArrayList<MatOfPoint>()
//                        Imgproc.findContours(imgGray, contours, Mat(img.size(), imgGray.type()), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE)
//
//                        var largestArea = 0.0
//                        var largestContourIndex = 0
//                        var boundingRect = Rect()
//                        for (i in 0 until contours.size) {
//                            val area = Imgproc.contourArea(contours[i])
//
//                            if (area > largestArea) {
//                                largestArea = area
//                                largestContourIndex = i
//                                boundingRect = Imgproc.boundingRect(contours[i])
//                            }
//                        }
//
//                        Imgproc.drawContours(img, contours, largestContourIndex, Scalar(0.0, 255.0, 0.0), 10)
//
//                        val croppedImg = Mat(img, boundingRect)
//                        val cropped = Mat(Size(boundingRect.width.toDouble(), boundingRect.height.toDouble()),img.type())
//
//
//                        Imgcodecs.imwrite(finalImagePath, croppedImg)
//
//                        img.release()
//                        imgGray.release()
//
                        result.success("Ok")
                    } catch (err: Exception) {
                        print("ERROR WITH OPENCV: $err")
                    }
                }
            }
        }
    }

    private fun startNewActivity() {
//        val REQUEST_CODE = 99
//        val preference = ScanConstants.OPEN_CAMERA
//        val intent = Intent(this, ScanActivity::class.java)
//        intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference)
//        startActivityForResult(intent, REQUEST_CODE)
//        val intent = Intent(this, CameraActivity::class.java)
//        startActivity(intent)
    }
}