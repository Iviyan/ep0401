package com.iv.ep0401k.controllers

import com.iv.ep0401k.models.Book
import com.iv.ep0401k.models.BookRental
import com.iv.ep0401k.models.BookRentalRepository
import com.iv.ep0401k.models.BooksRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.util.*
import javax.servlet.http.HttpServletResponse
import kotlin.jvm.optionals.getOrNull

@Controller
class BooksController {

    @Autowired
    lateinit var booksRepository: BooksRepository

    @Autowired
    lateinit var bookRentalRepository: BookRentalRepository

    @GetMapping("/")
    fun Index(
        model: Model,
        @RequestParam(name = "title", required = false) title: String?
    ): String {
        val books = if (title == null) booksRepository.findAll()
        else booksRepository.findByTitleContainsIgnoreCase(title)

        val bookRental = if (title == null) bookRentalRepository.findAll()
        else bookRentalRepository.findByBookIdIn(books.map { it.id })

        model.addAttribute("books", books)
        model.addAttribute("bookRental", bookRental)

        return "Books"
    }

    @OptIn(ExperimentalStdlibApi::class)
    @GetMapping("/books/{id}")
    fun getBook(
        @PathVariable(name = "id") id: Int,
        model: Model,
        response: HttpServletResponse
    ): String {
        val book = booksRepository.findById(id)
        model.addAttribute("model", book)
        model.addAttribute("book", book.getOrNull())
        if (book.isEmpty) response.status = HttpStatus.NOT_FOUND.value()
        return "Book"
    }

    @GetMapping("/books/new")
    fun addBook(model: Model): String {
        model.addAttribute("book", Book(0, "", "", 0, LocalDate.now(), ""))
        return "NewBook"
    }

    @PostMapping("/books")
    fun addBook(book: Book): String {
        booksRepository.save(book)

        return "redirect:/"
    }

    @PostMapping("/books/{id}")
    fun editBook(
        @PathVariable(name = "id") id: Int,
        @ModelAttribute book: Book
    ): String {
        book.id = id
        booksRepository.save(book)

        return "redirect:/"
    }

    @GetMapping("/books/{id}/delete")
    fun deleteBook(@PathVariable(name = "id") id: Int): String {
        booksRepository.deleteById(id)
        return "redirect:/"
    }

    // ---

    @OptIn(ExperimentalStdlibApi::class)
    @GetMapping("/book-rental/{id}")
    fun getBookRental(
        @PathVariable(name = "id") id: Int,
        model: Model,
        response: HttpServletResponse
    ): String {
        val bookRental = bookRentalRepository.findById(id)
        model.addAttribute("model", bookRental)
        model.addAttribute("bookRental", bookRental.getOrNull())
        if (bookRental.isEmpty) response.status = HttpStatus.NOT_FOUND.value()
        return "BookRental"
    }

    @GetMapping("/book-rental/new")
    fun addBookRental(model: Model): String {
        model.addAttribute("bookRental", BookRental(0, "", 0, LocalDate.now(), LocalDate.now()))
        return "NewBookRental"
    }

    @PostMapping("/book-rental")
    fun addBookRental(bookRental: BookRental): String {
        bookRentalRepository.save(bookRental)

        return "redirect:/"
    }

    @PostMapping("/book-rental/{id}")
    fun editBookRental(
        @PathVariable(name = "id") id: Int,
        @ModelAttribute bookRental: BookRental
    ): String {
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