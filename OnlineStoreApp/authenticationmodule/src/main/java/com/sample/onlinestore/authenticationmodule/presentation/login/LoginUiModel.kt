package com.sample.onlinestore.authenticationmodule.presentation.login

import com.sample.onlinestore.commonmodule.domain.model.FieldObject
import com.sample.onlinestore.commonmodule.foundation.base.Action
import com.sample.onlinestore.commonmodule.foundation.base.Event

// FiledObject is used for passing error message and value to the UI
data class LoginUiModel(
    val username: FieldObject<String>? = FieldObject(),
    val password: FieldObject<String>? = FieldObject()
)

sealed class LoginAction : Action {
    data class OnUsernameChanged(val username: String) : LoginAction()
    data class OnPasswordChanged(val password: String) : LoginAction()
    data object OnClickLogin : LoginAction()
}

sealed class LoginEvent : Event {
    data object LoginSuccess : LoginEvent()
}
