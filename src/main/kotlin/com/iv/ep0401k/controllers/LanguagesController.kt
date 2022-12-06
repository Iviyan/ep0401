package com.iv.ep0401k.controllers

import com.iv.ep0401k.models.Language
import com.iv.ep0401k.models.LanguagesRepository
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

private const val basePath: String = "/languages"

@Controller
@RequestMapping(basePath)
@PreAuthorize("hasAnyAuthority('admin', 'salesman')")
class LanguagesController : SingleControllerBase<Language, LanguagesRepository>() {

    override val path: String = basePath;
    override val listTitle: String = "Языки"
    override val propertyName: String = "Название"
    override val editTitle: String = "Язык";
    override val editTitleNotFound: String = "Язык не найден";
    override val newTitlePart: String = "языка";

}