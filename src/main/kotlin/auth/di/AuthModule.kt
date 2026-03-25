package app.turnity.auth.di

import app.turnity.auth.repository.UserRepository
import app.turnity.auth.repository.UserRepositoryImpl
import app.turnity.auth.service.AuthService
import org.koin.dsl.module

val authModule = module {
    single<UserRepository> { UserRepositoryImpl(get()) }
    single { AuthService(get(), get()) }
}
