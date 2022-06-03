package dev.numberonedroid.scheduler.activity

import java.io.Serializable


data class MyData(var year:Int, var month:Int, var day:Int, var title:String, var content:String, var starthour:Int, var startmin:Int, var endhour:Int, var endmin:Int):Serializable
