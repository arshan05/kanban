package com.example.kanban

import com.example.kanban.tables.Users
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

public var currentUser:Users = Users("","","")
var database = Firebase.database.reference