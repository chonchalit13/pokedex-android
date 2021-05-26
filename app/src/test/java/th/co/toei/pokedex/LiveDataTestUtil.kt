package th.co.toei.pokedex

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import kotlin.jvm.Throws

object LiveDataTestUtil {

    /**
     * Gets the value of a LiveData safely.
     */
    @Throws(InterruptedException::class)
    fun <T> getValue(liveData: LiveData<T>, timeout: Long = 2): T? {
        var data: T? = null
        val latch = CountDownLatch(1)
        val observer = object : Observer<T> {
            override fun onChanged(o: T?) {
                data = o
                latch.countDown()
                liveData.removeObserver(this)
            }
        }
        liveData.observeForever(observer)
        latch.await(timeout, TimeUnit.SECONDS)

        return data
    }
}