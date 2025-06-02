create database mianoe01_BelmanEksamen collate Danish_Norwegian_CI_AS
go

grant connect on database :: mianoe01_BelmanEksamen to dbo
go

grant view any column encryption key definition, view any column master key definition on database :: mianoe01_BelmanEksamen to [public]
go

create table dbo.[User]
(
    ID        int identity
    constraint ID
    primary key,
    FullName  varchar(200)                       not null,
    Email     nvarchar(200)                      not null
    constraint User_pk
    unique,
    Role      int          default 3             not null,
    Password  varchar(60)  default 'unavailable' not null,
    ImagePath varchar(200) default NULL
    )
    go

create table dbo.Reports
(
    ID         int identity
        constraint Reports_pk
            primary key,
    UserID     int not null
        constraint Reports_User_ID_fk
            references dbo.[User],
    ReportBlob varbinary(max) default NULL
)
    go

create table dbo.Orders
(
    ID          int identity
        constraint Orders_pk
            primary key,
    OrderNumber varchar(15)             not null
        constraint Orders_pk_2
            unique,
    ReportID    int
        constraint Orders_Reports_ID_fk
            references dbo.Reports,
    Approve     bit  default NULL,
    Documented  bit  default 0          not null,
    Size        int  default 1          not null,
    OrderDate   date default '2001-1-1' not null
)
    go

create table dbo.Logs
(
    ID      int identity
        constraint Logs_pk
            primary key,
    UserID  int
        constraint UserID_FK
            references dbo.[User],
    OrderID int
        constraint OrderID_FK
            references dbo.Orders,
    Date    date,
    Action  int not null
)
    go

create table dbo.Pictures
(
    ID              int identity
        constraint Pictures_pk
            primary key,
    Path            varchar(150)  not null,
    UserID          int           not null
        constraint Pictures_User_ID_fk
            references dbo.[User],
    OrderID         int
        constraint Pictures_Orders_ID_fk
            references dbo.Orders,
    Approved        bit default NULL,
    PicturePosition int default 0 not null
)
    go
