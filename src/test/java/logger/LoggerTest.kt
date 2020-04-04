package logger

import org.junit.Assert.assertNotNull
import org.junit.Test

class LoggerTest {

    @Test
    fun testConstructor() {
        val logger = Logger()

        assertNotNull(logger)
    }

}