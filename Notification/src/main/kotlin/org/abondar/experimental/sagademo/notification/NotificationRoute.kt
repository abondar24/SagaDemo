package org.abondar.experimental.sagademo.notification

import org.apache.camel.builder.RouteBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class NotificationRoute : RouteBuilder() {

    private val logger: Logger = LoggerFactory.getLogger(NotificationRoute::class.java)

    override fun configure() {
        from("jms:queue:notifyOrderCompletion")
            .routeId("completionRoute")
            .log("Received notification event: \${body}")
            .process { exchange ->
                val payload = exchange.getIn().getBody(String::class.java)
                logger.info("Got notification for order completion: $payload")

            }

        from("jms:queue:notifyOrderCancellation")
            .routeId("cancellationRoute")
            .process { exchange ->
                val payload = exchange.getIn().getBody(String::class.java)
                logger.info("Got notification for order cancellation: $payload")
            }

    }

}