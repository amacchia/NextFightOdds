package resource

import com.google.actions.api.ActionRequest
import com.google.actions.api.ActionResponse
import com.google.actions.api.DialogflowApp
import com.google.actions.api.ForIntent
import logger.Logger
import utils.NetworkUtils
import utils.ResponseGenerator
import java.util.*

class MyActionsApp : DialogflowApp() {

    @ForIntent("Default Welcome Intent")
    fun welcome(request: ActionRequest): ActionResponse {
        Logger.log("Welcome intent start.")
        val responseBuilder = getResponseBuilder(request)
        val rb = ResourceBundle.getBundle("resources")
        val user = request.user

        if (user != null && user.lastSeen != null) {
            responseBuilder.add(rb.getString("welcome_back"))
        } else {
            responseBuilder.add(rb.getString("welcome"))
        }

        Logger.log("Welcome intent end.")
        return responseBuilder.build()
    }


    @ForIntent("Get Fighter Name")
    fun getOdds(request: ActionRequest): ActionResponse {
        Logger.log("get odds intent started")
        val responseBuilder = getResponseBuilder(request)
        return try {
            val person = request.getParameter("person") as Map<*, *>
            val fighterName = person["name"] as String

            val allFights = NetworkUtils.retrieveOdds()
            val response = ResponseGenerator.generateResponse(fighterName, allFights)
            responseBuilder.add(response).endConversation()
            responseBuilder.build()
        } catch (e: Exception) {
            e.printStackTrace()
            responseBuilder.add(e.message ?: "There was an error processing your request.").build()
        }
    }

    @ForIntent("bye")
    fun bye(request: ActionRequest): ActionResponse {
        Logger.log("Bye intent start.")
        val responseBuilder = getResponseBuilder(request)
        val rb = ResourceBundle.getBundle("resources")

        responseBuilder.add(rb.getString("bye")).endConversation()
        Logger.log("Bye intent end.")
        return responseBuilder.build()
    }

}
