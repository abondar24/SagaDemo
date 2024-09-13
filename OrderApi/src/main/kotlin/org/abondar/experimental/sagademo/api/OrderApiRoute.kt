package org.abondar.experimental.sagademo.api

import org.abondar.experimental.sagademo.message.OrderCreateRequest
import org.abondar.experimental.sagademo.message.OrderMessage
import org.apache.camel.builder.RouteBuilder
import org.apache.camel.model.rest.RestBindingMode
import org.springframework.stereotype.Component


@Component
class OrderApiRoute : RouteBuilder() {
    override fun configure() {

        restConfiguration()
            .host("localhost")
            .port(8020)
            .enableCORS(true)
            .contextPath("/api")
            .apiContextPath("/api-doc")
            .apiProperty("api.title","Order Saga API")
            .apiProperty("api.version","1")
            .component("servlet")
            .bindingMode(RestBindingMode.auto)

        rest("/order")
            .consumes("application/json")
            .produces("application/json")

            .post()
            .routeId("orderPost")
            .apiDocs(true)
            .type(OrderCreateRequest::class.java)
            .to("jms:queue:startOrderProcessing")

            .delete("/{orderId}")
            .routeId("orderDelete")
            .consumes("application/json")
            .produces("application/json")
            .apiDocs(true)
            .to("jms:queue:cancelOrderProcessing")
    }
}