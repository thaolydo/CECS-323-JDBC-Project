create table WringtingGroups (
    GroupName varchar(50),
    HeadWriter varchar(50),
    YearFormed int,
    Subject varchar(50),
    constraint WringtingGroupPK primary key (GroupName)
);

create table Publishers (
    PublisherName varchar(50),
    PublisherAdress varchar(50),
    PublisherPhone varchar(50),
    PublisherEmail varchar(50),
    constraint PublisherPK primary key (PublisherName)
);

create table Books (
    GroupName varchar(50),
    PublisherName varchar(50),
    BookTitle varchar(50),
    YearPublished varchar(50),
    NumberPages int,
    constraint GroupNameFK foreign key(GroupName) references WringtingGroups(GroupName),
    constraint TitleAndPublisherNameUniqueness unique (BookTitle, PublisherName),
    constraint PublisherNameFK foreign key(PublisherName) references Publishers(PublisherName),
    constraint BookPK primary key(GroupName, BookTitle)
);
