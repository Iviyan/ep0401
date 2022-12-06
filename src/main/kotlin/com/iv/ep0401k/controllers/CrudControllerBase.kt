package com.iv.ep0401k.controllers

import com.iv.ep0401k.models.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.CrudRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid
import kotlin.jvm.optionals.getOrNull

@Suppress("SpringJavaInjectionPointsAutowiringInspection")
@Component
abstract class CrudControllerBase<TModel : ModelBase<TModelId>, TRepository : CrudRepository<TModel, TModelId>, TModelId> {

    @Autowired
    lateinit var repository: TRepository

    abstract val path: String
    abstract val templateFolder: String

    enum class LoadDependenciesMethod { NEW, EDIT }

    fun loadDependencies(model: Model, method: LoadDependenciesMethod) {}

    fun getBase(model: Model): String {
        val elements = repository.findAll()

        model.addAttribute("elements", elements)

        return "$templateFolder/List"
    }

    @OptIn(ExperimentalStdlibApi::class)
    fun editGetBase(id: TModelId, model: Model, response: HttpServletResponse): String {
        val element = repository.findById(id)
        model.addAttribute("model", element)
        model.addAttribute("element", element.getOrNull())
        if (element.isEmpty) response.status = HttpStatus.NOT_FOUND.value()

        loadDependencies(model, LoadDependenciesMethod.EDIT)

        return "$templateFolder/Edit"
    }

    fun newGetBase(element: TModel, model: Model): String {
        model.addAttribute("element", element)
        loadDependencies(model, LoadDependenciesMethod.NEW)

        return "$templateFolder/New"
    }

    fun newPostBase(element: TModel, bindingResult: BindingResult, model: Model): String {
        if (bindingResult.hasErrors()) {
            loadDependencies(model, LoadDependenciesMethod.NEW)

            return "$templateFolder/New"
        }

        repository.save(element)

        return "redirect:$path"
    }

    fun editPostBase(id: TModelId, element: TModel, bindingResult: BindingResult, model: Model): String {
        if (bindingResult.hasErrors()) {
            model.addAttribute("model", Optional.of(element))
            loadDependencies(model, LoadDependenciesMethod.EDIT)

            return "$templateFolder/Edit"
        }

        element.id = id
        repository.save(element)

        return "redirect:$path"
    }

    fun deleteGetBase(id: TModelId): String {
        repository.deleteById(id)
        return "redirect:$path"
    }

    // ----------

    @GetMapping()
    fun get(model: Model): String = getBase(model)

    @GetMapping("/{id}")
    fun editGet(
        @PathVariable(name = "id") id: TModelId,
        model: Model,
        response: HttpServletResponse
    ): String = editGetBase(id, model, response)

    @GetMapping("/new")
    fun newGet(element: TModel, model: Model): String = newGetBase(element, model)

    @PostMapping()
    fun newPost(
        @ModelAttribute("element") @Valid element: TModel,
        bindingResult: BindingResult,
        model: Model
    ): String = newPostBase(element, bindingResult, model)

    @PostMapping("/{id}")
    fun editPost(
        @PathVariable(name = "id") id: TModelId,
        @ModelAttribute("element") @Valid element: TModel, bindingResult: BindingResult,
        model: Model
    ): String = editPostBase(id, element, bindingResult, model)

    @GetMapping("/{id}/delete")
    fun deleteGet(@PathVariable(name = "id") id: TModelId): String = deleteGetBase(id)
}