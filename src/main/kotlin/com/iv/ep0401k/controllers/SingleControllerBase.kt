package com.iv.ep0401k.controllers

import com.iv.ep0401k.models.*
import org.springframework.data.repository.CrudRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.servlet.http.HttpServletResponse
import kotlin.jvm.optionals.getOrNull

@Component
abstract class SingleControllerBase<TModel : SingleModelBase, TRepository : CrudRepository<TModel, Int>>
    : CrudControllerBase<TModel, TRepository, Int>() {

    abstract val propertyName: String
    abstract val listTitle: String
    abstract val editTitle: String
    abstract val editTitleNotFound: String
    abstract val newTitlePart: String

    override val templateFolder: String = "single"

    private fun editModelInit(model: Model) {
        model.addAttribute("title", editTitle)
        model.addAttribute("titleNotFound", editTitleNotFound)
        model.addAttribute("propertyName", propertyName)
    }

    private fun newModelInit(model: Model) {
        model.addAttribute("titlePart", newTitlePart)
        model.addAttribute("propertyName", propertyName)
    }

    override fun getBase(model: Model): String {
        val elements = repository.findAll()

        model.addAttribute("elements", elements)
        model.addAttribute("title", listTitle)
        model.addAttribute("propertyName", propertyName)

        return "single/List"
    }

    @OptIn(ExperimentalStdlibApi::class)
    override fun editGetBase(id: Int, model: Model, response: HttpServletResponse): String {
        val element = repository.findById(id)
        model.addAttribute("model", element)
        model.addAttribute("element", element.getOrNull())
        if (element.isEmpty) response.status = HttpStatus.NOT_FOUND.value()

        editModelInit(model)

        return "single/Edit"
    }

    override fun newGetBase(element: TModel, model: Model): String {
        model.addAttribute("element", element)
        newModelInit(model)

        return "single/New"
    }

    override fun newPostBase(element: TModel, bindingResult: BindingResult, model: Model): String {
        if (bindingResult.hasErrors()) {
            newModelInit(model)
            return "single/New"
        }

        repository.save(element)

        return "redirect:$path"
    }

    override fun editPostBase(id: Int, element: TModel, bindingResult: BindingResult, model: Model): String {
        if (bindingResult.hasErrors()) {
            model.addAttribute("model", Optional.of(element))

            editModelInit(model)

            return "single/Edit"
        }

        element.id = id
        repository.save(element)

        return "redirect:$path"
    }

    override fun deleteGetBase(id: Int): String {
        repository.deleteById(id)
        return "redirect:$path"
    }
}