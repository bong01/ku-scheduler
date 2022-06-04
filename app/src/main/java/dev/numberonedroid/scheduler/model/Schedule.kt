package dev.numberonedroid.scheduler.model

data class Schedule(

        var id: Int?,
        var title:String,
        var content:String,
        var startAt: String,
        var endAt: String,
        var isDone: Boolean

)