package utils

import domain.Fight

class ResponseGenerator {

    companion object {
        fun generateResponse(fighterName: String, fights: List<Fight>): String {
            val matchingFight = FightMatcher.findMatchingFight(fights, fighterName)
            val favorite = matchingFight.favorite
            val underdog = matchingFight.underdog
            val favoriteOdds = OddsConverter.convertOdds(matchingFight.favoriteOdds)
            val underdogOdds = OddsConverter.convertOdds(matchingFight.underdogOdds)
            return "The odds ${favorite.name} wins is $favoriteOdds " +
                    "and the odds ${underdog.name} wins is $underdogOdds"
        }
    }

}