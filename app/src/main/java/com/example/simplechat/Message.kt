package com.example.simplechat

// The text stores the body of the message
// The displayName is used to store the user writing
// The key is a redundant field that is used to store the key for update use
// TODO either move this to a composite class so we are not storing the key twice or find a better way

data class Message (
    var text:  String? = null,
    var displayName: String? = null,
    var key: String? = null
)
