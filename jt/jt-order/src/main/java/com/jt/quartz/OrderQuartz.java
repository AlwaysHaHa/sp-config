package com.jt.quartz;

import java.util.Calendar;
import java.util.Date;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jt.mapper.OrderMapper;
import com.jt.pojo.Order;

//准备订单定时任务
@Component
public class OrderQuartz extends QuartzJobBean{

	@Autowired
	private OrderMapper orderMapper;

	/** 
	 * 目的:每隔1分钟 将超时订单状态修改1改为6 \
	 * 条件:now- created >30 分钟
	 * sql:update tb_order set status=6,updated=#{date} where status=1 and created<now-30
	 */
	@Override
	@Transactional
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		//获取当前时间的日历对象
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, -30);
		Date timeout = calendar.getTime();
		Order order = new Order();
		order.setStatus(6).setUpdated(new Date());
		UpdateWrapper<Order> updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq("status", 1).lt("created", timeout);   //lt 小于 le小于等于  gt大于  ge大于等于
		orderMapper.update(order, updateWrapper);
		System.out.println("定时任务执行成功");
		
		
		/*
		 * Calendar calendar = Calendar.getInstance(); calendar.add(Calendar.MINUTE,
		 * -30); Date date = calendar.getTime(); Order order = new Order();
		 * order.setStatus(6); order.setUpdated(new Date()); UpdateWrapper<Order>
		 * updateWrapper = new UpdateWrapper<>(); updateWrapper.eq("status",
		 * "1").lt("created",date); orderMapper.update(order, updateWrapper);
		 * System.out.println("定时任务执行");
		 */
	}
}
