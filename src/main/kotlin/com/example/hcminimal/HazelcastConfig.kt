package com.example.hcminimal

import com.hazelcast.config.Config
import com.hazelcast.core.Hazelcast
import com.hazelcast.core.HazelcastInstance
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import javax.annotation.PostConstruct
import java.io.IOException

@Configuration
class HazelcastConfig {

    private val log: Logger = LoggerFactory.getLogger(HazelcastConfig::class.java)

    @Bean("hazelcastInstance")
    @Throws(IOException::class)
    fun buildHazelcastInstance(environment: Environment): HazelcastInstance {
        System.setProperty("hazelcast.config", "classpath:hazelcast.xml")
        val hazelcastConfig = Config.load()
        val hazelcastInstance = Hazelcast.newHazelcastInstance(hazelcastConfig)
        return hazelcastInstance
    }
}
