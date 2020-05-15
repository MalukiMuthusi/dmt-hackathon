package codes.malukimuthusi.hackathon.data

import codes.malukimuthusi.hackathon.data.Mock.matatusList
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Route(
    val name: String = "CBD-Rongai",
    val start: String = "CBD",
    val End: String = "Rongai",
    val saccos: List<Sacco> = Mock.saccoLists
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "name" to name,
            "start" to start,
            "end" to End,
            "saccos" to saccos.toMap()
        )
    }
}

@IgnoreExtraProperties
data class Sacco(
    val name: String = "Sacco A",
    val fare: Fare = Fare(),
    val matatus: List<Matatu> = matatusList
) {
    @Exclude
    fun toMap(): Map<String, Any> {
        return mapOf(
            "sacco_name" to name,
            "fare" to fare.toMap(),
            "matatus" to matatus.ttoMap()
        )
    }


}

@Exclude
fun List<Sacco>.toMap(): List<Map<String, Any>> {
    return map {
        it.toMap()
    }
}

@IgnoreExtraProperties
class Matatu(
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
class Fare(
    var fourToFive: Int = 60,
    var fiveToSix: Int = 70,
    var sixToSeven: Int = 70,
    var sevenToEight: Int = 80,
    var eightToNine: Int = 90,
    var nineToTen: Int = 90,
    var tenToEleven: Int = 40
) {
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "four_five" to fourToFive,
            "five_six" to fiveToSix,
            "seven_Eight" to sevenToEight
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




