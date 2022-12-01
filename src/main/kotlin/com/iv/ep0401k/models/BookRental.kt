package com.iv.ep0401k.models

import org.springframework.data.repository.CrudRepository
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.stereotype.Repository
import java.time.LocalDate
import javax.persistence.*

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
    @JoinColumn(name="user_id", nullable=false)
    var user: User? = null
)

@Repository
interface BookRentalRepository : CrudRepository<BookRental, Int> {
    fun findByBookIdIn(bookIds: Collection<Int>) : Iterable<BookRental>
}