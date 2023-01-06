package com.example.kanban.tables

import java.io.Serializable

data class Tasks(val task_id: String? = "", val task_name:String? ="", val project_id:String ?="", val status:String?="",val assigned_id: String?="") :
    Serializable
