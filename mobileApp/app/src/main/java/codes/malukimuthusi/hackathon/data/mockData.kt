package codes.malukimuthusi.hackathon.data



//data class EverageFare(
//    override val from: String = "CBD",
//    override val to: String = "Kitengela",
//    override val fare: Int = 50
//) : ChartData {
//}
//
//val chartFares = listOf<EverageFare>(
//    EverageFare("Kitengela", "Nairobi", 60),
//    EverageFare("CBD", "Lang'ata", 80),
//    EverageFare("CBD", "Kiambu", 100),
//    EverageFare("CBD", "Kasarani", 70),
//    EverageFare("CBD", "Roysambu", 200),
//    EverageFare("CBD", "Lang'ata", 80),
//    EverageFare("Kitengela", "Nairobi", 60),
//    EverageFare("CBD", "Lang'ata", 80),
//    EverageFare("CBD", "Kiambu", 100),
//    EverageFare("CBD", "Kasarani", 70),
//    EverageFare("CBD", "Roysambu", 200),
//    EverageFare("CBD", "Lang'ata", 80),
//    EverageFare("CBD", "Kasarani", 70),
//    EverageFare("CBD", "Roysambu", 200),
//    EverageFare("CBD", "Lang'ata", 80),
//    EverageFare("Kitengela", "Nairobi", 60),
//    EverageFare("CBD", "Lang'ata", 80),
//    EverageFare("CBD", "Kiambu", 100),
//    EverageFare("CBD", "Kasarani", 70),
//    EverageFare("CBD", "Roysambu", 200),
//    EverageFare("CBD", "Lang'ata", 80),
//    EverageFare("CBD", "Kiambu", 100),
//    EverageFare("CBD", "Kasarani", 70),
//    EverageFare("CBD", "Roysambu", 200),
//    EverageFare("CBD", "Lang'ata", 80),
//    EverageFare("Kitengela", "Nairobi", 60),
//    EverageFare("CBD", "Lang'ata", 80),
//    EverageFare("CBD", "Kiambu", 100),
//    EverageFare("CBD", "Kasarani", 70),
//    EverageFare("CBD", "Roysambu", 200),
//    EverageFare("CBD", "Lang'ata", 80),
//    EverageFare("CBD", "Kasarani", 70)
//)

data class SaccoDetails(
    val name: String = "REMBO",
    val fare: Int = 50
)

val saccosList = listOf<SaccoDetails>(
    SaccoDetails(), SaccoDetails("UWAZO SACCO", 60),
    SaccoDetails("UTENGO SACCO", 70)
)