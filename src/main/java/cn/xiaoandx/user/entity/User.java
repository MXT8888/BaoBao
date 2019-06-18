package cn.xiaoandx.user.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import lombok.NoArgsConstructor;

import lombok.ToString;

@ToString
@NoArgsConstructor
@ApiModel(value = "参与记录实体")
@Data
public class User {

	@ApiModelProperty(value="用户Id")
	private Long user_id;
	
	@ApiModelProperty(value="open_id")
	private String  open_id;
	
	@ApiModelProperty(value="余额")
	private Double overage;
	
	@ApiModelProperty(value="昵称")
	private String nickname;
	
	@ApiModelProperty(value="头像")
	private String head_portrait;
}
