package com.iv.ep0401k.models

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

@Entity
@Table(name = "clients")
class Client(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id")
    var id: Int = 0,

    @OneToOne()
    @JoinColumns(
        value = [
            JoinColumn(name = "passport_serial", referencedColumnName = "serial"),
            JoinColumn(name = "passport_number", referencedColumnName = "number")
        ]
    )
    var passport: Passport = Passport(),

    @Column(name = "registration_date")
    var registrationDate: LocalDateTime = LocalDateTime.now(),

    @ManyToMany()
    @JoinTable(
        name = "book_rental",
        joinColumns = [JoinColumn(name = "client_id")],
        inverseJoinColumns = [JoinColumn(name = "book_id")],
    )
    var clients: MutableSet<Book>? = null,

    @OneToMany(mappedBy = "client")
    var rental: MutableSet<BookRental>? = null,
) {
    val fullName: String get() = "${passport.firstName} ${passport.lastName}" + (if (passport.patronymic != null) " ${passport.patronymic}" else "")
}

@Repository
interface ClientsRepository : CrudRepository<Client, Int>

class ClientDto(
    var id: Int = 0,
    var registrationDate: LocalDateTime = LocalDateTime.now(),

    @field:NotEmpty(message = "Cерия паспорта не может быть пустой")
    @field:Pattern(regexp = "^\\d{4}$", message = "Серия паспорта должна состоять из 4 цифр")
    var passportSerial: String = "",

    @field:NotEmpty(message = "Номер паспорта не может быть пустым")
    @field:Pattern(regexp = "^\\d{6}$", message = "Номер паспорта должен состоять из 4 цифр")
    var passportNumber: String = "",

    @field:NotEmpty(message = "Имя не может быть пустым")
    @field:Size(max = 30, message = "Имя не может быть длиннее 30 символов")
    var firstName: String = "",

    @field:NotEmpty(message = "Фамилия не может быть пустой")
    @field:Size(max = 30, message = "Фамилия не может быть длиннее 30 символов")
    var lastName: String = "",

    @field:Size(max = 30, message = "Отчество не может быть длиннее 30 символов")
    var patronymic: String? = null,
) {
    val fullName: String get() = "$firstName $lastName" + (if (patronymic != null) " $patronymic" else "")
    val serialAndNumber: String get() = "$passportSerial $passportNumber"

    fun toClient() = Client(
        id = id,
        registrationDate = registrationDate,
        passport = Passport(
            passportId = PassportId(
                serial = passportSerial,
                number = passportNumber,
            ),
            firstName = firstName,
            lastName = lastName,
            patronymic = patronymic,
        )
    )

    companion object {
        fun from(client: Client) = ClientDto(
            id = client.id,
            registrationDate = client.registrationDate,
            passportSerial = client.passport.passportId.serial,
            passportNumber = client.passport.passportId.number,
            firstName = client.passport.firstName,
            lastName = client.passport.lastName,
            patronymic = client.passport.patronymic,

            )
    }
}