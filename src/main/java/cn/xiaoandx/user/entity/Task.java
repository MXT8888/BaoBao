package cn.xiaoandx.user.entity;



import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
@ApiModel(value = "发布记录表实体")
@Data
public class Task {

	@ApiModelProperty(value="任务Id")
	private Long task_id;
	
	@ApiModelProperty(value="发布人Id")
	private Long user_id;
	
	@ApiModelProperty(value="发布任务状态")
	private String status;
	
	@ApiModelProperty(value="任务要求")
	private String claim;
	
	@ApiModelProperty(value="剩余赏金")
	private Double bounty;
	
	@ApiModelProperty(value="总赏金")
	private Double total_bounty;
	
	@ApiModelProperty(value="已领红包")
	private Integer red;
	
	@ApiModelProperty(value="总红包数")
	private Integer total_red;
	
	@ApiModelProperty(value="已参与人数")
	private Integer partner;
	
	@ApiModelProperty(value="总参与人数")
	private Integer total_partner;
	
	@ApiModelProperty(value="时间")
	private String  time;

	public Task(Long task_id, Long user_id, String status, String claim, Double bounty, Double total_bounty,
			Integer red, Integer total_red, Integer partner, Integer total_partner, String time) {
		this.task_id = task_id;
		this.user_id = user_id;
		this.status = status;
		this.claim = claim;
		this.bounty = bounty;
		this.total_bounty = total_bounty;
		this.red = red;
		this.total_red = total_red;
		this.partner = partner;
		this.total_partner = total_partner;
		this.time = time;
	}
	
	
}
