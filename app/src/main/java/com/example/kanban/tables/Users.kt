package com.example.kanban.tables

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Users(val name:String ?= null,val password: String ?= null)
