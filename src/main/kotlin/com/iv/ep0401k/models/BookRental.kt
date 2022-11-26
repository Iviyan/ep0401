package com.iv.ep0401k.models

import org.springframework.data.repository.CrudRepository
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

@Entity
@Table(name = "book_rental")
class BookRental (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id")
    var id: Int = 0,
    @Column(name = "client_name")
    @field:NotEmpty(message = "Название не может быть пустым")
    @field:Size(min = 1, max = 100, message = "Название должно содержать от 1 до 100 символов")
    var clientName: String = "",
    @Column(name = "book_id")
    var bookId: Int = 0,
    @Column(name = "start_date") @DateTimeFormat(pattern = "dd.MM.yyyy")
    var startDate: LocalDate = LocalDate.now(),
    @Column(name = "end_date") @DateTimeFormat(pattern = "dd.MM.yyyy")
    var endDate: LocalDate = LocalDate.now(),
)

@Repository
interface BookRentalRepository : CrudRepository<BookRental, Int> {
    fun findByBookIdIn(bookIds: Collection<Int>) : Iterable<BookRental>
}