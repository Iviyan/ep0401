package com.iv.ep0401k.models

import org.springframework.data.repository.CrudRepository
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "book_rental")
class BookRental (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id") var id: Int,
    @Column(name = "client_name") var clientName: String,
    @Column(name = "book_id") var bookId: Int,
    @Column(name = "start_date") @DateTimeFormat(pattern = "dd.MM.yyyy") var startDate: LocalDate,
    @Column(name = "end_date") @DateTimeFormat(pattern = "dd.MM.yyyy") var endDate: LocalDate,
)

@Repository
interface BookRentalRepository : CrudRepository<BookRental, Int> {
    fun findByBookIdIn(bookIds: Collection<Int>) : Iterable<BookRental>
}