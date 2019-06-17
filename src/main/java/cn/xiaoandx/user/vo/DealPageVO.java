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
 * <p>任务记录分页</p> 
 * @ClassName:DealPageVO   
 * @author: xiaoandx.周巍
 * @date: 2019-06-17 14:20
 * @since: JDK1.8
 * @version V0.1
 * @Copyright: 注意：本内容仅限于学习传阅，禁止外泄以及用于其他的商业目
 */
@Data
@ApiModel(description= "查询用户交易记录的分页方式信息")
public class DealPageVO {
	@ApiModelProperty(dataType = "Long",name = "userId",value="用户id")
	private Long userId; //用户id
	@ApiModelProperty(dataType = "Integer",name = "currentPage",value="第几页")
	private Integer currentPage; //当前页码
	@ApiModelProperty(dataType = "Integer",name = "pageSize",value="每页数据量")
	private Integer pageSize; //每页数据量
}
