CREATE TABLE IF NOT EXISTS `user` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  first_name varchar(255) NOT NULL,
  last_name varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255),
  `role_id` int,
  `subscriber` boolean DEFAULT 0
);
Create Index play_available_date_index On play(available_date Desc);
CREATE TABLE IF NOT EXISTS `role` (
  `role_id` int PRIMARY KEY AUTO_INCREMENT,
  `role_name` varchar(55) DEFAULT "user"
);

CREATE TABLE IF NOT EXISTS `play` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(255),
  `available_date` timestamp NULL,
  `play_date` timestamp NULL,
  `registered_date` timestamp DEFAULT CURRENT_TIMESTAMP(),
  `link` varchar(255),
  `nr_tickets` int DEFAULT 0,
  `image_url` varchar(255),
  INDEX(`available_date` ASC)
);

CREATE TABLE IF NOT EXISTS `ticket` (
  `id` int PRIMARY KEY AUTO_INCREMENT,
  `user_id` int,
  `play_id` int,
  `status` varchar(55) DEFAULT "free",
  `book_date` timestamp NULL,
  `pickup_date` timestamp NULL
);

ALTER TABLE `user` ADD FOREIGN KEY (`role_id`) REFERENCES `role` (`role_id`);

ALTER TABLE `ticket` ADD FOREIGN KEY (`user_id`) REFERENCES `user` (`id`);

ALTER TABLE `ticket` ADD FOREIGN KEY (`play_id`) REFERENCES `play` (`id`);


