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
package cn.xiaoandx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**  
 * <p>examinationv3项目启动</p> 
 * @ClassName:ExamV3WebApp   
 * @author: XIAOX.周巍 
 * @date: 2019年5月21日 下午4:31:06   
 * @since: JDK1.8
 * @version V2.0
 * @Copyright: 注意：本内容仅限于学习传阅，禁止外泄以及用于其他的商业目
 */
@SpringBootApplication
@EnableSwagger2
public class GreatAdventureWebApp extends SpringBootServletInitializer  {

	public static void main(String[] args) {
		SpringApplication.run(GreatAdventureWebApp.class, args);
	}
	
	/**   
	 * <p>Title: configure</p>   
	 * <p>Description: </p>   
	 * @param builder
	 * @return   
	 * @see org.springframework.boot.web.servlet.support.SpringBootServletInitializer#configure(org.springframework.boot.builder.SpringApplicationBuilder)   
	 */
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(GreatAdventureWebApp.class);
	}

}
