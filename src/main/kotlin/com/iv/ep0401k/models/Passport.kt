package com.iv.ep0401k.models

import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.io.Serializable
import javax.persistence.*
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

const val PassportSerialColumn = "serial"
const val PassportNumberColumn = "number"

@Embeddable
class PassportId (
    @Column(name = PassportSerialColumn)
    var serial: String = "",

    @Column(name = PassportNumberColumn)
    var number: String = "",
) : Serializable

@Entity
@Table(name = "passports")
class Passport(

    @EmbeddedId
    var passportId: PassportId = PassportId(),

    @OneToOne(mappedBy = "passport")
    var client: Client? = null,

    @Column(name = "first_name")
    var firstName: String = "",

    @Column(name = "last_name")
    var lastName: String = "",

    @Column(name = "patronymic")
    var patronymic: String? = null,
)

@Repository
interface PassportsRepository : CrudRepository<Passport, Int> {
    @Modifying @Transactional
    @Query("update passports set $PassportSerialColumn = :new_serial, $PassportNumberColumn = :new_number where $PassportSerialColumn = :old_serial and $PassportNumberColumn = :old_number", nativeQuery = true)
    fun updatePassport(
        @Param("old_serial") oldSerial: String,
        @Param("old_number") oldNumber: String,
        @Param("new_serial") newSerial: String,
        @Param("new_number") newNumber: String,
    ): Int
}