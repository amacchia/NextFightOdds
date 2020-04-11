package resource

import com.google.actions.api.ActionRequest
import domain.Fight
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import utils.NetworkUtils
import utils.ResponseGenerator

class MyActionsAppTest {

    private lateinit var actionsApp: MyActionsApp

    private val mockFighterName = "Mock Fighter"
    private val mockResponse = "Mocked Response"
    private val mockFightList = listOf<Fight>()
    private val actionRequest = mockk<ActionRequest> {
        every { getParameter("person") } returns mapOf("name" to mockFighterName)
        every { conversationData } returns mutableMapOf()
        every { userStorage } returns mutableMapOf()
        every { sessionId } returns "some-id"
    }

    @Before
    fun setup() {
        unmockkObject(ResponseGenerator)

        mockkObject(NetworkUtils)
        every { NetworkUtils.retrieveOdds() } returns mockFightList

        actionsApp = MyActionsApp()
    }

    @Test
    fun testConstructor() {
        assertNotNull(actionsApp)
    }

    @Test
    fun testGetOdds_SuccessfulRequest() {
        mockkObject(ResponseGenerator)
        every { ResponseGenerator.generateResponse(mockFighterName, mockFightList) } returns mockResponse

        val actionResponse = actionsApp.getOdds(actionRequest)

        assertTrue(actionResponse.toJson().contains(mockResponse))
    }

    @Test
    fun testGetOdds_UnsuccessfulRequest() {
        val actionResponse = actionsApp.getOdds(actionRequest)

        assertTrue(actionResponse.toJson().contains("No upcoming fight found for"))
    }

    @Test
    fun testGetOdds_UnsuccessfulRequestNoExceptionMessage() {
        mockkObject(ResponseGenerator)
        every { ResponseGenerator.generateResponse(mockFighterName, mockFightList) } throws Exception()

        val actionResponse = actionsApp.getOdds(actionRequest)

        assertTrue(actionResponse.toJson().contains("There was an error processing your request."))
    }

}