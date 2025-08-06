package com.systemcraft.formkmp.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.systemcraft.formkmp.core.form.FormState

@Composable
fun rememberFormState(): FormState {
    return remember { FormState() }
}