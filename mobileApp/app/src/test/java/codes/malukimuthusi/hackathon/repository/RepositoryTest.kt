package codes.malukimuthusi.hackathon.repository

import codes.malukimuthusi.hackathon.dataModel.Fare
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class RepositoryTest {
    private val fare = Fare(thirteen_fourteen = 50)

    // check whether current fare is the expected one.
    @Test
    fun getCurrentFare() {
        val nowFare = Repository.getCurrentFareFromSacco(fare)
        assertThat(nowFare).isEqualTo(50)
    }
}