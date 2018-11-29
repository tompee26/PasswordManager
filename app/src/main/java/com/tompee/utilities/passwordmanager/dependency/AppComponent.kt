package com.tompee.utilities.passwordmanager.dependency

import android.app.Application
import com.tompee.utilities.passwordmanager.PasswordManagerApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ActivityModule::class,
        ServiceModule::class,
        AppModule::class,
        CoreModule::class,
        AndroidSupportInjectionModule::class
    ]
)
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(passwordManagerApplication: PasswordManagerApplication)
}