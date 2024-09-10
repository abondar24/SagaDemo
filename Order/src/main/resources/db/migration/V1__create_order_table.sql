CREATE TABLE ms_order (
                                 id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                 order_id VARCHAR(255) NOT NULL,
                                 shipping_address VARCHAR(255) NOT NULL,
                                 recipient_name VARCHAR(255) NOT NULL,
                                 recipient_last_name VARCHAR(255) NOT NULL
);