package com.mashjulal.android.emailagent.data.datasource.impl.local.db

import android.arch.persistence.room.TypeConverter
import com.mashjulal.android.emailagent.domain.model.Protocol

class EntityTypeConverters {

    @TypeConverter
    fun toProtocol(ordinal: Int) = Protocol.values()[ordinal]

    @TypeConverter
    fun fromProtocol(protocol: Protocol) = protocol.ordinal
}