package com.loqui.esy.repository.entity

import com.loqui.esy.utils.KEYWORD_ROLE
import com.loqui.esy.utils.PASSWORD_FOR_CREDENTIALS_CONTAINER
import org.hibernate.Hibernate
import org.springframework.security.core.CredentialsContainer
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "users")
data class User(
    @Id
    @Column(name = "us_id")
    val id: UUID,
    val login: String,
    private var password: String,
    val email: String,
    val role: String,
    val enabled: Boolean = true
) : UserDetails, CredentialsContainer {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as User
        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    override fun toString(): String {
        return this::class.simpleName + "(id = $id , login = $login , password = $password , email = $email , role = $role , enabled = $enabled )"
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return Collections.singletonList(SimpleGrantedAuthority(KEYWORD_ROLE + this.role))
    }

    override fun getPassword(): String {
        return this.password
    }

    override fun getUsername(): String {
        return this.login
    }

    override fun isAccountNonExpired(): Boolean {
        return this.enabled
    }

    override fun isAccountNonLocked(): Boolean {
        return this.enabled
    }

    override fun isCredentialsNonExpired(): Boolean {
        return this.enabled
    }

    override fun isEnabled(): Boolean {
        return this.enabled
    }

    override fun eraseCredentials() {
        this.password = PASSWORD_FOR_CREDENTIALS_CONTAINER
    }
}
