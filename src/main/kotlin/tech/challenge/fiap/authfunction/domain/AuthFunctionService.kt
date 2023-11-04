package tech.challenge.fiap.authfunction.domain

import com.google.firebase.auth.FirebaseAuth
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.function.adapter.gcp.FunctionInvoker
import org.springframework.context.annotation.Bean
import org.springframework.messaging.Message
import org.springframework.messaging.support.MessageBuilder
import org.springframework.stereotype.Component
import java.util.function.Function

@Component
class AuthFunctionService {

    @Autowired
    lateinit var userRepository: ClienteRepository

    @Autowired
    lateinit var firebaseAuth: FirebaseAuth

    @Bean
    fun auth(): Function<AuthRequestDto, Message<*>> {
        return Function {
            val cliente = userRepository.findClienteByCpf(it.cpf) ?: kotlin.run {
                return@Function buildClienteNotFoundResponse()
            }

            val customClaims = mapOf(
                Pair("nome", cliente.nome),
                Pair("cpf", cliente.cpf)
            )

            return@Function firebaseAuth.createCustomToken(cliente.cpf, customClaims).let { token ->
                buildTokenResponse(token)
            }
        }
    }

    private fun buildTokenResponse(token: String): Message<TokenDto> {
        return MessageBuilder.withPayload(
            TokenDto(
                jwt = token
            )
        ).build()
    }

    private fun buildClienteNotFoundResponse(): Message<String> {
        return MessageBuilder
            .withPayload("Cliente não encontrado")
            .setHeader(FunctionInvoker.HTTP_STATUS_CODE, 404)
            .build()
    }
}