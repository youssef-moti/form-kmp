package com.systemcraft.formkmp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.systemcraft.formkmp.core.field.FormActions
import com.systemcraft.formkmp.core.field.FormCheckbox
import com.systemcraft.formkmp.core.field.FormDropdown
import com.systemcraft.formkmp.core.field.FormErrorSummary
import com.systemcraft.formkmp.core.field.FormFieldConfig
import com.systemcraft.formkmp.core.field.FormPasswordField
import com.systemcraft.formkmp.core.field.FormTextField
import com.systemcraft.formkmp.ui.rememberFormState
import com.systemcraft.formkmp.validation.ValidationResult
import com.systemcraft.formkmp.validation.ValidationTrigger
import com.systemcraft.formkmp.validation.Validator
import kotlinx.coroutines.delay
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
@Preview
fun App() {
    MaterialTheme {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .safeContentPadding()
                .fillMaxSize()
        ) {
            val formState = rememberFormState()

            LaunchedEffect(Unit) {
                formState.apply {
                    registerField(
                        key = "firstName",
                        initialValue = "",
                        validation = Validator<String>()
                            .required("First name is required")
                            .minLength(2, "First name must be at least 2 characters")
                    )

                    registerField(
                        key = "lastName",
                        initialValue = "",
                        validation = Validator<String>()
                            .required("Last name is required")
                            .minLength(2, "Last name must be at least 2 characters")
                    )

                    registerField(
                        key = "email",
                        initialValue = "",
                        validation = Validator<String>()
                            .required("Email is required")
                            .email("Please enter a valid email address")
                    )

                    registerField(
                        key = "password",
                        initialValue = "",
                        validation = Validator<String>()
                            .required("Password is required")
                            .minLength(8, "Password must be at least 8 characters")
                    )

                    registerField(
                        key = "confirmPassword",
                        initialValue = "",
                        validation = Validator<String>()
                            .required("Please confirm your password")
                            .custom { value, formData ->
                                val password = formData["password"] as? String
                                if (value != password) {
                                    ValidationResult.Invalid(listOf("Passwords do not match"))
                                } else ValidationResult.Valid
                            },
                        dependencies = setOf("password")
                    )

                    registerField(
                        key = "country",
                        initialValue = null as String?,
                        validation = Validator<String?>()
                            .required("Please select a country")
                    )

                    registerField(
                        key = "agreeToTerms",
                        initialValue = false,
                        validation = Validator<Boolean>()
                            .custom { value, _ ->
                                if (value != true) {
                                    ValidationResult.Invalid(listOf("You must agree to terms and conditions"))
                                } else ValidationResult.Valid
                            }
                    )

                    setValidationTriggers(
                        setOf(
                            ValidationTrigger.ON_BLUR,
                            ValidationTrigger.ON_SUBMIT
                        )
                    )
                }
            }


            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "User Registration Form",
                    style = MaterialTheme.typography.headlineSmall
                )

                FormErrorSummary(formState = formState)
                FormTextField(
                    key = "firstName",
                    formState = formState,
                    config = FormFieldConfig(
                        label = "First Name",
                        placeholder = "Enter your first name"
                    ),
                    // modifier = Modifier.weight(1f)
                )
                FormTextField(
                    key = "lastName",
                    formState = formState,
                    config = FormFieldConfig(
                        label = "Last Name",
                        placeholder = "Enter your last name"
                    ),
                    //modifier = Modifier.weight(1f)
                )

                FormTextField(
                    key = "email",
                    formState = formState,
                    config = FormFieldConfig(
                        label = "Email Address",
                        placeholder = "Enter your email",
                        keyboardType = androidx.compose.ui.text.input.KeyboardType.Email
                    )
                )

                FormPasswordField(
                    key = "password",
                    formState = formState,
                    config = FormFieldConfig(
                        label = "Password",
                        placeholder = "Enter your password"
                    )
                )

                FormPasswordField(
                    key = "confirmPassword",
                    formState = formState,
                    config = FormFieldConfig(
                        label = "Confirm Password",
                        placeholder = "Confirm your password"
                    )
                )

                FormDropdown(
                    key = "country",
                    formState = formState,
                    options = listOf(
                        "US" to "United States",
                        "CA" to "Canada",
                        "UK" to "United Kingdom",
                        "AU" to "Australia",
                        "DE" to "Germany",
                        "FR" to "France",
                        "JP" to "Japan"
                    ),
                    config = FormFieldConfig(
                        label = "Country",
                        placeholder = "Select your country"
                    )
                )

                FormCheckbox(
                    key = "agreeToTerms",
                    formState = formState,
                    label = "I agree to the Terms and Conditions"
                )

                FormActions(
                    formState = formState,
                    onSubmit = {
                        formState.submit { data ->
                            // Simulate API call
                            delay(2000)
                            println("Form submitted with data: $data")
                            // Show success message or navigate
                        }
                    }
                )
            }
        }
        }
    }
