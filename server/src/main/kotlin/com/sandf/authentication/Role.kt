package com.sandf.authentication

import com.sandf.api.model.Authority
import org.springframework.security.core.GrantedAuthority

enum class Role(private vararg val authorities: Authority) {

    ADMINISTRATOR(*Authority.values()),
    USER(
        Authority.VIEW_EVENT,
        Authority.CREATE_EVENT,
        Authority.SEARCH_EVENT,
        Authority.WRITE_NOTE,
        Authority.MODIFY_EVENT,
        Authority.DELETE_EVENT
    );

    fun authorities() = authorities.toList()

}

fun Collection<GrantedAuthority>.toAuthorities() = map { Authority.valueOf(it.authority) }

fun Collection<Authority>.toGrantedAuthorities() = map { GrantedAuthority { it.name } }
