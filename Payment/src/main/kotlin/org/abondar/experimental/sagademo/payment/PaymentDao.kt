package org.abondar.experimental.sagademo.payment

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface PaymentDao : JpaRepository<Payment,Long>{

  @Modifying(clearAutomatically = true)
  fun deleteByOrderId(@Param("orderId") orderId: String)

}