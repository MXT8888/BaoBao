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
 * <p>用户注册基本信息</p> 
 * @ClassName:WUser   
 * @author: XIAOX.周巍 
 * @date: 2019年5月18日 上午9:48:07   
 * @since: JDK1.8 
 * @version V2.0
 * @Copyright: 注意：本内容仅限于学习传阅，禁止外泄以及用于其他的商业目
 */
@ApiModel(value = "用户基本信息")
@Data
public class WUserVO {
	@ApiModelProperty(value = "用户临时code",name = "code")
	private String code;
	@ApiModelProperty(value = "用户微信头像地址",name = "head_portrait")
	private String head_portrait;
	@ApiModelProperty(value = "用户微信昵称",name = "nickname")
	private String nickname;
	 
}
