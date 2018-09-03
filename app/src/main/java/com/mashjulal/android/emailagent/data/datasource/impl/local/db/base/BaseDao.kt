package com.mashjulal.android.emailagent.data.datasource.impl.local.db.base

import io.reactivex.Completable
import io.reactivex.Single

interface BaseDao<T> {

    val TABLE_NAME: String
    val COLUMN_NAME_ID: String

    fun insert(obj: T): Single<Long>
    fun insert(obj: List<T>): Single<List<Long>>

    fun update(obj: T): Completable
    fun update(obj: List<T>): Completable

    fun delete(obj: T): Completable
    fun delete(obj: List<T>): Completable

    fun getAll(): Single<List<T>>
    fun getById(id: Long): Single<T>
}