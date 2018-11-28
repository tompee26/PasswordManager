package com.tompee.utilities.passwordmanager.service

import com.tompee.utilities.passwordmanager.core.cipher.Cipher
import com.tompee.utilities.passwordmanager.core.database.PackageDao
import com.tompee.utilities.passwordmanager.core.keystore.Keystore
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
        packageDao: PackageDao,
        keystore: Keystore,
        cipher: Cipher
    ): ResponseBuilder = ResponseBuilder(packageDao, keystore, cipher)
}