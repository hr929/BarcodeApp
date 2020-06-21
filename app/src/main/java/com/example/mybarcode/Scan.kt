package com.example.mybarcode

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import kotlinx.android.synthetic.main.activity_scan.*

class Scan : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)
        link.visibility=View.GONE
        scan_prompt.setOnClickListener {
            val i= Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(i,1000)
        }
        link.setOnClickListener {
            val intent=Intent(Intent.ACTION_VIEW)
            intent.setData(Uri.parse(link.text.toString()))
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==1000){
            if(resultCode== Activity.RESULT_OK){
                val photo=data?.extras?.get("data") as Bitmap
                getQR(photo)

            }
        }
    }
    private fun getQR(photo:Bitmap){
        val image=FirebaseVisionImage.fromBitmap(photo)
        val detector=FirebaseVision.getInstance().visionBarcodeDetector
        val result=detector.detectInImage(image).addOnSuccessListener {
            link.visibility= View.VISIBLE
            for(i in it){
                link.text=i.rawValue
            }
        }
    }
}