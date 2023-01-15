DROP TABLE IF EXISTS `member`;

CREATE TABLE `member`
(
    `id`      varchar(36)  NOT NULL,
    `name`    varchar(100) NOT NULL,
    `address` varchar(300) NOT NULL,
    `contact` varchar(20)  NOT NULL,
    PRIMARY KEY (`id`)
);
INSERT INTO `member`
VALUES ('104ccff3-c584-4782-a582-8a06479b46f6', 'Nuwan Ramindu', 'Galle', '078-1234567'),
       ('2714641a-301e-43d5-9d31-ad916d075ba6', 'Kasun Sampath', 'Galle', '077-1234567'),
       ('2714641a-301e-43d5-9d31-ad916d075ba7', 'Tharindu', 'Panadura', '011-1234567');

DROP TABLE IF EXISTS `book`;
CREATE TABLE `book`
(
    `isbn`   varchar(25)  NOT NULL,
    `title`  varchar(250) NOT NULL,
    `author` varchar(250) NOT NULL,
    `copies` int          NOT NULL DEFAULT '1',
    PRIMARY KEY (`isbn`)
);

INSERT INTO `book`
VALUES ('1234-1234', 'Patterns of Enterprise Application Architecture', 'Martin Fowler', 2),
       ('1234-4567', 'Application Architecture', 'Microsoft', 3),
       ('1234-7891', 'Clean Code', 'Robert Cecil Martin', 4),
       ('1234-9874', 'Test', 'Test', 1),
       ('4567-1234', 'UML Distilled', 'Martin Fowler', 1),
       ('4567-4567', 'SQL Specification 2011', 'Ansi', 1),
       ('4567-7891', 'ECMAScript Specification 2022', 'ECMA Body', 1),
       ('7891-1234', 'Java Language Specification', 'James Gosling', 1),
       ('9874-1234', 'Effective Java 3', 'Prasad Sir', 1);

DROP TABLE IF EXISTS `issue_note`;
CREATE TABLE `issue_note`
(
    `id`        int         NOT NULL AUTO_INCREMENT,
    `date`      date        NOT NULL,
    `member_id` varchar(36) NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `issue_note_ibfk_1` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`)
);
INSERT INTO `issue_note`
VALUES (1, '2022-11-14', '2714641a-301e-43d5-9d31-ad916d075ba7'),
       (3, '2022-11-14', '2714641a-301e-43d5-9d31-ad916d075ba7'),
       (8, '2022-11-15', '2714641a-301e-43d5-9d31-ad916d075ba7'),
       (9, '2022-11-15', '104ccff3-c584-4782-a582-8a06479b46f6'),
       (10, '2022-11-15', '104ccff3-c584-4782-a582-8a06479b46f6');

DROP TABLE IF EXISTS `issue_item`;
CREATE TABLE `issue_item`
(
    `issue_id` int         NOT NULL,
    `isbn`     varchar(25) NOT NULL,
    PRIMARY KEY (`issue_id`, `isbn`),
    CONSTRAINT `issue_item_ibfk_1` FOREIGN KEY (`issue_id`) REFERENCES `issue_note` (`id`),
    CONSTRAINT `issue_item_ibfk_2` FOREIGN KEY (`isbn`) REFERENCES `book` (`isbn`)
);
INSERT INTO `issue_item`
VALUES (3, '1234-1234'),
       (8, '1234-4567'),
       (9, '1234-4567'),
       (1, '1234-7891'),
       (3, '1234-7891'),
       (10, '1234-7891'),
       (8, '1234-9874'),
       (1, '4567-4567'),
       (3, '7891-1234');

DROP TABLE IF EXISTS `return`;

CREATE TABLE `return`
(
    `date`     date        NOT NULL,
    `issue_id` int         NOT NULL,
    `isbn`     varchar(25) NOT NULL,
    PRIMARY KEY (`issue_id`, `isbn`),
    CONSTRAINT `return_ibfk_1` FOREIGN KEY (`issue_id`, `isbn`) REFERENCES `issue_item` (`issue_id`, `isbn`)
);

INSERT INTO `return`
VALUES ('2022-11-14', 1, '4567-4567'),
       ('2022-11-15', 10, '1234-7891');
