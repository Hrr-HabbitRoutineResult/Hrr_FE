package com.example.hrr_android

import javax.inject.Inject

class UserRepository @Inject constructor(networkClient: NetworkClient) {

    private val userService = networkClient.userService



}