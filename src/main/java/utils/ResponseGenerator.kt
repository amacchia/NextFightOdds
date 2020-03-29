package utils

import domain.Fight

class ResponseGenerator {

    companion object {
        fun generateResponse(fighterName: String, fights: List<Fight>): String {
            val matchingFight = FightMatcher.findMatchingFight(fights, fighterName)
            val favorite = matchingFight.favorite
            val underdog = matchingFight.underdog
            val favoriteOdds = matchingFight.favoriteOdds
                    ?: throw Exception("No odds found for ${favorite.name} versus ${underdog.name}")
            val underdogOdds = matchingFight.underdogOdds
                    ?: throw Exception("No odds found for ${favorite.name} versus ${underdog.name}")
            val convertedFavoriteOdds = OddsConverter.convertOdds(favoriteOdds)
            val convertedUnderdogOdds = OddsConverter.convertOdds(underdogOdds)
            return "The odds ${favorite.name} wins is $convertedFavoriteOdds " +
                    "and the odds ${underdog.name} wins is $convertedUnderdogOdds"
        }
    }

}