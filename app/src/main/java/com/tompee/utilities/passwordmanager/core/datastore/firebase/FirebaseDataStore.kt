package com.tompee.utilities.passwordmanager.core.datastore.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.tompee.utilities.passwordmanager.core.datastore.DataStore
import com.tompee.utilities.passwordmanager.core.datastore.Identifier
import io.reactivex.Completable
import io.reactivex.Observable

class FirebaseDataStore(private val db: FirebaseFirestore) : DataStore {

    companion object {
        private const val ACCOUNT = "account"
    }

    override fun saveEncryptedIdentifier(email: String, key: String): Completable {
        return Completable.fromAction { db.collection(ACCOUNT).document(email).set(Identifier(key)) }
    }

    override fun getEncryptedIdentifier(email: String): Observable<String> {
        return Observable.create<String> { emitter ->
            db.collection(ACCOUNT).document(email)
                .addSnapshotListener {documentSnapshot, _ ->
                    if (emitter.isDisposed) {
                        emitter.onComplete()
                    }
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        emitter.onNext(documentSnapshot.toObject(Identifier::class.java)!!.data)
                    } else {
                        emitter.onNext("")
                    }
                }
        }
    }
}