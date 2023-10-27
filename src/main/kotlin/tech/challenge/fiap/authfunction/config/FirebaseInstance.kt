package tech.challenge.fiap.authfunction.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class FirebaseInstanceConfiguration {

    @Bean
    fun createFirebaseInstance(): FirebaseAuth {
        val options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.getApplicationDefault())
            .build()

        return FirebaseApp.initializeApp(options).let {
            FirebaseAuth.getInstance(it)
        }
    }
}