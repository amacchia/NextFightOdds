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
import org.json.JSONArray
import org.json.JSONObject
import org.slf4j.LoggerFactory
import java.util.*

/**
 * Implements all intent handlers for this Action. Note that your App must extend from DialogflowApp
 * if using Dialogflow or ActionsSdkApp for ActionsSDK based Actions.
 */
class MyActionsApp : DialogflowApp() {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(MyActionsApp::class.java)
    }

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


    @ForIntent("Get Fighter Name")
    fun getOdds(request: ActionRequest): ActionResponse {
        LOGGER.info("get odds intent started")
        val responseBuilder = getResponseBuilder(request)
        return try {
            val person = request.getParameter("person") as Map<*, *>
            val fighterName = person["name"] as String

            val allOddsJson = NetworkUtils.retrieveOdds()
            val response = generateResponse(fighterName, allOddsJson)
            responseBuilder.add(response).endConversation()
            responseBuilder.build()
        } catch (e: Exception) {
            responseBuilder.add(e.message ?: "There was an error processing your request.").build()
        }
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

    private fun generateResponse(fighterName: String, allOddsJson: JSONObject): String {
        val fights = allOddsJson.getJSONArray("data")
        val matchingFight = findMatchingFight(fighterName, fights)
        val fighters = matchingFight.getJSONArray("teams")
        val fighter1 = fighters.getString(0)
        val fighter2 = fighters.getString(1)
        val odds = getBettingOdds(matchingFight)
        val fighterOneOdds = odds.first
        val fighterTwoOdds = odds.second
        return "The odds $fighter1 wins is ${convertOdds(fighterOneOdds)} " +
                "and the odds $fighter2 wins is ${convertOdds(fighterTwoOdds)}"
    }

    private fun findMatchingFight(fighterName: String, allFights: JSONArray): JSONObject {
        val indexOfMatchingFight = allFights.indexOfFirst {
            isMatchingFight(it as JSONObject, fighterName)
        }

        if (indexOfMatchingFight == -1)
            throw Exception("No upcoming fight found for $fighterName")

        return allFights.getJSONObject(indexOfMatchingFight)
    }

    private fun isMatchingFight(fight: JSONObject, fighterName: String): Boolean {
        val fighters = fight.getJSONArray("teams")

        return if (fighters == null)
            false
        else
            fighters.getString(0) == fighterName || fighters.getString(1) == fighterName
    }

    private fun getBettingOdds(fight: JSONObject): Pair<Double, Double> {
        val bettingSites = fight.getJSONArray("sites")
        val site = bettingSites.getJSONObject(0)
        val oddsArray = site.getJSONObject("odds").getJSONArray("h2h")
        val fighterOneOdds = oddsArray.getDouble(0)
        val fighterTwoOdds = oddsArray.getDouble(1)
        return Pair(fighterOneOdds, fighterTwoOdds)
    }

    private fun convertOdds(decimalOdds: Double): String {
        return if (decimalOdds >= 2.0)
            convertFavoriteOdds(decimalOdds)
        else
            convertUnderdogOdds(decimalOdds)
    }

    private fun convertFavoriteOdds(decimalOdds: Double): String {
        val decInt = ((decimalOdds - 1) * 100).toInt()
        return "+$decInt"
    }

    private fun convertUnderdogOdds(decimalOdds: Double): String {
        val decInt = (-100 / (decimalOdds - 1)).toInt()
        return "$decInt"
    }
}
