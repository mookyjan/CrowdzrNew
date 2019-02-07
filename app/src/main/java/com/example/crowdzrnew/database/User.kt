package com.example.crowdzrnew.database

data class User (var id:Long =1000L,
                 var email:String?="",
                 var access_token : String?="")