package dev.numberonedroid.scheduler.model

import java.io.Serializable


data class MyData(
    var id: Int?,
    var year: Int,
    var month: Int,
    var day: Int,
    var title: String,
    var content: String,
    var starthour: Int,
    var startmin: Int,
    var endhour: Int,
    var endmin: Int,
    var isDone: Boolean
) : Serializable
