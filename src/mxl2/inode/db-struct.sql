

create schema inode;

-- SET search_path TO foo;
-- ALTER USER test SET search_path TO bar,foo;


-- GRANT USAGE ON SCHEMA tarzan TO jane;
-- REVOKE USAGE ON SCHEMA tarzan TO jane;


create table icls
(
	id       int not null primary key,
--	parent   int not null references itype(id),

	prim     int not null,   -- primitive types: 1: inode, 2: iref, 3: vint, 4: vtime, 5: vstr, 6: vbin, 7: vimg   
	name     varchar(400) not null,
	descr    varchar(4000)
);
create index icls_name_idx on icls(name);

create sequence inode_id_seq;
create sequence ivar_id_seq;

create table inode
(
	id	     int not null primary key,
	cls      int not null references icls(id),
	parent   int not null references inode(id),
	ord      int not null default 0,
	ctime    timestamp not null default CURRENT_TIMESTAMP,
	mtime    timestamp not null default CURRENT_TIMESTAMP,
	publ     boolean not null default 'f'
);
create index inode_cls_idx on inode(cls);
create index inode_parent_idx on inode(parent);
create index inode_mtime_idx on inode(mtime);

create table iref
(
	id       int not null primary key,
    cls      int not null references icls(id),
	src      int not null references inode(id),
	dst      int not null references inode(id),
 	ord      int not null default 0    
);
create index iref_cls_idx on iref(cls);
create index iref_src_idx on iref(src);
create index iref_dst_idx on iref(dst);

create table ivint
(
	id       int not null primary key,
	node     int not null references inode(id),
    cls      int not null references icls(id),
 	ord      int not null default 0,    
 	val      int not null    
);
create index ivnit_node_idx on ivint(node);

create table ivstr
(
	id       int not null primary key,
	node     int not null references inode(id),
    cls      int not null references icls(id),
 	ord      int not null default 0,
 	val      varchar not null    
);
create index ivstr_node_idx on ivstr(node);

create table ivimg
(
	id       int not null primary key,
	node     int not null references inode(id),
    cls      int not null references icls(id),
 	ord      int not null default 0,
	width    int not null,
    height   int not null,
    fmt      int not null default 0,              -- 0 - jpeg, 1 - png, 2 - gif
 	img      bytea not null
);
create index ivbin_node_idx on ivbin(node);

create table ivbin
(
	id       int not null primary key,
	node     int not null references inode(id),
    cls      int not null references icls(id),
 	ord      int not null default 0,    
 	val      bytea not null
);
create index ivbin_node_idx on ivbin(node);

create table ivtime
(
	id       int not null primary key,
	node     int not null references inode(id),
    cls      int not null references icls(id),
 	ord      int not null default 0,    
 	vtime    timestamp not null default CURRENT_TIMESTAMP
);
create index ivtime_node_idx  on ivtime(node);
create index ivtime_vtime_idx on ivtime(vtime);



-- data --

insert into icls ( id, prim, name, descr ) values ( 0,  1, 'inode root type', '' );

insert into inode( id, cls, parent ) values ( 0, 0, 0 );  -- root Inode

insert into icls ( id, prim, name, descr ) values ( 1,  1, 'inode type1', '' );
insert into icls ( id, prim, name, descr ) values ( 2,  1, 'inode type2', '' );
insert into icls ( id, prim, name, descr ) values ( 3,  1, 'inode type3', '' );
insert into icls ( id, prim, name, descr ) values ( 4,  1, 'inode type4', '' );

insert into icls ( id, prim, name, descr ) values ( 5,  2, 'ref type1', '' );
insert into icls ( id, prim, name, descr ) values ( 6,  2, 'ref type2', '' );
insert into icls ( id, prim, name, descr ) values ( 7,  2, 'ref type3', '' );

insert into icls ( id, prim, name, descr ) values ( 8,  3, 'int var1', '' );
insert into icls ( id, prim, name, descr ) values ( 9,  3, 'int var2', '' );
insert into icls ( id, prim, name, descr ) values ( 10, 3, 'int var3', '' );

insert into icls ( id, prim, name, descr ) values ( 11, 4, 'time var1', '' );
insert into icls ( id, prim, name, descr ) values ( 12, 4, 'time var2', '' );
insert into icls ( id, prim, name, descr ) values ( 13, 4, 'time var3', '' );

insert into icls ( id, prim, name, descr ) values ( 14, 5, 'str var1', '' );
insert into icls ( id, prim, name, descr ) values ( 15, 5, 'str var2', '' );
insert into icls ( id, prim, name, descr ) values ( 16, 5, 'str var3', '' );



-- 
-- eof
