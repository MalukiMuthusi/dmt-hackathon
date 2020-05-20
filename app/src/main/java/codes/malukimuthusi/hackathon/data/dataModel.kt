package codes.malukimuthusi.hackathon.data

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.PropertyName

@IgnoreExtraProperties
data class Route(
    val name: String? = "",
    val start: String? = "",
    val end: String? = "",
    val short_name: String? = "",
    val fare: Int? = 0,
    val saccos: MutableMap<String, Boolean>? = mutableMapOf(),
    @Exclude var key: String = ""


) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "name" to name,
            "start" to start,
            "end" to end,
            "saccos" to saccos
        )
    }
}

@IgnoreExtraProperties
data class Sacco(
    val name: String? = "",
    val fare: Fare? = Fare(),
    @PropertyName("Routes") val routes: MutableMap<String, Boolean>? = HashMap(),
    @Exclude var key: String? = ""
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "sacco_name" to name,
            "fare" to fare?.toMap(),
            "Routes" to routes
        )
    }
}

@Exclude
fun List<Sacco>.toMap(): List<Map<String, Any?>> {
    return map {
        it.toMap()
    }
}

@IgnoreExtraProperties
data class Matatu(
    val name: String = "Matatu_A",
    val numberPlate: String = "KCX XXX",
    val driverName: String = "driver",
    val conductor: String = "conductor"
) {
    @Exclude
    fun toMap(): Map<String, Any> {
        return mapOf(
            "matatu_name" to name,
            "number_plate" to numberPlate,
            "driver_name" to driverName,
            "conductor_name" to conductor
        )
    }

}

fun List<Matatu>.ttoMap(): List<Map<String, Any>> {
    return map {
        it.toMap()
    }
}

@IgnoreExtraProperties
data class Fare(
    @PropertyName("four_five") var fourToFive: Int? = 0,
    @PropertyName("five_six") var fiveToSix: Int? = 0,
    @PropertyName("six_seven") var sixToSeven: Int? = 0,
    @PropertyName("seven_eight") var sevenToEight: Int? = 0,
    @PropertyName("eight_nine") var eightToNine: Int? = 0,
    @PropertyName("nine_ten") var nineToTen: Int? = 0,
    @PropertyName("ten_eleven") var tenToEleven: Int? = 0,
    @PropertyName("eleven_twelve") var elevenToTwelve: Int? = 0,
    @PropertyName("twelve_thirteen") var twelveToThirteen: Int? = 0,
    @PropertyName("thirteen_fourteen") var thirteenToFourteen: Int? = 0,
    @PropertyName("fourteen_fiveteen") var fourteenToFiveteen: Int? = 0,
    @PropertyName("fiveteen_sixteen") var fiveteenToSixteen: Int? = 0,
    @PropertyName("sixteen_seventeen") var sixteenToSeventeen: Int? = 0,
    @PropertyName("seventeen_eighteen") var seventeenToEighteen: Int? = 0,
    @PropertyName("eighteen_nineteen") var eighteenToNineteen: Int? = 0,
    @PropertyName("nineteen_twenty") var nineteenToTwenty: Int? = 0,
    @PropertyName("twenty_twentyOne") var twentyTotwentyone: Int? = 0,
    @PropertyName("twentyone_twentytwo") var twentyoneToTwentytwo: Int? = 0,
    @PropertyName("twentytwo_twentythree") var twentytwoToTwentythree: Int? = 0
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "four_five" to fourToFive,
            "five_six" to fiveToSix,
            "seven_Eight" to sevenToEight,
            "eight_nine" to eightToNine,
            "nine_ten" to nineToTen,
            "ten_eleven" to tenToEleven,
            "eleven_twelve" to elevenToTwelve,
            "twelve_thirteen" to twelveToThirteen,
            "thirteen_fourteen" to thirteenToFourteen,
            "fourteen_fiveteen" to fourteenToFiveteen,
            "fiveteen_sixteen" to fiveteenToSixteen,
            "sixteen_seventeen" to sixteenToSeventeen,
            "seventeen_eighteen" to seventeenToEighteen,
            "eighteen_nineteen" to eighteenToNineteen,
            "nineteen_twenty" to nineteenToTwenty,
            "twenty_twentyOne" to twentyTotwentyone,
            "twentyone_twentytwo" to twentyoneToTwentytwo,
            "twentytwo_twentythree" to twentytwoToTwentythree
        )
    }

}

object Mock {
    val saccoLists = listOf(
        Sacco()
    )

    val matatusList = listOf<Matatu>(
        Matatu(),
        Matatu("A")
    )
}

/*
* Data interface of the fare chart.
*
* */
abstract class ChartData(
    val from: String?,
    val to: String?,
    val fare: Int?
) : Any()




