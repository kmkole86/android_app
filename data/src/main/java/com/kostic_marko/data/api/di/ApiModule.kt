package com.kostic_marko.data.api.di

import com.kostic_marko.data.api.data_source_impl.MovieApiDataSourceImpl
import com.kostic_marko.data.data_source.MovieApiDataSource
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val apiModule = module {
    single<HttpClient> {
        HttpClient(Android) {

            install(DefaultRequest) {
                url {
                    url("https://api.themoviedb.org/3/movie/")
                    parameters.append("api_key", "a794ee27f47722d30bc1c67e3df3522a")
                }
            }

            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
        }
    }

    singleOf(::MovieApiDataSourceImpl) bind MovieApiDataSource::class
}