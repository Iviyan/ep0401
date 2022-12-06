package com.iv.ep0401k.controllers

import com.iv.ep0401k.models.Supplier
import com.iv.ep0401k.models.SuppliersRepository
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

private const val basePath: String = "/suppliers"

@Controller
@RequestMapping(basePath)
@PreAuthorize("hasAnyAuthority('admin', 'salesman')")
class SuppliersController : CrudControllerBase<Supplier, SuppliersRepository, Int>() {

    override val path: String = basePath;
    override val templateFolder: String = "suppliers"

}