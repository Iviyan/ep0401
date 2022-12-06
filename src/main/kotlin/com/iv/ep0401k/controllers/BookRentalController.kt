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
@PreAuthorize("hasAnyAuthority('admin', 'user')")
class BookRentalController {

    @Autowired
    lateinit var booksRepository: BooksRepository

    @Autowired
    lateinit var usersRepository: UsersRepository

    @Autowired
    lateinit var bookRentalRepository: BookRentalRepository

    @OptIn(ExperimentalStdlibApi::class)
    @GetMapping("/book-rental/{id}")
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

    @GetMapping("/book-rental/new")
    fun addBookRental(bookRental: BookRental, model: Model): String {
        val books = booksRepository.findAll()
        val users = usersRepository.findAll()
        model.addAttribute("books", books)
        model.addAttribute("users", users.map { UserDto.from(it) })

        return "books/NewBookRental"
    }

    @PostMapping("/book-rental")
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

    @PostMapping("/book-rental/{id}")
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

    @GetMapping("/book-rental/{id}/delete")
    fun deleteBookRental(@PathVariable(name = "id") id: Int): String {
        bookRentalRepository.deleteById(id)
        return "redirect:/"
    }

}