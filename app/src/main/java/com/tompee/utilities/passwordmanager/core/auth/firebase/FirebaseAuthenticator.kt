package com.tompee.utilities.passwordmanager.core.auth.firebase

import com.google.firebase.auth.FirebaseAuth
import com.tompee.utilities.passwordmanager.core.auth.Authenticator
import io.reactivex.Completable
import io.reactivex.Single

class FirebaseAuthenticator(private val firebaseAuth: FirebaseAuth) : Authenticator {

    override fun getCurrentUser(): Single<String> {
        return Single.create<String> { emitter ->
            val currentUser = firebaseAuth.currentUser
            if (currentUser != null && currentUser.email != null) {
                emitter.onSuccess(currentUser.email!!)
            } else {
                emitter.onError(Throwable("No user logged in"))
            }
        }
    }

    override fun signup(email: String, password: String): Single<String> {
        return Completable.create { emitter ->
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        emitter.onComplete()
                    } else {
                        emitter.onError(Throwable(it.exception?.message))
                    }
                }
        }
            .andThen(Completable.fromAction { firebaseAuth.signOut() })
            .andThen(Single.create<String> { emitter ->
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        emitter.onSuccess(firebaseAuth.currentUser?.uid!!)
                    } else {
                        emitter.onError(Throwable(it.exception?.message))
                    }
                }
            })
    }

    override fun login(email: String, password: String): Single<String> =
        Single.create<String> { emitter ->
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    emitter.onSuccess(firebaseAuth.currentUser?.uid!!)
                } else {
                    emitter.onError(Throwable(it.exception?.message))
                }
            }
        }

    override fun logout(): Completable =
        Completable.create { emitter ->
            firebaseAuth.signOut()
            emitter.onComplete()
        }
}