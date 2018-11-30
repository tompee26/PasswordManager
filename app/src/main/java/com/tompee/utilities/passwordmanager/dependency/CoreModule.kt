package com.tompee.utilities.passwordmanager.dependency

import android.content.Context
import androidx.room.Room
import com.google.firebase.auth.FirebaseAuth
import com.tompee.utilities.passwordmanager.Constants
import com.tompee.utilities.passwordmanager.core.asset.AssetManager
import com.tompee.utilities.passwordmanager.core.auth.Authenticator
import com.tompee.utilities.passwordmanager.core.auth.firebase.FirebaseAuthenticator
import com.tompee.utilities.passwordmanager.core.autofill.AutofillManager
import com.tompee.utilities.passwordmanager.core.autofill.impl.AutofillManagerImpl
import com.tompee.utilities.passwordmanager.core.biometric.BiometricManager
import com.tompee.utilities.passwordmanager.core.biometric.reprint.ReprintBiometricManager
import com.tompee.utilities.passwordmanager.core.cipher.Cipher
import com.tompee.utilities.passwordmanager.core.cipher.aes.AESCipher
import com.tompee.utilities.passwordmanager.core.clipboard.ClipboardManager
import com.tompee.utilities.passwordmanager.core.database.PackageDao
import com.tompee.utilities.passwordmanager.core.database.PasswordDatabase
import com.tompee.utilities.passwordmanager.core.database.SiteDao
import com.tompee.utilities.passwordmanager.core.generator.PasswordGenerator
import com.tompee.utilities.passwordmanager.core.generator.impl.RandomPasswordGenerator
import com.tompee.utilities.passwordmanager.core.packages.PackageManager
import com.tompee.utilities.passwordmanager.core.packages.impl.PackageManagerImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CoreModule {

    @Provides
    @Singleton
    fun provideBiometricManager(reprintBiometricManager: ReprintBiometricManager): BiometricManager =
        reprintBiometricManager

    @Provides
    @Singleton
    fun provideReprintBiometricManager(): ReprintBiometricManager = ReprintBiometricManager()

    @Provides
    @Singleton
    fun providePackageManager(packageManagerImpl: PackageManagerImpl): PackageManager = packageManagerImpl

    @Provides
    @Singleton
    fun providePackageManagerImpl(context: Context): PackageManagerImpl = PackageManagerImpl(context)

    @Provides
    @Singleton
    fun provideCipher(): Cipher = AESCipher()

    @Provides
    @Singleton
    fun provideClipboardManagerImpl(context: Context): ClipboardManager = ClipboardManager(context)

    @Provides
    @Singleton
    fun provideAssetManager(context: Context): AssetManager = AssetManager(context)

    @Provides
    @Singleton
    fun provideAutofillManager(autofillManagerImpl: AutofillManagerImpl): AutofillManager = autofillManagerImpl

    @Provides
    @Singleton
    fun provideAutofillManagerImpl(context: Context): AutofillManagerImpl = AutofillManagerImpl(context)

    @Provides
    @Singleton
    fun provideDatabase(context: Context): PasswordDatabase {
        return Room.databaseBuilder(context, PasswordDatabase::class.java, Constants.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun providePackageDao(database: PasswordDatabase): PackageDao = database.packageDao()

    @Provides
    @Singleton
    fun provideSiteDao(database: PasswordDatabase): SiteDao = database.siteDao()

    @Provides
    @Singleton
    fun providePasswordGenerator(randomPasswordGenerator: RandomPasswordGenerator): PasswordGenerator =
        randomPasswordGenerator

    @Provides
    @Singleton
    fun provideRandomPasswordGenerator(): RandomPasswordGenerator = RandomPasswordGenerator()

    @Provides
    @Singleton
    fun provideAuthenticator(firebaseAuthenticator: FirebaseAuthenticator) : Authenticator = firebaseAuthenticator

    @Provides
    @Singleton
    fun provideFirebaseAuthenticator() : FirebaseAuthenticator = FirebaseAuthenticator(FirebaseAuth.getInstance())
}