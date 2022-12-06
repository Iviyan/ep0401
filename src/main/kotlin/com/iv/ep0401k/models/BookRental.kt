package com.iv.ep0401k.models

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "book_rental")
class BookRental (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id")
    override var id: Int = 0,

    @Column(name = "start_date")
    var startDate: LocalDate = LocalDate.now(),

    @Column(name = "end_date")
    var endDate: LocalDate = LocalDate.now(),

    @ManyToOne
    @JoinColumn(name="book_id", nullable=false)
    var book: Book? = null,

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    var user: User? = null
) : ModelBase<Int>

@Repository
interface BookRentalRepository : CrudRepository<BookRental, Int> {
    fun findByBookIdIn(bookIds: Collection<Int>) : Iterable<BookRental>
}