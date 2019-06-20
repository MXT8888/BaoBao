/*
 * Copyright 2012-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.xiaoandx.commons.utils;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cn.xiaoandx.commons.core.DaoCode;
import cn.xiaoandx.commons.core.Parameter;
import cn.xiaoandx.user.dao.UserDao;
import cn.xiaoandx.user.entity.Partner;
import cn.xiaoandx.user.entity.Task;
import lombok.extern.slf4j.Slf4j;

/**  
 * <p>定时返还未领取完成的红包</p> 
 * @ClassName:TimingRed   
 * @author: xiaoandx.周巍
 * @date: 2019-06-19 22:13
 * @since: JDK1.8
 * @version V0.1
 * @Copyright: 注意：本内容仅限于学习传阅，禁止外泄以及用于其他的商业目
 */
@Component
@Transactional
@Slf4j
public class TimingRed implements DaoCode, Parameter {
	
	@Autowired
	private UserDao userDao;
	
	/**
	 *<p>10分钟判断一次</p> 
	 * @Title: TimingRedAll    
	 * @version:V0.1         
	 * @return:void
	 */
	@Scheduled(cron = "0 */10 * * * ?")
	public void TimingRedAll() {
		List<Task> listTask = userDao.findBySta(RELEASE_OK);
		if (ZEROUSERID != listTask.size()) {
			for (Task t : listTask) {
				try {
					Long cTime = IdAndTimeUtil.timeSubtraction(t.getTime(), IdAndTimeUtil.getDate());
					if (cTime >= SUCCESS) {
						/*
						 * || t.getTotal_partner() - t.getPartner() > ZEROUSERID
						 * (暂时不加人数的条件，需要该条件时候，还需要判断该任务的所有参与者都已经审核了)
						 */
						if (t.getPartner() == ZEROUSERID || t.getTotal_partner() - t.getPartner() > ZEROUSERID) {
							List<Partner> listPar = userDao.findParByTaskId(t.getTask_id(), DEFAULT_STA);
							if (listPar.isEmpty()) {
								int numberOne = userDao.updateTaskMo(DOUBLE_NUMBER, t.getTask_id(), TASK_END);
								if (ERROR != numberOne) {
									int numberTwo = userDao.updateUser(t.getUser_id(), t.getTotal_bounty());
									if (ERROR != numberTwo) {
										int numberTh = userDao.addDealByUserId(t.getUser_id(), MISSION_BALANCE,
												t.getTotal_bounty());
										if (ERROR != numberTh) {
											log.info("task " + t.getTask_id() + " success");
										} else {
											log.error("Error in update deal table by xiaoandx");
										}
									} else {
										log.error("Failed to update user table by xiaoandx");
									}
								} else {
									log.error("Failed to update task table by xiaoandx");
								}
							} else {
								log.info("task " + t.getTask_id() + "  partner not null and sta is daushenghe by xiaoandx");
							}
						} else {
							log.info("task " + t.getTask_id() + " Number of participants by xiaoandx");
						}
					} else {
						log.info("task " + t.getTask_id() + " Less than a day by xiaoandx");
					}
				} catch (Exception e) {
					log.error("Error in calculating time by xiaoandx");
				}
			}
		} else {
			log.info("list task is null by xiaoandx");
		}
	}
}
