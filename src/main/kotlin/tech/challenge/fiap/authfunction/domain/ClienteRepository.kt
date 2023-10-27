package tech.challenge.fiap.authfunction.domain

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.RowMapper
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class ClienteRepository {

    @Autowired
    lateinit var queryTemplate: NamedParameterJdbcTemplate

    fun findClienteByCpf(cpf: String): ClienteDto? {
        val query = """
            select * from cliente c where c.cpf = :cpf;
        """

        val parameters = MapSqlParameterSource().also {
            it.addValue("cpf", cpf)
        }

        return queryTemplate.queryForObject(query, parameters, RowMapper { rs, _ ->
            ClienteDto(
                id = rs.getString("id"),
                nome = rs.getString("nome"),
                email = rs.getString("email"),
                cpf = rs.getString("cpf")
            )
        })
    }

}