package utils

import org.junit.Assert.assertEquals
import org.junit.Test

class OddsConverterTest {

    @Test
    fun testConvertingFavoriteOdds() {
        val odds = 1.2

        val convertedOdds = OddsConverter.convertOdds(odds)

        assertEquals("-500", convertedOdds)
    }


    @Test
    fun testConvertingUnderdogOddsLimit() {
        val odds = 2.0

        val convertedOdds = OddsConverter.convertOdds(odds)

        assertEquals("+100", convertedOdds)
    }

    @Test
    fun testConvertingUnderdogOdds() {
        val odds = 3.5

        val convertedOdds = OddsConverter.convertOdds(odds)

        assertEquals("+250", convertedOdds)
    }
}