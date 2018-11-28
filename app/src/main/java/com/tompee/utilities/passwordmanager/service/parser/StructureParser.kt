package com.tompee.utilities.passwordmanager.service.parser

import android.app.assist.AssistStructure
import com.tompee.utilities.passwordmanager.service.model.AuthField

class StructureParser {

    fun parse(assistStructure: AssistStructure): AuthField {
        val nodeCount = assistStructure.windowNodeCount
        val parsedField = AuthField()
        for (i in 0 until nodeCount) {
            parseMetadata(assistStructure.getWindowNodeAt(i).rootViewNode, parsedField)
        }
        return parsedField
    }

    private fun parseMetadata(viewNode: AssistStructure.ViewNode, field: AuthField) {
        /* We are only interested in username and password combinations. For now, auto fill hints are not yet checked */

        val hint = viewNode.hint?.toLowerCase()
        if (hint?.contains("user") == true && viewNode.className.contains("EditText")) {
            field.usernameField = viewNode
        } else if (hint?.contains("pass") == true && viewNode.className.contains("EditText")) {
            field.passwordField = viewNode
        }

        val childCount = viewNode.childCount
        for (i in 0 until childCount) {
            parseMetadata(viewNode.getChildAt(i), field)
        }
    }
}