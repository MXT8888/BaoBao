package cn.xiaoandx.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>任务实体对象</p> 
 * @ClassName:TaskVO   
 * @author: xiaoandx.周巍
 * @date: 2019-06-12 10:53
 * @since: JDK1.8
 * @version V0.1
 * @Copyright: 注意：本内容仅限于学习传阅，禁止外泄以及用于其他的商业目
 */
@ApiModel(description="任务实体对象")
@Data
@NoArgsConstructor
public class TaskVO {
	@ApiModelProperty(dataType = "Long",name = "user_id",value = "用户id")
	private Long user_id;
	@ApiModelProperty(dataType = "String",name = "claim",value = "任务要求")
	private String claim;
	@ApiModelProperty(dataType = "double",name = "total_bounty",value = "总赏金（小数点后两位）")
	private double total_bounty;
	@ApiModelProperty(dataType = "Integer",name = "total_red",value = "红包总量")
	private Integer total_red;
	@ApiModelProperty(dataType = "Integer",name = "total_red",value = "总参与人数")
	private Integer total_partner;
}
