package com.iv.ep0401k.controllers

import com.iv.ep0401k.models.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid
import kotlin.jvm.optionals.getOrNull

@Controller
class BooksController {

    @Autowired lateinit var booksRepository: BooksRepository
    @Autowired lateinit var bookRentalRepository: BookRentalRepository
    @Autowired lateinit var authorsRepository: AuthorsRepository
    @Autowired lateinit var categoriesRepository: CategoriesRepository
    @Autowired lateinit var genresRepository: GenresRepository
    @Autowired lateinit var languagesRepository: LanguagesRepository
    @Autowired lateinit var countriesRepository: CountriesRepository

    fun loadDependencies(model: Model) {
        val authors = authorsRepository.findAll()
        val categories = categoriesRepository.findAll()
        val genres = genresRepository.findAll()
        val languages = languagesRepository.findAll()
        val countries = countriesRepository.findAll()

        model.addAttribute("authors", authors)
        model.addAttribute("categories", categories)
        model.addAttribute("genres", genres)
        model.addAttribute("languages", languages)
        model.addAttribute("countries", countries)
    }

    @GetMapping("/") @PreAuthorize("hasAnyAuthority('admin', 'user', 'salesman')")
    fun index(
        model: Model,
        @RequestParam(name = "title", required = false) title: String?
    ): String {
        val books = if (title == null) booksRepository.findAll()
        else booksRepository.findByTitleContainsIgnoreCase(title)

        val bookRental = if (title == null) bookRentalRepository.findAll()
        else bookRentalRepository.findByBookIdIn(books.map { it.id })

        model.addAttribute("books", books)
        model.addAttribute("bookRental", bookRental)

        return "books/Books"
    }

    @OptIn(ExperimentalStdlibApi::class)
    @GetMapping("/books/{id}") @PreAuthorize("hasAnyAuthority('admin', 'salesman')")
    fun getBook(
        @PathVariable(name = "id") id: Int,
        model: Model,
        response: HttpServletResponse
    ): String {
        val book = booksRepository.findById(id)
        model.addAttribute("model", book)
        model.addAttribute("book", book.getOrNull())
        if (book.isEmpty) response.status = HttpStatus.NOT_FOUND.value()

        loadDependencies(model)

        return "books/Book"
    }

    @GetMapping("/books/new") @PreAuthorize("hasAnyAuthority('admin', 'salesman')")
    fun addBook(book: Book, model: Model): String {
        loadDependencies(model)
        return "books/NewBook"
    }

    @PostMapping("/books") @PreAuthorize("hasAnyAuthority('admin', 'salesman')")
    fun addBook(@Valid book: Book, bindingResult: BindingResult, model: Model): String {
        if (bindingResult.hasErrors()) {
            loadDependencies(model)
            return "books/NewBook"
        }

        booksRepository.save(book)

        return "redirect:/"
    }

    @PostMapping("/books/{id}") @PreAuthorize("hasAnyAuthority('admin', 'salesman')")
    fun editBook(
        @PathVariable(name = "id") id: Int,
        @Valid book: Book, bindingResult: BindingResult,
        model: Model
    ): String {
        if (bindingResult.hasErrors()) {
            model.addAttribute("model", Optional.of(book))
            loadDependencies(model)
            return "books/Book"
        }

        book.id = id
        book.genres = booksRepository.findById(id).orElseThrow().genres // TODO: replace it
        booksRepository.save(book)

        return "redirect:/"
    }

    @GetMapping("/books/{id}/delete") @PreAuthorize("hasAnyAuthority('admin', 'salesman')")
    fun deleteBook(@PathVariable(name = "id") id: Int): String {
        booksRepository.deleteById(id)
        return "redirect:/books/$id"
    }

    @PostMapping("/books/{bookId}/genres") @PreAuthorize("hasAnyAuthority('admin', 'salesman')")
    fun addGenre(
        @PathVariable(name = "bookId") bookId: Int,
        @RequestParam genreId: Int
    ): String {
        val book = booksRepository.findById(bookId).orElseThrow()
        if (!book.genres!!.any { it.id == genreId }) {
            book.genres!!.add(Genre(id = genreId))
            booksRepository.save(book)
        }
        return "redirect:/books/$bookId"
    }

    @GetMapping("/books/{bookId}/genres/{genreId}/delete") @PreAuthorize("hasAnyAuthority('admin', 'salesman')")
    fun deleteGenre(
        @PathVariable(name = "bookId") bookId: Int,
        @PathVariable(name = "genreId") genreId: Int,
    ): String {
        val book = booksRepository.findById(bookId).orElseThrow()
        book.genres!!.removeIf { it.id == genreId }
        booksRepository.save(book)
        return "redirect:/books/$bookId"
    }

}