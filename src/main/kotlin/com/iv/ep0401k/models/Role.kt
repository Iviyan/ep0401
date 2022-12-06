package com.iv.ep0401k.models

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import javax.persistence.*


@Entity
@Table(name = "roles")
class Role(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id")
    override var id: Int = 0,

    @Column(name = "name")
    var name: String = "",

    @OneToMany(mappedBy="role")
    var users: MutableSet<User>? = null
) : ModelBase<Int>

enum class Roles(val id: Int) {
    ADMIN(1),
    USER(2),
    SALESMAN(3)
}

@Repository
interface RolesRepository : CrudRepository<Role, Int>
