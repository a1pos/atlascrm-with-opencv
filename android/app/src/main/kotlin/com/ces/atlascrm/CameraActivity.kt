package com.ces.atlascrm

import android.os.Bundle
import android.view.SurfaceView
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import org.opencv.android.*
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import org.opencv.imgproc.Imgproc.CHAIN_APPROX_SIMPLE
import org.opencv.core.Scalar
import android.R.attr.y
import android.R.attr.x
import android.widget.Toast
import org.opencv.core.Core
import org.opencv.core.MatOfPoint
import org.opencv.core.Mat
import org.opencv.android.BaseLoaderCallback
import org.opencv.android.OpenCVLoader
import org.opencv.core.MatOfPoint2f
import org.opencv.android.CameraBridgeViewBase
import org.opencv.android.LoaderCallbackInterface
import org.opencv.android.JavaCameraView
import java.util.*
import kotlin.collections.ArrayList


class CameraActivity : AppCompatActivity(), CameraBridgeViewBase.CvCameraViewListener2 {

    //view holder
    internal var cameraBridgeViewBase: CameraBridgeViewBase? = null

    //camera listener callback
    private var baseLoaderCallback: BaseLoaderCallback? = null

    //image holder
    private var bwIMG: Mat? = null
    private var hsvIMG: Mat? = null
    private var lrrIMG: Mat? = null
    private var urrIMG: Mat? = null
    private var dsIMG: Mat? = null
    private var usIMG: Mat? = null
    private var cIMG: Mat? = null
    private var hovIMG: Mat? = null
    private var approxCurve: MatOfPoint2f? = null

    private var threshold: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.camera_activity)

        threshold = 100

        cameraBridgeViewBase = findViewById(R.id.HelloOpenCvView)
        cameraBridgeViewBase!!.visibility = SurfaceView.VISIBLE
        cameraBridgeViewBase!!.setCvCameraViewListener(this)

        //create camera listener callback
        baseLoaderCallback = object : BaseLoaderCallback(this) {
            override fun onManagerConnected(status: Int) {
                when (status) {
                    LoaderCallbackInterface.SUCCESS -> {
                        bwIMG = Mat()
                        dsIMG = Mat()
                        hsvIMG = Mat()
                        lrrIMG = Mat()
                        urrIMG = Mat()
                        usIMG = Mat()
                        cIMG = Mat()
                        hovIMG = Mat()
                        approxCurve = MatOfPoint2f()
                        cameraBridgeViewBase!!.enableView()
                    }
                    else -> super.onManagerConnected(status)
                }
            }
        }

    }

    override fun onCameraViewStarted(width: Int, height: Int) {

    }

    override fun onCameraViewStopped() {

    }

    override fun onCameraFrame(inputFrame: CvCameraViewFrame): Mat {

        val gray = inputFrame.gray()
        val dst = inputFrame.rgba()

        Imgproc.pyrDown(gray, dsIMG, Size((gray.cols() / 2).toDouble(), (gray.rows() / 2).toDouble()))
        Imgproc.pyrUp(dsIMG, usIMG, gray.size())

        Imgproc.Canny(usIMG, bwIMG, 0.0, threshold.toDouble())

        Imgproc.dilate(bwIMG, bwIMG, Mat(), Point(-1.0, 1.0), 1)

        val contours = ArrayList<MatOfPoint>()

        cIMG = bwIMG!!.clone()

        Imgproc.findContours(cIMG, contours, hovIMG, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE)


        for (cnt in contours) {

            val curve = MatOfPoint2f(*cnt.toArray())

            Imgproc.approxPolyDP(curve, approxCurve, 0.02 * Imgproc.arcLength(curve, true), true)

            val numberVertices = approxCurve!!.total().toInt()

            val contourArea = Imgproc.contourArea(cnt)

            if (Math.abs(contourArea) < 100) {
                continue
            }

            //Rectangle detected
            if (numberVertices in 4..6) {

                val cos = ArrayList<Double>()

                for (j in 2 until numberVertices + 1) {
                    cos.add(angle(approxCurve!!.toArray()[j % numberVertices], approxCurve!!.toArray()[j - 2], approxCurve!!.toArray()[j - 1]))
                }

                cos.sort()

                val mincos = cos.get(0)
                val maxcos = cos.get(cos.size - 1)

                if (numberVertices == 4 && mincos >= -0.1 && maxcos <= 0.3) {
                    setLabel(dst, "X", cnt)
                }

            }


        }

        return dst

    }

    override fun onPause() {
        super.onPause()
        if (cameraBridgeViewBase != null) {
            cameraBridgeViewBase!!.disableView()
        }
    }

    override fun onResume() {
        super.onResume()
        if (!OpenCVLoader.initDebug()) {
            Toast.makeText(applicationContext, "There is a problem", Toast.LENGTH_SHORT).show()
        } else {
            baseLoaderCallback!!.onManagerConnected(BaseLoaderCallback.SUCCESS)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (cameraBridgeViewBase != null) {
            cameraBridgeViewBase!!.disableView()
        }
    }

    private fun angle(pt1: Point, pt2: Point, pt0: Point): Double {
        val dx1 = pt1.x - pt0.x
        val dy1 = pt1.y - pt0.y
        val dx2 = pt2.x - pt0.x
        val dy2 = pt2.y - pt0.y
        return (dx1 * dx2 + dy1 * dy2) / Math.sqrt((dx1 * dx1 + dy1 * dy1) * (dx2 * dx2 + dy2 * dy2) + 1e-10)
    }

    private fun setLabel(im: Mat, label: String, contour: MatOfPoint) {
        val fontface = Core.FONT_HERSHEY_SIMPLEX
        val scale = 3.0//0.4;
        val thickness = 3//1;
        val baseline = IntArray(1)
        val text = Imgproc.getTextSize(label, fontface, scale, thickness, baseline)
        val r = Imgproc.boundingRect(contour)
        val pt = Point(r.x + (r.width - text.width) / 2, r.y + (r.height + text.height) / 2)
        Imgproc.putText(im, label, pt, fontface, scale, Scalar(255.0, 0.0, 0.0), thickness)
    }

}

//class CameraActivity : AppCompatActivity(), CameraBridgeViewBase.CvCameraViewListener2 {
//    init {
//        if (OpenCVLoader.initDebug()) {
//            println("Successfully loaded OPENCV")
//        } else {
//            println("failed to load OPENCV")
//        }
//    }
//
//    private var mOpenCvCameraView: JavaCamera2View? = null
//
//    private val mLoaderCallback = object : BaseLoaderCallback(this) {
//        override fun onManagerConnected(status: Int) {
//            when (status) {
//                LoaderCallbackInterface.SUCCESS -> {
//                    mOpenCvCameraView!!.enableView()
//                }
//                else -> {
//                    super.onManagerConnected(status)
//                }
//            }
//        }
//    }
//
//    private var mRgba: Mat? = null
//    private var mIntermediateMat: Mat? = null
//    private var mGray: Mat? = null
//
//    private var hierarchy: Mat? = null
//
//    private var contours: ArrayList<MatOfPoint>? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
//        setContentView(R.layout.camera_activity)
//
//        mOpenCvCameraView = findViewById(R.id.HelloOpenCvView)
//        mOpenCvCameraView!!.visibility = SurfaceView.VISIBLE
//        mOpenCvCameraView!!.setCvCameraViewListener(this)
//    }
//
//    override fun onCameraFrame(inputFrame: CvCameraViewFrame?): Mat {
//        mRgba = inputFrame!!.rgba()
//        mGray = inputFrame!!.gray()
//        contours = ArrayList()
//        hierarchy = Mat()
//
//        Imgproc.threshold(mRgba, mRgba, 195.0, 255.0, Imgproc.THRESH_BINARY)
//
//        Imgproc.Canny(mRgba, mIntermediateMat, 80.0, 100.0)
//        Imgproc.findContours(mIntermediateMat, contours, hierarchy, Imgproc.RETR_TREE, CHAIN_APPROX_SIMPLE, Point(0.0, 0.0))
//
//        var largestArea = 0.0
//        var largestContourIndex = 0
//        var boundingRect = Rect()
//        for(i in 0 until (contours as ArrayList<MatOfPoint>).size){
//            val area = Imgproc.contourArea((contours as ArrayList<MatOfPoint>)[i])
//
//            if(area > largestArea){
//                largestArea = area
//                largestContourIndex = i
//                boundingRect = Imgproc.boundingRect((contours as ArrayList<MatOfPoint>)[i])
//            }
//        }
//
//        hierarchy!!.release()
//
//        Imgproc.drawContours(inputFrame!!.rgba(), contours, largestContourIndex, Scalar(0.0, 255.0, 0.0), 10)
//        return inputFrame!!.rgba()
//    }
//
//    override fun onCameraViewStarted(width: Int, height: Int) {
////        mRgba = Mat(height, width, CvType.CV_8UC4)
////        mIntermediateMat = Mat(height, width, CvType.CV_8UC4)
////        mGray = Mat(height, width, CvType.CV_8UC1)
////        hierarchy = Mat()
//    }
//
//
//    public override fun onResume() {
//        super.onResume()
//        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, mLoaderCallback)
//    }
//
//    public override fun onPause() {
//        super.onPause()
//        if (mOpenCvCameraView != null)
//            mOpenCvCameraView!!.disableView()
//    }
//
//    public override fun onDestroy() {
//        super.onDestroy()
//        if (mOpenCvCameraView != null)
//            mOpenCvCameraView!!.disableView()
//    }
//
//
//    override fun onCameraViewStopped() {
//        mRgba!!.release()
//        mGray!!.release()
//        mIntermediateMat!!.release()
//        hierarchy!!.release()
//    }
//}