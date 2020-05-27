package codes.malukimuthusi.hackathon.webService

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

const val BASE_URL = "http://localhost:8080/otp/routers/default/index/"
val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

interface OpenTripPlanner {

    // get all stops
    @GET("stops/")
    suspend fun allStops(): List<Stop>

    // get stop with given id
    @GET("stops/{stopID}")
    suspend fun givenStop(
        @Path(
            "stopID"
        ) stopID: String
    ): Stop

    // get all the routes
    suspend fun getAllRoutes()

    // get a specific Route
    @GET("routes/{routeId}")
    suspend fun getSpecificRoute(
        @Path("routeId") routeId: String
    )

    // get the stops near a choosen coordinate
    @GET("stops")
    suspend fun getListOfStops(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("maxLat") maxLat: Double,
        @Query("minLat") minLat: Double,
        @Query("maxLon") maxLog: Double,
        @Query("minLon") minLog: Double,
        @Query("radius") radius: Double
    ): List<Stop>?
}

object OpenTripPlannerService {
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val otpServices = retrofit.create(OpenTripPlanner::class.java)
}