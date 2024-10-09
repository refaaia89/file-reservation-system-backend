CREATE TABLE `roles` (
     `id` BIGINT NOT NULL AUTO_INCREMENT,
     `name` VARCHAR(50) NOT NULL,
     `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
     `updated_at` TIMESTAMP NULL,
     PRIMARY KEY (`id`),
     constraint roles_name_unique unique (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;


CREATE TABLE `users` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `username`	VARCHAR(100) NOT NULL,
    `email`	VARCHAR(255) NOT NULL,
    `password`	VARCHAR(255) NOT NULL,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NULL,
    `role_id`	BIGINT,
    Primary Key (`id`),
    constraint users_email_unique unique (`email`),
    constraint users_username_unique unique (`username`),
    constraint users_roles_fk foreign key (`role_id`) references `roles`(`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;


CREATE TABLE `groups` (
     `id` BIGINT NOT NULL AUTO_INCREMENT,
     `name`	VARCHAR(50) NOT NULL,
     `description` TEXT,
     `allowed_extension_file_types` VARCHAR(255),
     `is_public` BIT NOT NULL,
     `max_members_count` INT,
     `max_files_count` INT,
     `max_allowed_file_size_in_mb` INT,
     `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
     `updated_at` TIMESTAMP NULL,
     `administrator_id`	BIGINT,
     Primary Key (`id`),
     constraint groups_name_unique unique (`name`),
     constraint groups_users_fk foreign key (`administrator_id`) references `users`(`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;


CREATE TABLE `group_members` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `user_id`	BIGINT NOT NULL,
    `group_id`	BIGINT NOT NULL,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NULL,
    Primary Key (`id`),
    constraint group_members_group_fk foreign key (`group_id`) references `groups`(`id`),
    constraint group_members_users_fk foreign key (`user_id`) references `users`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;


CREATE TABLE `logs` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `type`	ENUM('Response','Request','Exception') NOT NULL,
    `data` longtext NOT NULL,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NULL,
    `user_id`	BIGINT,
    Primary Key (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;


CREATE TABLE `files` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name`	VARCHAR(255) NOT NULL,
    `is_checked_in` BIT NOT NULL DEFAULT FALSE,
    `checked_in_until_time` TIMESTAMP NULL,
    `created_by`  BIGINT NOT NULL,
    `updated_by`  BIGINT NOT NULL,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NULL,
    `version` BIGINT,
    `owner_id`	BIGINT,
    `group_id`	BIGINT NOT NULL,
    Primary Key (`id`),
    constraint files_groups_fk foreign key (`group_id`) references `groups`(`id`) ON DELETE CASCADE ,
    constraint files_users_fk foreign key (`owner_id`) references `users`(`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;

CREATE TABLE `check_processes` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `checked_in_at`  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `checked_out_at`  TIMESTAMP NULL,
    `is_checked_out_at_time` BIT NULL,
    `user_id`	BIGINT,
    `file_id`	BIGINT NOT NULL,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NULL,
    Primary Key (`id`),
    constraint check_processes_files_fk foreign key (`file_id`) references `files`(`id`) ON DELETE CASCADE ,
    constraint check_processes_users_fk foreign key (`user_id`) references `users`(`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;

CREATE TABLE `permissions` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(100) NOT NULL,
    `description` TEXT,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NULL,
    PRIMARY KEY (`id`),
    constraint permissions_name_unique unique (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;


CREATE TABLE `roles_permissions` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `role_id`	BIGINT NOT NULL,
    `permission_id`	BIGINT NOT NULL,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NULL,
    Primary Key (`id`),
    constraint roles_permissions_roles_fk foreign key (`role_id`) references `roles`(`id`) ON DELETE CASCADE ,
    constraint roles_permissions_permissions_fk foreign key (`permission_id`) references `permissions`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;


CREATE TABLE `tokens` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `token`	TEXT NOT NULL,
    `token_type` ENUM('BEARER') NOT NULL,
    `expired` BIT,
    `revoked` BIT,
    `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP NULL,
    `user_id`	BIGINT NOT NULL,
    Primary Key (`id`),
    constraint tokens_users_fk foreign key (`user_id`) references `users`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=UTF8;