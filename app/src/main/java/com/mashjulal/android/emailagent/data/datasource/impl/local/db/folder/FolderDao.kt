package com.mashjulal.android.emailagent.data.datasource.impl.local.db.folder

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import io.reactivex.Single

@Dao
interface FolderDao {

    @Query("""
        SELECT *
        FROM folder
        WHERE account_id = :accountId
    """)
    fun getAllByAccountId(accountId: Long)
            : Single<List<FolderEntity>>

    @Query("""
        SELECT *
        FROM folder
        WHERE account_id = :accountId
        AND name = :folderName
    """)
    fun getByAccountIdAndFolderName(accountId: Long, folderName: String)
            : Single<FolderEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(folderEntity: FolderEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg folderEntity: FolderEntity): List<Long>

}