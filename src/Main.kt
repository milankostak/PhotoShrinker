import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.core.Size
import org.opencv.imgcodecs.Imgcodecs
import org.opencv.imgproc.Imgproc
import org.opencv.imgproc.Imgproc.INTER_AREA
import java.io.File


object Main {

    private val baseFolder = """C:\path\2020\orig\""".replace("\\", "/")
    private const val folder = "Folder"

    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME)
        val innerFolders = File(baseFolder).list()
        val filtered = innerFolders?.filter { f -> f.contains(folder) }
        filtered?.forEach { innerFolder ->
            val origFolder = "$baseFolder$innerFolder/"
            val thumbsFolder = origFolder.replace("/orig/", "/thumbs/")
            val galleryFolder = origFolder.replace("/orig/", "/gallery/")
            val images = File(origFolder).list()
            for (image in images!!) {
                val input = Imgcodecs.imread(origFolder + image)
                create(input, thumbsFolder + image, 300, 225)
                create(input, galleryFolder + image, 2000, 1500)
            }
        }
    }

    private fun create(input: Mat, newImagePath: String, targetWidth: Int, targetHeight: Int) {
        val w = input.width().toDouble()
        val h = input.height().toDouble()
        val ratio = if (h > w) {
            h.div(targetWidth)
        } else {
            h.div(targetHeight)
        }

        val thumb = Mat()
        Imgproc.resize(input, thumb, Size(), 1 / ratio, 1 / ratio, INTER_AREA)
        println("${thumb.width()} ${thumb.height()} $newImagePath")
        Imgcodecs.imwrite(newImagePath, thumb)
    }

}
