package org.technoserve.farmcollector.ui.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Intent
import android.view.KeyEvent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import org.joda.time.Instant
import org.technoserve.farmcollector.R
import org.technoserve.farmcollector.database.CollectionSite
import org.technoserve.farmcollector.database.FarmViewModel
import org.technoserve.farmcollector.database.FarmViewModelFactory

@Composable
fun AddSite(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        FarmListHeader(
            title = stringResource(id = R.string.add_site),
            onAddFarmClicked = { /* Handle adding a farm here */ },
            onBackClicked = { navController.popBackStack() },
            showAdd = false
        )
        Spacer(modifier = Modifier.height(16.dp))
        SiteForm(navController)
    }
}

@SuppressLint("MissingPermission", "Recycle")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SiteForm(navController: NavController) {
    val context = LocalContext.current as Activity
    var isValid by remember { mutableStateOf(true) }
    var name by remember { mutableStateOf("") }
    var agentName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var village by remember { mutableStateOf("") }
    var district by remember { mutableStateOf("") }

    val farmViewModel: FarmViewModel = viewModel(
        factory = FarmViewModelFactory(context.applicationContext as Application)
    )

    fun isValidPhoneNumber(phoneNumber: String): Boolean {
        val regex = Regex("^\\+?(?:[0-9] ?){6,14}[0-9]\$")
        return regex.matches(phoneNumber)
    }

    fun validateForm(): Boolean {
        isValid = // You can display an error message for this field if needed
            !(name.isBlank() || agentName.isBlank() || village.isBlank() || district.isBlank())

        return isValid
    }

    val scrollState = rememberScrollState()
    val fillForm = stringResource(id = R.string.fill_form)

    val (focusRequester1) = FocusRequester.createRefs()
    val (focusRequester2) = FocusRequester.createRefs()
    val (focusRequester3) = FocusRequester.createRefs()
    val (focusRequester4) = FocusRequester.createRefs()
    val (focusRequester5) = FocusRequester.createRefs()
    val (focusRequester6) = FocusRequester.createRefs()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
            .verticalScroll(state = scrollState)
    ) {
        Row {
            TextField(
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onDone = { focusRequester1.requestFocus() }
                ),
                value = name,
                onValueChange = { name = it },
                label = { Text(stringResource(id = R.string.site_name) + " (*)") },
                supportingText = { if (!isValid && name.isBlank()) Text("Site Name should not be empty") },
                isError = !isValid && name.isBlank(),
                colors = TextFieldDefaults.textFieldColors(
                    errorLeadingIconColor = Color.Red,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .onKeyEvent {
                        if (it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ENTER) {
                            focusRequester1.requestFocus()
                        }
                        false
                    }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onDone = { focusRequester2.requestFocus() }
            ),
            value = agentName,
            onValueChange = { agentName = it },
            label = { Text(stringResource(id = R.string.agent_name) + " (*)") },
            supportingText = { if (!isValid && agentName.isBlank()) Text("Agent Name should not be empty") },
            isError = !isValid && agentName.isBlank(),
            colors = TextFieldDefaults.textFieldColors(
                errorLeadingIconColor = Color.Red,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .onKeyEvent {
                    if (it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ENTER) {
                        focusRequester2.requestFocus()
                    }
                    false
                }
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Phone
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusRequester3.requestFocus() }
            ),
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text(stringResource(id = R.string.phone_number)) },
            supportingText = {
                if (!isValid && phoneNumber.isNotEmpty() && !isValidPhoneNumber(phoneNumber)) Text("Invalid Phone Number")
            },
            isError = !isValid && phoneNumber.isNotEmpty() && !isValidPhoneNumber(phoneNumber),
            colors = TextFieldDefaults.textFieldColors(
                errorLeadingIconColor = Color.Red,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .onKeyEvent {
                    if (it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ENTER) {
                        focusRequester3.requestFocus()
                    }
                    false
                }
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Email
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusRequester4.requestFocus() },
            ),
            value = email,
            onValueChange = { email = it },
            label = { Text(stringResource(id = R.string.email)) },
            supportingText = {
                if (!isValid && email.isNotEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(
                        email
                    ).matches()
                )
                    Text("Invalid Email Address")
            },
            isError = !isValid && email.isNotEmpty() && !android.util.Patterns.EMAIL_ADDRESS.matcher(
                email
            ).matches(),
            colors = TextFieldDefaults.textFieldColors(
                errorLeadingIconColor = Color.Red,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .onKeyEvent {
                    if (it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ENTER) {
                        focusRequester4.requestFocus()
                    }
                    false
                }
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onDone = { focusRequester5.requestFocus() }
            ),
            value = village,
            onValueChange = { village = it },
            label = { Text(stringResource(id = R.string.village) + " (*)") },
            supportingText = { if (!isValid && village.isBlank()) Text("Village should not be empty") },
            isError = !isValid && village.isBlank(),
            colors = TextFieldDefaults.textFieldColors(
                errorLeadingIconColor = Color.Red,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .onKeyEvent {
                    if (it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_ENTER) {
                        focusRequester5.requestFocus()
                    }
                    false
                }
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            singleLine = true,
            value = district,
            onValueChange = { district = it },
            label = { Text(stringResource(id = R.string.district) + " (*)") },
            supportingText = { if (!isValid && district.isBlank()) Text("District should not be empty") },
            isError = !isValid && district.isBlank(),
            colors = TextFieldDefaults.textFieldColors(
                errorLeadingIconColor = Color.Red,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (validateForm()) {
                    addSite(
                        farmViewModel,
                        name,
                        agentName,
                        phoneNumber,
                        email,
                        village,
                        district
                    )
                    val returnIntent = Intent()
                    context.setResult(Activity.RESULT_OK, returnIntent)
//                    context.finish()
                    navController.navigate("siteList")
                    // Show toast indicating success
                    Toast.makeText(context, "Site added successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, fillForm, Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(text = stringResource(id = R.string.add_site))
        }
    }
}

fun addSite(
    farmViewModel: FarmViewModel,
    name: String,
    agentName: String,
    phoneNumber: String,
    email: String,
    village: String,
    district: String,
): CollectionSite {
    val site = CollectionSite(
        name,
        agentName,
        phoneNumber,
        email,
        village,
        district,
        createdAt = Instant.now().millis,
        updatedAt = Instant.now().millis
    )
    farmViewModel.addSite(site)
    return site
}