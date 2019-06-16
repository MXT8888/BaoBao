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
package cn.xiaoandx.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.xiaoandx.commons.core.DaoCode;
import cn.xiaoandx.commons.core.Parameter;
import cn.xiaoandx.commons.core.PublicErrorCode;
import cn.xiaoandx.commons.exception.CommonException;
import cn.xiaoandx.user.dao.UserDao;
import cn.xiaoandx.user.entity.Task;
import cn.xiaoandx.user.vo.OperationalStatusVO;
import cn.xiaoandx.user.vo.PartnerVO;

/**  
 * <p> </p> 
 * @ClassName:ParticipantService   
 * @author: xiaoandx.周巍
 * @date: 2019-06-14 23:36
 * @since: JDK1.8
 * @version V0.1
 * @Copyright: 注意：本内容仅限于学习传阅，禁止外泄以及用于其他的商业目
 */
@Service
@Transactional
public class ParticipantService implements DaoCode, Parameter {
	
	@Autowired
	private UserDao userDao;
	
	/**
	 *<p>提交保存xx用户参与的任务数据</p> 
	 * @Title: addPartner    
	 * @version:V0.1     
	 * @param partnerVO		参与任务数据对象
	 * @return:OperationalStatusVO	操作状态
	 */
	public OperationalStatusVO addPartner(PartnerVO partnerVO) {
		// TODO Auto-generated method stub
		//1.向参与记录表插入一条数据
		int codeNumber = userDao.addPartner(partnerVO, DEFAULT_STA, DOUBLE_NUMBER);
		if(ERROR == codeNumber) {
			return new OperationalStatusVO(40040, "add partnerTable error");
		}
		Task taskNew = userDao.findTaskByTaskId(partnerVO.getTask_id());
		if(ENTER_NUMBER != ( taskNew.getTotal_partner() - taskNew.getPartner() )) {
			int codeNumberT = userDao.updateByTaskId(partnerVO.getTask_id());
			if(ERROR != codeNumberT) {
				return new OperationalStatusVO(200, "Participation in mission success");
			}
			throw new CommonException(PublicErrorCode.PARAM_EXCEPTION.getIntValue(), "Participation in mission error (参与失败)");
		}
		throw new CommonException(PublicErrorCode.PARAM_EXCEPTION.getIntValue(), "The number is full (人数已满)");
	}

}
