package com.example.kanban.tables

import java.io.Serializable

data class Projects(val project_id: String? = "", val project_name:String? ="", val owner:String? = "") : Serializable
