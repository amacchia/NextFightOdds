/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example

import com.google.actions.api.ActionRequest
import com.google.actions.api.ActionResponse
import com.google.actions.api.DialogflowApp
import com.google.actions.api.ForIntent
import com.google.actions.api.response.ResponseBuilder
import com.google.api.services.actions_fulfillment.v2.model.User
import com.google.gson.internal.LinkedTreeMap
import org.json.JSONArray
import org.json.JSONObject
import java.util.ResourceBundle
import java.util.stream.Collectors
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

/**
 * Implements all intent handlers for this Action. Note that your App must extend from DialogflowApp
 * if using Dialogflow or ActionsSdkApp for ActionsSDK based Actions.
 */
class MyActionsApp : DialogflowApp() {

    @ForIntent("Default Welcome Intent")
    fun welcome(request: ActionRequest): ActionResponse {
        LOGGER.info("Welcome intent start.")
        val responseBuilder = getResponseBuilder(request)
        val rb = ResourceBundle.getBundle("resources")
        val user = request.user

        if (user != null && user.lastSeen != null) {
            responseBuilder.add(rb.getString("welcome_back"))
        } else {
            responseBuilder.add(rb.getString("welcome"))
        }

        LOGGER.info("Welcome intent end.")
        return responseBuilder.build()
    }

    @ForIntent("bye")
    fun bye(request: ActionRequest): ActionResponse {
        LOGGER.info("Bye intent start.")
        val responseBuilder = getResponseBuilder(request)
        val rb = ResourceBundle.getBundle("resources")

        responseBuilder.add(rb.getString("bye")).endConversation()
        LOGGER.info("Bye intent end.")
        return responseBuilder.build()
    }

    @ForIntent("Get Fighter Name")
    fun getOdds(request: ActionRequest): ActionResponse {
        LOGGER.info("get odds intent started")
        val responseBuilder = getResponseBuilder(request)
        try {
            val person = request.getParameter("person") as Map<*, *>
            val fighterName = person["name"] as String

            val url = URL("https://api.the-odds-api.com/v3/odds?apiKey=b1fe9034711f9f456a4f388d4e7d677b&sport=mma_mixed_martial_arts&region=us")
            val con = url.openConnection() as HttpURLConnection
            con.requestMethod = "GET"

            val resCode = con.responseCode

            if (resCode > 299)
                responseBuilder.add("There was an error with your request, sorry bro.").endConversation()
            else {
                val resStream = con.inputStream
                val buffReader = BufferedReader(InputStreamReader(resStream))
                val content = StringBuffer()
                buffReader.forEachLine {
                    content.append(it)
                }
                con.disconnect()

                val resJson = JSONObject(content.toString())
                val fights = resJson.getJSONArray("data")

                val indexOfMatchingFight = fights.indexOfFirst {
                    val fight = it as JSONObject
                    val fighters = fight.getJSONArray("teams")
                    if (fighters == null) {
                        false
                    } else {
                        fighters.getString(0) == fighterName || fighters.getString(1) == fighterName
                    }
                }

                val matchingFight = fights.getJSONObject(indexOfMatchingFight)
                val fighters = matchingFight.getJSONArray("teams")
                val fighter1 = fighters.getString(0)
                val fighter2 = fighters.getString(1)
                val site = matchingFight.getJSONArray("sites").getJSONObject(0) // Just use the first site
                val odds = site.getJSONObject("odds").getJSONArray("h2h")
                val response = "The odds $fighter1 wins is ${convertOdds(odds.getDouble(0))} " +
                        "and the odds $fighter2 wins is ${convertOdds(odds.getDouble(1))}"
                responseBuilder.add(response).endConversation()
            }

            return responseBuilder.build()
        } catch (e: Exception) {
            return responseBuilder.add(e.toString()).build()
        }
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(MyActionsApp::class.java)
    }

    private fun convertOdds(decimalOdds: Double): String {
        return if (decimalOdds >= 2.0) {
            val decInt = ((decimalOdds - 1) * 100).toInt()
            "+$decInt"
        } else {
            val decInt = (-100 / (decimalOdds - 1)).toInt()
            "$decInt"
        }
    }
}
