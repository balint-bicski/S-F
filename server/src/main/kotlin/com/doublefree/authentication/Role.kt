package com.doublefree.authentication

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

enum class Authority : GrantedAuthority {

    VIEW_CAFF, DOWNLOAD_CAFF, UPLOAD_CAFF, DELETE_CAFF, MODIFY_CAFF, PAYMENT, SEARCH_CAFF, WRITE_NOTE, DELETE_NOTE, DELETE_USER;

    override fun getAuthority() = name

}
