package com.iv.ep0401k.models

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id


@Entity(name = "t")
class Test(
    @Id @GeneratedValue val Id: Int,
    val Text: String
)

@Repository
interface TestRepository : CrudRepository<Test, Int> {

    //fun findByLastName(lastName: String): Iterable<Customer>
}
