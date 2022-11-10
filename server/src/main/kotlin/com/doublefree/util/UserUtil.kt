package com.doublefree.util

import lombok.experimental.UtilityClass
import org.springframework.security.core.context.SecurityContextHolder

@UtilityClass
class UserUtil {

    companion object {
        fun emailOfLoggedInUser(): String = SecurityContextHolder.getContext().authentication.name
    }

}
