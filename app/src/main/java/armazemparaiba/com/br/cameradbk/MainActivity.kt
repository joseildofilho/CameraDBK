package armazemparaiba.com.br.cameradbk

import android.Manifest
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import armazemparaiba.com.br.cameradbk.dataset.DatasetManager
import com.otaliastudios.cameraview.SessionType
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import java.io.File

class MainActivity : AppCompatActivity() {

    private val file = createFolder()
    private var quant:Int = 0
    private val dataset: DatasetManager = DatasetManager(Environment.getExternalStorageDirectory())
    private var time: Long = 0
    private var commonPath = File("     ")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.RECORD_AUDIO),
                0)

        setupCamera()

        setupInput()

        resetTimer()

        btn_settings.setOnClickListener {
            toast("não implementado ainda!")
        }

    }

    private fun setupCamera() {
        camera.setLifecycleOwner(this)
        camera.sessionType = SessionType.VIDEO
        btn_toggle_record.setOnClickListener {
            if (camera.isCapturingVideo.not()) {
                var nome = txt_input.text.toString()
                if (nome.isEmpty().not()){
                    val split = nome.split(",", "/").map {
                        it.replace(" ", "")
                    }
                    resetTimer()
                    camera.startCapturingVideo(dataset.getFile(split[0],split[1], split[2]))
                    toast("Gravando")
                } else {
                    toast("Preencha o campo")
                }
            } else {
                camera.stopCapturingVideo()
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
                }
                Thread.sleep(200L)
            }
        }
    }

    private fun setupInput() = btn_ok.setOnClickListener {
        decodeInput(txt_input.text.toString())
    }

    private fun decodeInput(input: String) {

        if (input.first() == '.') {
            val command = input.replace(".","").split(" ")
            if (command[0] == "t" || command[0] == "time") {
                camera.videoMaxDuration = command[1].toInt() * 1000
                toast("Duração configurada para ${camera.videoMaxDuration/1000} segundos")
            } else {
                toast("commando não conhecido")
            }
        } else {
            toast("identificador não conhecido")
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
