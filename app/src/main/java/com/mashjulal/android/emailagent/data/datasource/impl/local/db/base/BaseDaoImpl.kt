package com.mashjulal.android.emailagent.data.datasource.impl.local.db.base

import android.arch.persistence.db.SimpleSQLiteQuery
import android.arch.persistence.room.*
import com.mashjulal.android.emailagent.utils.toIoCompletable
import com.mashjulal.android.emailagent.utils.toIoSingle
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers


abstract class BaseDaoImpl<T> : BaseDao<T> {

    @Insert
    protected abstract fun insertOrUpdateBlocking(obj: T): Long

    override fun insert(obj: T): Single<Long> {
        return { insertOrUpdateBlocking(obj) }.toIoSingle()
    }

    @Insert
    protected abstract fun insertOrUpdateBlocking(obj: List<T>): List<Long>

    override fun insert(obj: List<T>): Single<List<Long>> {
        return { insertOrUpdateBlocking(obj) }.toIoSingle()
    }

    @Update(onConflict = OnConflictStrategy.IGNORE)
    protected abstract fun updateBlocking(obj: T)

    override fun update(obj: T): Completable {
        return { updateBlocking(obj) }.toIoCompletable()
    }

    @Update(onConflict = OnConflictStrategy.IGNORE)
    protected abstract fun updateBlocking(obj: List<T>)

    override fun update(obj: List<T>): Completable {
        return { updateBlocking(obj) }.toIoCompletable()
    }

    @Delete
    protected abstract fun deleteBlocking(obj: T)

    override fun delete(obj: T): Completable{
        return { deleteBlocking(obj) }.toIoCompletable()
    }

    @Delete
    protected abstract fun deleteBlocking(obj: List<T>)

    override fun delete(obj: List<T>): Completable{
        return { deleteBlocking(obj) }.toIoCompletable()
    }

    override fun getAll(): Single<List<T>> {
        val query = SimpleSQLiteQuery("SELECT * FROM $TABLE_NAME")
        return execSelectToList(query).subscribeOn(Schedulers.io())
    }

    override fun getById(id: Long): Single<T> {
        val query = SimpleSQLiteQuery(
                "SELECT * FROM $TABLE_NAME WHERE $COLUMN_NAME_ID = ?", arrayOf<Any>(id)
        )
        return execSelectSingle(query).subscribeOn(Schedulers.io())
    }

    @RawQuery
    protected abstract fun execSelectSingle(query: SimpleSQLiteQuery): Single<T>

    @RawQuery
    protected abstract fun execSelectToList(query: SimpleSQLiteQuery): Single<List<T>>

}

