package com.iv.ep0401k.controllers

import com.iv.ep0401k.models.Genre
import com.iv.ep0401k.models.GenresRepository
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

private const val basePath: String = "/genres"

@Controller
@RequestMapping(basePath)
@PreAuthorize("hasAnyAuthority('admin', 'salesman')")
class GenresController : SingleControllerBase<Genre, GenresRepository>() {

    override val path: String = basePath;
    override val listTitle: String = "Жанры"
    override val propertyName: String = "Название"
    override val editTitle: String = "Жанр";
    override val editTitleNotFound: String = "Жанр не найден";
    override val newTitlePart: String = "жанра";

}