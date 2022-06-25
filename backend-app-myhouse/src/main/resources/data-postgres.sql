insert into users (deleted,username,password,email,wrong_logins) values (false,'user1','$2a$12$XG8yzdx3RghoK0y2RnMON.gLs1ky7e/R4toD.kSCwIokZWKKdIVSW','test@gmail.com',0);

insert into users (deleted,username,password,email,wrong_logins) values (false,'user2','$2a$12$XG8yzdx3RghoK0y2RnMON.gLs1ky7e/R4toD.kSCwIokZWKKdIVSW','test2@gmail.com',0);
insert into users (deleted,username,password,email,wrong_logins) values (false,'user3','$2a$12$XG8yzdx3RghoK0y2RnMON.gLs1ky7e/R4toD.kSCwIokZWKKdIVSW','test3@gmail.com',0);

--privileges

insert into privileges(name,deleted) VALUES ('PKI_ACCESS',false);           --1
insert into privileges(name,deleted) VALUES ('READ_ONE_GROUP',false);       --2
insert into privileges(name,deleted) VALUES ('READ_GROUPS',false);          --3
insert into privileges(name,deleted) VALUES ('CREATE_REALESTATE',false);    --4
insert into privileges(name,deleted) VALUES ('EDIT_REALESTATE',false);      --5
insert into privileges(name,deleted) VALUES ('READ_ONE_USER',false);        --6
insert into privileges(name,deleted) VALUES ('READ_USERS',false);           --7
insert into privileges(name,deleted) VALUES ('REGISTER_USER',false);        --8
insert into privileges(name,deleted) VALUES ('EDIT_USER',false);            --9
insert into privileges(name,deleted) VALUES ('DELETE_USER',false);          --10
insert into privileges(name,deleted) VALUES ('CREATE_CSR',false);           --11
insert into privileges(name,deleted) VALUES ('CREATE_GROUP',false);         --12
insert into privileges(name,deleted) VALUES ('DELETE_REALESTATE',false);    --13

--roles

insert into roles (role,deleted) values ('ROLE_ADMIN',false);
insert into roles (role,deleted) values ('ROLE_OWNER',false);
insert into roles (role,deleted) values ('ROLE_TENANT',false);

--roles-privileges
--admin privileges
insert into ROLES_PRIVILEGES (role_id,privilege_id) VALUES (1,1);
insert into ROLES_PRIVILEGES (role_id,privilege_id) VALUES (1,2);
insert into ROLES_PRIVILEGES (role_id,privilege_id) VALUES (1,3);
insert into ROLES_PRIVILEGES (role_id,privilege_id) VALUES (1,4);
insert into ROLES_PRIVILEGES (role_id,privilege_id) VALUES (1,5);
insert into ROLES_PRIVILEGES (role_id,privilege_id) VALUES (1,6);
insert into ROLES_PRIVILEGES (role_id,privilege_id) VALUES (1,7);
insert into ROLES_PRIVILEGES (role_id,privilege_id) VALUES (1,8);
insert into ROLES_PRIVILEGES (role_id,privilege_id) VALUES (1,9);
insert into ROLES_PRIVILEGES (role_id,privilege_id) VALUES (1,10);
insert into ROLES_PRIVILEGES (role_id,privilege_id) VALUES (1,11);
insert into ROLES_PRIVILEGES (role_id,privilege_id) VALUES (1,12);
insert into ROLES_PRIVILEGES (role_id,privilege_id) VALUES (1,13);

--owner privileges
insert into ROLES_PRIVILEGES (role_id,privilege_id) VALUES (2,2);
insert into ROLES_PRIVILEGES (role_id,privilege_id) VALUES (2,6);
insert into ROLES_PRIVILEGES (role_id,privilege_id) VALUES (2,11);

--tenant privileges
insert into ROLES_PRIVILEGES (role_id,privilege_id) VALUES (3,6);
insert into ROLES_PRIVILEGES (role_id,privilege_id) VALUES (3,11);

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

INSERT INTO USERS_GROUPS_TENANTING(user_id, group_id) VALUES (2, 2);
