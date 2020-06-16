CREATE TABLE WritingGroup
(
    GroupName VARCHAR(25) NOT NULL,
    HeadWriter VARCHAR(50) ,
    YearFormed INTEGER ,
    Subject VARCHAR(30) ,
    CONSTRAINT WritingGroup_pk PRIMARY KEY (GroupName)
);

CREATE TABLE Publisher
(
    PublisherName VARCHAR(25) NOT NULL,
    PublisherAddress VARCHAR(50) ,
    PublisherPhone VARCHAR(14) ,
    PublisherEmail VARCHAR(30) ,
    CONSTRAINT Publisher_pk PRIMARY KEY (PublisherName)
);

CREATE TABLE Book
(
    GroupName VARCHAR(25) NOT NULL,
    BookTitle VARCHAR(50) NOT NULL,
    PublisherName VARCHAR(25) ,
    YearPublished INTEGER ,
    NumberPages INTEGER,
    CONSTRAINT Book_pk PRIMARY KEY (GroupName,BookTitle),
    CONSTRAINT BookGroup_fk FOREIGN KEY (GroupName) REFERENCES WritingGroup(GroupName),
    CONSTRAINT BookPublisher_fk FOREIGN KEY (PublisherName) REFERENCES Publisher (PublisherName) ,
    CONSTRAINT TitleAndPublisherNameUniqueness unique (BookTitle, PublisherName)
);

Insert Into WritingGroup
    (GroupName,HeadWriter,YearFormed,Subject)
Values
    ('Creative Writters', 'J. K. Rowling', 2000, 'Fiction'),
    ('Happy Team', 'Homer Simson', 1995, 'Psychology'),
    ('California Dream', 'John Snow', 2010, 'LocalGuide'),
    ('DreamTravel', 'BearGrills', 2010, 'Travel'),
    ('Data Structures', 'PWeiss', 1999, 'CS'),
    ('Java Code', 'Sawitch', 2003, 'CS');


Insert Into Publisher
    (PublisherName,PublisherAddress,PublisherPhone,PublisherEmail)
Values
    ('WILLEY', 'WILLEY St,91-11-23260877', '55-11-33257660', 'WDT@VSNL.NET'),
    ('WROX', NULL, NULL, 'INFO@WROX.COM'),
    ('TECHMEDIA', NULL, '91-11-33257660', 'BOOKS@TECHMEDIA.COM'),
    ('McGraw-Hill Education', '3860 Taylor Station Rd, Blacklick, OH 43004', '614-755-4151', 'contact@mheducation.com'),
    ('Pearsons', 'Los Angeles, CA 43004', '324-755-4151', 'contact@earsons.com');

Insert Into Book
    (GroupName,BookTitle,PublisherName,YearPublished,NumberPages)
Values
    ('Creative Writters', 'Harry Potter', 'WROX', 1999, 450),
    ('Java Code', 'Java Program', 'McGraw-Hill Education', 2018, 350),
    ('Data Structures', 'C++ Program', 'WROX', 2018, 350),
    ('Data Structures', 'Python coding', 'TECHMEDIA', 2018, 350),
    ('DreamTravel', 'Travel Planet', 'WILLEY', 2018, 350),
    ('DreamTravel', 'Camping Tutorial', 'WILLEY', 2018, 350),
    ('Data Structures', 'JavaDB basics', 'TECHMEDIA', 2018, 350);