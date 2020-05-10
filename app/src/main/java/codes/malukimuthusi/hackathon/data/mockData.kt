package codes.malukimuthusi.hackathon.data

data class AvarageFare(
    val from: String = "CBD",
    val to: String = "Kitengela",
    val fare: Int = 50
) {
}

val chartFares = listOf<AvarageFare>(
    AvarageFare("Kitengela", "Nairobi", 60),
    AvarageFare("CBD", "Lang'ata", 80),
    AvarageFare("CBD", "Kiambu", 100),
    AvarageFare("CBD", "Kasarani", 70),
    AvarageFare("CBD", "Roysambu", 200),
    AvarageFare("CBD", "Lang'ata", 80),
    AvarageFare("Kitengela", "Nairobi", 60),
    AvarageFare("CBD", "Lang'ata", 80),
    AvarageFare("CBD", "Kiambu", 100),
    AvarageFare("CBD", "Kasarani", 70),
    AvarageFare("CBD", "Roysambu", 200),
    AvarageFare("CBD", "Lang'ata", 80),
    AvarageFare("CBD", "Kasarani", 70),
    AvarageFare("CBD", "Roysambu", 200),
    AvarageFare("CBD", "Lang'ata", 80),
    AvarageFare("Kitengela", "Nairobi", 60),
    AvarageFare("CBD", "Lang'ata", 80),
    AvarageFare("CBD", "Kiambu", 100),
    AvarageFare("CBD", "Kasarani", 70),
    AvarageFare("CBD", "Roysambu", 200),
    AvarageFare("CBD", "Lang'ata", 80),
    AvarageFare("CBD", "Kiambu", 100),
    AvarageFare("CBD", "Kasarani", 70),
    AvarageFare("CBD", "Roysambu", 200),
    AvarageFare("CBD", "Lang'ata", 80),
    AvarageFare("Kitengela", "Nairobi", 60),
    AvarageFare("CBD", "Lang'ata", 80),
    AvarageFare("CBD", "Kiambu", 100),
    AvarageFare("CBD", "Kasarani", 70),
    AvarageFare("CBD", "Roysambu", 200),
    AvarageFare("CBD", "Lang'ata", 80),
    AvarageFare("CBD", "Kasarani", 70)
)

data class SaccoDetails(
    val name: String = "REMBO",
    val fare: Int = 50
) {

}

val saccosList = listOf<SaccoDetails>(
    SaccoDetails(), SaccoDetails("UWAZO SACCO", 60),
    SaccoDetails("UTENGO SACCO", 70)
)