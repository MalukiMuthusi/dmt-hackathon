package codes.malukimuthusi.hackathon.webService

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap


const val BASE_URL = "http://localhost:8080/otp/routers/default/"
val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

interface OpenTripPlanner {

    // get all stops
    @GET("index/stops/")
    suspend fun allStops(): List<Stop>

    // get stop with given id
    @GET("index/stops/{stopID}")
    suspend fun givenStop(
        @Path(
            "stopID"
        ) stopID: String
    ): Stop

    // get all the routes
    @GET("index/routes")
    suspend fun getAllRoutes(): List<Route>

    // get a specific Route
    @GET("index/routes/{routeId}")
    suspend fun getSpecificRoute(
        @Path("routeId") routeId: String
    )

    // get the stops near a choosen coordinate
    @GET("index/stops")
    suspend fun getListOfStops(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("maxLat") maxLat: Double,
        @Query("minLat") minLat: Double,
        @Query("maxLon") maxLog: Double,
        @Query("minLon") minLog: Double,
        @Query("radius") radius: Double
    ): List<Stop>

    //plan a trip
    @GET("plan")
    suspend fun planTrip(
        @QueryMap options: Map<String, String>?
    ): Response
}

object OpenTripPlannerService {
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(okHttpClient)
        .build()

    val otpServices = retrofit.create(OpenTripPlanner::class.java)
}

val okHttpClient = OkHttpClient.Builder().apply {
    addInterceptor(Interceptor {
        val builder = it.request().newBuilder()
        builder.header("Content-Type", "*/*")
        builder.header("Accept", "application/json")
        return@Interceptor it.proceed(builder.build())
    })
}.build()