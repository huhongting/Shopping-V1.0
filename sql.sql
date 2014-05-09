/*�����洢���̡����ѷ������µ�ʱ�䳬��30��Ķ�������Ϊ���ջ�״̬��*/
DELIMITER | 
DROP PROCEDURE IF EXISTS shopping_process | 
CREATE PROCEDURE shopping_process()
	BEGIN
		update orders set state = 'RECEIVED' where to_days(now())-TO_DAYs(createdate) >= 30 and state = 'DELIVERED';
	END
|

/*������ʱ����ÿ��30��ִ��һ�Ρ�*/
SET GLOBAL event_scheduler = 1;
CREATE EVENT IF NOT EXISTS shopping_event
ON SCHEDULE EVERY 30 second STARTS NOW()
ON COMPLETION PRESERVE
DO CALL shopping_process();

/*���ü�ʱ��*/
ALTER EVENT shopping_event ON
COMPLETION PRESERVE ENABLE;