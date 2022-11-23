package com.doublefree.authentication

import com.doublefree.api.model.Authority
import org.springframework.security.core.GrantedAuthority

enum class Role(private vararg val authorities: Authority) {

    ADMINISTRATOR(*Authority.values()),
    USER(
        Authority.VIEW_CAFF,
        Authority.DOWNLOAD_CAFF,
        Authority.UPLOAD_CAFF,
        Authority.SEARCH_CAFF,
        Authority.WRITE_NOTE
    );

    fun authorities() = authorities.toList()

}

fun Collection<GrantedAuthority>.toAuthorities() = map { Authority.valueOf(it.authority) }

fun Collection<Authority>.toGrantedAuthorities() = map { GrantedAuthority { it.name } }
