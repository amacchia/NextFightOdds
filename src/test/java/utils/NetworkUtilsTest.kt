package utils

import org.junit.Assert.assertNotNull
import org.junit.Test

class NetworkUtilsTest {

//    @Test
//    fun testRetrieveOdds_FailedRequest() {
//        try {
//            val mockHttpConnection = mockk<HttpURLConnection> {
//                every { responseCode } returns 404
//            }
//
//            mockkConstructor(URL::class)
//            every { anyConstructed<URL>().openConnection() } returns mockHttpConnection
//            every { anyConstructed<HttpURLConnection>().responseCode } returns 404
//
//            assertThrows<Exception> { NetworkUtils.retrieveOdds() }
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }

    @Test
    fun testConstructor() {
        val networkUtils = NetworkUtils()

        assertNotNull(networkUtils)
    }

}