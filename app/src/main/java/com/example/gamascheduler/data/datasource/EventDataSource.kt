package com.example.gamascheduler.data.datasource

import com.example.gamascheduler.data.model.Event
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class EventRemoteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    companion object {
        private const val EVENT_COLLECTION = "events"
    }

    fun getEvents(): Flow<List<Event>> = callbackFlow {
        val listener = firestore.collection(EVENT_COLLECTION)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    try {
                        val events = snapshot.toObjects(Event::class.java)
                        trySend(events)
                    } catch (e: Exception) {
                        close(e)
                    }
                }
            }

        awaitClose { listener.remove() }
    }

    suspend fun createEvent(event: Event) {
        firestore.collection(EVENT_COLLECTION)
            .add(event)
            .await()
    }
}