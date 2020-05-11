package codes.malukimuthusi.hackathon.data

data class Route(
    val name: String = "CBD-Rongai",
    val start: String = "CBD",
    val End: String = "Rongai",
    val sacco: List<Sacco> = saccoList
)

class Sacco(
    val name: String = "Sacco A",
    val fare: Fare = Fare(),
    val matatus: List<Matatu> = matatusList
) {

}

class Matatu(
    val name: String = "Matatu_A",
    val numberPlate: String = "KCX XXX",
    val driverName: String = "driver",
    val conductor: String = "conductor"
) {

}

class Fare(
    var fourToFive: Int = 60,
    var fiveToSix: Int = 70,
    var sixToSeven: Int = 70,
    var sevenToEight: Int = 80,
    var eightToNine: Int = 90,
    var nineToTen: Int = 90,
    var tenToEleven: Int = 40
) {

}

val saccoList = listOf<Sacco>(
    Sacco(),
    Sacco("Sacco 2")
)

val matatusList = listOf<Matatu>(
    Matatu(),
    Matatu("A")
)
