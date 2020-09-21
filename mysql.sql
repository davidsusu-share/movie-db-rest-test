CREATE TABLE `search_event` (
    `search_event_id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `api_name` VARCHAR(100) NOT NULL,
    `search_term` VARCHAR(100) NOT NULL,
    `served_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX `api_name` (`api_name`),
    INDEX `search_term` (`search_term`),
    INDEX `served_at` (`served_at`)
) ENGINE=InnoDB;
