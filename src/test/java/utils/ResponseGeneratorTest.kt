package utils

import domain.Fight
import domain.Fighter
import io.mockk.every
import io.mockk.mockkObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.jupiter.api.assertThrows

class ResponseGeneratorTest {

    private val favoriteFighter = Fighter("Favorite Name")
    private val underdogFighter = Fighter("Underdog Name")
    private val favoriteOdds = 1.5
    private val underdogOdds = 2.5

    @Test
    fun testResponseGenerator_ValidFight() {
        val expectedResponse = "The odds Favorite Name wins is -200 and the odds Underdog Name wins is +150"
        val mockedFight = Fight(favoriteFighter, underdogFighter, favoriteOdds, underdogOdds)
        val mockFightList = listOf(mockedFight)
        mockkObject(FightMatcher)
        every { FightMatcher.findMatchingFight(mockFightList, favoriteFighter.name) } returns mockedFight

        val actualResponse = ResponseGenerator.generateResponse(favoriteFighter.name, mockFightList)

        assertEquals(expectedResponse, actualResponse)
    }

    @Test
    fun testResponseGenerator_FightHasNoOddsFavorite() {
        val mockedInvalidFight = Fight(favoriteFighter, underdogFighter, null, 2.1)
        val mockFightList = listOf(mockedInvalidFight)
        mockkObject(FightMatcher)
        every { FightMatcher.findMatchingFight(mockFightList, favoriteFighter.name) } returns mockedInvalidFight

        assertThrows<Exception> { ResponseGenerator.generateResponse(favoriteFighter.name, mockFightList) }
    }

    @Test
    fun testResponseGenerator_FightHasNoOddsUndergo() {
        val mockedInvalidFight = Fight(favoriteFighter, underdogFighter, 1.0, null)
        val mockFightList = listOf(mockedInvalidFight)
        mockkObject(FightMatcher)
        every { FightMatcher.findMatchingFight(mockFightList, favoriteFighter.name) } returns mockedInvalidFight

        assertThrows<Exception> { ResponseGenerator.generateResponse(favoriteFighter.name, mockFightList) }
    }

    @Test
    fun testConstructor() {
        val responseGenerator = ResponseGenerator()

        assertNotNull(responseGenerator)
    }

}