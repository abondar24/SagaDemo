package org.abondar.experimental.sagademo.inventory

import org.abondar.experimental.sagademo.inventory.dao.ItemDao
import org.abondar.experimental.sagademo.inventory.dao.OrderInventoryDao
import org.abondar.experimental.sagademo.inventory.data.Item
import org.abondar.experimental.sagademo.inventory.data.OrderInventory
import org.abondar.experimental.sagademo.inventory.processor.InventoryCancelProcessor
import org.abondar.experimental.sagademo.inventory.processor.InventoryProcessor
import org.abondar.experimental.sagademo.message.InventoryMessage
import org.abondar.experimental.sagademo.message.ItemMessage
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory
import org.apache.camel.RoutesBuilder
import org.apache.camel.builder.AdviceWith
import org.apache.camel.component.jms.JmsComponent
import org.apache.camel.impl.DefaultCamelContext
import org.apache.camel.test.junit5.CamelTestSupport
import org.apache.camel.test.spring.junit5.CamelSpringBootTest
import org.apache.camel.test.spring.junit5.EnableRouteCoverage
import org.apache.camel.test.spring.junit5.MockEndpoints
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyList
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*

@MockEndpoints
@CamelSpringBootTest
@EnableRouteCoverage
class InventoryRouteTest : CamelTestSupport() {

    @Mock
    lateinit var itemDao: ItemDao

    @Mock
    lateinit var orderInventoryDao: OrderInventoryDao

    @InjectMocks
    lateinit var inventoryProcessor: InventoryProcessor

    @InjectMocks
    lateinit var inventoryCancelProcessor: InventoryCancelProcessor

    override fun createRouteBuilder(): RoutesBuilder {
        return InventoryRoute(inventoryProcessor, inventoryCancelProcessor)
    }

    override fun createCamelContext(): DefaultCamelContext {
        val context = DefaultCamelContext()

        val connectionFactory = ActiveMQConnectionFactory("vm://localhost?broker.persistent=false")
        val activeMQComponent = JmsComponent()
        activeMQComponent.connectionFactory = connectionFactory

        context.addComponent("jms", activeMQComponent)

        return context
    }

    @Test
    fun `test process inventory route`() {
        val inventoryMessage = InventoryMessage(listOf(ItemMessage("test", 1)))
        val item = Item(itemId = "test", name = "test", quantity = 1)
        val orderInventory = OrderInventory(orderId = "test", items =  listOf(item))

        AdviceWith.adviceWith(context, "inventoryRoute") {
            it.replaceFromWith("direct:inventoryRoute")
        }

        `when`(itemDao.findByItemIdIn(anyList())).thenReturn(listOf(item))
        `when`(orderInventoryDao.save(any(OrderInventory::class.java))).thenReturn(orderInventory)
        doNothing().`when`(itemDao).updateInventory("test",orderInventory)
        doNothing().`when`(itemDao).updateQuantity("test",1)


        template.sendBodyAndHeaders("direct:inventoryRoute",null, mapOf(
            "InventoryMessage" to inventoryMessage,
            "OrderId" to "test",
        ))

        verify(itemDao, times(1)).findByItemIdIn(anyList())
        verify(itemDao, times(1)).updateInventory("test",orderInventory)
        verify(itemDao, times(1)).updateQuantity("test",1)
        verify(orderInventoryDao, times(1)).save(any(OrderInventory::class.java))


    }

    @Test
    fun `test revert inventory route`() {
        val inventoryMessage = InventoryMessage(listOf(ItemMessage("test", 1)))


        AdviceWith.adviceWith(context, "revertInventoryRoute") {
            it.replaceFromWith("direct:revertInventory")


        }

        doNothing().`when`(itemDao).updateInventory("test",null)
        doNothing().`when`(itemDao).updateQuantity("test",1)
        doNothing().`when`(orderInventoryDao).deleteByOrderId("test")

        template.sendBodyAndHeaders("direct:revertInventory",null,mapOf(
            "InventoryMessage" to inventoryMessage,
            "OrderId" to "test",
        ))

        verify(itemDao, times(1)).updateInventory("test",null)
        verify(itemDao, times(1)).updateQuantity("test",1)
        verify(orderInventoryDao, times(1)).deleteByOrderId("test")


    }

}