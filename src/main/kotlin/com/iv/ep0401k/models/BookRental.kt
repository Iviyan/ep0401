package com.iv.ep0401k.models

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "book_rental")
class BookRental (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id") val id: Int,
    @Column(name = "client_name") val clientName: String,
    @Column(name = "book_id") val bookId: Int,
    @Column(name = "start_date") val startDate: Date,
    @Column(name = "end_date") val endDate: Date,
)

@Repository
interface BookRentalRepository : CrudRepository<BookRental, Int> {
    fun findByBookIdIn(bookIds: Collection<Int>) : Iterable<BookRental>
}