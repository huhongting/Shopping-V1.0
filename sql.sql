/*创建存储过程。将已发货且下单时间超过30天的订单更新为已收货状态。*/
DELIMITER | 
DROP PROCEDURE IF EXISTS shopping_process | 
CREATE PROCEDURE shopping_process()
	BEGIN
		update orders set state = 'RECEIVED' where to_days(now())-TO_DAYs(createdate) >= 30 and state = 'DELIVERED';
	END
|

/*创建定时器。每隔30天执行一次。*/
SET GLOBAL event_scheduler = 1;
CREATE EVENT IF NOT EXISTS shopping_event
ON SCHEDULE EVERY 30 second STARTS NOW()
ON COMPLETION PRESERVE
DO CALL shopping_process();

/*启用计时器*/
ALTER EVENT shopping_event ON
COMPLETION PRESERVE ENABLE;