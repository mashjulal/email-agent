package com.mashjulal.android.emailagent.utils

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

fun Disposable.addToComposite(compositeDisposable: CompositeDisposable) {
    compositeDisposable.add(this)
}

fun <R> (() -> R).toIoSingle(): Single<R> {
    return Single.fromCallable(this).subscribeOn(Schedulers.io())
}

fun (() -> Any).toIoCompletable(): Completable {
    return Completable.fromCallable(this).subscribeOn(Schedulers.io())
}

fun <R> (() -> R).toIoMaybe(): Maybe<R> {
    return Maybe.fromCallable(this).subscribeOn(Schedulers.io())
}

fun <R> (() -> R).toIoFlowable(): Flowable<R> {
    return Flowable.fromCallable(this).subscribeOn(Schedulers.io())
}