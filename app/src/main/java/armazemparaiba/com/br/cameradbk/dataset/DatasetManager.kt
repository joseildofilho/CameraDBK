package armazemparaiba.com.br.cameradbk.dataset

import java.io.File

class DatasetManager(folder:File, base: String = "/videos") {

    private var databaseFolder: File
    private var id = 0 //TODO some mecanism to get a global id

    init {
        if (folder.isDirectory) {
            databaseFolder = File(folder, base)
            if(databaseFolder.exists().not())
                databaseFolder.mkdirs()
        } else
            throw Exception("não é uma pasta")
    }

    fun createFolder(file:String): File {
        val f = File(databaseFolder, file)
        if (!f.exists())
            f.mkdirs()
        return f
    }

    fun getFile(produto:String, marca:String, nome:String):File {
        val fileName = "$nome#${id++}.mp4"
        val destination = createFolder("/$produto/$marca/")
        return File(destination, fileName)
    }
}