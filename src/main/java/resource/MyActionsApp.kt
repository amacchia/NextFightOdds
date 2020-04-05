package resource

import com.google.actions.api.ActionRequest
import com.google.actions.api.ActionResponse
import com.google.actions.api.DialogflowApp
import com.google.actions.api.ForIntent
import logger.Logger
import utils.NetworkUtils
import utils.ResponseGenerator

class MyActionsApp : DialogflowApp() {

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

}
