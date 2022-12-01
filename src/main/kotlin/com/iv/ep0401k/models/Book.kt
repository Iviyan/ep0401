package com.iv.ep0401k.models

import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.*
import javax.persistence.*
import javax.validation.constraints.*


@Entity
@Table(name = "books")
class Book(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id")
    var id: Int = 0,
    @Column(name = "title")
    @field:NotEmpty(message = "Название не может быть пустым")
    @field:Size(min = 1, max = 200, message = "Название должно содержать от 1 до 200 символов")
    var title: String = "",
    @Column(name = "author")
    @field:NotEmpty(message = "Поле не может быть пустым")
    @field:Size(min = 2, max = 100, message = "Автор должен состоять из 1 до 100 символов")
    var author: String = "",
    @Column(name = "page_count")
    @field:Min(value = 1, message = "Количество страниц должно быть больше 0")
    var pageCount: Int = 0,
    @Column(name = "release_date") @DateTimeFormat(pattern = "dd.MM.yyyy")
    var releaseDate: LocalDate = LocalDate.now(),
    @Column(name = "description")
    var description: String = "",

    @OneToMany(mappedBy="book")
    var rental: MutableSet<BookRental>? = null,

    @ManyToMany()
    @JoinTable(
        name = "book_rental",
        joinColumns = [ JoinColumn(name = "book_id") ],
        inverseJoinColumns = [ JoinColumn(name = "user_id") ],
    )
    var users: MutableSet<User>? = null
)

@Repository
interface BooksRepository : CrudRepository<Book, Int> {

    /*@Query("select * from books where position(lower(:title) in lower(title)) > 0", nativeQuery = true)
    fun findByTitle(@Param("title") title: String): Iterable<Book>*/

    fun findByTitleContainsIgnoreCase(@Param("title") title: String): Iterable<Book>
}
