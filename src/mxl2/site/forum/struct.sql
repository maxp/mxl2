--
-- mxl2.site.forum
--


-- tags
--
create table forum_tag
(
	id       serial primary key,
	cls      int not null default 0,		    -- tag class
	grp      int not null default 0,		    -- tag group
	capt     varchar(80) not null default '',   -- short tag caption
	title    varchar(400) not null              -- tag title
	
-- ref count ?
);
create index forum_tag_idx_capt on forum_tag( capt );


-- threads
--
create table forum_thr
(
	id       serial primary key,
	state    int not null default 0,		-- 0: draft, 1: published, 2 - closed

	cuser   int not null,					-- thread creator
	muser   int not null,					-- last updateer
		-- references user(id) on update cascade
	ctime    timestamp not null default CURRENT_TIMESTAMP,
	mtime    timestamp not null default CURRENT_TIMESTAMP,

	caddr    varchar(80) not null default '',  -- originator's ip address
	
	ref_id   int not null default 0,		-- parent node reference
	msgnum   int not null default 0,		-- message count
	title    varchar(400) not null default ''
);
create index forum_thr_idx_cuser on forum_thr( cuser );
create index forum_thr_idx_mtime on forum_thr( mtime );
create index forum_thr_idx_ref_id on forum_thr( ref_id );


-- tags-threads xref
--
create table forum_tag_thr
(
	id       serial primary key,
    tag      int not null references forum_tag( id ),
    thr      int not null references forum_thr( id ),
	ord      int not null default 0
);
create index forum_tag_thr_idx_tag on forum_tag_thr( tag );
create index forum_tag_thr_idx_thr on forum_tag_thr( thr );


-- forum messages
--
create table forum_msg
(
	id       serial primary key,
	thr_id   int not null references forum_thr( id ),

    cuser    int not null,          			-- message creator
--	muser    int not null,
	ctime    timestamp not null default CURRENT_TIMESTAMP,
	mtime    timestamp not null default CURRENT_TIMESTAMP,

	caddr    varchar(80) not null default '',	-- originator's ip address
--	maddr    varchar(80) not null default '',

	state    int not null default 0,			-- not used
	
	bin      int not null default 0,			-- number of attached resources
	fmt      int not null default 0,			-- message text format	
	txt      varchar not null default '' 		-- message text
);
create index forum_msg_idx_thr_id on forum_msg( thr_id );


-- forum lastreads
--
create table forum_lrd
(
	id       serial primary key,
	user_id  int not null,
	thr_id   int not null references forum_thr( id ) on update cascade on delete cascade,
	msg_id   int not null references forum_msg( id ) on update cascade on delete cascade
);
create index forum_lrd_idx_user_thr on forum_ldr( user_id, thr_id );


-- binary attaches
--
create table forum_bin
(
	id       serial primary key,
	msg_id   int not null references forum_msg( id ) on update cascade on delete cascade,
	cls      int not null,						-- 0: binary file, 1: gif, 2: jpg, 3: png
	ord      int not null default 0,
	size     int not null,						-- size of binary in bytes
	width    int not null default 0,			-- width of image
	height   int not null default 0,			-- height of image
	data     bytea
);
create index forum_bin_idx_msg_id on forum_bin( msg_id );

-- thread notifications
--
create table forum_nfy
(
	id       serial primary key,
	user_id  int not null,						-- references user(id)
	thr_id   int not null references forum_thr(id) on update cascade on delete cascade,
	cnt      int not null default 0				-- send email once when becomes > 0
);
create index forum_nfy_idx_user on forum_nfy( user_id );
create index forum_nfy_idx_thr  on forum_nfy( thr_id );


--
-- eof