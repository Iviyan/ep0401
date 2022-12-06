package com.iv.ep0401k.controllers

import com.iv.ep0401k.models.*
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.util.*

private const val basePath: String = "/authors"

@Controller
@RequestMapping(basePath)
@PreAuthorize("hasAnyAuthority('admin', 'salesman')")
class AuthorsController : SingleControllerBase<Author, AuthorsRepository>() {

    override val path: String = basePath;
    override val listTitle: String = "Авторы"
    override val propertyName: String = "Имя"
    override val editTitle: String = "Автор";
    override val editTitleNotFound: String = "Автор не найден";
    override val newTitlePart: String = "автора";

}