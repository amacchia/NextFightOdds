package utils

import domain.Fight
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class NetworkUtils {
    companion object {
        private const val URL = "https://api.the-odds-api.com/v3/odds?apiKey="
        private const val API_KEY = "b1fe9034711f9f456a4f388d4e7d677b"
        private const val SPORT_QUERY_PARAM = "&sport=mma_mixed_martial_arts&region=us"
        private const val REGION_QUERY_PARAM = "&region=us"
        private const val ODDS_ENDPOINT = URL + API_KEY + SPORT_QUERY_PARAM + REGION_QUERY_PARAM

        fun retrieveOdds(): List<Fight> {
            val url = URL(ODDS_ENDPOINT)
            val httpURLConnection = url.openConnection() as HttpURLConnection
            httpURLConnection.requestMethod = "GET"

            val resCode = httpURLConnection.responseCode
            if (resCode > 299)
                throw Exception("Error retrieving sports odds. Status code: $resCode")

            val convertedFights = convertResponseToJson(httpURLConnection.inputStream)
            httpURLConnection.disconnect()
            return convertedFights
        }

        private fun convertResponseToJson(inputStream: InputStream): List<Fight> {
            val responseBody = convertBodyToString(inputStream)
            val fights = parseFightsArray(responseBody)
            return JsonUtils.convertAllFightsFromJson(fights)
        }

        private fun convertBodyToString(inputStream: InputStream): String {
            val buffReader = BufferedReader(InputStreamReader(inputStream))
            val content = StringBuffer()
            buffReader.forEachLine {
                content.append(it)
            }
            return content.toString()
        }

        private fun parseFightsArray(responseBody: String): JSONArray {
            val responseObject = JSONObject(responseBody)
            return responseObject.getJSONArray("data")
        }
    }
}