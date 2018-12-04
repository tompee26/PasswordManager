package com.tompee.utilities.passwordmanager.core.datastore.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.tompee.utilities.passwordmanager.core.datastore.DataStore
import com.tompee.utilities.passwordmanager.core.datastore.Identifier
import com.tompee.utilities.passwordmanager.core.datastore.PackageModel
import com.tompee.utilities.passwordmanager.core.datastore.SiteModel
import io.reactivex.Completable
import io.reactivex.Observable

class FirebaseDataStore(private val db: FirebaseFirestore) : DataStore {

    companion object {
        private const val ACCOUNT = "account"
        private const val PACKAGES = "packages"
        private const val SITES = "sites"
    }

    override fun saveEncryptedIdentifier(email: String, key: String): Completable {
        return Completable.fromAction { db.collection(ACCOUNT).document(email).set(Identifier(key)) }
    }

    override fun getEncryptedIdentifier(email: String): Observable<String> {
        return Observable.create<String> { emitter ->
            db.collection(ACCOUNT).document(email)
                .addSnapshotListener { documentSnapshot, _ ->
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

    override fun savePackage(email: String, pack: PackageModel): Completable {
        return Completable.fromAction {
            db.collection(ACCOUNT).document(email).collection(PACKAGES).add(pack)
        }
    }

    override fun saveSite(email: String, site: SiteModel): Completable {
        return Completable.fromAction {
            db.collection(ACCOUNT).document(email).collection(SITES).add(site)
        }
    }

    override fun deletePackages(email: String): Completable {
        return Completable.create { emitter ->
            db.collection(ACCOUNT).document(email).collection(PACKAGES).get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        document.reference.delete()
                    }
                    emitter.onComplete()
                }.addOnFailureListener {
                    emitter.onError(it)
                }
        }
    }

    override fun deleteSites(email: String): Completable {
        return Completable.create { emitter ->
            db.collection(ACCOUNT).document(email).collection(SITES).get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        document.reference.delete()
                    }
                    emitter.onComplete()
                }.addOnFailureListener {
                    emitter.onError(it)
                }
        }
    }

    override fun getPackages(email: String): Observable<PackageModel> {
        return Observable.create { emitter ->
            db.collection(ACCOUNT).document(email).collection(PACKAGES).get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        emitter.onNext(document.toObject(PackageModel::class.java))
                    }
                    emitter.onComplete()
                }.addOnFailureListener {
                    emitter.onError(it)
                }
        }
    }

    override fun getSites(email: String): Observable<SiteModel> {
        return Observable.create { emitter ->
            db.collection(ACCOUNT).document(email).collection(SITES).get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        emitter.onNext(document.toObject(SiteModel::class.java))
                    }
                    emitter.onComplete()
                }.addOnFailureListener {
                    emitter.onError(it)
                }
        }
    }
}