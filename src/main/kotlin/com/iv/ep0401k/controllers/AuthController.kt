package com.iv.ep0401k.controllers

import com.iv.ep0401k.models.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import javax.validation.Valid

@Controller
class AuthController {

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    lateinit var usersRepository: UsersRepository

    @Autowired
    lateinit var passportsRepository: PassportsRepository

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
}