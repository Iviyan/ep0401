package com.iv.ep0401k.controllers

import com.iv.ep0401k.models.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import java.util.*

private const val basePath: String = "/selections"

@Controller
@RequestMapping(basePath)
@PreAuthorize("hasAnyAuthority('admin', 'salesman')")
class SelectionsController : CrudControllerBase<Selection, SelectionsRepository, Int>() {

    override val path: String = basePath;
    override val templateFolder: String = "selections"

    @Autowired lateinit var booksRepository: BooksRepository

    override fun loadDependencies(model: Model, method: LoadDependenciesMethod) {
        if (method != LoadDependenciesMethod.EDIT) return

        val books = booksRepository.findAll()

        model.addAttribute("books", books)
    }

    override fun editPostBase(id: Int, element: Selection, bindingResult: BindingResult, model: Model): String {
        if (bindingResult.hasErrors()) {
            model.addAttribute("model", Optional.of(element))
            loadDependencies(model, LoadDependenciesMethod.EDIT)

            return "$templateFolder/Edit"
        }

        element.id = id
        element.books = repository.findById(id).orElseThrow().books // TODO: replace it
        repository.save(element)

        return "redirect:$path"
    }

    @PostMapping("{selectionId}/books")
    fun addBook(
        @PathVariable(name = "selectionId") selectionId: Int,
        @RequestParam bookId: Int
    ): String {
        val selection = repository.findById(selectionId).orElseThrow()
        if (!selection.books!!.any { it.id == bookId }) {
            selection.books!!.add(Book(id = bookId))
            repository.save(selection)
        }
        return "redirect:$basePath/$selectionId"
    }

    @GetMapping("{selectionId}/books/{bookId}/delete")
    fun deleteBook(
        @PathVariable(name = "selectionId") selectionId: Int,
        @PathVariable(name = "bookId") bookId: Int
    ): String {
        val selection = repository.findById(bookId).orElseThrow()
        selection.books!!.removeIf { it.id == bookId }
        repository.save(selection)
        return "redirect:$basePath/$selectionId"
    }

}