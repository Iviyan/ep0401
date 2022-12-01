package com.iv.ep0401k.controllers

import com.iv.ep0401k.models.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid
import kotlin.jvm.optionals.getOrNull


@Controller
class UsersController {

    @Autowired
    lateinit var usersRepository: UsersRepository

    @Autowired
    lateinit var passportsRepository: PassportsRepository

    @Autowired
    lateinit var rolesRepository: RolesRepository

    @Autowired
    lateinit var booksRepository: BooksRepository

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder


    @GetMapping("/register")
    private fun registration(model: Model): String? {
        model.addAttribute("user", UserDto())
        return "Register"
    }

    @PostMapping("/register")
    private fun registration(@ModelAttribute("user") @Valid user: UserDto, bindingResult: BindingResult, model: Model): String? {
        if (bindingResult.hasErrors()) return "/Register"

        val userFromDb: User? = usersRepository.findByLogin(user.login)
        if (userFromDb != null) {
            bindingResult.addError(FieldError("user", "login", "Пользователь с таким логином уже существует"))
            return "/Register"
        }

        user.role = Role(id = Roles.USER.id)
        user.password = passwordEncoder.encode(user.password)

        val userEntity = user.toUser()
        passportsRepository.save(userEntity.passport)
        usersRepository.save(userEntity)
        return "redirect:/login"
    }


    @GetMapping("/users")
    fun Index(model: Model): Any {
        val users = usersRepository.findAll()
        val usersDto = users.map { UserDto.from(it) }

        model.addAttribute("users", usersDto)

        return "users/Users"
    }

    /*@GetMapping("/books/{id}/users")
    @ResponseBody
    fun getBookUsers(
        @PathVariable(name = "id") id: Int,
    ): Any {
        val users = booksRepository.findById(id).orElseThrow().users
        return users!!.map { UserDto.from(it) }
    }*/

    @OptIn(ExperimentalStdlibApi::class)
    @GetMapping("/users/{id}")
    fun getUser(
        @PathVariable(name = "id") id: Int,
        model: Model,
        response: HttpServletResponse
    ): String {
        val roles = rolesRepository.findAll()
        model.addAttribute("roles", roles)

        val user = usersRepository.findById(id)
        val userDto = if (user.isPresent) Optional.of(UserDto.from(user.get())) else Optional.empty()
        model.addAttribute("model", userDto)
        model.addAttribute("user", userDto.getOrNull())
        if (user.isEmpty) response.status = HttpStatus.NOT_FOUND.value()
        return "users/User"
    }


    @PostMapping("/users/{id}")
    fun editUser(
        @PathVariable(name = "id") id: Int,
        @Valid @ModelAttribute("user") user: UserDto, bindingResult: BindingResult,
        model: Model
    ): String {
        if (bindingResult.hasErrors()) {
            val roles = rolesRepository.findAll()
            model.addAttribute("roles", roles)

            model.addAttribute("model", Optional.of(user))
            return "users/User"
        }

        val userData = usersRepository.findById(id).orElseThrow()

        user.id = id
        val userEntity = user.toUser()
        userEntity.passport.id = userData.passport.id
        passportsRepository.save(userEntity.passport)
        usersRepository.save(userEntity)

        return "redirect:/users"
    }

    @GetMapping("/users/new")
    fun addUser(user: UserDto, model: Model): String {
        val roles = rolesRepository.findAll()
        model.addAttribute("roles", roles)

        model.addAttribute("user", user)
        return "users/NewUser"
    }

    @PostMapping("/users")
    fun addUser(@Valid @ModelAttribute("user") user: UserDto, bindingResult: BindingResult,
                  model: Model): String {
        if (bindingResult.hasErrors()) {
            val roles = rolesRepository.findAll()
            model.addAttribute("roles", roles)

            return "users/NewUser"
        }

        val userEntity = user.toUser()

        passportsRepository.save(userEntity.passport)
        usersRepository.save(userEntity)

        return "redirect:/users"
    }

    @GetMapping("/users/{id}/delete")
    fun deleteUser(@PathVariable(name = "id") id: Int): String {
        usersRepository.deleteById(id)
        return "redirect:/users"
    }
}