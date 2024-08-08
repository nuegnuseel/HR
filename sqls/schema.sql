CREATE TABLE `User`
(
    `User_ID` VARCHAR(20) NOT NULL,
    `user`    VARCHAR(20) NULL,
    `rank`    VARCHAR(20) NULL
);

CREATE TABLE `Dept`
(
    `Dept_ID` VARCHAR(20) NOT NULL,
    `dept`    VARCHAR(20) NULL
);

CREATE TABLE `Attend`
(
    `Attend_No` VARCHAR(20) NOT NULL,
    `status`    VARCHAR(20) NULL
);

CREATE TABLE `CheckAttend`
(
    `Check_Atd` VARCHAR(20) NOT NULL,
    `Attend_No` VARCHAR(20) NOT NULL,
    `User_ID`   VARCHAR(20) NOT NULL,
    `date`      VARCHAR(20) NULL
);

CREATE TABLE `CheckDept`
(
    `Dept_User_ID` VARCHAR(20) NOT NULL,
    `User_ID2`     VARCHAR(20) NOT NULL,
    `Dept_ID`      VARCHAR(20) NOT NULL
);

ALTER TABLE `User`
    ADD CONSTRAINT `PK_USER` PRIMARY KEY (
                                          `User_ID`
        );

ALTER TABLE `Dept`
    ADD CONSTRAINT `PK_DEPT` PRIMARY KEY (
                                          `Dept_ID`
        );

ALTER TABLE `Attend`
    ADD CONSTRAINT `PK_ATTEND` PRIMARY KEY (
                                            `Attend_No`
        );

ALTER TABLE `CheckAttend`
    ADD CONSTRAINT `PK_CHECKATTEND` PRIMARY KEY (
                                                 `Check_Atd`,
                                                 `Attend_No`,
                                                 `User_ID`
        );

ALTER TABLE `CheckDept`
    ADD CONSTRAINT `PK_CHECKDEPT` PRIMARY KEY (
                                               `Dept_User_ID`,
                                               `User_ID2`,
                                               `Dept_ID`
        );

ALTER TABLE `CheckAttend`
    ADD CONSTRAINT `FK_Attend_TO_CheckAttend_1` FOREIGN KEY (
                                                             `Attend_No`
        )
        REFERENCES `Attend` (
                             `Attend_No`
            );

ALTER TABLE `CheckAttend`
    ADD CONSTRAINT `FK_User_TO_CheckAttend_1` FOREIGN KEY (
                                                           `User_ID`
        )
        REFERENCES `User` (
                           `User_ID`
            );

ALTER TABLE `CheckDept`
    ADD CONSTRAINT `FK_User_TO_CheckDept_1` FOREIGN KEY (
                                                         `User_ID2`
        )
        REFERENCES `User` (
                           `User_ID`
            );

ALTER TABLE `CheckDept`
    ADD CONSTRAINT `FK_Dept_TO_CheckDept_1` FOREIGN KEY (
                                                         `Dept_ID`
        )
        REFERENCES `Dept` (
                           `Dept_ID`
            );

