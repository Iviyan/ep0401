package com.iv.ep0401k.controllers

import com.iv.ep0401k.models.*
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.util.*

private const val basePath: String = "/categories"

@Controller
@RequestMapping(basePath)
@PreAuthorize("hasAnyAuthority('admin', 'salesman')")
class CategoriesController : SingleControllerBase<Category, CategoriesRepository>() {

    override val path: String = basePath;
    override val listTitle: String = "Категории"
    override val propertyName: String = "Имя"
    override val editTitle: String = "Категория";
    override val editTitleNotFound: String = "Категория не найдена";
    override val newTitlePart: String = "категории";

}