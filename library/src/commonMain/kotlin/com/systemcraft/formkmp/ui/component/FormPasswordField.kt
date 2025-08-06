package com.systemcraft.formkmp.ui.component

import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.systemcraft.formkmp.core.field.FormFieldConfig
import com.systemcraft.formkmp.core.form.FormState

/**
 * Password input field
 */
@Composable
fun FormPasswordField(
    key: String,
    formState: FormState,
    config: FormFieldConfig = FormFieldConfig()
) {
    var passwordVisible by remember { mutableStateOf(false) }

    FormTextField(
        key = key,
        formState = formState,
        config = config.copy(
            keyboardType = KeyboardType.Password,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    /*Icon(
                        imageVector = if (passwordVisible) {
                            Icons.Default.Visibility
                        } else {
                            Icons.Default.VisibilityOff
                        },
                        contentDescription = if (passwordVisible) "Hide password" else "Show password"
                    )

                     */
                }
            }
        )
    )
}