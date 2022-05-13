insert into users (obrisan,username,password,email) values (false,'user1','$2a$12$XG8yzdx3RghoK0y2RnMON.gLs1ky7e/R4toD.kSCwIokZWKKdIVSW','test@gamil.com');

--roles

insert into roles (role,deleted) values ('ROLE_ADMIN',false);

--user-roles

INSERT INTO USER_ROLE (user_id, role_id) VALUES (1, 1); 
