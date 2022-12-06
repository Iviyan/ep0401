package com.iv.ep0401k.models

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import javax.persistence.*
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size


@Entity
@Table(name = "genres")
class Genre(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id")
    override var id: Int = 0,

    @Column(name = "name")
    @field:NotEmpty @field:Size(min = 2, max = 100, message = "Поле должно содержать от 1 до 100 символов")
    override var name: String = "",

    @ManyToMany(mappedBy="genres")
    var books: MutableSet<Book>? = null
) : SingleModelBase

@Repository
interface GenresRepository : CrudRepository<Genre, Int>


/*
@Entity
@Table(name = "book_genres")
class BookGenre (
    @Id
    @ManyToOne
    @JoinColumn(name="book_id", nullable=false)
    var book: Book? = null,

    @Id
    @ManyToOne
    @JoinColumn(name="genre_id", nullable=false)
    var genre: Genre? = null
)

@Repository
interface BookGenresRepository : CrudRepository<BookGenre, Int> {
    fun findByBookIdIn(bookIds: Collection<Int>) : Iterable<BookRental>
}*/
