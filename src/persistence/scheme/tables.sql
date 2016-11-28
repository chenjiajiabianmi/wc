
create table resource (
	modifyDate timestamp,
	createDate timestamp,
	originId varchar(64) not null,
	resourceType varchar(64),
	originalLocation varchar(128),
	resourceName varchar(64) not null,
	url varchar(128),
	unique key originid_resourceid (originId, resourceName)
);


create table pageInfo (
	modifyDate timestamp,
	createDate timestamp,
	uniqueId varchar(64) not null,
	title varchar(256),
	url varchar(128),
	unique key page_unique_id (uniqueId)
);

