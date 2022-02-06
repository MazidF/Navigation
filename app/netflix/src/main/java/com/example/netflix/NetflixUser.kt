package com.example.netflix

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream
import java.io.Serializable

data class NetflixUser(val name: String, val family: String, val email: String) : Serializable {
    var userName = "$name $family"
    var phone: String? = null
    var birthday: String? = null
    var image: Bitmap? = null

    class SerializableUser(user: NetflixUser) : Serializable {
        private val name = user.name
        private val email = user.email
        private val phone = user.phone
        private val family = user.family
        private val userName = user.userName
        private val birthday = user.birthday
        private var bytes: ByteArray

        init {
            val stream = ByteArrayOutputStream()
            user.image?.compress(Bitmap.CompressFormat.PNG, 100, stream)
            bytes = stream.toByteArray()
        }

        fun toUser() = NetflixUser(name, family, email).apply {
            this.phone = this@SerializableUser.phone
            this.userName = this@SerializableUser.userName
            this.birthday = this@SerializableUser.birthday
            this.image = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        }
    }

    fun save() = SerializableUser(this)
}
