CREATE TABLE Users(
	user_id [int] IDENTITY(1,1) NOT NULL,
	name [nvarchar](100) NOT NULL,
	contact_no nvarchar(15) NOT NULL,
	alternate_contact_no nvarchar(15),
	gender nvarchar(10) NOT NULL,
	address nvarchar(300) NOT NULL,
	pincode numeric(6,0) NOT NULL,

	CONSTRAINT [PK_User] PRIMARY KEY CLUSTERED 
	(
		[user_id] ASC
	) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY] ;

CREATE TABLE Vendor(
	vendor_id [int] IDENTITY(1,1) NOT NULL,
	name [nvarchar](100) NOT NULL,
	contact_no [numeric](10, 0) NOT NULL,
	alternate_contact_no [numeric](10, 0),
	address nvarchar(300) NOT NULL,
	email_id nvarchar(50) NOT NULL,

	CONSTRAINT [PK_vendor] PRIMARY KEY CLUSTERED 
	(
		[vendor_id] ASC
	) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY] ;


CREATE TABLE Driver(
	driver_id [int] IDENTITY(1,1) NOT NULL,
	driver_name [nvarchar](100) NOT NULL,
	driver_contact_no [numeric](10, 0) NOT NULL,

	CONSTRAINT [PK_Driver] PRIMARY KEY CLUSTERED 
	(
		[driver_id] ASC
	) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY] ;

CREATE TABLE Cab(
	cab_id [int] IDENTITY(1,1) NOT NULL,
	cab_no [nvarchar](8) NOT NULL,
	cab_capacity int NOT NULL,

	CONSTRAINT [PK_Cab] PRIMARY KEY CLUSTERED 
	(
		[cab_id] ASC
	) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY] ;


CREATE TABLE pickup_drop_details(
	pickup_drop_details_id [int] IDENTITY(1,1) NOT NULL,
	user_id [int] NOT NULL,
	travel_date date NOT NULL,
	in_time time(0) NOT NULL,
	out_time time(0) NOT NULL,

	CONSTRAINT [PK_pickup_drop_details] PRIMARY KEY CLUSTERED 
	(
		[pickup_drop_details_id] ASC
	) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY] ;


ALTER TABLE [dbo].[pickup_drop_details] WITH CHECK ADD CONSTRAINT [FK_User_pickup_drop_details] FOREIGN KEY([user_id]) REFERENCES [dbo].[Users] ([user_id])

ALTER TABLE [dbo].[pickup_drop_details] CHECK CONSTRAINT [FK_User_pickup_drop_details]


CREATE TABLE clubbing_details(
	clubbing_details_id [int] IDENTITY(1,1) NOT NULL,
	travel_date date NOT NULL,
	travel_time time(0) NOT NULL,
	user_id [int] NOT NULL,
	cab_id [int] NOT NULL,
	pickup_or_drop varchar(1) NOT NULL,
	CONSTRAINT [PK_clubbing_details] PRIMARY KEY CLUSTERED 
	(
		[clubbing_details_id] ASC
	) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY] ;


ALTER TABLE [dbo].[clubbing_details] WITH CHECK ADD CONSTRAINT [FK_User_clubbing_details] FOREIGN KEY([user_id]) REFERENCES [dbo].[Users] ([user_id])

ALTER TABLE [dbo].[clubbing_details] CHECK CONSTRAINT [FK_User_clubbing_details]

ALTER TABLE [dbo].[clubbing_details] WITH CHECK ADD CONSTRAINT [FK_cab_clubbing_details] FOREIGN KEY([cab_id]) REFERENCES [dbo].[cab] ([cab_id])

ALTER TABLE [dbo].[clubbing_details] CHECK CONSTRAINT [FK_cab_clubbing_details]

CREATE TABLE In_Time(
	In_Time_id [int] IDENTITY(1,1) NOT NULL,
	travel_time time(0) NOT NULL,
	CONSTRAINT [PK_In_Time] PRIMARY KEY CLUSTERED 
	(
		[In_Time_id] ASC
	) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY] ;

CREATE TABLE Out_Time(
	Out_Time_id [int] IDENTITY(1,1) NOT NULL,
	travel_time time(0) NOT NULL,
	CONSTRAINT [PK_Out_Time] PRIMARY KEY CLUSTERED 
	(
		[Out_Time_id] ASC
	) WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY] ;

CREATE TABLE [dbo].[push_notification_details](
	[push_notification_details_id] [int] IDENTITY(1,1) NOT NULL,
	[user_id] [int] NOT NULL,
	[notification_id] [varchar](512) NOT NULL,
 CONSTRAINT [PK_push_notification_details] PRIMARY KEY CLUSTERED 
(
	[push_notification_details_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

ALTER TABLE [dbo].[push_notification_details]  WITH CHECK ADD  CONSTRAINT [FK_User_push_notification_details] FOREIGN KEY([user_id])
REFERENCES [dbo].[Users] ([user_id])

ALTER TABLE [dbo].[push_notification_details] CHECK CONSTRAINT [FK_User_push_notification_details]


insert into In_Time values ('6:00'),('7:00'),('8:30'),('9:30'),('11:00'),('12:00'),('14:00'),('22:30');

insert into Out_Time values ('7:30'),('15:00'),('16:00'),('17:30'),('18:30'),('20:00'),('21:00'),('23:00');