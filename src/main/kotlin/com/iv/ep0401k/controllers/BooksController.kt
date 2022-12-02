package com.iv.ep0401k.controllers

import com.iv.ep0401k.models.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.access.annotation.Secured
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

    @Autowired
    lateinit var booksRepository: BooksRepository

    @Autowired
    lateinit var usersRepository: UsersRepository

    @Autowired
    lateinit var bookRentalRepository: BookRentalRepository

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
        return "books/Book"
    }

    @GetMapping("/books/new") @PreAuthorize("hasAnyAuthority('admin', 'salesman')")
    fun addBook(book: Book): String = "books/NewBook"

    @PostMapping("/books") @Secured("admin", "salesman")
    fun addBook(@Valid book: Book, bindingResult: BindingResult): String {
        if (bindingResult.hasErrors()) return "books/NewBook"

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
            return "books/Book"
        }

        book.id = id
        booksRepository.save(book)

        return "redirect:/"
    }

    @GetMapping("/books/{id}/delete") @PreAuthorize("hasAnyAuthority('admin', 'salesman')")
    fun deleteBook(@PathVariable(name = "id") id: Int): String {
        booksRepository.deleteById(id)
        return "redirect:/"
    }

    // ---

    @OptIn(ExperimentalStdlibApi::class)
    @GetMapping("/book-rental/{id}") @PreAuthorize("hasAnyAuthority('admin', 'user')")
    fun getBookRental(
        @PathVariable(name = "id") id: Int,
        model: Model,
        response: HttpServletResponse
    ): String {
        val books = booksRepository.findAll()
        val users = usersRepository.findAll()
        model.addAttribute("books", books)
        model.addAttribute("users", users.map { UserDto.from(it) })

        val bookRental = bookRentalRepository.findById(id)
        model.addAttribute("model", bookRental)
        model.addAttribute("bookRental", bookRental.getOrNull())
        if (bookRental.isEmpty) response.status = HttpStatus.NOT_FOUND.value()
        return "books/BookRental"
    }

    @GetMapping("/book-rental/new") @PreAuthorize("hasAnyAuthority('admin', 'user')")
    fun addBookRental(bookRental: BookRental, model: Model): String {
        val books = booksRepository.findAll()
        val users = usersRepository.findAll()
        model.addAttribute("books", books)
        model.addAttribute("users", users.map { UserDto.from(it) })

        return "books/NewBookRental"
    }

    @PostMapping("/book-rental") @PreAuthorize("hasAnyAuthority('admin', 'user')")
    fun addBookRental(
        @Valid bookRental: BookRental, bindingResult: BindingResult,
        model: Model
    ): String {
        val books = booksRepository.findAll()
        val users = usersRepository.findAll()
        model.addAttribute("books", books)
        model.addAttribute("users", users.map { UserDto.from(it) })

        if (bindingResult.hasErrors()) return "books/NewBookRental"

        bookRentalRepository.save(bookRental)

        return "redirect:/"
    }

    @PostMapping("/book-rental/{id}") @PreAuthorize("hasAnyAuthority('admin', 'user')")
    fun editBookRental(
        @PathVariable(name = "id") id: Int,
        @Valid bookRental: BookRental, bindingResult: BindingResult,
        model: Model
    ): String {
        val books = booksRepository.findAll()
        val users = usersRepository.findAll()
        model.addAttribute("books", books)
        model.addAttribute("users", users.map { UserDto.from(it) })

        if (bindingResult.hasErrors()) {
            model.addAttribute("model", Optional.of(bookRental))
            return "books/BookRental"
        }

        bookRental.id = id
        bookRentalRepository.save(bookRental)

        return "redirect:/"
    }

    @GetMapping("/book-rental/{id}/delete") @PreAuthorize("hasAnyAuthority('admin', 'user')")
    fun deleteBookRental(@PathVariable(name = "id") id: Int): String {
        bookRentalRepository.deleteById(id)
        return "redirect:/"
    }

}