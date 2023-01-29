package com.example.kanban.tables

import android.provider.ContactsContract.CommonDataKinds.Email


data class Users(var name:String? = "", var username:String? ="",var email: String? ="", var profilePicture: String?="", var introduction:String?="", var skills:MutableList<String> = mutableListOf())

