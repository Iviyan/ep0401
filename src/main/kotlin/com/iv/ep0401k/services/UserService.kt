package com.iv.ep0401k.services

import com.iv.ep0401k.models.User
import com.iv.ep0401k.models.UsersRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service


@Service
class UserDetailService : UserDetailsService {

    private lateinit var usersRepository: UsersRepository

    @Autowired
    fun UserService(usersRepository: UsersRepository) {
        this.usersRepository = usersRepository
    }

    override fun loadUserByUsername(username: String?): UserDetails {
        return UserDetailsPrincipal(usersRepository.findByLogin(username)!!)
    }

}

class UserDetailsPrincipal @Autowired constructor(user: User) : UserDetails {
    var user: User

    init {
        this.user = user
    }

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return listOf(SimpleGrantedAuthority(user.role.name))
    }

    override fun getPassword(): String = user.password

    override fun getUsername(): String = user.login

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}
