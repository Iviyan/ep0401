package com.iv.ep0401k.controllers

import com.iv.ep0401k.models.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.RequestMapping

private const val basePath: String = "/shipments"

@Controller
@RequestMapping(basePath)
@PreAuthorize("hasAnyAuthority('admin', 'salesman')")
class ShipmentsController : CrudControllerBase<Shipment, ShipmentsRepository, Int>() {

    override val path: String = basePath;
    override val templateFolder: String = "shipments"

    @Autowired
    lateinit var booksRepository: BooksRepository

    @Autowired
    lateinit var suppliersRepository: SuppliersRepository

    override fun loadDependencies(model: Model, method: LoadDependenciesMethod) {
        val books = booksRepository.findAll()
        val suppliers = suppliersRepository.findAll()

        model.addAttribute("books", books)
        model.addAttribute("suppliers", suppliers)
    }

}