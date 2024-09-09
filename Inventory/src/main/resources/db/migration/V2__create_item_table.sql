CREATE TABLE item (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      item_id VARCHAR(255) NOT NULL,
                      name VARCHAR(255) NOT NULL,
                      quantity INT NOT NULL,
                      order_inventory_id INT,
                      CONSTRAINT fk_order_inventory
                          FOREIGN KEY (order_inventory_id)
                              REFERENCES order_inventory(id)
);