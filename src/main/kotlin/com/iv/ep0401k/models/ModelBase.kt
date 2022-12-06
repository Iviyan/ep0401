package com.iv.ep0401k.models

interface ModelBase<T> {
    var id: T
}

interface SingleModelBase : ModelBase<Int> {
    var name: String
}