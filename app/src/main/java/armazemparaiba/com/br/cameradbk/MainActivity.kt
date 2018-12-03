package armazemparaiba.com.br.cameradbk

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.se.omapi.Session
import android.support.v4.app.ActivityCompat
import android.util.Log
import com.otaliastudios.cameraview.SessionType
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.toast
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    private var recording = false
    private val file = File(Environment.getExternalStorageDirectory().absolutePath + "/video.mp4")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.RECORD_AUDIO),
                0)


        camera.setLifecycleOwner(this)
        camera.sessionType = SessionType.VIDEO
        camera.addCameraListener(VideoListener())
        btn_toggle_record.setOnClickListener {
            if (!recording) {
                camera.startCapturingVideo(file)
                toast("Gravando")
            } else {
                camera.stopCapturingVideo()
                toast("Gravação parada")
            }
            recording = recording.not()

        }

    }

    override fun onResume() {
        super.onResume()
        camera.start()
    }

    override fun onPause() {
        super.onPause()
        camera.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        camera.destroy()
    }

}
