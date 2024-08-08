package com.example.hcminimal

import org.slf4j.LoggerFactory
import com.hazelcast.core.HazelcastInstance
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import com.hazelcast.collection.IQueue
import javax.annotation.PreDestroy

@RestController
class InfoController(private val hazelcastInstance: HazelcastInstance) {

    private val logger = LoggerFactory.getLogger(InfoController::class.java)
    private lateinit var testq: IQueue<String>

    @GetMapping("/info")
    fun getInfo(): String {
        hazelcastInstance.cluster.members.map {
            logger.info("Found member: ${it.address.host}")
        }
        return "OK"
    }

    @Scheduled(fixedRate = 1000)
    private fun takeFromIQueue() {
        testq = hazelcastInstance.getQueue("testq")
        val testqVal: String? = testq.take() // Using this line causes the error
        // val testqVal: String? = testq.poll() // Using this line works
    }
}
