package armazemparaiba.com.br.cameradbk

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.util.Log
import com.camerakit.CameraKitView
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    private var recording = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_toggle_record.setOnClickListener {
            camera.captureImage { cameraKitView: CameraKitView?, bytes: ByteArray? ->
                Log.d("CapturingImage","Starting")
                var save = File(Environment.getExternalStorageDirectory(), "picture.jpg")
                try {
                    var fos = FileOutputStream(save.path)
                    fos.write(bytes)
                    fos.close()
                    Log.d("CapturingImage","everything alright")
                    Log.d("CapturingImage", "Image stored on :${save.path}")
                } catch (e:Exception) {
                    Log.d("CapturingImage","Error")
                    Log.e("CapturingImage", "",e)
                }

            }
            recording != recording
        }

        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE),
                0)

    }

    override fun onStart() {
        super.onStart()
        camera.onStart()
    }

    override fun onResume() {
        super.onResume()
        camera.onResume()
    }

    override fun onPause() {
        camera.onPause()
        super.onPause()
    }

    override fun onStop() {
        camera.onStop()
        super.onStop()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        camera.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


}
