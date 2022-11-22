package com.iv.ep0401k.controllers

import com.iv.ep0401k.models.Book
import com.iv.ep0401k.models.BookRental
import com.iv.ep0401k.models.BookRentalRepository
import com.iv.ep0401k.models.BooksRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
class BooksController {

    @Autowired
    lateinit var booksRepository: BooksRepository

    @Autowired
    lateinit var bookRentalRepository: BookRentalRepository

    @GetMapping("/books")
    @ResponseBody
    fun getBooks(@RequestParam(name = "title", required = false) title: String?): Iterable<Book> {
        if (title == null) return booksRepository.findAll()
        return booksRepository.findByTitleContainsIgnoreCase(title)
    }

    @GetMapping("/book-rental")
    @ResponseBody
    fun getBookRental(): Iterable<BookRental> {
        return bookRentalRepository.findAll()
    }

    @PostMapping("/books")
    fun addBook(
        @RequestParam title: String,
        @RequestParam author: String,
        @RequestParam pageCount: Int,
        @RequestParam @DateTimeFormat(pattern = "dd.MM.yyyy") releaseDate: Date,
        @RequestParam description: String
    ): Book {
        var newBook = Book(0, title, author, pageCount, releaseDate, description)

        newBook = booksRepository.save(newBook)

        return newBook
    }

    @PostMapping("/book-rental")
    fun addBookRental(
        @RequestParam clientName: String,
        @RequestParam bookId: Int,
        @RequestParam @DateTimeFormat(pattern = "dd.MM.yyyy") startDate: Date,
        @RequestParam @DateTimeFormat(pattern = "dd.MM.yyyy") endDate: Date,
    ): BookRental {
        var newBookRental = BookRental(0, clientName, bookId, startDate, endDate)

        newBookRental = bookRentalRepository.save(newBookRental)

        return newBookRental
    }

    /*@GetMapping("/")
    fun Index(model: Model,
                 @RequestParam(name = "title", required = false) title: String?
    ) : Iterable<Book> {
        val books = if (title == null) booksRepository.findAll()
        else booksRepository.findByTitleContainsIgnoreCase(title)

        val bookRental = if (title == null) bookRentalRepository.findAll()
        else bookRentalRepository.findByBookIdIn(books.map { it.id })

        model.addAttribute("books", books)
        model.addAttribute("bookRental", bookRental)

        return "Books"
    }*/

}