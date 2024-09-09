CREATE TABLE payment (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      order_id VARCHAR(255) NOT NULL,
                      sum NUMERIC(20,2) NOT NULL,
                      currency VARCHAR(255) NOT NULL ,
                      payment_type VARCHAR(255) NOT NULL
);