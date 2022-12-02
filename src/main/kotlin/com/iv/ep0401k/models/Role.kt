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
@Table(name = "roles")
class Role(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id")
    var id: Int = 0,

    @Column(name = "name")
    var name: String = "",

    @OneToMany(mappedBy="role")
    var users: MutableSet<User>? = null
)

enum class Roles(val id: Int) {
    ADMIN(1),
    USER(2),
    SALESMAN(3)
}

@Repository
interface RolesRepository : CrudRepository<Role, Int>
