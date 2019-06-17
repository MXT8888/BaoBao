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
package cn.xiaoandx.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.xiaoandx.commons.core.Parameter;
import cn.xiaoandx.user.service.ParticipantService;
import cn.xiaoandx.user.vo.OperationalStatusVO;
import cn.xiaoandx.user.vo.PartnerVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**  
 * <p>参与者模块</p> 
 * @ClassName:ParticipantController   
 * @author: xiaoandx.周巍
 * @date: 2019-06-14 23:30
 * @since: JDK1.8
 * @version V0.1
 * @Copyright: 注意：本内容仅限于学习传阅，禁止外泄以及用于其他的商业目
 */
@RequestMapping("/v1/open/participant")
@RestController
@Api(tags = "参与者模块操作API")
public class ParticipantController implements Parameter{
	
	@Autowired
	private ParticipantService participantService;
	
	/**
	 *<p>提交保存xx用户参与的任务数据</p> 
	 * @Title: addPartner    
	 * @version:V0.1     
	 * @param partnerVO		参与任务数据对象
	 * @return:OperationalStatusVO	操作状态
	 */
	@PostMapping(value = "/addPartner", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "POST", value = "提交保存xx用户参与的任务数据", notes = "根据user_id,task_id等条件（还需要传入参与任务数据，需要封装为一个对象传给后端）<br><b>@autho xiaox.周巍</b>")
	public OperationalStatusVO addPartner(@ApiParam(value = "参与任务数据对象*必填", required = true) @RequestBody PartnerVO partnerVO) {
		if(ENTER_NUMBER != partnerVO.getUser_id() && ENTER_NUMBER != partnerVO.getTask_id()) {
			if(null != partnerVO.getImage_url() && null != partnerVO.getNote() && NO_ZIFUCUAN != partnerVO.getImage_url()) {
				return participantService.addPartner(partnerVO);
			}
			return new OperationalStatusVO(40040, "image_url or note is null");
		}
		return new OperationalStatusVO(40040, "user_id or task_id is 0");
	}
}
