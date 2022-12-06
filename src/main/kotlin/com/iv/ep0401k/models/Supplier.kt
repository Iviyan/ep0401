package com.iv.ep0401k.models

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import javax.persistence.*
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size


@Entity
@Table(name = "suppliers")
class Supplier(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id")
    override var id: Int = 0,

    @Column(name = "inn")
    @field:Pattern(regexp = "^\\d{10}$|^\\d{12}$", message = "ИНН должен состоять из 10 или 12 цифр")
    var inn: String = "",

    @Column(name = "name")
    @field:NotEmpty @field:Size(min = 2, max = 100, message = "Поле должно содержать от 1 до 100 символов")
    var name: String = "",

    @OneToMany(mappedBy="supplier")
    var shipments: MutableSet<Shipment>? = null
) : ModelBase<Int>

@Repository
interface SuppliersRepository : CrudRepository<Supplier, Int>
