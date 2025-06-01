INSERT INTO Orders (OrderNumber, ReportID, Approve, Documented, Size, OrderDate) VALUES ('583-29481-703-2', null, null, true, 1, '2025-04-28');
INSERT INTO Orders (OrderNumber, ReportID, Approve, Documented, Size, OrderDate) VALUES ('109-83752-194-8', null, null, false, 1, '2025-04-29');
INSERT INTO Orders (OrderNumber, ReportID, Approve, Documented, Size, OrderDate) VALUES ('720-01045-665-1', null, null, false, 2, '2025-04-30');
INSERT INTO Orders (OrderNumber, ReportID, Approve, Documented, Size, OrderDate) VALUES ('492-67398-320-7', null, null, false, 1, '2025-05-01');
INSERT INTO Orders (OrderNumber, ReportID, Approve, Documented, Size, OrderDate) VALUES ('831-54907-021-9', null, null, false, 1, '2025-05-02');
INSERT INTO Orders (OrderNumber, ReportID, Approve, Documented, Size, OrderDate) VALUES ('264-93827-104-6', null, null, false, 2, '2025-05-03');
INSERT INTO Orders (OrderNumber, ReportID, Approve, Documented, Size, OrderDate) VALUES ('705-12049-387-3', null, null, false, 1, '2025-05-04');
INSERT INTO Orders (OrderNumber, ReportID, Approve, Documented, Size, OrderDate) VALUES ('198-37460-251-0', null, null, false, 1, '2025-05-05');
INSERT INTO Orders (OrderNumber, ReportID, Approve, Documented, Size, OrderDate) VALUES ('347-80516-639-4', null, null, false, 1, '2025-05-06');
INSERT INTO Orders (OrderNumber, ReportID, Approve, Documented, Size, OrderDate) VALUES ('610-47283-970-7', null, null, false, 1, '2025-05-07');
INSERT INTO Orders (OrderNumber, ReportID, Approve, Documented, Size, OrderDate) VALUES ('823-19064-543-1', null, null, false, 2, '2025-05-08');
INSERT INTO Orders (OrderNumber, ReportID, Approve, Documented, Size, OrderDate) VALUES ('076-58192-746-8', null, null, false, 1, '2025-05-09');
INSERT INTO Orders (OrderNumber, ReportID, Approve, Documented, Size, OrderDate) VALUES ('991-30675-832-5', null, null, false, 2, '2025-05-10');
INSERT INTO Orders (OrderNumber, ReportID, Approve, Documented, Size, OrderDate) VALUES ('450-72819-109-2', null, null, false, 2, '2025-05-11');
INSERT INTO Orders (OrderNumber, ReportID, Approve, Documented, Size, OrderDate) VALUES ('382-46730-204-9', null, null, false, 2, '2025-05-12');
INSERT INTO Orders (OrderNumber, ReportID, Approve, Documented, Size, OrderDate) VALUES ('451-00176-987-9', null, null, false, 1, '2025-05-13');

INSERT INTO [User] (FullName, Email, Role, Password, ImagePath) VALUES ('LiseLotte Madsen', 'liselotte.madsen@example.com', 2, '$2a$12$zXQK82QH55JAWPxJbAd06OGmbtougNQsiQs9OSK61HNf6.VcGoH/2', null); -- password: liselotte
INSERT INTO [User] (FullName, Email, Role, Password, ImagePath) VALUES ('Lars Sørensen', 'lars.sorensen@example.com', 3, '$2a$12$3qeiL7G79EJtkGVlVWbzMeDH1SCzryMFbD7Tlca/W/cDbq9CLf8/e', null); -- password: Lars
INSERT INTO [User] (FullName, Email, Role, Password, ImagePath) VALUES ('Leslie Andersen', 'leslie.andersen@example.com', 1, '$2a$12$YDH5kL9dI6q0ef.RWKLEB.A0Ggecpkwxd9jYbgi1qOPe5VMDmCV8m', null); -- password: Leslie

INSERT INTO Pictures (Path, UserID, OrderID, Approved, PicturePosition) VALUES ('coffee_front.jpg', 2, 1, null, 1);
INSERT INTO Pictures (Path, UserID, OrderID, Approved, PicturePosition) VALUES ('coffee_right.jpg', 2, 1, null, 2);
INSERT INTO Pictures (Path, UserID, OrderID, Approved, PicturePosition) VALUES ('coffee_back.jpg', 2, 1, null, 3);
INSERT INTO Pictures (Path, UserID, OrderID, Approved, PicturePosition) VALUES ('coffee_left.jpg', 2, 1, null, 4);
INSERT INTO Pictures (Path, UserID, OrderID, Approved, PicturePosition) VALUES ('coffee_top.jpg', 2, 1, null, 5);