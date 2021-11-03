package com.jh.basicscodelabweek1

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jh.basicscodelabweek1.ui.theme.BasicsCodelabWeek1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BasicsCodelabWeek1Theme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    MyApp()
                }
            }
        }
    }
}

@Composable
fun OnboardingScreen( onClick: () -> Unit ) {

    Surface {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Welcome to the Basics Codelab!")
            Button(
                modifier = Modifier.padding(vertical = 24.dp),
                onClick = onClick
            ) {
                Text("Continue")
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Surface(color = MaterialTheme.colors.background, modifier = Modifier.padding(4.dp, 8.dp)) {
        
        Card(
            backgroundColor = MaterialTheme.colors.primary,
            modifier = Modifier.padding(4.dp, 8.dp)
        ) {

            CardContent( name )

        }

    }
}

@Composable
fun CardContent( name: String ) {

    var expanded by remember { mutableStateOf( false ) }

    Row( modifier = Modifier.padding( 24.dp ) ) {

        Column(modifier = Modifier
            .weight(1f)
            .animateContentSize(
                animationSpec = spring(
                    Spring.DampingRatioMediumBouncy,
                    Spring.StiffnessMedium
                )
            )
        ) {

            Text(text = "Hello, ")
            Text(text = name, style = MaterialTheme.typography.h4.copy(
                fontWeight = FontWeight.ExtraBold
            ))
            if ( expanded ) {

                Text(text = LocalContext.current.getString(R.string.test).repeat(3))

            }

        }
        IconButton(onClick = {

            expanded = !expanded

        }) {
            Icon(imageVector =  if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore, null)
        }

    }

}

@Composable
fun MyApp() {

    var shouldShowOnBoarding by remember { mutableStateOf( true ) }

    if ( shouldShowOnBoarding )
        OnboardingScreen { shouldShowOnBoarding = !shouldShowOnBoarding }
    else
        Greetings()

}

@Composable
fun Greetings(names: List<String> = List(1000){"$it"}) {

    LazyColumn( modifier = Modifier.padding(4.dp)) {

        items(items = names) { name ->
            Greeting(name = name)
        }

    }

}

@Preview(showBackground = true, widthDp = 320, heightDp = 320)
@Composable
fun OnboardingPreview() {
    BasicsCodelabWeek1Theme() {
        OnboardingScreen( onClick = {})
    }
}

@Preview(
    showBackground = true,
    widthDp = 320,
    uiMode = UI_MODE_NIGHT_YES,
    name = "DefaultPreviewDark"
)

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BasicsCodelabWeek1Theme {

        Greetings()

    }
}