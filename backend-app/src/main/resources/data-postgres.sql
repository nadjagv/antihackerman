insert into users (deleted,username,password,email) values (false,'user1','$2a$12$XG8yzdx3RghoK0y2RnMON.gLs1ky7e/R4toD.kSCwIokZWKKdIVSW','test@gmail.com');

insert into users (deleted,username,password,email) values (false,'user2','$2a$12$XG8yzdx3RghoK0y2RnMON.gLs1ky7e/R4toD.kSCwIokZWKKdIVSW','test2@gmail.com');
insert into users (deleted,username,password,email) values (false,'user3','$2a$12$XG8yzdx3RghoK0y2RnMON.gLs1ky7e/R4toD.kSCwIokZWKKdIVSW','test3@gmail.com');

--privileges

insert into privileges(name,deleted) VALUES ('READ',false);

--roles

insert into roles (role,deleted) values ('ROLE_ADMIN',false);
insert into roles (role,deleted) values ('ROLE_OWNER',false);
insert into roles (role,deleted) values ('ROLE_TENANT',false);

--roles-privileges

insert into ROLES_PRIVILEGES (role_id,privilege_id) VALUES (1,1);

--user-roles

INSERT INTO USER_ROLE (user_id, role_id) VALUES (1, 1);
INSERT INTO USER_ROLE (user_id, role_id) VALUES (2, 2);
INSERT INTO USER_ROLE (user_id, role_id) VALUES (2, 3);
INSERT INTO USER_ROLE (user_id, role_id) VALUES (3, 2);

INSERT INTO GROUPS (deleted, name) VALUES (false, 'New York group');
INSERT INTO GROUPS (deleted, name) VALUES (false, 'Dubai group');

INSERT INTO REALESTATES (deleted, location, name, group_id) VALUES (false, 'Boulevard 3', 'Empire State Building', 1);
INSERT INTO REALESTATES (deleted, location, name, group_id) VALUES (false, '5th Avenue, New York', 'Chrysler Building', 1);

INSERT INTO REALESTATES (deleted, location, name, group_id) VALUES (false, 'Main 43, Dubai', 'Burj Khalifa', 2);

INSERT INTO USERS_GROUPS_OWNING(user_id, group_id) VALUES (2, 1);
INSERT INTO USERS_GROUPS_OWNING(user_id, group_id) VALUES (3, 2);

INSERT INTO USERS_REALESTATES_TENANTING(user_id, realestate_id) VALUES (2, 3);

