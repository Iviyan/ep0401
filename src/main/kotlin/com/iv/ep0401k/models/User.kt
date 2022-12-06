package com.iv.ep0401k.models

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

@Entity
@Table(name = "users")
class User (

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id")
    override var id: Int = 0,

    @Column(name = "login")
    var login: String = "",

    @Column(name = "password")
    var password: String = "",

    @OneToOne()
    @JoinColumn(name = "passport_id", referencedColumnName = "id")
    var passport: Passport = Passport(),

    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    var role: Role = Role(),

    @Column(name = "registration_date")
    var registrationDate: LocalDateTime = LocalDateTime.now(),

    @ManyToMany()
    @JoinTable(
        name = "book_rental",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "book_id")],
    )
    var books: MutableSet<Book>? = null,

    @OneToMany(mappedBy = "user")
    var rental: MutableSet<BookRental>? = null,
) : ModelBase<Int> {
    val fullName: String get() = "${passport.firstName} ${passport.lastName}" + (if (passport.patronymic != null) " ${passport.patronymic}" else "")
}

@Repository
interface UsersRepository : CrudRepository<User, Int> {
    fun findByLogin(s: String?): User?
}

class UserDto(
    var id: Int = 0,
    var registrationDate: LocalDateTime = LocalDateTime.now(),

    @field:NotEmpty(message = "Логин не может быть пустым")
    var login: String = "",
    @field:NotEmpty(message = "Пароль не может быть пустым")
    var password: String = "",
    @field:NotNull
    var role: Role = Role(),

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

    fun toUser() = User(
        id = id,
        login = login,
        password = password,
        role = role,
        registrationDate = registrationDate,
        passport = Passport(
            // id = passportId,
            serial = passportSerial,
            number = passportNumber,
            firstName = firstName,
            lastName = lastName,
            patronymic = patronymic,
        )
    )

    companion object {
        fun from(user: User) = UserDto(
            id = user.id,
            login = user.login,
            password = user.password,
            role = user.role,
            registrationDate = user.registrationDate,
            //passportId = user.passport.id,
            passportSerial = user.passport.serial,
            passportNumber = user.passport.number,
            firstName = user.passport.firstName,
            lastName = user.passport.lastName,
            patronymic = user.passport.patronymic,
        )
    }
}