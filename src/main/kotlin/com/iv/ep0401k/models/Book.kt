package com.iv.ep0401k.models

import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*
import javax.persistence.*


@Entity @Table(name = "books")
class Book (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id") val id: Int,
    @Column(name = "title") val title: String,
    @Column(name = "author") val author: String,
    @Column(name = "page_count") val pageCount: Int,
    @Column(name = "release_date") val releaseDate: Date,
    @Column(name = "description") val description: String,
)

@Repository
interface BooksRepository : CrudRepository<Book, Int> {

    /*@Query("select * from books where position(lower(:title) in lower(title)) > 0", nativeQuery = true)
    fun findByTitle(@Param("title") title: String): Iterable<Book>*/

    fun findByTitleContainsIgnoreCase(@Param("title") title: String): Iterable<Book>
}
