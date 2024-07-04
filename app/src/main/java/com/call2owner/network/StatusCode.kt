package com.call2owner.network

import java.io.Serializable

sealed class StatusCode(val code: String, val description: String) : Serializable {
    object SUCCESS : StatusCode("000", "Response Success")
    object FAILURE : StatusCode("001", "Response Failure")
    object PENDING : StatusCode("002", "Response Pending")
    object BANK_SERVER_ERROR : StatusCode("003", "Bank server error when using banking service")
    object NOT_FOUND : StatusCode("007", "Not found")
    object INITIATED : StatusCode("008", "Initiated like payouts")
    object SERVICE_DOWN : StatusCode("111", "Service Down")
    object NO_ACCESS : StatusCode("222", "No access")
    object VALIDATE_SESSION :
        StatusCode("100", "Validate Session use when session id pass in the login send otp")

    object INTERNAL_SERVER_ERROR : StatusCode("999", "Exception Internal server error")
    object NullValue : StatusCode("009", "Not Registered for Money Transfer")
}


enum class UserType(val code: String, val displayName: String) {
    AGENT("1", "Retailer"),
    DISTRIBUTOR("2", "Distributor"),
    MASTER_DISTRIBUTOR("3", "Master Distributor"),
    EMPLOYEE("6", "Employee"),
}

//
//enum class UserType(val code: String, val displayName: String) {
//    AGENT("1", "Retailer"),
//    DISTRIBUTOR("2", "Distributor"),
//    MASTER_DISTRIBUTOR("3", "Master Distributor"),
//    SUPER_DISTRIBUTOR("4", "Super Distributor"),
//    CHANNEL_PARTNER("5", "Channel Partner"),
//    EMPLOYEE("6", "Employee"),
//    SUB_ADMIN("7", "Sub Admin"),
//    ADMIN("0", "Admin")
//}


