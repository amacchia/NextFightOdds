package utils

import domain.Fight
import domain.Fighter
import org.json.JSONArray
import org.json.JSONObject

class JsonUtils {

    companion object {

        private const val FAVORITE_INDEX = 0
        private const val UNDERDOG_INDEX = 1

        fun convertAllFightsFromJson(allFights: JSONArray): List<Fight> {
            val fights = mutableListOf<Fight>()
            allFights.forEach {
                val fightJson = it as JSONObject
                val fight = convertSingleFightFromJson(fightJson)
                fights.add(fight)
            }
            return fights
        }

        private fun convertSingleFightFromJson(fight: JSONObject): Fight {
            val fighterNames = fight.getJSONArray("teams")
            val bettingSites = fight.getJSONArray("sites")

            val (favorite, underdog) = parseFighterNames(fighterNames)
            val (favoriteOdds, underdogOdds) = parseFightOdds(bettingSites)

            return Fight(favorite, underdog, favoriteOdds, underdogOdds)
        }

        private fun parseFighterNames(fighterNames: JSONArray): Pair<Fighter, Fighter> {
            val favoriteName = fighterNames.getString(FAVORITE_INDEX)
            val underdogName = fighterNames.getString(UNDERDOG_INDEX)
            val favorite = Fighter(favoriteName)
            val underdog = Fighter(underdogName)
            return Pair(favorite, underdog)
        }

        private fun parseFightOdds(bettingSites: JSONArray): Pair<Double?, Double?> {
            return if (bettingSites.length() > 0) {
                val selectedSite = bettingSites.getJSONObject(0)
                val odds = selectedSite.getJSONObject("odds")
                val h2hOdds = odds.getJSONArray("h2h")
                val favoriteOdds = h2hOdds.getDouble(FAVORITE_INDEX)
                val underDogOdds = h2hOdds.getDouble(UNDERDOG_INDEX)
                Pair(favoriteOdds, underDogOdds)
            } else
                Pair(null, null)
        }
    }
}