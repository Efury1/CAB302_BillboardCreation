CREATE TABLE users (
    username VARCHAR(40) PRIMARY KEY NOT NULL, 
    password_hash VARCHAR(255) NOT NULL, 
    password_salt VARCHAR(255) NOT NULL, 
    perm_create BOOLEAN NOT NULL, 
    perm_edit_all_billboards BOOLEAN NOT NULL, 
    perm_edit_users BOOLEAN NOT NULL, 
    perm_schedule BOOLEAN NOT NULL);

CREATE TABLE billboards (
    --  Unique ID
    billboard_ID INT PRIMARY KEY NOT NULL AUTO_INCREMENT, 
    --  Name given to the billboard
    billboard_name VARCHAR(255) DEFAULT "Unnamed Billboard", 
    --  Main text of the billboard
    billboard_message VARCHAR(255),
    --  Body text of billboard
    billboard_info TEXT,
    picture MEDIUMBLOB);

CREATE TABLE schedules (
    schedule_ID INT PRIMARY KEY NOT NULL AUTO_INCREMENT, 
    schedule_ts TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    --  The date that the billboard schedule initialises on
    start_date DATE NOT NULL, 
    --  The ending date of the schedule - identical to the start_date for one-off billboards
    end_date DATE NOT NULL, 
    --  Time that the schedule starts for the day
    time_start TIME NOT NULL, 
    --  Time that the schedule ends for the day
    time_end TIME NOT NULL,
    repeat_frequency SET('Mon', 'Tue', 'Wed', 
    'Thur', 'Fri', 'Sat', 'Sun', 'Daily') NOT NULL);


CREATE TABLE user_billboards (
    username VARCHAR(40) NOT NULL,
    billboard_ID INT NOT NULL,
    PRIMARY KEY (username, billboard_ID),
    FOREIGN KEY (username) REFERENCES users(username),
    FOREIGN KEY (billboard_ID) REFERENCES billboards(billboard_ID)
);

CREATE TABLE billboard_schedule (
    schedule_ID INT NOT NULL,
    billboard_ID INT NOT NULL,
    PRIMARY KEY (schedule_ID),
    FOREIGN KEY (schedule_ID) REFERENCES schedules(schedule_ID),
    FOREIGN KEY (billboard_ID) REFERENCES billboards(billboard_ID)
);