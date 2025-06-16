package com.grepp.moodlink_mail.infra.config

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Configuration
class WebMvcConfig : WebMvcConfigurer {
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/favicon.ico").addResourceLocations("classpath:/static/")
            .setCachePeriod(3600 * 24 * 30)
    }

    @Bean
    fun objectMapper(): ObjectMapper {
        val kotlinModule = KotlinModule.Builder()
            .withReflectionCacheSize(512)
            .configure(KotlinFeature.NullToEmptyCollection, false)
            .configure(KotlinFeature.NullToEmptyMap, false)
            .configure(KotlinFeature.NullIsSameAsDefault, false)
            .configure(KotlinFeature.SingletonSupport, false)
            .build()

        val timeModule = JavaTimeModule()
            .addSerializer(CustomLocalDateTimeSerializer())
        return ObjectMapper()
            .registerModules(kotlinModule, timeModule)
            .configure(SerializationFeature.FAIL_ON_UNWRAPPED_TYPE_IDENTIFIERS, false)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    }
}

class CustomLocalDateTimeSerializer : StdSerializer<LocalDateTime>(LocalDateTime::class.java) {
    companion object {
        const val DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss"
    }

    override fun serialize(value: LocalDateTime, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeString(value.format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)))
    }
}