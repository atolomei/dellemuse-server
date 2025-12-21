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

alter table artwork add column if not exists site_owner_id bigint references  site(id) on delete restrict;
alter table resource add column if not exists size bigint default 0;

alter table artwork add column if not exists url character varying(1024);

alter table artexhibition add column if not exists shortname character varying(1024);
alter table artexhibition add column shortnameKey character varying(512);

alter table artexhibition 			add column if not exists state integer default 1;
alter table artexhibitionguide 		add column if not exists state integer default 1;
alter table artexhibitionitem  		add column if not exists state integer default 1;

alter table artwork              	add column if not exists state integer default 1;
alter table floor                  	add column if not exists state integer default 1;
alter table guidecontent          	add column if not exists state integer default 1;
alter table institution           	add column if not exists state integer default 1;
alter table  institutionalcontent   add column if not exists state integer default 1;

alter table  person                 add column if not exists state integer default 1;
alter table  resource               add column if not exists state integer default 1;
alter table  room                   add column if not exists state integer default 1;
alter table  site                   add column if not exists state integer default 1;
alter table  users     				add column if not exists state integer default 1;
alter table  institutiontype   		add column if not exists state integer default 1;
alter table artexhibitionstatustype add column if not exists state integer default 1;
alter table roomtype  				add column if not exists state integer default 1;

 alter table artexhibitionitem add column  if not exists floorstr character varying (1024);
 alter table artexhibitionitem add column  if not exists roomstr character varying (1024);
 alter table resource add column if not exists durationMilliseconds bigint default 0;
 alter table artwork add column if not exists qrcode bigint references resource(id) on delete restrict;
 alter table resource add column tag character varying(512);
 

 

 
alter table guideContent add column if not exists audioKey character varying(1024);
alter table guideContent add column if not exists videoKey character varying(1024);


alter table artExhibition add column if not exists language character varying(24);
alter table artExhibitionGuide add column if not exists language character varying(24);
alter table artExhibitionItem add column if not exists language character varying(24);
alter table artExhibitionStatusType add column if not exists language character varying(24);
alter table artWork add column if not exists language character varying(24);
alter table artWorkType add column if not exists language character varying(24);
alter table favorite add column if not exists language character varying(24);
alter table floor add column if not exists language character varying(24);
alter table floortype add column if not exists language character varying(24);
alter table guideContent add column if not exists language character varying(24);
alter table institution add column if not exists language character varying(24);
alter table institutionalContent add column if not exists language character varying(24);
alter table institutionType add column if not exists language character varying(24);
alter table person add column if not exists language character varying(24);
alter table resource add column if not exists language character varying(24);
alter table room  add column if not exists language character varying(24);
alter table roomType  add column if not exists language character varying(24);
alter table site add column if not exists language character varying(24);
alter table sitetype add column if not exists language character varying(24);
alter table users add column if not exists language character varying(24);


alter table artExhibition add column if not exists 				draft text;
alter table artExhibitionGuide add column if not exists 		draft text;
alter table artExhibitionItem add column if not exists 			draft text;
alter table artExhibitionStatusType add column if not exists 	draft text;
alter table artWork add column if not exists 					draft text;
alter table artWorkType add column if not exists 				draft text;
alter table favorite add column if not exists 					draft text;
alter table floor add column if not exists 						draft text;
alter table floortype add column if not exists 					draft text;
alter table guideContent add column if not exists 				draft text;
alter table institution add column if not exists 				draft text;
alter table institutionalContent add column if not exists 		draft text;
alter table institutionType add column if not exists 			draft text;
alter table person add column if not exists 					draft text;
alter table resource add column if not exists 					draft text;
alter table site add column if not exists 						draft text;
alter table sitetype add column if not exists 					draft text;
alter table users add column if not exists 						draft text;
alter table room add column if not exists 						draft text;  


alter table artWork alter column draft type text;
alter table artExhibition alter column draft type text;
alter table artexhibitionguide alter column draft type text;
alter table artexhibitionitem alter column draft type text;   
alter table floor alter column draft type text;            
alter table guidecontent alter column draft type text;   
alter table institution alter column draft type text;       
alter table person alter column draft type text;           
alter table site alter column draft type text;  


alter table artExhibitionStatusType alter column draft type text;  
alter table artWorkType alter column draft type text;  
alter table favorite alter column draft type text;  
alter table floor alter column draft type text;  
alter table floortype alter column draft type text;  
alter table institutionalContent alter column draft type text;  
alter table institutionType alter column draft type text;  
alter table resource alter column draft type text;  
alter table sitetype alter column draft type text;  
alter table users alter column draft type text;  


alter table artwork add column masterlanguage character varying(24) default 'es'; alter table artwork add column translation integer default 0;
alter table artexhibition add column masterlanguage character varying(24) default 'es'; alter table artexhibition add column translation integer default 0;
alter table artexhibitionitem add column masterlanguage character varying(24) default 'es'; alter table artexhibitionitem add column translation integer default 0;
alter table guidecontent add column masterlanguage character varying(24) default 'es'; alter table guidecontent add column translation integer default 0;
alter table artexhibitionguide add column masterlanguage character varying(24) default 'es'; alter table artexhibitionguide add column translation integer default 0;
alter table institution add column masterlanguage character varying(24) default 'es'; alter table institution add column translation integer default 0;


alter table artexhibitionitem add column audio bigint references resource(id) on delete restrict;
alter table artexhibitionitem add column audio bigint references resource(id) on delete restrict;
alter table artexhibitionitem add column video bigint references resource(id) on delete restrict;




alter table institutionrecord add column name_hash bigint;
alter table institutionrecord add column subtitle_hash bigint;
alter table institutionrecord add column info_hash bigint;
alter table institutionrecord add column intro_hash bigint;


alter table siterecord add column name_hash bigint;
alter table siterecord add column subtitle_hash bigint;
alter table siterecord add column info_hash bigint;
alter table siterecord add column intro_hash bigint;

alter table artexhibitionrecord add column name_hash bigint;
alter table artexhibitionrecord add column subtitle_hash bigint;
alter table artexhibitionrecord add column info_hash bigint;
alter table artexhibitionrecord add column intro_hash bigint;

 

alter table artexhibitionsectionrecord add column name_hash bigint;
alter table artexhibitionsectionrecord add column subtitle_hash bigint;
alter table artexhibitionsectionrecord add column info_hash bigint;
alter table artexhibitionsectionrecord add column intro_hash bigint;

alter table artexhibitionitemrecord add column name_hash bigint;
alter table artexhibitionitemrecord add column subtitle_hash bigint;
alter table artexhibitionitemrecord add column info_hash bigint;
alter table artexhibitionitemrecord add column intro_hash bigint;

alter table artexhibitionguiderecord add column name_hash bigint;
alter table artexhibitionguiderecord add column subtitle_hash bigint;
alter table artexhibitionguiderecord add column info_hash bigint;
alter table artexhibitionguiderecord add column intro_hash bigint;

alter table guidecontentrecord add column name_hash bigint;
alter table guidecontentrecord add column subtitle_hash bigint;
alter table guidecontentrecord add column info_hash bigint;
alter table guidecontentrecord add column intro_hash bigint;

alter table personrecord add column name_hash bigint;
alter table personrecord add column subtitle_hash bigint;
alter table personrecord add column info_hash bigint;
alter table personrecord add column intro_hash bigint;


alter table artworkrecord add column name_hash bigint;
alter table artworkrecord add column subtitle_hash bigint;
alter table artworkrecord add column info_hash bigint;
alter table artworkrecord add column intro_hash bigint;


alter table artworkRecord alter column name_hash set default 0;
alter table artworkRecord alter column info_hash set default 0;
alter table artworkRecord alter column intro_hash set default 0;
alter table artworkRecord alter column subtitle_hash set default 0;
delete from artworkRecord;


alter table artexhibitionRecord alter column name_hash set default 0;
alter table artexhibitionRecord alter column info_hash set default 0;
alter table artexhibitionRecord alter column intro_hash set default 0;
alter table artexhibitionRecord alter column subtitle_hash set default 0;
delete from artexhibitionRecord;


alter table personRecord alter column name_hash set default 0;
alter table personRecord alter column info_hash set default 0;
alter table personRecord alter column intro_hash set default 0;
alter table personRecord alter column subtitle_hash set default 0;
delete from personRecord;


alter table artexhibitionguideRecord alter column name_hash set default 0;
alter table artexhibitionguideRecord alter column info_hash set default 0;
alter table artexhibitionguideRecord alter column intro_hash set default 0;
alter table artexhibitionguideRecord alter column subtitle_hash set default 0;
delete from artexhibitionguideRecord;


alter table guidecontentRecord alter column name_hash set default 0;
alter table guidecontentRecord alter column info_hash set default 0;
alter table guidecontentRecord alter column intro_hash set default 0;
alter table guidecontentRecord alter column subtitle_hash set default 0;
delete from guidecontentRecord;






alter table institution add column spec text;
alter table site add column spec text;
alter table artexhibitionsection add column spec text;
alter table artexhibitionitem add column spec text; 
alter table artexhibitionguide  add column spec text;
alter table guidecontent add column spec text;
alter table person add column spec text;
alter table artwork add column spec text;
alter table guidecontent add column spec text;



alter table institutionrecord add column spec text;
alter table siterecord add column spec_hash bigint;
alter table artexhibitionsectionrecord add column spec_hash bigint;
alter table artexhibitionitemrecord add column spec_hash bigint; 
alter table artexhibitionguiderecord  add column spec_hash bigint;
alter table guidecontentrecord add column spec_hash bigint;
alter table personrecord add column spec_hash bigint;
alter table artworkrecord add column spec_hash bigint;
alter table guidecontentRecord add column spec_hash bigint;



alter table institutionrecord add column spec_hash bigint default 0;
alter table siterecord add column spec_hash bigint default 0;
alter table artexhibitionsectionrecord add column spec_hash bigint default 0;
alter table artexhibitionitemrecord add column spec_hash bigint default 0; 
alter table artexhibitionguiderecord  add column spec_hash bigint default 0;
alter table guidecontentrecord add column spec_hash bigint default 0;
alter table personrecord add column spec_hash bigint default 0;
alter table artworkrecord add column spec_hash bigint default 0;
alter table guidecontentRecord add column spec_hash bigint default 0;








alter table institutionrecord add column  otherJson text;
alter table siterecord  add column  otherJson text;
alter table artexhibitionsectionrecord  add column  otherJson text;
alter table artexhibitionitemrecord  add column  otherJson text;
alter table artexhibitionguiderecord   add column  otherJson text;
alter table guidecontentrecord  add column  otherJson text;
alter table personrecord  add column  otherJson text;
alter table artworkrecord  add column  otherJson text;
 


alter table institutionrecord add column otherJson_hash bigint default 0;
alter table siterecord add column otherJson_hash bigint default 0;
alter table artexhibitionsectionrecord add column otherJson_hash bigint default 0;
alter table artexhibitionitemrecord add column otherJson_hash bigint default 0; 
alter table artexhibitionguiderecord  add column otherJson_hash bigint default 0;
alter table guidecontentrecord add column otherJson_hash bigint default 0;
alter table personrecord add column otherJson_hash bigint default 0;
alter table artworkrecord add column otherJson_hash bigint default 0;
 


alter table institutionrecord add column  opens text;
alter table siterecord  add column  opens text;
alter table artexhibitionsectionrecord  add column  opens text;
alter table artexhibitionitemrecord  add column  openstext;
alter table artexhibitionguiderecord   add column  opens text;
alter table guidecontentrecord  add column  opens text;
alter table personrecord  add column  opens text;
alter table artworkrecord  add column opens text;

alter table institutionrecord add column opens_hash bigint default 0;
alter table siterecord add column opens_hash bigint default 0;
alter table artexhibitionsectionrecord add column opens_hash bigint default 0;
alter table artexhibitionitemrecord add column opens_hash bigint default 0; 
alter table artexhibitionguiderecord  add column opens_hash bigint default 0;
alter table guidecontentrecord add column opens_hash bigint default 0;
alter table personrecord add column opens_hash bigint default 0;
alter table artworkrecord add column opens_hash bigint default 0;


update institutionrecord set opens_hash = 0;
update siterecord set opens_hash = 0;
update artexhibitionsectionrecord set opens_hash = 0;
update artexhibitionitemrecord set opens_hash = 0; 
update artexhibitionguiderecord  set opens_hash = 0;
update guidecontentrecord set opens_hash = 0;
update personrecord set opens_hash = 0;
update artworkrecord set opens_hash = 0;























COMMIT;


BEGIN;

-- Records FK key have on delete cascade -> when the parent object is deleted, the record is deleted

CREATE TABLE artWorkRecord (
						id				     bigint primary key default nextval('sequence_id'),
						language 			 character varying(24) not null,
						artwork_id bigint    references artwork(id) on delete cascade, 
						name				 character varying(512),
						shortName 			 character varying(64),
						subtitle		 	 character varying(1024),
						spec		 		 text,
						info		 		 text,
						intro		 		 text,
						photo				 bigint references resource(id) on delete restrict,
						video				 bigint references resource(id) on delete restrict,
						audio				 bigint references resource(id) on delete restrict,
						usethumbnail 		 boolean default true,
						created				 timestamp with time zone DEFAULT now() not null,
						lastmodified		 timestamp with time zone DEFAULT now() not null,
						lastmodifieduser	 bigint references users(id) on delete restrict not null,
						draft         		 text
 						);

						
		
CREATE TABLE artExhibitionRecord (
	id						bigint primary key default nextval('sequence_id'),
	artexhibition_id bigint references artexhibition(id) on delete cascade, 
	language 			    character varying(24) not null,
	name                    character varying(1024),
 	subtitle                character varying(1024), 
 	opens                   character varying(2048),
 	info                    text,                 
	photo 				    bigint references resource(id) on delete restrict,
	video 				    bigint references resource(id) on delete restrict,
	audio 				    bigint references resource(id) on delete restrict,
 	created				    timestamp with time zone DEFAULT now() not null,
	lastmodified		    timestamp with time zone DEFAULT now() not null,
	lastmodifieduser	    bigint references users(id) on delete restrict not null,
	draft         		    text
);


CREATE TABLE artExhibitionGuideRecord (
	id					 	 bigint primary key default nextval('sequence_id'),
	artexhibitionguide_id    bigint references artexhibitionguide(id) on delete cascade, 
	language 			     character varying(24) not null,
	name                     character varying(1024),
 	subtitle                 character varying(1024), 
 	info                     text,                 
	photo 				     bigint references resource(id) on delete restrict,
	video 				     bigint references resource(id) on delete restrict,
	audio 				     bigint references resource(id) on delete restrict,
 	created				     timestamp with time zone DEFAULT now() not null,
	lastmodified		     timestamp with time zone DEFAULT now() not null,
	lastmodifieduser	     bigint references users(id) on delete restrict not null,
	draft         		     text
);
						

CREATE TABLE artexhibitionitemRecord  (
	id						   bigint primary key default nextval('sequence_id'),
	artexhibitionitem_id       bigint references artexhibitionitem(id) on delete cascade, 
	language 				   character varying(24) not null,
	name                       character varying(1024),
 	subtitle                   character varying(1024),  
 	opens                      character varying(2048),
 	info                       text,                 
	photo 				       bigint references resource(id) on delete restrict,
	video 				       bigint references resource(id) on delete restrict,
	audio 				       bigint references resource(id) on delete restrict,
 	created					   timestamp with time zone DEFAULT now() not null,
	lastmodified		       timestamp with time zone DEFAULT now() not null,
	lastmodifieduser	       bigint references users(id) on delete restrict not null,
	draft         		       text
);
		
 
CREATE TABLE floorRecord  (
	id					 bigint primary key default nextval('sequence_id'),
	floor_id bigint      references floor(id) on delete cascade, 
	language 			 character varying(24) not null,
    name                 character varying(1024),
	subtitle             character varying(1024),   	 
 	info                 text,                
	photo 				 bigint references resource(id) on delete restrict,
	video 				 bigint references resource(id) on delete restrict,
	audio 				 bigint references resource(id) on delete restrict,
 	created				 timestamp with time zone DEFAULT now() not null,
	lastmodified		 timestamp with time zone DEFAULT now() not null,
	lastmodifieduser	 bigint references users(id) on delete restrict not null,
	draft         		 text
);

  
CREATE TABLE guidecontentRecord   (
	id						 bigint primary key default nextval('sequence_id'),
	guidecontent_id bigint   references guidecontent(id) on delete cascade, 
	language 				 character varying(24) not null,
    name                     character varying(1024),
	subtitle                 character varying(1024),   	 
 	info                     text,                
 	photo 				     bigint references resource(id) on delete restrict,
	video 				     bigint references resource(id) on delete restrict,
	audio 				     bigint references resource(id) on delete restrict,
	created					 timestamp with time zone DEFAULT now() not null,
	lastmodified		     timestamp with time zone DEFAULT now() not null,
	lastmodifieduser	     bigint references users(id) on delete restrict not null,
	draft         		     text
);
 


CREATE TABLE institutionRecord   (
	id						 bigint primary key default nextval('sequence_id'),
	institution_id bigint    references institution(id) on delete cascade, 
	language 				 character varying(24) not null,
    name                     character varying(1024),
	subtitle                 character varying(1024),   	 
 	info                     text,                
 	photo 				      bigint references resource(id) on delete restrict,
	video 				      bigint references resource(id) on delete restrict,
	audio 				      bigint references resource(id) on delete restrict,
 	created					  timestamp with time zone DEFAULT now() not null,
	lastmodified		      timestamp with time zone DEFAULT now() not null,
	lastmodifieduser	      bigint references users(id) on delete restrict not null,
	draft         		      text
);


CREATE TABLE personRecord    (
	id						 bigint primary key default nextval('sequence_id'),
	person_id bigint    	references person(id) on delete cascade, 
	language 				 character varying(24) not null,
    name                     character varying(1024),
	subtitle                 character varying(1024),   	 
 	info                     text,                
  	photo 				     bigint references resource(id) on delete restrict,
	video 				     bigint references resource(id) on delete restrict,
	audio 				     bigint references resource(id) on delete restrict,
	created					 timestamp with time zone DEFAULT now() not null,
	lastmodified		     timestamp with time zone DEFAULT now() not null,
	lastmodifieduser	     bigint references users(id) on delete restrict not null,
	draft         		     text
);


CREATE TABLE roomRecord    (
	id					 	 bigint primary key default nextval('sequence_id'),
	room_id bigint  	   	 references room(id) on delete cascade, 
	language 				 character varying(24) not null,
    name                     character varying(1024),
	subtitle                 character varying(1024),   	 
 	info                     text,                
 	photo 				     bigint references resource(id) on delete restrict,
	video 				     bigint references resource(id) on delete restrict,
	audio 				     bigint references resource(id) on delete restrict,
 	created					 timestamp with time zone DEFAULT now() not null,
	lastmodified		     timestamp with time zone DEFAULT now() not null,
	lastmodifieduser	     bigint references users(id) on delete restrict not null,
	draft         		     text
);


CREATE TABLE siteRecord    (
	id						 bigint primary key default nextval('sequence_id'),
	site_id bigint  	  	 references site(id) on delete cascade, 
	language 				 character varying(24) not null,
    name                     character varying(1024),
	subtitle                 character varying(1024),   	 
 	info                     text,                
 	photo 				     bigint references resource(id) on delete restrict,
	video 				     bigint references resource(id) on delete restrict,
	audio 				     bigint references resource(id) on delete restrict,
 	created					 timestamp with time zone DEFAULT now() not null,
    lastmodified		     timestamp with time zone DEFAULT now() not null,
	lastmodifieduser	     bigint references users(id) on delete restrict not null,
	draft         	    	text
);


CREATE TABLE artExhibitionSection (
	id						 bigint primary key default nextval('sequence_id'),
	artexhibition_id 		bigint   references artexhibition(id) on delete cascade, 
	language 				 character varying(24) not null,
    name                     character varying(1024),
	subtitle                 character varying(1024),   	 
 	info                     text,                
 	state                    integer default 0,     
 	photo 				     bigint references resource(id) on delete restrict,
	video 				     bigint references resource(id) on delete restrict,
	audio 				     bigint references resource(id) on delete restrict,
	masterlanguage            character varying(24) default 'es',
    translation               integer  default 0,
	draft         	    	  text,
	created					 timestamp with time zone DEFAULT now() not null,
    lastmodified		     timestamp with time zone DEFAULT now() not null,
	lastmodifieduser	     bigint references users(id) on delete restrict not null
);


CREATE TABLE artExhibitionSectionRecord (
	id								 bigint primary key default nextval('sequence_id'),
	artExhibitionSection_id bigint 	 references artExhibition(id) on delete cascade, 
	language 				 character varying(24) not null,
    name                     character varying(1024),
	subtitle                 character varying(1024),   	 
 	info                     text,                
 	photo 				     bigint references resource(id) on delete restrict,
	video 				     bigint references resource(id) on delete restrict,
	audio 				     bigint references resource(id) on delete restrict,
 	created					 timestamp with time zone DEFAULT now() not null,
    lastmodified		     timestamp with time zone DEFAULT now() not null,
	lastmodifieduser	     bigint references users(id) on delete restrict not null,
	draft         	    	 text
);



COMMIT;


BEGIN
	
	
CREATE TABLE audiostudio (
						id 						 bigint  primary key default nextval('sequence_id'),
						guideContent_id 		 bigint references guideContent(id) 		  on delete cascade,
						artExhibitionGuide_id 	 bigint references artExhibitionGuide(id)  on delete cascade,
						info 				 text,
						settings    		 jsonb,
						settings_json  		 json,
						audio 				 bigint references resource(id) on delete restrict,
						speechaudio			 bigint references resource(id) on delete restrict,
						created				 timestamp with time zone DEFAULT now() not null,
						lastmodified		 timestamp with time zone DEFAULT now() not null,
						lastmodifieduser	 bigint references users(id) on delete restrict not null
						);
COMMIT;
		



						
	
CREATE TABLE awpe (
	artwork_id		  bigint references artwork(id),
	person_id		  bigint references person(id),
	PRIMARY KEY (artwork_id, person_id)
);




