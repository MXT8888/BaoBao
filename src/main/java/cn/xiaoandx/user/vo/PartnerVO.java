package cn.xiaoandx.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>参与记录实体</p> 
 * @ClassName:Partner   
 * @author: xiaoandx.周巍
 * @date: 2019-06-12 12:22
 * @since: JDK1.8
 * @version V0.1
 * @Copyright: 注意：本内容仅限于学习传阅，禁止外泄以及用于其他的商业目
 */
@ApiModel(value = "参与记录实体")
@Data
public class PartnerVO {
	
	@ApiModelProperty(dataType = "int",name = "user_id",value="用户Id")
	private int user_id;
	
	@ApiModelProperty(dataType = "int",name = "task_id",value="参与的任务Id")
	private int task_id;
	
	@ApiModelProperty(dataType = "String",name = "image_url",value="任务图片地址")
	private String image_url;
	
	@ApiModelProperty(dataType = "int",name = "is_public",value="是否公开（0为公开，1为不公开）")
	private int is_public;
	
	@ApiModelProperty(dataType = "String",name = "note",value="说明")
	private String note;
	
}
