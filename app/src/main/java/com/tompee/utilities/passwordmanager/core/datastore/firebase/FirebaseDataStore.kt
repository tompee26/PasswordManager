package com.tompee.utilities.passwordmanager.core.datastore.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.tompee.utilities.passwordmanager.core.datastore.DataStore
import io.reactivex.Completable
import io.reactivex.Single

class FirebaseDataStore(private val db: FirebaseFirestore) : DataStore {

    companion object {
        private const val ACCOUNT = "account"
    }

    override fun saveEncryptedIdentifier(email: String, key: String): Completable {
        return Completable.fromAction { db.collection(ACCOUNT).document(email).set(key) }
    }

    override fun getEncryptedIdentifier(email: String, key: String): Single<String> {
        return Single.create<String> { emitter ->
            val docRef = db.collection(ACCOUNT).document(email)
            docRef.get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    emitter.onSuccess(documentSnapshot.toObject(String::class.java)!!)
                } else {
                    emitter.onError(Throwable("Identifier not found"))
                }
            }.addOnFailureListener { emitter.onError(Throwable(it.message)) }
        }
    }
}