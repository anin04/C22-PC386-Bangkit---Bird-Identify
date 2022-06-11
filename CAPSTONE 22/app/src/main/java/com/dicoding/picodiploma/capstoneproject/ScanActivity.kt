package com.dicoding.picodiploma.capstoneproject


import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.toBitmap
import com.dicoding.picodiploma.capstoneproject.ml.BismillahCapstoneProjectFix
import kotlinx.android.synthetic.main.activity_scan.*
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer


class ScanActivity : AppCompatActivity() {
    lateinit var bitmap: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)


        val fileName = "label.txt"
        val inputString = application.assets.open(fileName).bufferedReader().use{it.readText()}
        var townList = inputString.split("\n")

        var tv:TextView = findViewById(R.id.tvPenjelasan)

        var select: Button = findViewById(R.id.button)
        select.setOnClickListener(View.OnClickListener {
            var intent:Intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"

            startActivityForResult(intent, 2)
        })

        var identify:Button = findViewById(R.id.btnIdenti)
        identify.setOnClickListener(View.OnClickListener {


            val model = BismillahCapstoneProjectFix.newInstance(this)
            val imageview = findViewById<ImageView>(R.id.imageView)
            var bitmap = imageview.getDrawable().toBitmap()
            bitmap = Bitmap.createScaledBitmap(bitmap, 224, 224, true)
            val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
            val tensorImage = TensorImage(DataType.FLOAT32)
            tensorImage.load(bitmap)
            val byteBuffer = tensorImage.buffer
             inputFeature0.loadBuffer(byteBuffer)
            val outputs = model.process(inputFeature0)
            val outputFeature0 = outputs.outputFeature0AsTensorBuffer
            var max = getMax(outputFeature0.floatArray)
            Toast.makeText(this,townList[max], Toast.LENGTH_LONG).show()
            model.close()
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        imageView.setImageURI(data?.data)

        var uri: Uri?= data?.data
        bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,uri)


    }
    fun getMax(arr:FloatArray): Int{
        var ind=0
        var min = 0.0f
        for(i in 0..2)
        {
            if(arr[i] > min)
            {
                min = arr[i]
                ind = i;
            }
        }
        return ind

    }

}