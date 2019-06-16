package cn.xiaoandx.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
@ApiModel(value = "任务统计")
@Data
public class TotalTasks {

	@ApiModelProperty(value="任务总数")
	private Integer totalTask;
	
	@ApiModelProperty(value="发布或收到的金额")
	private Double balance;
	
	@ApiModelProperty(value="总红包数")
	private Integer totalRedNum;

	public TotalTasks(Integer totalTask, Double balance, Integer totalRedNum) {
		this.totalTask = totalTask;
		this.balance = balance;
		this.totalRedNum = totalRedNum;
	}

	public TotalTasks(Integer totalTask, Double balance) {
		this.totalTask = totalTask;
		this.balance = balance;
	}
	
	
}
