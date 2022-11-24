package com.iv.ep0401k.models

import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.*
import javax.persistence.*


@Entity
@Table(name = "books")
class Book(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id") var id: Int,
    @Column(name = "title") var title: String,
    @Column(name = "author") var author: String,
    @Column(name = "page_count") var pageCount: Int,
    @Column(name = "release_date") @DateTimeFormat(pattern = "dd.MM.yyyy") var releaseDate: LocalDate,
    @Column(name = "description") var description: String,
)

@Repository
interface BooksRepository : CrudRepository<Book, Int> {

    /*@Query("select * from books where position(lower(:title) in lower(title)) > 0", nativeQuery = true)
    fun findByTitle(@Param("title") title: String): Iterable<Book>*/

    fun findByTitleContainsIgnoreCase(@Param("title") title: String): Iterable<Book>
}
