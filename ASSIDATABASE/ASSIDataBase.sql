CREATE DATABASE Shopee;
USE Shopee;
CREATE TABLE Address(
	AddressID 			INT NOT NULL PRIMARY KEY,
    Address_Description NVARCHAR(100) NOT NULL
);

CREATE TABLE Customer(
	CustomerID 			VARCHAR(10) NOT NULL PRIMARY KEY,
    C_name 				NVARCHAR(50) NOT NULL,
    Dateofbirth 		DATE,
    C_gender 			ENUM('M','F'),
    C_email 			VARCHAR(50) NOT NULL,
    C_phonenumber 		VARCHAR(11)
);
SELECT CustomerID, C_name  FROM Customer LIMIT 10;
CREATE TABLE Customer_address(
	CustomerID 			VARCHAR(10) NOT NULL,
    AddressID 			INT NOT NULL,
    FOREIGN KEY (CustomerID) REFERENCES Customer(CustomerID),
    FOREIGN KEY (AddressID) REFERENCES Address(AddressID),
    PRIMARY KEY(CustomerID,AddressID)
);

CREATE TABLE Bank_Account(
	BankID 				INT NOT NULL PRIMARY KEY,
    BankAccount_name 	NVARCHAR(100)
);

CREATE TABLE Current_bank(
	BankID 				INT NOT NULL,
    CustomerID 			VARCHAR(10) NOT NULL,
    FOREIGN KEY (BankID) REFERENCES Bank_Account(BankID),
    FOREIGN KEY (CustomerID) REFERENCES Customer(CustomerID),
    PRIMARY KEY(BankID,CustomerID)
);

/* XONG PHẦN KHÁCH HÀNG
Đến phần mặt hàng */

CREATE TABLE Category(
	CategoryID			INT NOT NULL PRIMARY KEY,
    C_type				NVARCHAR(100) NOT NULL
);

CREATE TABLE Item(
	ItemID 				VARCHAR(10) NOT NULL PRIMARY KEY,
    I_name 				NVARCHAR(100) NOT NULL,
    CategoryID 			INT NOT NULL,
    I_details 			NVARCHAR(300),
    I_amount 			INT NOT NULL,
    I_brand 			NVARCHAR(50),
    I_warehouse 		NVARCHAR(50),
    I_status 			NVARCHAR(30) NOT NULL,
    FOREIGN KEY (CategoryID) REFERENCES Category(CategoryID)
);

CREATE TABLE Pictures(
	PictureID 			INT NOT NULL PRIMARY KEY,
    I_image				NVARCHAR(100)
);

CREATE TABLE Pictures_item(
	PictureID 			INT NOT NULL,
    ItemID 				VARCHAR(10) NOT NULL,
    FOREIGN KEY (PictureID) REFERENCES Pictures(PictureID),
    FOREIGN KEY (ItemID) REFERENCES Item(ItemID),
    PRIMARY KEY (PictureID,ItemID)
);

CREATE TABLE Colors(
	ColorsID 			INT NOT NULL PRIMARY KEY,
    C_type 				NVARCHAR(30)
);

CREATE TABLE Item_corlors(
	ItemID 				VARCHAR(10) NOT NULL,
    ColorsID 			INT NOT NULL,
    FOREIGN KEY (ColorsID) REFERENCES Colors(ColorsID),
    FOREIGN KEY (ItemID) REFERENCES Item(ItemID),
    PRIMARY KEY (ItemID,ColorsID)
);

CREATE TABLE Sizes(
	SizeID 				INT NOT NULL PRIMARY KEY,
    S_type 				VARCHAR(20)
);

CREATE TABLE Item_sizes(
	ItemID 				VARCHAR(10) NOT NULL,
    SizeID 				INT NOT NULL,
    FOREIGN KEY (SizeID) REFERENCES Sizes(SizeID),
    FOREIGN KEY (ItemID) REFERENCES Item(ItemID),
    PRIMARY KEY (ItemID,SizeID)
);

CREATE TABLE Price(
	PriceID 			INT NOT NULL PRIMARY KEY,
    Cost 				INT NOT NULL,
    Discount 			INT NOT NULL,
    From_date 			DATE,
    To_date 			DATE
);

CREATE TABLE Current_price(
	PriceID 			INT NOT NULL,
	ItemID 				VARCHAR(10) NOT NULL,
    FOREIGN KEY (PriceID) REFERENCES Price(PriceID),
    FOREIGN KEY (ItemID) REFERENCES Item(ItemID),
    PRIMARY KEY (PriceID,ItemID)
);

/* Hết phần Item
Đến phần oder and bill */

CREATE TABLE Delivery(
	DeliveryID 			INT NOT NULL PRIMARY KEY,
    D_type 				NVARCHAR(50),
    Fee_shipping 		INT NOT NULL
);

CREATE TABLE Payment(
	PaymentID 			INT NOT NULL,
    CustomerID 			VARCHAR(10) NOT NULL,
    P_type 				NVARCHAR(50),
    FOREIGN KEY (CustomerID) REFERENCES Customer(CustomerID),
    PRIMARY KEY (PaymentID)
);

CREATE TABLE Item_order(
	OrderID 			VARCHAR(10) NOT NULL PRIMARY KEY,
    CustomerID 			VARCHAR(10) NOT NULL,
    ItemID 				VARCHAR(10) NOT NULL,
    O_amount 			INT NOT NULL,
    DeliveryID 			INT NOT NULL,
    Total_price 		INT NOT NULL,
    PaymentID 			INT NOT NULL,
    O_date 				DATE,
    O_status 			NVARCHAR(50),
    FOREIGN KEY (CustomerID) REFERENCES Customer(CustomerID),
    FOREIGN KEY (ItemID) REFERENCES Item(ItemID),
	FOREIGN KEY (DeliveryID) REFERENCES Delivery(DeliveryID),
    FOREIGN KEY (PaymentID) REFERENCES Payment(PaymentID)
);

-- INDEX
CREATE INDEX Cn_index ON Customer(C_name);
CREATE INDEX In_index ON Item(I_name);

-- VIEW
CREATE VIEW getprice_view
AS SELECT i.ItemID,p.Cost,p.Discount
FROM Item as i INNER JOIN Current_price as c ON i.ItemID = c.ItemID INNER JOIN Price as p ON c.PriceID = p .PriceID;
CREATE VIEW search_view
AS SELECT *
FROM Item;
SELECT ItemID,I_name,CategoryID,I_details,I_amount,I_brand,I_warehouse,I_status FROM search_view;
SELECT ItemID,Cost,Discount FROM getprice_view;

-- STORE PROCEDURE
DELIMITER //
CREATE PROCEDURE getprice(IN ItemID VARCHAR(10))
BEGIN
	SELECT ItemID,p.Cost,p.Discount
    FROM Item as i INNER JOIN Current_price as c ON i.ItemID = c.ItemID 
    INNER JOIN Price as p ON c.PriceID = p .PriceID
    WHERE ItemID LIKE ItemID;
END //
DELIMITER ;
CALL getprice(5); 

DELIMITER //
CREATE PROCEDURE updateItem(IN U_ItemID VARCHAR(10),IN i_name NVARCHAR(100),IN i_details NVARCHAR(300)
,IN i_amount INT,IN  i_brand NVARCHAR(50),IN i_warehouse NVARCHAR(50),IN i_status NVARCHAR(30))
BEGIN
	UPDATE Item AS i
    SET i.I_name = i_name, i.I_details = i_details, i.I_amount = i_amount,
    i.I_brand = i_brand, I.i_warehouse = i_warehouse, I.i_status = i_status
    WHERE ItemID LIKE U_ItemID;
END //
DELIMITER ;

-- TRIGGER
DELIMITER $$
CREATE TRIGGER tg_CheckAmount BEFORE UPDATE
	ON Item FOR EACH ROW
BEGIN
	IF NEW.I_amount < 0 THEN
		SIGNAL SQLSTATE '45001' SET MESSAGE_TEXT = 'Phai Lon Hon 0';
		END IF;
END $$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER Order_CheckAmount BEFORE UPDATE
	ON Item_order FOR EACH ROW
BEGIN
	IF NEW.O_amount < 0 THEN
		SIGNAL SQLSTATE '45001' SET MESSAGE_TEXT = 'Phai Lon Hon 0';
		END IF;
END $$
DELIMITER ;

-- UPDATE
UPDATE Item AS i SET i.I_name = "Z900", i.CategoryID = 7, i.I_details = "912CC", i.I_amount = 50, i.I_brand = "Kawasaki", i.I_warehouse = "TPHCM" WHERE i.ItemID = 'I01';
SELECT* FROM Item;

-- SEARCH BY NAME
SELECT * FROM Item WHERE I_name LIKE "H%";

-- SEARCH BY CATEGORY AND NAME
SELECT *
FROM Item AS i INNER JOIN Category AS c ON i.CategoryID = c.CategoryID
WHERE c.C_type LIKE "Xe" AND I_name LIKE "H%";

-- SEARCH BY CATEGORY, NAME AND PRICE RANGE
SELECT *
FROM Item AS i INNER JOIN Category AS c ON i.CategoryID = c.CategoryID 
INNER JOIN Current_price AS b ON b.ItemID = i.ItemID INNER JOIN Price AS p ON b.PriceID = p.PriceID
WHERE c.C_type LIKE "Xe" AND I_name LIKE "K%" AND p.Cost >= 250000 AND p.Cost <= 550000000;

-- SHOW ORDER INFORMATION
SELECT i.OrderID, i.CustomerID, i.ItemID, i.O_amount, i.DeliveryID, a.Address_Description, i.Total_price, p.P_type, i.O_date, i.O_status
FROM Item_order AS i INNER JOIN Customer AS c ON i.CustomerID = c.CustomerID 
INNER JOIN Customer_address AS c1 ON c.CustomerID = c1.CustomerID 
INNER JOIN Address AS a ON c1.AddressID = a.AddressID
INNER JOIN Payment AS p ON i.PaymentID = p.PaymentID;

-- SHOW ITEM INFORMATION
SELECT a.ItemID, a.I_name, b.C_type, a.I_details,d.I_image, f.C_type, h.S_type, k.Cost , k.Discount, k.From_date, k.To_date, a.I_amount, a.I_brand , a.I_warehouse, a.I_status
FROM Item AS a INNER JOIN Category AS b ON a.CategoryID = b.CategoryID 
INNER JOIN Pictures_item AS c ON a.ItemID = c.ItemID INNER JOIN Pictures AS d ON c.PictureID = d.PictureID
INNER JOIN Item_corlors AS e ON a.ItemID = e.ItemID INNER JOIN Colors AS f ON e.ColorsID = f.ColorsID
INNER JOIN Item_sizes AS g ON a.ItemID = g.ItemID INNER JOIN Sizes AS h ON g.SizeID = h.SizeID
INNER JOIN Current_price AS i ON a.ItemID = i.ItemID INNER JOIN Price AS k ON i.PriceID = k.PriceID;

SELECT * FROM Item_order;

-- SHOW CUSTOMER INFORMATION
SELECT c.CustomerID, c.C_name, c.Dateofbirth, c.C_gender, c.C_email, c.C_phonenumber, a.Address_Description, e.BankAccount_name
FROM Customer as c INNER JOIN Customer_address as b ON c.CustomerID = b.CustomerID INNER JOIN Address as a ON a.AddressID=b.AddressID
INNER JOIN Current_bank AS d ON c.CustomerID = d.CustomerID INNER JOIN Bank_Account AS e ON d.BankID = e.BankID;

SELECT * FROM Customer;



-- INSERT 
INSERT Category VALUES(1,"Do choi tre em");INSERT Category VALUES(2,"Do dien tu");INSERT Category VALUES(3,"Do gia dung");INSERT Category VALUES(4,"Thiet bi y te");
INSERT Category VALUES(5,"Thuc pham");INSERT Category VALUES(6,"My pham");INSERT Category VALUES(7,"Xe");INSERT Category VALUES(8,"Sua");
INSERT Category VALUES(9,"Do uong");INSERT Category VALUES(10,"Phu kien");

INSERT INTO Item VALUES("I01","TH Truemilk",8,"Sua co duong",100,"TH","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I02","Bong ngoay tai",10,"100% bong tu nhien",100,"Aten","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I03","Kawasaki Z1000",7,"1043CC",100,"Kawasaki","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I04","BMW S1000RR",7,"999CC",100,"BMW","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I05","CB 1000 Neo",7,"998CC",100,"Honda","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I06","Yamaha R1M",7,"999CC",100,"Yamaha","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I07","Sung phun nuoc",1,"Nhua",100,"China","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I08","Lego",1,"Xep hinh",100,"China","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I09","Bup be babie",1,"Nhan vat hoat hinh",100,"USA","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I10","Tho Bug",1,"Nhan vat hoat hinh",100,"USA","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I11","Oto dieu khien",1,"Mo hinh sieu xe",100,"China","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I12","May bay dieu khien",1,"Mo hinh phan luc f16",100,"China","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I13","Gay ton ngo khong",1,"Nhua cung",100,"China","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I14","Co ca ngua",1,"Tro choi tri tue",100,"China","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I15","Co ti phu",1,"Tro choi tri tue",100,"China","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I16","O cam dien",2,"Extention Plug",100,"China","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I17","Bong den tuyp",2,"Sieu sang",100,"Rang dong","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I18","Noi com dien",2,"De nau com",100,"China","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I19","Macbook Pro",2,"Hang sieu xin",100,"Apple","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I20","Loa thung",2,"1000000W",100,"China","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I21","DDJ 400",2,"Dung cu choi nhac",100,"Pioneer","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I22","Tivi",2,"Sieu net",100,"Sony","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I23","VGA GTX2080GT",2,"Card do hoa",100,"Geforce","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I24","I9 9999K",2,"CPU",100,"Intel","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I25","Note9",2,"DTDD",100,"Samsung","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I26","Khoan",3,"Tang kem nhieu mui khoan",100,"China","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I27","Dao",3,"Sieu sac",100,"China","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I28","Keo",3,"Sieu cat",100,"China","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I29","Bua",3,"Thor dung",100,"China","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I30","Kim",3,"Chuyen be rang",100,"China","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I31","Phich nuoc",3,"Dung 1000L",100,"Rang Dong","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I32","Binh cuu hoa",3,"Khong dung de dap lua",100,"China","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I33","Xeng",3,"Dung de danh nhau",100,"China","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I34","Choi",3,"Dung de danh con",100,"VNXK","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I35","Luoc",3,"Dung de gai",100,"China","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I36","Guong",3,"Soi thau tam hon",100,"China","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I37","Moc treo QA",3,"Co the treo mom",100,"China","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I38","Nang",4,"Dung tay de chong",100,"China","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I39","Xe lan",4,"Dung de ngoi",100,"China","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I40","May do duong huyet",4,"Dung de do huyet ap",100,"China","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I41","May do huyet ap",4,"Dung de do duong huyet",100,"China","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I42","May noi soi",4,"Noi soi duoc ca vi",100,"China","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I43","Ong tiem",4,"Chi dung tiem lon",100,"China","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I44","May tho",4,"Khong dung de tho",100,"China","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I45","May tro tim",4,"Dung de tho",100,"China","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I46","Bong bang",4,"De cam mau",100,"China","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I47","Giuong benh",4,"De nam",100,"China","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I48","Rau sach",5,"Dung lam thuc an cho chim",100,"China","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I49","Ga 45Kg",5,"Dung lam thuc an cho chim",100,"China","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I50","Dao minhon",5,"Dung lam thuc an cho chim",100,"China","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I51","Dao to",5,"Dung lam thuc an cho chim",100,"China","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I52","Thit bo",5,"Bo",100,"China","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I53","Thit lon",5,"Lon",100,"China","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I54","Thit ga",5,"Thit",100,"China","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I55","Pho an lien",5,"Pho",100,"China","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I56","Chao yen",5,"Chao",100,"China","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I57","Banh my",5,"Banh",100,"China","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I58","Kem chong nang",6,"Chong ret",100,"China","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I59","Kem che khuyet diem",6,"Khong che",100,"China","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I60","Gotsen",6,"Tri nut got chan",100,"China","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I61","Nuoc hoa",6,"Mui thoi dac trung",100,"China","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I62","Gel vuot toc",6,"Vuot phat troc dau",100,"China","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I63","Muc ke mat",6,"Mau tim",100,"China","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I64","Mat na",6,"Cham soc da mong",100,"China","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I65","Phan hong",6,"Lam den ma",100,"China","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I66","Son",6,"1 Ng boi 2 Ng do",100,"China","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I67","Sua rua mat",6,"Dung de rua chan",100,"China","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I68","Super DukeR",7,"1299CC",100,"KTM","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I69","Ducati 1199",7,"1199CC",100,"Ducati","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I70","H2R",7,"999CC",100,"Kawasaki","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I71","Hayabusa",7,"1400CC",100,"Kawasaki","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I72","Lamborghini Aventador",7,"2000HP",100,"Lamborghini","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I73","Porsche Carera GT",7,"1200HP",100,"Porsche","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I74","BMW M3",7,"1100HP",100,"BMW","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I75","CB500",7,"448CC",100,"Honda","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I76","Rebell 500",7,"448CC",100,"honda","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I77","Vulcan 650",7,"649CC",100,"kawasaki","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I78","Vinamilk",8,"Sua co duong",100,"China","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I79","Vina soy",8,"Sua co duong",100,"China","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I80","Milo",8,"Sua co duong",100,"China","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I81","Co gai ha lan",8,"Sua co duong",100,"China","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I82","Ensure mama A+",8,"Sua co duong",100,"China","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I83","Ensure Gold",8,"Sua co duong",100,"China","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I84","Sua ong tho",8,"Sua co duong",100,"China","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I85","Sua Chua",8,"Sua co duong",100,"China","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I86","Sua bot",8,"Sua co duong",100,"China","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I87","Volka",9,"SRuou",100,"China","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I88","Chivas",9,"Ruou",100,"China","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I89","Ruou Tao meo",9,"Ruou",100,"China","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I90","Ruou ran",9,"Ruou",100,"China","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I91","Cocacola",9,"do uong co ga",100,"China","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I92","Monster",9,"nuoc tang luc",100,"China","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I93","Bia ha noi",9,"bia",100,"China","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I94","Bia sai gon",9,"bia",100,"China","Kho Hanoi","Con hang");
INSERT INTO Item VALUES("I95","Bia viet ha",9,"Bia",100,"China","Kho Hanoi","Con hang");

-- INSERT CUSTOMER
INSERT INTO Customer VALUES("C01","Hoang",19990404,"M","Hoang@gmail.com",0949999999);
INSERT INTO Customer VALUES("C02","Hong",19990413,"F","Hong@gmail.com",0967999999);
INSERT INTO Customer VALUES("C03","Hanh",19990423,"F","Hanh@gmail.com",0956999999);
INSERT INTO Customer VALUES("C04","Huong",19990416,"F","Huong@gmail.com",0199999999);
INSERT INTO Customer VALUES("C05","Hung",19990919,"M","Hung@gmail.com",0923999999);
INSERT INTO Customer VALUES("C06","Hai",19991212,"M","Hai@gmail.com",0999459999);
INSERT INTO Customer VALUES("C07","Hien",19990909,"M","Hien@gmail.com",0934999999);
INSERT INTO Customer VALUES("C08","Hiep",19990606,"M","Hiep@gmail.com",0912999999);
INSERT INTO Customer VALUES("C09","Hieu",19940808,"M","Hieu@gmail.com",0956999999);
INSERT INTO Customer VALUES("C10","Hien",19930204,"F","Hien@gmail.com",0945999999);
INSERT INTO Customer VALUES("C11","Hau",19930405,"M","Hau@gmail.com",0999239999);
INSERT INTO Customer VALUES("C12","Nga",19930705,"F","Nga@gmail.com",0999549999);
INSERT INTO Customer VALUES("C13","Ngan",19930506,"F","Ngan@gmail.com",0943999999);
INSERT INTO Customer VALUES("C14","Trinh",19920604,"F","Trinh@gmail.com",0199999999);
INSERT INTO Customer VALUES("C15","Thuy",19910504,"F","Thuy@gmail.com",0923999999);
INSERT INTO Customer VALUES("C16","Thu",19950302,"F","Thu@gmail.com",0999919999);
INSERT INTO Customer VALUES("C17","Tien",19940506,"F","Tien@gmail.com",0999899999);
INSERT INTO Customer VALUES("C18","Tuan",19970912,"M","Tuan@gmail.com",0999799999);
INSERT INTO Customer VALUES("C19","Tai",19930921,"M","Tai@gmail.com",0999969999);
INSERT INTO Customer VALUES("C20","Tuyen",19940508,"M","Tuyen@gmail.com",0999995999);
INSERT INTO Customer VALUES("C21","Chien",19930902,"M","Chien@gmail.com",0999994999);
INSERT INTO Customer VALUES("C22","Truong",19950802,"M","Truong@gmail.com",0999399999);
INSERT INTO Customer VALUES("C23","Trieu",19900601,"M","Trieu@gmail.com",0999992999);
INSERT INTO Customer VALUES("C24","Thang",19950201,"M","Thang@gmail.com",0999991999);
INSERT INTO Customer VALUES("C25","Hoa",19950703,"F","Hoa@gmail.com",0999999899);
INSERT INTO Customer VALUES("C26","Phong",19970302,"M","Phong@gmail.com",0999999799);
INSERT INTO Customer VALUES("C27","Phuong",19970322,"M","Phuong@gmail.com",0999999699);
INSERT INTO Customer VALUES("C28","Phien",19960101,"M","Phien@gmail.com",0999999599);
INSERT INTO Customer VALUES("C29","Son",19950603,"M","Son@gmail.com",0999999499);
INSERT INTO Customer VALUES("C30","Sen",19910603,"F","Sen@gmail.com",0999999399);
INSERT INTO Customer VALUES("C31","Sung",19940201,"M","Sung@gmail.com",0999999299);
INSERT INTO Customer VALUES("C32","Cuong",19950405,"M","Cuong@gmail.com",0999999199);
INSERT INTO Customer VALUES("C33","Kien",19910703,"M","Kien@gmail.com",0999999909);
INSERT INTO Customer VALUES("C34","Ken",19990321,"M","Ken@gmail.com",0999999919);
INSERT INTO Customer VALUES("C35","Linh",19950901,"F","Linh@gmail.com",0999999929);
INSERT INTO Customer VALUES("C36","Nhat",19910703,"F","Nhat@gmail.com",0999999939);
INSERT INTO Customer VALUES("C37","Duong",19990909,"M","Duong@gmail.com",0999999949);
INSERT INTO Customer VALUES("C38","Thieu",19910101,"M","Thieu@gmail.com",0999999959);
INSERT INTO Customer VALUES("C39","Trang",19920202,"F","Trang@gmail.com",0999999969);
INSERT INTO Customer VALUES("C40","Trung",19930303,"M","Trung@gmail.com",0999999979);
INSERT INTO Customer VALUES("C41","Tu",19940404,"M","Tu@gmail.com",0999999989);
INSERT INTO Customer VALUES("C42","Nghia",19950505,"M","Nghia@gmail.com",0999999990);
INSERT INTO Customer VALUES("C43","Nguyet",19960606,"F","Nguyet@gmail.com",0999999991);
INSERT INTO Customer VALUES("C44","Hue",19970707,"F","Hue@gmail.com",0999999992);
INSERT INTO Customer VALUES("C45","Vinh",19980808,"M","Vinh@gmail.com",0999999993);
INSERT INTO Customer VALUES("C46","Thoa",19990909,"F","Thoa@gmail.com",0999999994);
INSERT INTO Customer VALUES("C47","Cham",19900102,"F","Cham@gmail.com",0999999995);
INSERT INTO Customer VALUES("C48","Chung",19910304,"M","Chung@gmail.com",0999999996);
INSERT INTO Customer VALUES("C49","Quy",19920405,"M","Quy@gmail.com",0999999997);
INSERT INTO Customer VALUES("C50","Quyen",19930908,"M","Quyen@gmail.com",0999999998);

INSERT INTO Price VALUES(1,5000,0,null,null); 
INSERT INTO Price VALUES(2,10000,0,null,null); 
INSERT INTO Price VALUES(3,400000000,15000000,20200601,20201001); 
INSERT INTO Price VALUES(4,650000000,30000000,20200601,20200901); 
INSERT INTO Price VALUES(5,550000000,15000000,20200601,20201001); 
INSERT INTO Price VALUES(6,850000000,50000000,20200601,20200901); 
INSERT INTO Price VALUES(7,15000,0,null,null); 
INSERT INTO Price VALUES(8,50000,0,null,null); 
INSERT INTO Price VALUES(9,500000,0,null,null); 
INSERT INTO Price VALUES(10,250000,0,null,null);
INSERT INTO Current_price VALUES(1,"I01");
INSERT INTO Current_price VALUES(2,"I02");
INSERT INTO Current_price VALUES(3,"I03");
INSERT INTO Current_price VALUES(4,"I04");
INSERT INTO Current_price VALUES(5,"I05");
INSERT INTO Current_price VALUES(6,"I06");
INSERT INTO Current_price VALUES(7,"I07");
INSERT INTO Current_price VALUES(8,"I08");
INSERT INTO Current_price VALUES(9,"I09");
INSERT INTO Current_price VALUES(10,"I10");

INSERT INTO Payment VALUES(1,"C01","Tien mat");
INSERT INTO Payment VALUES(2,"C02","Credit card");
INSERT INTO Payment VALUES(3,"C03","Tien mat");
INSERT INTO Payment VALUES(4,"C04","Credit card");
INSERT INTO Payment VALUES(5,"C05","Tien mat");
INSERT INTO Payment VALUES(6,"C06","Credit card");
INSERT INTO Payment VALUES(7,"C07","Tien mat");
INSERT INTO Payment VALUES(8,"C08","Credit card");
INSERT INTO Payment VALUES(9,"C09","Tien mat");
INSERT INTO Payment VALUES(10,"C10","Credit card");
INSERT INTO Delivery VALUES(1,"Giao nhanh",40000);
INSERT INTO Delivery VALUES(2,"Giao hang tiet kiem",30000);
INSERT INTO Delivery VALUES(3,"Giao toan quoc",500000);

INSERT INTO Item_order VALUES("1","C01","I01",4,1,60000,1,20200620,"Da giao");
INSERT INTO Item_order VALUES("2","C02","I02",2,2,50000,2,20200620,"Da giao");
INSERT INTO Item_order VALUES("3","C03","I03",1,3,385500000,3,20200620,"Da giao");
INSERT INTO Item_order VALUES("4","C04","I04",1,1,620040000,4,20200620,"Da giao");
INSERT INTO Item_order VALUES("5","C05","I05",1,2,535030000,5,20200620,"Da giao");
INSERT INTO Item_order VALUES("6","C06","I06",1,3,800500000,6,20200620,"Da giao");
INSERT INTO Item_order VALUES("7","C07","I07",2,2,60000,7,20200620,"Da giao");
INSERT INTO Item_order VALUES("8","C08","I08",2,3,600000,8,20200620,"Da giao");
INSERT INTO Item_order VALUES("9","C09","I09",2,1,1040000,9,20200620,"Da giao");
INSERT INTO Item_order VALUES("10","C10","I10",2,1,540000,10,20200620,"Da giao");
