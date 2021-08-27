package com.donsidro.get.it.done.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Web
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import com.donsidro.get.it.done.data.entities.Note
import com.donsidro.get.it.done.ui.notesview.NotesViewModel
import com.donsidro.get.it.done.utils.StaggeredVerticalGrid
import org.koin.androidx.compose.getViewModel

@Composable
fun ActionbarCompose() {
    val viewModel = getViewModel<NotesViewModel>()
    val fabShape = RoundedCornerShape(50)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Bottom app bar + FAB")
                },
                backgroundColor = Color(0xFFFF5470),
                elevation = AppBarDefaults.TopAppBarElevation
                     )
        },
        content = {
            NoteList(viewModel.notes)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    var note = Note(
                        0,
                        "",
                        "",
                        "adsklaladkkldllsdalddak" +
                                "asdlkadladladalkalddal" +
                                "aldadklalkaslaslkadslkaslka" +
                                "a" +
                                "sadldasldaslkdaslkalsdkaklsd" +
                                "adsklasdlaslasdkldsadsakldalsklkdsa" +
                                "adslkaslakk",
                        "",
                        "",
                        "",
                        "",
                        ""
                                   )
                    viewModel.saveNote(note = note)
                },
                shape = fabShape,
                backgroundColor = Color(0xFFFF8C00)
                                ) {
                Icon(
                    Icons.Filled.Add,
                    ""
                    )
            }
        },
        isFloatingActionButtonDocked = true,
        floatingActionButtonPosition = FabPosition.End,
        bottomBar = {
            BottomAppBar(
                backgroundColor = Color.Blue,
                cutoutShape = CircleShape
                        ) {
                IconButton(onClick = { /* doSomething() */ }) {
                    Icon(
                        Icons.Filled.AddCircle,
                        ""
                        )
                }
                IconButton(onClick = { /* doSomething() */ }) {
                    Icon(
                        Icons.Filled.Image,
                        ""
                        )
                }
                IconButton(onClick = { /* doSomething() */ }) {
                    Icon(
                        Icons.Filled.Web,
                        ""
                        )
                }
                // The actions should be at the end of the BottomAppBar
                Spacer(
                    Modifier.weight(
                        1f,
                        true
                                   )
                      )
            }
        }
            )
}

@Composable
fun NoteList(notes: LiveData<List<Note>>) {
    val noteList by notes.observeAsState(initial = emptyList())
    if (noteList.isEmpty()) {
        LiveDataLoadingComponent()
    } else {
        LiveDataComponentList(noteList)
    }
}

@Composable
fun LiveDataLoadingComponent() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
          ) {
        CircularProgressIndicator(modifier = Modifier.wrapContentWidth(CenterHorizontally))
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LiveDataComponentList(noteList: List<Note>) {
    LazyColumn {
        item {
            StaggeredVerticalGrid(
                maxColumnWidth = 300.dp,
                modifier = Modifier.padding(4.dp)
                                 ) {
                noteList.forEach { note ->

                }
            }
        }
    }
}

@Preview
@Composable
fun ComposablePreview() {
    ActionbarCompose()
}
