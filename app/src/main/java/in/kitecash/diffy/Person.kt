package `in`.kitecash.diffy

import java.util.Date

data class Person(
    val id: Long,
    val name: String,
    val age: Int,
    val sex: String,
    val phoneNumber: String,
    val updated: Date
)