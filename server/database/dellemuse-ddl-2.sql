-- ------------------------------------------------------------------------------------------------------------------------------
--
-- Dellemuse / Allemuse
--
-- Un museo (del latín, musēum y este, a su vez, del griego, Μουσείον, 'santuario de las musas')
-- en italiano «luogo sacro alle Muse»
--
--

-- psql dellemuse

-- dellemuse=# set client_encoding to 'utf8';
-- dellemuse=# \i dellemuse-datos.sql


-- ------------------------------------------------------------------------------------------------------------------------------
BEGIN;
set client_encoding to 'utf8';
COMMIT;


BEGIN;



	
					
/**
CREATE TABLE Role (
						id					 bigint primary key default nextval('sequence_id'),
						name				 character varying(512) not null,
						state 				 integer default 1,
						created				 timestamp with time zone DEFAULT now() not null,
						lastmodified		 timestamp with time zone DEFAULT now() not null,
						lastmodifieduser	 bigint references users(id) on delete restrict not null
					);
*/				



CREATE TABLE user_roles (
						id						 bigint primary key default nextval('sequence_id'),
						user_id					 bigint references users(id) on delete cascade not null,
						general_role_id			 bigint references roleGeneral(id) on delete cascade not null,
						site_role_id			 bigint references roleSite(id) on delete cascade not null,
						institution_role_id		 bigint references roleInstitution(id) on delete cascade not null
						);

						
CREATE TABLE RoleGeneral (
						id					 bigint primary key default nextval('sequence_id'),
						name				 character varying(512) not null,
						state 				 integer default 1,
						created				 timestamp with time zone DEFAULT now() not null,
						lastmodified		 timestamp with time zone DEFAULT now() not null,
						lastmodifieduser	 bigint references users(id) on delete restrict not null
					);

					
						
CREATE TABLE RoleInstitution (
						id					 bigint primary key default nextval('sequence_id'),
						name				 character varying(512) not null,
						state 				 integer default 1,
						institution_id 		 bigint references institution(id) on delete cascade not null,	
						created				 timestamp with time zone DEFAULT now() not null,
						lastmodified		 timestamp with time zone DEFAULT now() not null,
						lastmodifieduser	 bigint references users(id) on delete restrict not null
						);
					
CREATE TABLE RoleSite (
						id					 bigint primary key default nextval('sequence_id'),
						name				 character varying(512) not null,
						state 				 integer default 1,
						site_id 		 	 bigint references site(id) on delete cascade not null,	
						created				 timestamp with time zone DEFAULT now() not null,
						lastmodified		 timestamp with time zone DEFAULT now() not null,
						lastmodifieduser	 bigint references users(id) on delete restrict not null
						);

						
					
					
CREATE TABLE Privilege (
						id					 bigint primary key default nextval('sequence_id'),
						name				 character varying(512) not null,
						created				 timestamp with time zone DEFAULT now() not null,
						lastmodified		 timestamp with time zone DEFAULT now() not null,
						lastmodifieduser	 bigint references users(id) on delete restrict not null
					);
											
CREATE TABLE Roles_privileges (
						id					 bigint primary key default nextval('sequence_id'),
						role_id				 bigint references role(id) on delete cascade not null,
						privilege_id		 bigint references privilege(id) on delete cascade not null
 					);
					
COMMIT;
		

insert into privilege (name, lastmodifieduser) values ('read', (select id from users where username like 'root%' limit 1));
insert into privilege (name, lastmodifieduser) values ('create', (select id from users where username like 'root%' limit 1));
insert into privilege (name, lastmodifieduser) values ('write', (select id from users where username like 'root%' limit 1));
insert into privilege (name, lastmodifieduser) values ('delete', (select id from users where username like 'root%' limit 1));

insert into role (name, lastmodifieduser) values ('ROLE_ADMIN', (select id from users where username like 'root%' limit 1));
insert into role (name, lastmodifieduser) values ('INSTITUTION_ADMIN', (select id from users where username like 'root%' limit 1));
insert into role (name, lastmodifieduser) values ('SITE_ADMIN', (select id from users where username like 'root%' limit 1));
insert into role (name, lastmodifieduser) values ('SITE_EDITOR', (select id from users where username like 'root%' limit 1));














