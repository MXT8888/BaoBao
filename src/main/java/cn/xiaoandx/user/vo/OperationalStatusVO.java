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
package cn.xiaoandx.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**  
 * <p>查询操作状态</p> 
 * @ClassName:OperationalStatusVO   
 * @author: xiaoandx.周巍
 * @date: 2019-06-13 09:31
 * @since: JDK1.8
 * @version V0.1
 * @Copyright: 注意：本内容仅限于学习传阅，禁止外泄以及用于其他的商业目
 */
@ApiModel(description="操作状态")
@Data
public class OperationalStatusVO {
	@ApiModelProperty(dataType = "Integer",name = "statusCode",value="操作状态码(发布任务时返回是任务id)")
	private Integer statusCodeORTaskId;
	@ApiModelProperty(dataType = "String",name = "statusMassgerstatusMassger",value="操作结果")
	private String statusMassger;
	
	/**   
	 * @Title:OperationalStatusVO   
	 * @date:2019年6月13日上午9:33:45
	 * @param statusCode
	 * @param statusMassger
	 */
	public OperationalStatusVO(Integer statusCode, String statusMassger) {
		this.statusCodeORTaskId = statusCode;
		this.statusMassger = statusMassger;
	}
}
