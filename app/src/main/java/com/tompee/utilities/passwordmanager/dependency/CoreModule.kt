package com.tompee.utilities.passwordmanager.dependency

import android.content.Context
import androidx.room.Room
import com.tompee.utilities.passwordmanager.Constants
import com.tompee.utilities.passwordmanager.core.biometric.BiometricManager
import com.tompee.utilities.passwordmanager.core.biometric.reprint.ReprintBiometricManager
import com.tompee.utilities.passwordmanager.core.cipher.Cipher
import com.tompee.utilities.passwordmanager.core.cipher.impl.CipherImpl
import com.tompee.utilities.passwordmanager.core.database.PackageDao
import com.tompee.utilities.passwordmanager.core.database.PasswordDatabase
import com.tompee.utilities.passwordmanager.core.keystore.Keystore
import com.tompee.utilities.passwordmanager.core.keystore.impl.KeystoreImpl
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
    fun provideCipher(cipherImpl: CipherImpl): Cipher = cipherImpl

    @Provides
    @Singleton
    fun provideCipherImpl(): CipherImpl = CipherImpl()

    @Provides
    @Singleton
    fun provideKeystore(keystoreImpl: KeystoreImpl): Keystore = keystoreImpl

    @Provides
    @Singleton
    fun provideKeystoreImpl(): KeystoreImpl = KeystoreImpl()

    @Provides
    @Singleton
    fun provideDatabase(context: Context): PasswordDatabase {
        return Room.databaseBuilder(context, PasswordDatabase::class.java, Constants.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideMedicineDao(database: PasswordDatabase): PackageDao = database.packageDao()
}