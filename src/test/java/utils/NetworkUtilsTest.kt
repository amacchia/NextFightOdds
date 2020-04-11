package utils

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import org.junit.Assert.assertNotNull
import org.junit.Ignore
import org.junit.Test
import org.junit.jupiter.api.assertThrows
import java.net.HttpURLConnection
import java.net.URL

class NetworkUtilsTest {

    @Test
    @Ignore("StackOverflowError when trying to mock URL class")
    fun testRetrieveOdds_FailedRequest() {
        val mockHttpConnection = mockk<HttpURLConnection> {
            every { responseCode } returns 404
        }

        mockkConstructor(URL::class)
        every { anyConstructed<URL>().openConnection() } returns mockHttpConnection

        assertThrows<Exception> { NetworkUtils.retrieveOdds() }
    }

    @Test
    fun testConstructor() {
        val networkUtils = NetworkUtils()

        assertNotNull(networkUtils)
    }

}