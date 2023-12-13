DROP DATABASE IF EXISTS Flight_Database;
CREATE DATABASE IF NOT EXISTS Flight_Database;
FLUSH PRIVILEGES;
USE Flight_Database;

DROP TABLE IF EXISTS AIRCRAFT;
CREATE TABLE AIRCRAFT (
	AircraftID			int not null,
	Name				varchar(225) not null,
	primary key (AircraftID)
);

DROP TABLE IF EXISTS FLIGHT;
CREATE TABLE FLIGHT (
	FlightID			int not null,  		
    Origin				varchar(255) not null,
	Destination			varchar(255) not null,
	Date 				date not null,
	Time 				time not null,
    AircraftID 			int,
	primary key (FlightID), 
	foreign key (AircraftID) references AIRCRAFT(AircraftID)
);

DROP TABLE IF EXISTS SEAT;
CREATE TABLE SEAT(
	SeatID				int not null,
    Col				int not null,
    RowLetter			varchar(225) not null,
    Tier 				varchar(225) not null,
    AircraftID			int,
	primary key (SeatID),
    foreign key (AircraftID) references Aircraft(AircraftID)
);

CREATE TABLE IF NOT EXISTS PAYMENT(
	PaymentID      int             NOT NULL AUTO_INCREMENT,
    CardNumber     VARCHAR(255)    NOT NULL,
    Expiry         date            NOT NULL,
	primary key(PaymentID)
);

DROP TABLE IF EXISTS REGISTERED_USER;
CREATE TABLE REGISTERED_USER(
	MembershipID		int not null,
    FName				varchar(255) not null,
    LName				varchar(255) not null,
    Email				varchar(255) not null,
    Password			varchar(255) not null, 
    CardID				int,
    primary key (MembershipID),
    foreign key (CardID) references Payment(PaymentID)
);    


CREATE TABLE IF NOT EXISTS TICKET(
	TicketID 		int NOT NULL,
    SeatID			int not null,
    FlightID		int not null, 		
	MembershipID	int,
    Price 			int not null, 
    primary key (TicketID),
    foreign key (SeatID) references Seat(SeatID),
	foreign key (FlightID) references Flight(FlightID)
); 

INSERT INTO AIRCRAFT (AircraftID, Name) VALUES
("1","Gulfstream G550"),
("2","Bombardier Global 7500s");

INSERT INTO FLIGHT (FlightID, Origin, Destination, Date, Time, AircraftID) VALUES
("101", "Vancouver, YVR", "Los Angeles, LAX", "2024-05-24", "010:00:00", "1"),
("102", "Los Angeles, LAX", "New York, JFK", "2024-06-11", "011:30:00", "1"),
("103", "Nashville, BNA", "Toronto, YYZ", "2024-07-04", "013:45:00", "2"),
("104", "Toronto, YYZ", "New York, JFK", "2024-08-20", "020:00:00", "2");

INSERT INTO  SEAT (SeatID, Col, RowLetter, Tier, AircraftID) VALUES
("1", "1", "A", "Business", "1"),
("2", "2", "A", "Business", "1"),
("3", "1", "B", "Comfort", "1"),
("4", "2", "B", "Comfort", "1"),
("5", "1", "C", "Ordinary", "1"),
("6", "2", "C", "Ordinary", "1"),
("7", "1", "D", "Ordinary", "1"),
("8", "2", "D", "Ordinary", "1"),
("9", "1", "A", "Business", "2"),
("10", "2", "A", "Business", "2"),
("11", "1", "B", "Comfort", "2"),
("12", "2", "B", "Comfort", "2"),
("13", "1", "C", "Ordinary", "2"),
("14", "2", "C", "Ordinary", "2"),
("15", "1", "D", "Ordinary", "2"),
("16", "2", "D", "Ordinary", "2"); 

INSERT INTO PAYMENT(PaymentID, CardNumber, Expiry) VALUES
("1", "1122 3344 5566 7788", "2025-01-01"); 

INSERT INTO REGISTERED_USER(MembershipID, FName, LName, Email, Password, CardID) VALUES
("1001", "Jane", "Doe", "jane.doe@gmail.com", "password123", "1");
     


    
    