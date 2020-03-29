package resource

import com.google.actions.api.ActionRequest
import com.google.actions.api.ActionResponse
import com.google.actions.api.DialogflowApp
import com.google.actions.api.ForIntent
import org.slf4j.LoggerFactory
import utils.NetworkUtils
import utils.ResponseGenerator
import java.util.*

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

            val allFights = NetworkUtils.retrieveOdds()
            val response = ResponseGenerator.generateResponse(fighterName, allFights)
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

}
