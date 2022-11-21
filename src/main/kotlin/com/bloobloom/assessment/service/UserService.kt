package com.bloobloom.assessment.service

import com.bloobloom.assessment.model.User
import com.bloobloom.assessment.model.repository.UserRepository
import com.bloobloom.assessment.security.AuthorizedUser
import com.bloobloom.assessment.security.toSecurityDetails
import com.bloobloom.assessment.service.exceptions.DuplicatedEntityException
import com.bloobloom.assessment.service.exceptions.NoEntityFoundException
import com.bloobloom.assessment.service.exceptions.security.WeakPasswordException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*
import java.util.regex.Pattern

@Service
class UserService : UserDetailsService {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    fun create(user: User): User {
        if (!userRepository.existsByEmail(user.email)) {
            checkThePasswordComplexity(user.password)
            log.info("Adding new user with email: ${user.email}")
            return userRepository.insert(user.copy(password = passwordEncoder.encode(user.password)))
        } else {
            log.error("User with email: ${user.email} is already exists ")
            throw DuplicatedEntityException("User with email: ${user.email} is already exists ")
        }
    }

    fun delete(id: String): User {
        return withUser(id) {
            log.info("Delete user with id: $id")
            userRepository.delete(it)
            return@withUser it
        }
    }

    fun getAll(): List<User> {
        return userRepository.findAll()
    }

    fun getUserByEmail(email: String): User? {
        log.info("Display user info that email: $email")
        return userRepository.findByEmail(email)
    }

    fun getUserById(id: String): User {
        log.info("Display user info that id: $id")
        return userRepository.findById(id).orElseThrow {
            NoEntityFoundException("User with id: $id not found")
        }
    }


    fun getCurrentUser(): User {
        val user =
            userRepository.findByEmail((SecurityContextHolder.getContext().authentication.principal as AuthorizedUser).username)
        if (user != null) {
            return user
        } else throw NoEntityFoundException(
            "User with email: ${(SecurityContextHolder.getContext().authentication.principal as AuthorizedUser).username} is not found"
        )
    }

    fun update(user: User, id: String): User {
        return withUser(id) {
            log.info(
                "Update changes of user that id: $id with new info of: " +
                        "email: ${user.email}, full name: ${user.fullName}, ${user.role}"
            )
            user.id = id
            return@withUser userRepository.save(user)
        }
    }

    private fun withUser(id: String, dataBaseCRUDCommand: (user: User) -> User): User {
        val user: Optional<User> = userRepository.findById(id)
        if (user.isPresent) {
            return dataBaseCRUDCommand(user.get())
        } else {
            log.error("User with id: $id does not exist")
            throw NoEntityFoundException(
                "User with id: $id does not exist"
            )
        }
    }

    override fun loadUserByUsername(email: String): UserDetails {
        val user = userRepository.findByEmail(email)
        if (user == null) {
            log.error("User with email: $email does not exist")
            throw NoEntityFoundException("User with email: $email does not exist")
        } else {
            log.info("Display user that email: $email")
            return user.toSecurityDetails()
        }
    }

    private fun checkThePasswordComplexity(password: String) {
        val containsSpecialChar = Pattern.compile("[!@#$%&*()_+=|<>?{}\\[\\]~-]").matcher(password).find()
        if (password.length < 8 || !containsSpecialChar) {
            log.error("Weak password: '$password' is less than 8 character or it contains special character")
            throw WeakPasswordException(
                "Weak password: '$password' is less than 8 character or it contains special character"
            )
        }
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(UserService::class.java)
    }
}
