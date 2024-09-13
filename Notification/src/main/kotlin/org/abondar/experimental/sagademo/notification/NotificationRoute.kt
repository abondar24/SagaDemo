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
            .log("Got notification for order completion: \${header.OrderId}")
            .to("seda:camel")

        from("jms:queue:notifyOrderCancellation")
            .routeId("cancellationRoute")
            .log("Got notification for order cancellation: \${header.OrderId}")
            .to("seda:camel")

    }

}