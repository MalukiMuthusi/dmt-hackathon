package codes.malukimuthusi.hackathon.dataModel

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

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

    @Exclude
    override fun toString(): String {
        return name ?: super.toString()
    }
}


@IgnoreExtraProperties
data class Sacco(
    val name: String? = "",
    val fare: Fare? = Fare(),
    val Routes: MutableMap<String, Boolean>? = HashMap(),
    @Exclude var key: String? = ""
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "name" to name,
            "fare" to fare?.toMap(),
            "Routes" to Routes
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
    var four_five: Int? = 0,
    var five_six: Int? = 0,
    var six_seven: Int? = 0,
    var seven_eight: Int? = 0,
    var eight_nine: Int? = 0,
    var nine_ten: Int? = 0,
    var ten_eleven: Int? = 0,
    var eleven_twelve: Int? = 0,
    var twelve_thirteen: Int? = 0,
    var thirteen_fourteen: Int? = 0,
    var fourteen_fiveteen: Int? = 0,
    var fiveteen_sixteen: Int? = 0,
    var sixteen_seventeen: Int? = 0,
    var seventeen_eighteen: Int? = 0,
    var eighteen_nineteen: Int? = 0,
    var nineteen_twenty: Int? = 0,
    var twenty_twentyOne: Int? = 0,
    var twentyone_twentytwo: Int? = 0,
    var twentytwo_twentythree: Int? = 0
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "four_five" to four_five,
            "five_six" to five_six,
            "seven_Eight" to seven_eight,
            "eight_nine" to eight_nine,
            "nine_ten" to nine_ten,
            "ten_eleven" to ten_eleven,
            "eleven_twelve" to eleven_twelve,
            "twelve_thirteen" to twelve_thirteen,
            "thirteen_fourteen" to thirteen_fourteen,
            "fourteen_fiveteen" to fourteen_fiveteen,
            "fiveteen_sixteen" to fiveteen_sixteen,
            "sixteen_seventeen" to sixteen_seventeen,
            "seventeen_eighteen" to seventeen_eighteen,
            "eighteen_nineteen" to eighteen_nineteen,
            "nineteen_twenty" to nineteen_twenty,
            "twenty_twentyOne" to twenty_twentyOne,
            "twentyone_twentytwo" to twentyone_twentytwo,
            "twentytwo_twentythree" to twentytwo_twentythree
        )
    }

    companion object {
        fun createFareWithSingleValue(vG: Int): Fare {
            return Fare(
                vG, vG, vG, vG, vG, vG, vG, vG, vG, vG, vG, vG, vG, vG, vG, vG, vG, vG, vG
            )
        }
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




