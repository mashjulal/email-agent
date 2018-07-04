package com.mashjulal.android.emailagent.objects.mailbox

import android.os.Parcel
import android.os.Parcelable
import javax.mail.Flags

class Message : Parcelable {

    val isRead: Boolean
    val from: List<String>
    val subject: String

    constructor(parcel: Parcel) {
        isRead = parcel.readInt() != 0
        val lst = listOf<String>()
        parcel.readStringList(lst)
        from = lst
        subject = parcel.readString()
    }

    constructor(message: javax.mail.Message) {
        isRead = message.flags.contains(Flags.Flag.SEEN)
        from = message.from.map { it.toString() }
        subject = message.subject
    }

    constructor(subject: String, from: List<String>, isRead: Boolean) {
        this.isRead = isRead
        this.from = from
        this.subject = subject
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(if (isRead) 1 else 0)
        parcel.writeStringList(from)
        parcel.writeString(subject)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Message> {
        override fun createFromParcel(parcel: Parcel): Message {
            return Message(parcel)
        }

        override fun newArray(size: Int): Array<Message?> {
            return arrayOfNulls(size)
        }
    }
}