package com.jh.basiccodelabweek2_1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.materialIcon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import coil.compose.rememberImagePainter
import com.jh.basiccodelabweek2_1.ui.theme.BasicCodeLabWeek2_1Theme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BasicCodeLabWeek2_1Theme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {

                    TwoTexts(Modifier, "1","2")
                }
            }
        }
    }
}

@Composable
fun StaggeredGrid(
    modifier: Modifier,
    rows: Int = 3,
    content: @Composable () -> Unit
) {

    Layout(modifier = modifier, content = content) { measurables, constraints ->

        val rowWidths = IntArray( rows ) { 0 }
        val rowHeights = IntArray( rows ) { 0 }

        val placeables = measurables.mapIndexed { index, measurable ->

            val placeable = measurable.measure( constraints )

            val row = index % rows
            rowWidths[row] += placeable.width
            rowHeights[row] = Math.max(rowHeights[row], placeable.height)

            placeable

        }

        val width = rowWidths.maxOrNull()
            ?.coerceIn(constraints.minWidth.rangeTo(constraints.maxWidth)) ?: constraints.minWidth

        val height = rowHeights.sumOf { it }
            .coerceIn(constraints.minHeight.rangeTo(constraints.maxHeight))

        val rowY = IntArray(rows) { 0 }

        for (i in 1 until rows) {
            rowY[i] = rowY[i-1] + rowHeights[i-1]
        }

        layout(width, height) {

            val rowX = IntArray(rows) { 0 }

            placeables.forEachIndexed { index, placeable ->
                val row = index % rows
                placeable.placeRelative(
                    x = rowX[row],
                    y = rowY[row]
                )
                rowX[row] += placeable.width

            }

        }

    }

}

@Composable
fun DecoupledConstraintLayout() {
    BoxWithConstraints {
        val constraints = if (maxWidth < maxHeight) {
            decoupledConstraints(margin = 16.dp) // Portrait constraints
        } else {
            decoupledConstraints(margin = 32.dp) // Landscape constraints
        }

        ConstraintLayout(constraints) {
            Button(
                onClick = { /* Do something */ },
                modifier = Modifier.layoutId("button")
            ) {
                Text("Button")
            }

            Text("Text", Modifier.layoutId("text"))
        }
    }

}

@Composable
fun TwoTexts(modifier: Modifier = Modifier, text1: String, text2: String) {
    Row(modifier = modifier.height(IntrinsicSize.Min)) {
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(start = 4.dp)
                .wrapContentWidth(Alignment.Start),
            text = text1
        )

        Divider(color = Color.Black, modifier = Modifier.fillMaxHeight().width(1.dp))
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(end = 4.dp)
                .wrapContentWidth(Alignment.End),

            text = text2
        )
    }
}

private fun decoupledConstraints(margin: Dp): ConstraintSet {
    return ConstraintSet {
        val button = createRefFor("button")
        val text = createRefFor("text")

        constrain(button) {
            top.linkTo(parent.top, margin= margin)
        }
        constrain(text) {
            top.linkTo(button.bottom, margin)
        }
    }
}

@Composable
fun ConstraintLayoutExample() {

    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
        // Creates references for the three composables
        // in the ConstraintLayout's body
        val (button1, button2, text) = createRefs()

        val guideline = createGuidelineFromStart(0.5f)

        Button(
            onClick = { /* Do something */ },
            modifier = Modifier.constrainAs(button1) {
                top.linkTo(parent.top, margin = 16.dp)
                start.linkTo(guideline)
            }
        ) {
            Text("Button 1")
        }

        Text("Loooooooooooooooooooooooooooooooooooooong Text", Modifier.constrainAs(text) {
            top.linkTo(button1.bottom, margin = 16.dp)
            centerAround(button1.end) // button1의 end 가운데에 위치함 (constraint top, bottom에 둘 다 준 효과)
            width = Dimension.wrapContent
        })

        val barrier = createEndBarrier(button1, text)
        Button(
            onClick = { /* Do something */ },
            modifier = Modifier.constrainAs(button2) {
                top.linkTo(parent.top, margin = 16.dp)
                start.linkTo(barrier)
            }
        ) {
            Text("Button 2")
        }
    }

}

val topics = listOf(
    "Arts & Crafts", "Beauty", "Books", "Business", "Comics", "Culinary",
    "Design", "Fashion", "Film", "History", "Maths", "Music", "People", "Philosophy",
    "Religion", "Social sciences", "Technology", "TV", "Writing"
)

@Composable
fun StaggeredGridResult() {

    Row( modifier = Modifier
        .background(color = Color.LightGray)
        .padding(100.dp)
        .size(200.dp)
        .horizontalScroll(rememberScrollState()) ) {

        StaggeredGrid(modifier = Modifier) {

            for ( topic in topics )
                Chip(modifier = Modifier.padding(10.dp), text = topic)

        }

    }

}

@Composable
fun Chip( modifier: Modifier, text: String ) {

    Card( modifier = modifier, shape = RoundedCornerShape(8.dp) ) {

        Row( modifier = Modifier.padding(5.dp, 3.dp), verticalAlignment = Alignment.CenterVertically ) {

            Box(modifier = Modifier
                .size(16.dp)
                .background(Color.Cyan, CircleShape))
            
            Spacer(modifier = Modifier.width(4.dp))

            Text( text = text, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center )

        }

    }

}



@Composable
fun Profile() {
    
    Row( modifier = Modifier
        .clickable { }
        .padding(80.dp)
        .clip(RoundedCornerShape(10.dp))
        .background(MaterialTheme.colors.primary)
        .padding(40.dp)
    ) {
        
        Surface(
            modifier = Modifier
                .size(50.dp)
                .align(CenterVertically),
            shape = CircleShape,
            color = MaterialTheme.colors.onSurface.copy(0.2f)
        ) {

        }
        Column(
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterVertically)
        ) {

            Text(text = "JongHan LEE", fontWeight = FontWeight.Bold)
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {

                Text("1 minutes ago", style = MaterialTheme.typography.body2)

            }

        }
        
    }

}

@Composable
fun LayoutCodeLab() {

    Scaffold( topBar = {

        TopAppBar(
            title = { Text(text = "Layout CodLab") },
            modifier = Modifier.background(MaterialTheme.colors.primary),
            actions = {
                IconButton(onClick = { /* doSomething() */ }) {
                    Icon(Icons.Filled.Favorite, contentDescription = null)
                }
            })
        
    }) { innerPadding ->

        BodyContent(modifier = Modifier.padding(40.dp))

    }
    
}

@Composable
fun BodyContent( modifier: Modifier ) {

    val listSize = 100
    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Column( ) {

        Row( modifier = Modifier.fillMaxWidth() ) {
            Button(onClick = {
                coroutineScope.launch {
                    // 0 is the first item index
                    repeat( 99 ) {

                        scrollState.animateScrollToItem(it)
                        delay(300)

                    }

                }
            }, modifier = Modifier.weight(1f)) {
                Text("Start Scroll")
            }

            Button(onClick = {
                coroutineScope.launch {
                    // listSize - 1 is the last index of the list
                    scrollState.animateScrollToItem(listSize - 1)
                }
            }, modifier = Modifier.weight(1f)) {
                Text("Scroll to the end")
            }
        }

        LazyColumn( modifier = modifier, state = scrollState ) {

            items( 100 ) {
                
                ImageListItem(cnt = it)

            }

        }

    }

}

fun Modifier.firstBaselineToTop(
    firstBaselineToTop: Dp
) = this.then(
    layout { measurable, constraints ->

        val placeable = measurable.measure(constraints)
        check(placeable[FirstBaseline] != AlignmentLine.Unspecified)
        val firstBaseline = placeable[FirstBaseline]
        // Height of the composable with padding - first baseline
        val placeableY = firstBaselineToTop.roundToPx() - firstBaseline
        val height = placeable.height + placeableY
        layout(placeable.width, height) {
            // Where the composable gets placed
            placeable.placeRelative(0, placeableY)
        }
    }
)

@Composable
fun TextWithPaddingToBaselinePreview() {
    Surface(color = Color.White) {
        Text("Hi there!", Modifier.firstBaselineToTop(32.dp))
    }
}

@Composable
fun TextWithNormalPaddingPreview() {
    BasicCodeLabWeek2_1Theme {
        Surface(color = Color.White) {
            Text("Hi there!", Modifier.padding(top = 32.dp))
        }

    }
}

@Composable
fun MyColumn( modifier: Modifier, content: @Composable ()->Unit ) {

    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->

        val placeables = measurables.map {

            it.measure(constraints)

        }
        var yPos = 0
        var xPos = 0
        layout(constraints.maxWidth, constraints.maxHeight) {

            placeables.forEach {

                it.placeRelative( xPos, yPos )
                xPos += it.width
                yPos += it.height

            }

        }

    }

}

@Composable
fun MyBodyContent(modifier: Modifier = Modifier) {
    MyColumn(modifier.padding(8.dp)) {
        Text("111")
        Text("222")
        Text("333")
        Text("444")
    }
}

@Composable
fun ImageListItem( cnt: Int ) {

    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(10.dp)) {

        Image(
            painter = rememberImagePainter(
                data = "https://developer.android.com/images/brand/Android_Robot.png"
            ),
            contentDescription = "Android Logo",
            modifier = Modifier.size(50.dp)
        )
        Spacer(Modifier.width(10.dp))
        Text("Item #$cnt", style = MaterialTheme.typography.subtitle1)
    }
    
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BasicCodeLabWeek2_1Theme {
        ConstraintLayoutExample()
    }
}