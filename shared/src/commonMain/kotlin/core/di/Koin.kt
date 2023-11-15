package core.di

import data.repository.AuthRepositoryImpl
import data.repository.ProductRepositoryImpl
import data.service.AuthApiService
import data.service.ProductApiService
import domain.repository.AuthRepository
import domain.repository.ProductRepository
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import presentation.auth.AuthViewModel
import presentation.products.HomeViewModel


fun initKoin(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    appDeclaration()
    modules(
        service,
        repositoryModule,
        viewModelModule,
    )
}

val service = module {
    single {
        val json = Json {
            ignoreUnknownKeys = true
            useAlternativeNames = false
            isLenient = true
            prettyPrint = true
        }
        val loggerObj = object : Logger {
            override fun log(message: String) {
                co.touchlab.kermit.Logger.d { message }
            }
        }
        val client = HttpClient {
            install(ContentNegotiation) { json(json) }
            install(Logging) { logger = loggerObj }
        }
        client
    }
    single { AuthApiService(get()) }
    single { ProductApiService(get()) }
}

val repositoryModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single<ProductRepository> { ProductRepositoryImpl(get()) }
}

val viewModelModule = module {
    factory { AuthViewModel(get()) }
    factory { HomeViewModel(get()) }
}