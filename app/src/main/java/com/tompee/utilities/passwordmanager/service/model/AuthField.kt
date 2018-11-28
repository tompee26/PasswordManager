package com.tompee.utilities.passwordmanager.service.model

import android.app.assist.AssistStructure

data class AuthField(var usernameField: AssistStructure.ViewNode? = null, var passwordField: AssistStructure.ViewNode? = null)