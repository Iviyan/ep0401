package com.iv.ep0401k.utils

class StringUtils {
    companion object{
        fun removeTrailingSlash(s: String) : String = s.trimEnd('/')
    }
}