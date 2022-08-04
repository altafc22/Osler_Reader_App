package com.altafrazzque.oslerreaderapp.adpater

import com.altafrazzque.oslerreaderapp.model.User

interface UserListAdapterListener {
    fun onItemClick(user: User)
}