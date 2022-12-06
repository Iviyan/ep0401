package com.iv.ep0401k.models

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import javax.persistence.*
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

@Entity
@Table(name = "countries")
class Country(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id")
    override var id: Int = 0,

    @Column(name = "name")
    @field:NotEmpty @field:Size(min = 2, max = 100, message = "Поле должно содержать от 1 до 100 символов")
    override var name: String = "",

    @OneToMany(mappedBy="country")
    var books: MutableSet<Book>? = null
) : SingleModelBase

@Repository
interface CountriesRepository : CrudRepository<Country, Int>
