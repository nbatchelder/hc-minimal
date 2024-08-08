package com.example.hcminimal

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling
import javax.annotation.PreDestroy
import com.hazelcast.core.HazelcastInstance
import org.springframework.beans.factory.annotation.Autowired

@SpringBootApplication
@EnableScheduling
class HcMinimalApplication {
    @Autowired
    private lateinit var hazelcastInstance: HazelcastInstance

    @PreDestroy
    fun shutdown(){
        hazelcastInstance.shutdown()
    }
}

fun main(args: Array<String>) {
    runApplication<HcMinimalApplication>(*args)
}
