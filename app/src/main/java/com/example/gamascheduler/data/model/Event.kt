package com.example.gamascheduler.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName

data class Event(
    val id: String = "",
    val title: String = "",
    val owner: String = "",
    val type: Int = 0,

    @get:PropertyName("created_on")
    @set:PropertyName("created_on")
    var createdOn: Timestamp? = null,

    @get:PropertyName("happening_on")
    @set:PropertyName("happening_on")
    var happeningOn: Timestamp? = null,

    @get:PropertyName("assigned_to")
    @set:PropertyName("assigned_to")
    var assignedTo: List<String> = emptyList()
)