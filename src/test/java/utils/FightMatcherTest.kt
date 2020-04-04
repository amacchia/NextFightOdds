package utils

import domain.Fight
import domain.Fighter
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.assertThrows

class FightMatcherTest {

    private val favoriteFighter1 = Fighter("Favorite One")
    private val favoriteFighter2 = Fighter("Favorite Two")
    private val favoriteFighter3 = Fighter("Favorite Three")

    private val underdogFighter1 = Fighter("Underdog One")
    private val underdogFighter2 = Fighter("Underdog Two")
    private val underdogFighter3 = Fighter("Underdog Three")

    private val fightOne = Fight(favoriteFighter1, underdogFighter1, null, null)
    private val fightTwo = Fight(favoriteFighter2, underdogFighter2, null, null)
    private val fightThree = Fight(favoriteFighter3, underdogFighter3, null, null)

    private val fightList = listOf(fightOne, fightTwo, fightThree)

    @Test
    fun testFightMatcher_MatchingFightExistsFavorite() {
        val matchingFight = FightMatcher.findMatchingFight(fightList, favoriteFighter1.name)

        assertEquals(matchingFight, fightOne)
    }

    @Test
    fun testFightMatcher_MatchingFightExistsUnderdog() {
        val matchingFight = FightMatcher.findMatchingFight(fightList, underdogFighter1.name)

        assertEquals(matchingFight, fightOne)
    }

    @Test
    fun testFightMatcher_MatchingFightDoesNotExist() {
        assertThrows<Exception> { FightMatcher.findMatchingFight(fightList, "Fake Name") }
    }

    @Test
    fun testConstructor() {
        val fightMatcher = FightMatcher()

        assertNotNull(fightMatcher)
    }

}