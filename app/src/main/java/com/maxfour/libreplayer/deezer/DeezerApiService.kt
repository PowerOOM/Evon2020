package com.maxfour.libreplayer.deezer

import android.content.Context
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query
import java.io.File
import java.util.*

private const val BASE_QUERY_ARTIST = "search/artist"
private const val BASE_URL = "https://api.deezer.com/"

interface DeezerApiService {

    @GET("$BASE_QUERY_ARTI