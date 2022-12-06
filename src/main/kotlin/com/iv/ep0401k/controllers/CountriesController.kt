package com.iv.ep0401k.controllers

import com.iv.ep0401k.models.CountriesRepository
import com.iv.ep0401k.models.Country
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

private const val basePath: String = "/countries"

@Controller
@RequestMapping(basePath)
@PreAuthorize("hasAnyAuthority('admin', 'salesman')")
class CountriesController : SingleControllerBase<Country, CountriesRepository>() {

    override val path: String = basePath;
    override val listTitle: String = "Страны"
    override val propertyName: String = "Название"
    override val editTitle: String = "Страна"
    override val editTitleNotFound: String = "Страна не найдена"
    override val newTitlePart: String = "страны"

}