-- 创建预约门店订单表的触发器,用于校验预约时间是否与现有预约时间交集
CREATE TRIGGER check_store_order_overlap
    BEFORE INSERT ON t_order
    FOR EACH ROW
BEGIN
    IF EXISTS (
        SELECT 1 FROM t_order
        WHERE store_id = NEW.store_id AND box_id = NEW.box_id
        AND ((NEW.start_time >= start_time AND NEW.start_time < end_time)
        OR (NEW.end_time > start_time AND NEW.end_time <= end_time)
        OR (NEW.start_time <= start_time AND NEW.end_time >= end_time))
    ) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = '预约时间与他人预约时间重叠,请重新选择预约时间';
END IF;
END;