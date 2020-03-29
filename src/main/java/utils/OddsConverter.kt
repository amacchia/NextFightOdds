package utils

class OddsConverter {

    companion object {

        fun convertOdds(decimalOdds: Double): String {
            return if (decimalOdds >= 2.0)
                convertFavoriteOdds(decimalOdds)
            else
                convertUnderdogOdds(decimalOdds)
        }

        private fun convertFavoriteOdds(decimalOdds: Double): String {
            val decInt = ((decimalOdds - 1) * 100).toInt()
            return "+$decInt"
        }

        private fun convertUnderdogOdds(decimalOdds: Double): String {
            val decInt = (-100 / (decimalOdds - 1)).toInt()
            return "$decInt"
        }

    }

}