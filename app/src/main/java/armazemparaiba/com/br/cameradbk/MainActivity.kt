package armazemparaiba.com.br.cameradbk

import android.Manifest
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
import org.jetbrains.anko.*
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    private var recording = false
    private val file = createFolder()

    private var quant:Int = 0;

    private val dataset: DatasetManager = DatasetManager(Environment.getExternalStorageDirectory())

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
                var nome = txt_nome.text.toString()
                val split = nome.split(",", "/").map {
                    it.replace(" ", "")
                }
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
            recording = recording.not()

        }

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
