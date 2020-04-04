package utils

import domain.Fight

class FightMatcher {

    companion object {
        fun findMatchingFight(fights: List<Fight>, fighterName: String): Fight {
            val indexOfMatchingFight = fights.indexOfFirst {
                isMatchingFight(it, fighterName)
            }

            if (indexOfMatchingFight == -1)
                throw Exception("No upcoming fight found for $fighterName")

            return fights[indexOfMatchingFight]
        }

        private fun isMatchingFight(fight: Fight, fighterName: String): Boolean {
            val favorite = fight.favorite
            val underdog = fight.underdog

            return favorite.name.equals(fighterName, true) ||
                    underdog.name.equals(fighterName, true)
        }

    }

}