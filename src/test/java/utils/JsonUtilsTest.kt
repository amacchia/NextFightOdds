package utils

import helpers.responseDataJson
import org.json.JSONArray
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.assertThrows

class JsonUtilsTest {

    @Test
    fun testConvertAllFightsFromJson_ValidJson() {
        val jsonArray = JSONArray(responseDataJson)

        val fightsList = JsonUtils.convertAllFightsFromJson(jsonArray)

        assertTrue(fightsList.size == 2)
    }

    @Test
    fun testConvertAllFightsFromJson_CannotConvertToJsonObject() {
        val jsonArray = JSONArray()
        jsonArray.put(true)

        assertThrows<ClassCastException> { JsonUtils.convertAllFightsFromJson(jsonArray) }
    }

    @Test
    fun testConstructor() {
        val jsonUtils = JsonUtils()

        assertNotNull(jsonUtils)
    }

}