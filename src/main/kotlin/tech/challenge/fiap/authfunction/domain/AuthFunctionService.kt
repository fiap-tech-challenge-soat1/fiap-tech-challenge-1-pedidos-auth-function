package tech.challenge.fiap.authfunction.domain

import com.google.firebase.auth.FirebaseAuth
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import java.lang.RuntimeException
import java.util.function.Function

@Component
class AuthFunctionService {

    @Autowired
    lateinit var userRepository: ClienteRepository

    @Autowired
    lateinit var firebaseAuth: FirebaseAuth

    @Bean
    fun auth(): Function<AuthRequestDto, TokenDto> {
        return Function {
            val cliente = userRepository.findClienteByCpf(it.cpf) ?: throw RuntimeException("Usuário não encontrado")

            val customClaims = mapOf(
                Pair("id", cliente.id),
                Pair("nome", cliente.nome),
                Pair("email", cliente.email),
                Pair("cpf", cliente.cpf),
                Pair("teste", "123")
            )

            val cpf = it.cpf.filter { c -> c.isDigit() }
            val token = firebaseAuth.createCustomToken(cpf, customClaims)

            return@Function TokenDto(
                jwt = token
            )
        }
    }
}