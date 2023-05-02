```
-- url.url_map definition

CREATE TABLE `url_map` (
  `origin` varchar(500) NOT NULL,
  `owner` varchar(255) NOT NULL,
  `created` timestamp NOT NULL,
  `short` varchar(7) NOT NULL,
  PRIMARY KEY (`origin`,`owner`),
  UNIQUE KEY `UK_vos0m9ub0goqj3nh6oqscx9m` (`short`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- url.visit_history definition

CREATE TABLE `visit_history` (
  `id` bigint NOT NULL,
  `browser` varchar(10) DEFAULT NULL,
  `language` varchar(2) DEFAULT NULL,
  `location` varchar(20) DEFAULT NULL,
  `visited` timestamp NOT NULL,
  `short` varchar(7) NOT NULL,
  PRIMARY KEY (`id`,`short`),
  KEY `FKgj2htux7trd6ekrpyce3bn1cy` (`short`),
  CONSTRAINT `FKgj2htux7trd6ekrpyce3bn1cy` FOREIGN KEY (`short`) REFERENCES `url_map` (`short`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
```