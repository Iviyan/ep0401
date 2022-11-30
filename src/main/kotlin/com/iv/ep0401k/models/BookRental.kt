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

    @Column(name = "start_date") @DateTimeFormat(pattern = "dd.MM.yyyy")
    var startDate: LocalDate = LocalDate.now(),

    @Column(name = "end_date") @DateTimeFormat(pattern = "dd.MM.yyyy")
    var endDate: LocalDate = LocalDate.now(),

    @ManyToOne
    @JoinColumn(name="book_id", nullable=false)
    var book: Book? = null,

    @ManyToOne
    @JoinColumn(name="client_id", nullable=false)
    var client: Client? = null
)

@Repository
interface BookRentalRepository : CrudRepository<BookRental, Int> {
    fun findByBookIdIn(bookIds: Collection<Int>) : Iterable<BookRental>
}