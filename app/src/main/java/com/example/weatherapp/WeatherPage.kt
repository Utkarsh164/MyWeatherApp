package com.example.weatherapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.weatherapp.api.NetworkResponse
import com.example.weatherapp.api.weatherModel

@Composable
fun WeatherPage(viewModel: WeatherViewModel) {


    var city by remember {
        mutableStateOf("")
    }
    
    
    
    val weatherResult=viewModel.weatherResult.observeAsState()



    val keyboardController = LocalSoftwareKeyboardController.current
    
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp, 40.dp, 10.dp, 0.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly) {

            OutlinedTextField(value =city , onValueChange ={
                city=it
            },
                label = {
                    Text(text = "Location")
            }, modifier = Modifier.weight(0.5f) )


            IconButton(onClick = {
                viewModel.getCity(city)
                keyboardController?.hide()

            }) {
                Icon(painter = painterResource(id = R.drawable.baseline_search_24),
                    contentDescription = "search")
            }
            }


        when(val result = weatherResult.value){
            is NetworkResponse.Error -> {
                Spacer(modifier = Modifier.size(200.dp))
                Text(text = result.message, fontSize = 40.sp)
            }
            NetworkResponse.Loading -> {
                Spacer(modifier = Modifier.size(250.dp))
                CircularProgressIndicator()
            }
            is NetworkResponse.Success ->
            {
                WeatherDetails(data = result.data)
            }
            null -> {}



        }
    }
}


@Composable
fun WeatherDetails(data : weatherModel)
{
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Bottom
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Location icon",
                modifier = Modifier.size(40.dp)
            )
            Text(text = data.location.name, fontSize = 30.sp,)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = data.location.country, fontSize = 18.sp,color= Color.Gray)
        }
        Spacer(modifier = Modifier.height(16.dp))


        Text(text = "${data.current.temp_c}⁰ c"
            ,fontSize = 56.sp
            ,fontWeight = FontWeight.Bold )

        AsyncImage(model = "https:${data.current.condition.icon}".replace("64x64","128x128")
            , contentDescription = ""
        , modifier = Modifier.size(160.dp))
        Text(text = data.current.condition.text
            ,fontSize = 20.sp
            ,fontWeight = FontWeight.Bold
            ,color =Color.Gray )

        Card {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                    , horizontalArrangement = Arrangement.SpaceAround
                ) {
                   weatherkeyValue(KEY= "Humidity", value = data.current.humidity.toString())

                    weatherkeyValue(KEY= "Wind  speed", value = data.current.wind_kph.toString()+" Km/h")
                }



                Row(
                    modifier = Modifier.fillMaxWidth()
                    , horizontalArrangement = Arrangement.SpaceAround
                ) {
                    weatherkeyValue(KEY= "UV", value = data.current.uv.toString())

                    weatherkeyValue(KEY= "Participation", value = data.current.precip_mm.toString()+" mm")
                }



                Row(
                    modifier = Modifier.fillMaxWidth()
                    , horizontalArrangement = Arrangement.SpaceAround
                ) {
                    weatherkeyValue(KEY= "Local Time", value = data.location.localtime.split(" ")[1])

                    weatherkeyValue(KEY= "Local Time", value = data.location.localtime.split(" ")[0])
                }
            }
        }
    }
}
@Composable
fun weatherkeyValue(KEY : String, value: String)
{
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = value, fontWeight = FontWeight.Bold, fontSize = 24.sp)
        Text(text = KEY, fontWeight = FontWeight.SemiBold, color = Color.Gray )
    }
}
