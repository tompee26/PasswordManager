package com.tompee.utilities.passwordmanager.service

import android.content.Context
import com.tompee.utilities.passwordmanager.core.cipher.Cipher
import com.tompee.utilities.passwordmanager.core.database.PackageDao
import com.tompee.utilities.passwordmanager.core.keystore.Keystore
import com.tompee.utilities.passwordmanager.core.packages.PackageManager
import com.tompee.utilities.passwordmanager.dependency.scope.ServiceScope
import com.tompee.utilities.passwordmanager.service.parser.StructureParser
import com.tompee.utilities.passwordmanager.service.response.ResponseBuilder
import dagger.Module
import dagger.Provides

@Module
class FillServiceModule {

    @Provides
    @ServiceScope
    fun provideParser(): StructureParser = StructureParser()

    @Provides
    @ServiceScope
    fun provideResponseBuilder(
        context: Context,
        packageDao: PackageDao,
        packageManager: PackageManager,
        keystore: Keystore,
        cipher: Cipher
    ): ResponseBuilder = ResponseBuilder(context, packageDao, packageManager, keystore, cipher)
}