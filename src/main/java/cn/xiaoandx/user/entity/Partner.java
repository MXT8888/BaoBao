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
public class Partner {
	
	@ApiModelProperty(value="参与记录Id")
	private Long partner_id;
	
	@ApiModelProperty(value="用户Id")
	private Long user_id;
	
	@ApiModelProperty(value="参与的任务Id")
	private Long task_id;
	
	@ApiModelProperty(value="任务图片地址")
	private String image_url;
	
	@ApiModelProperty(value="是否公开")
	private Integer is_public;
	
	@ApiModelProperty(value="说明")
	private String note;
	
	@ApiModelProperty(value="审核状态")
	private String status;
	
	@ApiModelProperty(value="获得金额")
	private Double bounty;
	
	@ApiModelProperty(value="时间")
	private String time;

	public Partner(Long partner_id, Long user_id, Long task_id, String image_url, Integer is_public, String note,
			String status, Double bounty, String time) {
		this.partner_id = partner_id;
		this.user_id = user_id;
		this.task_id = task_id;
		this.image_url = image_url;
		this.is_public = is_public;
		this.note = note;
		this.status = status;
		this.bounty = bounty;
		this.time = time;
	}

	
}
