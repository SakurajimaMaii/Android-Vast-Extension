import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ave.vastgui.tools.io.appExternalCacheDir
import com.ave.vastgui.tools.io.appExternalFilesDir
import com.ave.vastgui.tools.io.appInternalCacheDir
import com.ave.vastgui.tools.io.appInternalFilesDir
import com.ave.vastgui.tools.io.copyFile
import com.ave.vastgui.tools.io.destroy
import com.ave.vastgui.tools.io.getPath
import com.ave.vastgui.tools.io.mimeType
import com.ave.vastgui.tools.io.mkDirs
import com.ave.vastgui.tools.io.mkFile
import com.ave.vastgui.tools.io.move
import com.ave.vastgui.tools.io.rename
import com.ave.vastgui.tools.io.uri
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import kotlin.math.min

@RunWith(AndroidJUnit4::class)
class FileTests {

    @Test
    fun storage() {
        Log.d(TAG, "appInternalFilesDir = ${appInternalFilesDir().absolutePath} \n" +
                "appInternalCacheDir = ${appInternalCacheDir().absolutePath} \n" +
                "appExternalFilesDir = ${appExternalFilesDir("").absolutePath} \n" +
                "appExternalCacheDir = ${appExternalCacheDir().absolutePath}")
    }

    @Test
    fun mkFile() {
        val result = File(appInternalFilesDir(), "temp.txt").mkFile()
        Assert.assertTrue(result.isSuccess)
    }

    @Test
    fun mkDirs() {
        val result = File(appInternalFilesDir(), "temp").mkDirs()
        Assert.assertTrue(result.isSuccess)
    }

    @Test
    fun destroy() {
        val fileResult = File(appInternalFilesDir(), "temp.txt").destroy()
        Assert.assertTrue(fileResult.isSuccess)
        val dirResult = File(appInternalFilesDir(), "temp").destroy()
        Assert.assertTrue(dirResult.isSuccess)
    }

    @Test
    fun copy() {
        val temp = File(appInternalFilesDir(), "temp.txt")
        temp.mkFile()
        val tempDir = File(appInternalFilesDir(), "temp")
        val duplicate = File(tempDir, "temp.txt")
        tempDir.mkDirs()
        Assert.assertTrue(temp.copyFile(duplicate).isSuccess)
    }

    @Test
    fun move() {
        val tempDir = File(appInternalFilesDir(), "temp")
        tempDir.mkDirs()
        val result = tempDir.move(File(appInternalFilesDir(), "temp/temp1"))
        Log.d(TAG, result.exceptionOrNull()?.stackTraceToString().toString())
        Assert.assertTrue(result.isSuccess)
    }

    @Test
    fun rename() {
        val temp = File(appInternalFilesDir(), "temp.txt")
        temp.mkFile()
        Assert.assertTrue(temp.rename("temp1.txt").isSuccess)
    }

    @Test
    fun extension() {
        val temp = File(appInternalFilesDir(), "temp.txt")
        Assert.assertEquals("txt", temp.extension)
    }

    @Test
    fun uri() {
        val temp = File(appInternalFilesDir(), "temp.txt")
        Log.d(TAG, temp.uri("com.ave.vastgui.tools.test").toString())
        Log.d(TAG, temp.uri().toString())
    }

    @Test
    fun path() {
        val path = getPath { appInternalFilesDir().path f "dir1" f "dir2" }
        Log.d(TAG, path)
    }

    @Test
    fun mimeType() {
        val mimeType = File(appInternalFilesDir(), "temp.txt").mimeType()
        Assert.assertEquals("text/plain", mimeType)
    }


    companion object {
        private const val TAG = "FileTests"
    }

}