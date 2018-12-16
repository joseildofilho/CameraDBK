package armazemparaiba.com.br.cameradbk

import android.Manifest
import android.app.Service
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.se.omapi.Session
import android.support.v4.app.ActivityCompat
import android.util.Log
import armazemparaiba.com.br.cameradbk.R.id.camera
import armazemparaiba.com.br.cameradbk.dataset.DatasetManager
import com.otaliastudios.cameraview.SessionType
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.coroutines.delay
import org.jetbrains.anko.*
import org.jetbrains.anko.coroutines.experimental.bg
import java.io.File
import java.io.FileOutputStream
import java.lang.Thread.sleep
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.locks.Lock

class MainActivity : AppCompatActivity() {

    private val file = createFolder()
    private var quant:Int = 0
    private val dataset: DatasetManager = DatasetManager(Environment.getExternalStorageDirectory())
    private var time: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.RECORD_AUDIO),
                0)

        setupCamera()

        resetTimer()

        btn_settings.setOnClickListener {
            toast("não implementado ainda!")
        }

    }

    private fun setupCamera() {
        camera.setLifecycleOwner(this)
        camera.sessionType = SessionType.VIDEO
        camera.videoMaxDuration = 6000
        camera.addCameraListener(VideoListener())
        btn_toggle_record.setOnClickListener {
            if (camera.isCapturingVideo.not()   ) {
                var nome = txt_nome.text.toString()
                val split = nome.split(",", "/").map {
                    it.replace(" ", "")
                }
                resetTimer()
                camera.startCapturingVideo(dataset.getFile(split[0],split[1], split[2]))
                txt_nome.isFocusable = false
                txt_nome.isEnabled = false
                toast("Gravando")
            } else {
                camera.stopCapturingVideo()
                txt_nome.isFocusable = true
                txt_nome.isEnabled = true
                toast("Gravação parada")
            }
        }

        doAsync {
            while (true) {
                if(camera.isCapturingVideo) {
                    val delta = (System.currentTimeMillis() - time)
                    uiThread {
                        txt_time.text = "${delta / 1000 / 60}:${delta / 1000 % 60}"
                    }
                } else {
                    Thread.sleep(200L)
                }
            }
        }
    }

    private fun resetTimer() {
        time = System.currentTimeMillis()
        txt_time.text = "00:00"
    }

    private fun createFolder():File {
        val f = File(Environment.getExternalStorageDirectory(),"/videos")
        if (!f.exists())
            f.mkdir()
        return f
    }

    private fun getPlace(nome:String): File {
        quant += 1
        return File(file, "$nome#$quant.mp4")
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
