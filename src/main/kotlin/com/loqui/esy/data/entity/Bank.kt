package com.loqui.esy.data.entity

import org.hibernate.Hibernate
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "bank")
data class Bank(
    @Id
    @Column(name = "ba_id")
    val id: UUID,
    var name: String,
    var description: String,
    var main: Boolean,
    @ManyToOne
    @JoinColumn(name = "us_id")
    val user: User
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Bank
        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    override fun toString(): String {
        return this::class.simpleName + "(id = $id , name = $name , description = $description , main = $main , user = ${user.id} )"
    }
}
