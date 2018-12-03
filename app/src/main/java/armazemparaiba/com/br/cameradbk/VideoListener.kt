package armazemparaiba.com.br.cameradbk

import android.util.Log
import com.otaliastudios.cameraview.CameraListener
import java.io.File

class VideoListener: CameraListener() {
    override fun onVideoTaken(video: File?) {
        Log.d("VL", "Funciona")
    }
}