package com.iv.ep0401k.models

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate
import javax.persistence.*
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size


@Entity
@Table(name = "selections")
class Selection(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id")
    override var id: Int = 0,

    @Column(name = "creation_date")
    var creationDate: LocalDate = LocalDate.now(),

    @Column(name = "name")
    @field:NotEmpty @field:Size(min = 2, max = 100, message = "Поле должно содержать от 1 до 100 символов")
    var name: String = "",

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "selection_books",
        joinColumns = [ JoinColumn(name = "selection_id") ],
        inverseJoinColumns = [ JoinColumn(name = "book_id") ],
    )

    var books: MutableSet<Book>? = null
) : ModelBase<Int>

@Repository
interface SelectionsRepository : CrudRepository<Selection, Int>
