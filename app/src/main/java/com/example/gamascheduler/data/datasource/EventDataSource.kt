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
                        val events = snapshot.documents.mapNotNull { doc ->
                            doc.toObject(Event::class.java)?.copy(id = doc.id)
                        }
                        trySend(events)
                    } catch (e: Exception) {
                        close(e)
                    }
                }
            }

        awaitClose { listener.remove() }
    }

    suspend fun createEvent(event: Event) {
        val docRef = firestore.collection(EVENT_COLLECTION).document()
        val eventWithId = event.copy(id = docRef.id)
        docRef.set(eventWithId).await()
    }


    suspend fun updateEvent(event: Event) {
        firestore.collection(EVENT_COLLECTION)
            .document(event.id)
            .set(event)
            .await()
    }
}