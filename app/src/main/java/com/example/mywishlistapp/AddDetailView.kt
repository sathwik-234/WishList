package com.example.mywishlistapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.navigation.NavHostController
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.mywishlistapp.data.Wish
import kotlinx.coroutines.launch

@Composable
fun AddDetailView(
    id : Long,
    viewModel: WishViewModel,
    navController: NavController
){
    val snackMessage = remember {
        mutableStateOf(" ")
    }

    val scope = rememberCoroutineScope()

    val scaffoldState = rememberScaffoldState()
    if(id != 0L){
//        val wish = viewModel.getAWishById(id).collectAsState(initial = Wish(0L, "", ""))
//        viewModel.wishTitleState = wish.value.title
//        viewModel.wishDescriptionState = wish.value.description
        val wishState = viewModel.getAWishById(id).collectAsState(initial = Wish(0L,"",""))
        val wish = wishState.value
        viewModel.wishTitleState = wish.title
        viewModel.wishDescriptionState = wish.description
    }
    else{
        viewModel.wishTitleState = ""
        viewModel.wishDescriptionState = ""
    }
    Scaffold(
        topBar = {AppBarView(title =
         if (id!= 0L) stringResource(R.string.update_wish) else stringResource(R.string.add_wish)
        ){
            navController.navigateUp()
        } },
        scaffoldState = scaffoldState
    ){
        Column(modifier = Modifier
            .padding(it)
            .wrapContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Spacer(modifier = Modifier.heightIn(10.dp))

            WishTextField(label = "Title",
                value = viewModel.wishTitleState,
                onValueChanged = {
                    viewModel.onWishTitleChanged(it)
                })
            WishTextField(label = "Description",
                value = viewModel.wishDescriptionState,
                onValueChanged = {
                    viewModel.onWishDescriptionState(it)
                })
            Spacer(modifier = Modifier.heightIn(10.dp))
            Button(onClick = {
                if(viewModel.wishTitleState.isNotEmpty() && viewModel.wishDescriptionState.isNotEmpty()){
                    if (id != 0L){
                        //TODO Update Wish
                        viewModel.updateWish(
                            Wish(
                                id = id,
                                title = viewModel.wishTitleState.trim(),
                                description = viewModel.wishDescriptionState.trim()
                            )
                        )
                    }
                    else{
                        //TODO Add Wish
                        viewModel.addWish(
                            Wish(
                                title = viewModel.wishTitleState.trim(),
                                description = viewModel.wishDescriptionState.trim()
                            )
                        )
                        snackMessage.value = "A wish has been created"
                    }
                }
                else{
                    snackMessage.value = "Enter fields to create a wish created"
                }
                scope.launch {
                    navController.navigateUp()
                }
            }) {
                 Text(
                     text = if (id != 0L) stringResource(R.string.update_wish)
                     else stringResource(
                         R.string.add_wish
                     ),
                     style = TextStyle(

                         fontSize = 18.sp
                     )
                 )
            }
        }
    }
}

@Composable
fun WishTextField(
    label : String,
    value : String,
    onValueChanged : (String)->Unit
){
    OutlinedTextField(
        value = value,
        onValueChange = onValueChanged,
        label = { Text(text = label, color = Color.Black)},
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = Color.Black,
            focusedBorderColor = colorResource(id = R.color.black),
            unfocusedBorderColor = colorResource(id = R.color.black),
            cursorColor = colorResource(id = R.color.black),
            focusedLabelColor = colorResource(id = R.color.black),
            unfocusedLabelColor = colorResource(id = R.color.black)
        )
    )
}

@Preview(showBackground = true)
@Composable
fun WishTextFieldPrev(){
    WishTextField(label = "Text", value = "", onValueChanged = {})
}