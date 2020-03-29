package domain

data class Fight(
        val favorite: Fighter,
        val underdog: Fighter,
        val favoriteOdds: Double?,
        val underdogOdds: Double?
)