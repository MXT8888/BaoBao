package cn.xiaoandx.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@ToString
@NoArgsConstructor
@ApiModel(value = "审核VO")
@Data
public class ToExamine {

	@ApiModelProperty(value = "用户Id")
	private Long userId;
	
	@ApiModelProperty(value = "任务Id")
	private Long taskId;
	
	@ApiModelProperty(value = "状态码")
	private String stateCode;
}
