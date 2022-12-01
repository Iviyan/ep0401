package com.iv.ep0401k.models

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import javax.persistence.*

@Entity
@Table(name = "passports")
class Passport(

    @Column(name = "id") @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0,

    @Column(name = "serial")
    var serial: String = "",

    @Column(name = "number")
    var number: String = "",

    @OneToOne(mappedBy = "passport")
    var user: User? = null,

    @Column(name = "first_name")
    var firstName: String = "",

    @Column(name = "last_name")
    var lastName: String = "",

    @Column(name = "patronymic")
    var patronymic: String? = null,
)

@Repository
interface PassportsRepository : CrudRepository<Passport, Int>