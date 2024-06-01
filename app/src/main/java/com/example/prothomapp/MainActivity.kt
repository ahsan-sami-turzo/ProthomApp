package com.example.prothomapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prothomapp.ui.theme.ProthomAppTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProthomAppTheme {
                // A surface container using the 'background' color from the theme
                Surface (
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}



@Composable
fun MainScreen() {
    var backgroundColor by remember { mutableStateOf(Color.LightGray) }
    var displayText by remember { mutableStateOf("") }

    val buttonData = listOf(
        "Buffet" to Color.hsl(0.49F, 0.24F, 0.75F),
        "Gym" to Color.hsl(0.49F, 0.24F, 0.75F),
        "Bus" to Color.hsl(0.49F, 0.24F, 0.75F),
        "Prayer" to Color.hsl(0.49F, 0.24F, 0.75F),
        "Library" to Color.hsl(0.49F, 0.24F, 0.75F),
        "Event" to Color.hsl(0.49F, 0.24F, 0.75F),
        "Sauna" to Color.hsl(0.49F, 0.24F, 0.75F),
        "SportsHall" to Color.hsl(0.49F, 0.24F, 0.75F),
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
    ) {
        CentralDisplay(backgroundColor = backgroundColor, displayText = displayText)
        ButtonLayout(
            buttonData = buttonData,
            onButtonClicked = { label, color ->
                backgroundColor = color
                displayText = when (label) {
                    "Prayer" -> getNextPrayerTime()
                    "Buffet" -> getRestaurantMenu()
                    "Bus" -> getNextBusTimes()
                    "Gym" -> getGymOccupancy()
                    "Library" -> getLibraryOccupancy()
                    "Event" -> getEventStatus()
                    "Sauna" -> "Next bus to sauna: 30 mins"
                    "SportsHall" -> getSportsHallStatus()
                    else -> ""
                }
            },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun CentralDisplay(backgroundColor: Color, displayText: String) {
    Box(
        modifier = Modifier
            .size(300.dp)
            .background(backgroundColor, shape = CircleShape)
            .border(2.dp, Color.White, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = displayText,
            color = Color.hsl(0.26F, 0.60F, 0.18F),
            // color = Color.LightGray,
            modifier = Modifier.padding(16.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ButtonLayout(buttonData: List<Pair<String, Color>>, onButtonClicked: (String, Color) -> Unit, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(bottom = 16.dp)
    ) {
        ButtonRow(buttonData = buttonData.subList(0, 4), onButtonClicked = onButtonClicked)
        Spacer(modifier = Modifier.height(16.dp))
        ButtonRow(buttonData = buttonData.subList(4, 8), onButtonClicked = onButtonClicked)
    }
}

@Composable
fun ButtonRow(buttonData: List<Pair<String, Color>>, onButtonClicked: (String, Color) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        buttonData.forEach { (label, color) ->
            RectangularButton(label, color) { onButtonClicked(label, color) }
        }
    }
}

@Composable
fun RectangularButton(text: String, color: Color, onClick: () -> Unit) {
    val symbolMap = mapOf(
        "Buffet" to "🍴",
        "Gym" to "🏋️",
        "Bus" to "🚌",
        "Prayer" to "🕌",
        "Library" to "📚",
        "Event" to "🎉",
        "Sauna" to "🧖",
        "SportsHall" to "🏀"
    )

    Box(
        modifier = Modifier
            .size(width = 80.dp, height = 50.dp)
            .background(color, shape = RoundedCornerShape(8.dp))
            .border(1.dp, Color.White, RoundedCornerShape(8.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = symbolMap[text] ?: text,
            color = Color.White,
            fontSize = 20.sp,
            modifier = Modifier.padding(10.dp)
        )
    }
}

fun getNextPrayerTime(): String {
    val prayerTimes = mapOf(
        "Fajr" to "12:45 pm",
        "Shurooq" to "3:29 am",
        "Dhuhr" to "1:05 pm",
        "Asr" to "5:44 pm",
        "Maghrib" to "10:41 pm",
        "Isha" to "1:25 am"
    )

    val currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
    val timeMap = prayerTimes.mapValues { it.value.to24HourFormat() }

    val nextPrayer = timeMap.entries
        .filter { it.value > currentTime }
        .minByOrNull { it.value }

    return nextPrayer?.let {
        "Next prayer is ${it.key} at ${prayerTimes[it.key]}"
    } ?: "No more prayers today"
}

fun String.to24HourFormat(): String {
    val parser = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    return formatter.format(parser.parse(this) ?: Date())
}


fun getNextBusTimes(): String {
    val busScheduleWeekday = listOf(
        "5:45", "6:10", "6:25", "6:45", "7:10", "7:25", "7:40", "8:00", "8:15", "8:30", "8:45", "9:00", "9:15", "9:30", "9:45", "10:00", "10:15", "10:30", "10:45", "11:00", "11:15", "11:30", "11:45", "12:00", "12:15", "12:30", "12:45", "13:00", "13:15", "13:30", "13:45", "14:00", "14:15", "14:30", "14:45", "15:00", "15:15", "15:30", "15:45", "16:00", "16:15", "16:30", "16:45", "17:00", "17:15", "17:30", "17:45", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"
    )
    val busScheduleSaturday = listOf(
        "7:30", "8:00", "8:30", "9:00", "9:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"
    )
    val busScheduleSunday = listOf(
        "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00"
    )

    val currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
    val currentDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)

    val schedule = when (currentDay) {
        Calendar.SATURDAY -> busScheduleSaturday
        Calendar.SUNDAY -> busScheduleSunday
        else -> busScheduleWeekday
    }

    val nextTimes = schedule.filter { it > currentTime }
    return if (nextTimes.size >= 2) {
        "Next buses: ${nextTimes[0]}, ${nextTimes[1]}"
    } else {
        "No more buses today"
    }
}

fun getRestaurantMenu(): String {
    return """
        🍲 Mushroom soup (V)
        🥗 Harkis and cabbage casserole (V)
        🍜 Turkey and vegetable noodles
        🌭 Baked sausages and mashed potatoes
    """.trimIndent()
}

fun getGymOccupancy(): String {
    val occupancy = mapOf(
        "total" to 19,
        "muscle-training" to 11,
        "free-hand" to 8
    )
    return """
        Total: ${occupancy["total"]} users
        🏋️ ${occupancy["muscle-training"]} users
        🏃 ${occupancy["free-hand"]} users
    """.trimIndent()
}

fun getLibraryOccupancy(): String {
    val occupancy = mapOf(
        "Einstein" to "free all day",
        "Faraday" to "free until 5:30 PM",
        "Doppler" to "occupied",
        "Galilei" to "occupied"
    )
    return """
        📚 Library Occupancy:
        📖 Einstein: ${occupancy["Einstein"]}
        📖 Faraday: ${occupancy["Faraday"]}
        📖 Doppler: ${occupancy["Doppler"]}
        📖 Galilei: ${occupancy["Galilei"]}
    """.trimIndent()
}

fun getEventStatus(): String {
    return "🎉 Event Status: No events today."
}

fun getSportsHallStatus(): String {
    return "🏀 Sports Hall: Occupied until 6:30 PM."
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ProthomAppTheme {
        MainScreen()
    }
}


