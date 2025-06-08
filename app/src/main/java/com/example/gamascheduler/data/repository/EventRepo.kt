package com.example.gamascheduler.data.repository

import com.example.gamascheduler.data.datasource.EventRemoteDataSource
import com.example.gamascheduler.data.model.Event
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EventRepository @Inject constructor(
    private val remoteDataSource: EventRemoteDataSource
) {
    fun getEvents(): Flow<List<Event>> = remoteDataSource.getEvents()

    suspend fun createEvent(event: Event) = remoteDataSource.createEvent(event)
}