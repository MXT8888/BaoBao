package cn.xiaoandx.user.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.xiaoandx.commons.core.Parameter;
import cn.xiaoandx.commons.core.PublicErrorCode;
import cn.xiaoandx.commons.exception.CommonException;
import cn.xiaoandx.user.entity.Deal;
import cn.xiaoandx.user.entity.Partner;
import cn.xiaoandx.user.entity.Task;
import cn.xiaoandx.user.entity.User;
import cn.xiaoandx.user.service.UserService;
import cn.xiaoandx.user.vo.DealPageSaVO;
import cn.xiaoandx.user.vo.DealPageVO;
import cn.xiaoandx.user.vo.OperationalStatusVO;
import cn.xiaoandx.user.vo.PartnerList;
import cn.xiaoandx.user.vo.TaskVO;
import cn.xiaoandx.user.vo.ToExamine;
import cn.xiaoandx.user.vo.TotalTasks;
import cn.xiaoandx.user.vo.WUserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "任务相关Api")
@RequestMapping("/v1/open/task")
public class UserController implements Parameter {

	@Autowired
	private UserService userService;
	
	/**
 	 *<p>判断用户登录注册</p> 
	 * @Title: get    
	 * @version:V1.0     
	 * @param wuser			包含code与用户信息的对象
	 * @return:User	返回User对象（对象包含user_id等基本信息）
	 */
	@PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "POST", value = "根据微信code，获取微信userId,向数据库插入该用户", notes = "根据微信code（还需要传入用户信息），获取微信userId（前端将userid缓存起来）"
    		+ "<br>过程中会进行判断微信用户是否已经注册，未注册进行注册<br><b>@autho xiaox.周巍</b>")
    public User get(@ApiParam(value = "用户信息（头像地址，昵称）+临时code *必填",required = true)@RequestBody WUserVO wuser) {
    	if(null != wuser.getCode()) {
            String openid = userService.getWeixinOpenInfo(wuser.getCode());
            User usernew = userService.findUser(openid,wuser);
            return usernew;
    	}else {
    		throw new CommonException(PublicErrorCode.PARAM_EXCEPTION.getIntValue(), "参数用户code异常");
    	}
    }
	
	/**
	 *<p>提交新的任务</p> 
	 * @Title: addTask    
	 * @version:V0.1     
	 * @param taskVO	提交任务对象
	 * @return:Task	对象数据
	 */
	@PostMapping(value = "/addTask", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "POST", value = "提交新的任务，将任务记录保存在数据库", notes = "提交新的任务，将任务记录保存在数据库（要封装一个任务数据对象参考下面格式）"
    		+ "<br>插入成功后，返回数据对象，失败返回异常<br><b>@autho xiaox.周巍</b>")
	public OperationalStatusVO addTask(@ApiParam(value = "任务内容信息*必填",required = true)@RequestBody TaskVO taskVO) {
		if( taskVO.getClaim() != null && 0 != taskVO.getUser_id() && null != taskVO.getUser_id() &&
				taskVO.getTotal_bounty() != ENTER_NUMBER) {
			return userService.addTask(taskVO);
		}
		throw new CommonException(PublicErrorCode.PARAM_EXCEPTION.getIntValue(), "Input parameter error");
	}
	
	/**
	 *<p>查询XXX用户的总金额</p> 
	 * @Title: findUserById    
	 * @version:V0.1     
	 * @param user_id	用户id
	 * @return:User		返回的对象
	 */
	@GetMapping(value = "/findUserById/{user_id}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET", value = "查询XXX用户的总金额", notes = "前端传入用户id，返回一个user对象（集合包含总金额+userId）<b>@autho xiaox.周巍</b>")
	public User findUserById(@ApiParam(value = "用户id*必填",required = true) @PathVariable("user_id") int user_id) {
		if(ENTER_NUMBER != user_id) {
			return userService.findUserById(user_id);
		}
		throw new CommonException(PublicErrorCode.PARAM_EXCEPTION.getIntValue(), "Input parameter error");
	}

	/**
	 * 
 	 *<p>查询某个用户所有交易记录</p> 
	 * @Title: get    
	 * @version:V1.0     
	 * @param userId		用户Id
	 * @return:List<Deal>	
	 */
	@GetMapping(value = "/findDeal/{user_id}",produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET",value = "查询用户交易记录",notes = "<br><br><b>@author xiaox.金彩</b>")
	@ApiResponses({
		@ApiResponse(code = 400,message = "未传入指定参数"),
		@ApiResponse(code = 404,message = "未找到指定页面")
	})
	public List<Deal> findDealByUserId(@ApiParam(value = "用户id*必填",required = true) @PathVariable("user_id") Integer user_id) {
		if(ENTER_NUMBER != user_id && null != user_id) {
			return userService.findDealByUserId(user_id);
		}
		throw new CommonException(PublicErrorCode.PARAM_EXCEPTION.getIntValue(), "Input parameter error");
	}
	
	/**
	 *<p>查询xx用户第几页的交易记录</p> 
	 * @Title: findDealByUserIdPage    
	 * @version:V0.1     
	 * @param dealPageVO	分页数据
	 * @return:DealPageSaVO	数据集合
	 */
	@PostMapping(value = "/findDealByUserIdPage", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(httpMethod = "POST", value = "查询xx用户第几页的交易记录", notes = "提交一个带有用户id+第几页+每页的数据量的对象"
    		+ "<br>查询成功后，返回数据集合对象，失败返回异常<br><b>@autho xiaox.周巍</b>")
	public DealPageSaVO findDealByUserIdPage(@ApiParam(value = "查询对象信息*必填",required = true)@RequestBody DealPageVO dealPageVO){
		if(null == dealPageVO.getUserId() || null == dealPageVO.getCurrentPage() ||
				null == dealPageVO.getPageSize() || dealPageVO.getCurrentPage() < 1 ||
						dealPageVO.getPageSize() < 1) {
			throw new CommonException(PublicErrorCode.PARAM_EXCEPTION.getIntValue(), "Input dealPageVO error");
		}
		return userService.findDealByUserIdPage(dealPageVO);
	}
	
	/**
	 *<p>获取七牛云上传图片的凭证token</p> 
	 * @Title: getToken    
	 * @version:V0.1     
	 * @return:Map<String,String>	返回tokenjson字符串
	 */
	@GetMapping(value = "/getToken")
	@ApiOperation(httpMethod = "GET", value = "获取七牛云上传文件token凭证", notes = "返回前端含有token属性的json字符串<b>@autho xiaox.周巍</b>")
	public Map<String, String> getToken() {
		return userService.getToken();
	}

	/**
	 * 查询发布累计数
	 * @param userId
	 * @return
	 */
	@GetMapping(value = "/getTasksPublished/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET", value = "任务记录 查询发布累计数", notes = "任务记录 查询“发布过的任务”界面的总任务数，总金额数，总红包数<br><br>@athor 董泽东<b></b>")
	public TotalTasks getTasksPublished(@ApiParam(value = "用户Id", required = true) @PathVariable Long userId) {
		// userId不能为空
		if (userId == null) {
			return null;
		}
		return userService.getTasksPublished(userId);
	}

	/**
	 * 查询该用户发布的所有任务
	 * @param userId
	 * @return
	 */
	@GetMapping(value = "/getTask/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET", value = "任务记录 查询该用户发布的所有任务", notes = "任务记录 查询该用户所有发布的任务<br><br>@athor 董泽东<b></b>")
	public List<Task> getTask(@ApiParam(value = "用户Id", required = true) @PathVariable Long userId) {
		// userId不能为空
		if (userId == null) {
			return null;
		}
		return userService.getTask(userId);
	}

	// 参与任务查询

	/**
	 * 查询做过任务统计
	 * @param userId
	 * @return
	 */
	@GetMapping(value = "/getPartner/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET", value = "任务记录 查询做过任务统计", notes = "任务记录  查询“做过的任务”界面的总任务数，总领取金额数，总领取红包数<br><br>@athor 董泽东<b></b>")
	public TotalTasks getPartner(@ApiParam(value = "用户Id", required = true) @PathVariable Long userId) {
		// userId不能为空
		if (userId == null) {
			return null;
		}
		return userService.getPartner(userId);
	}

	/**
	 * 查询该用户做过的所有任务
	 * @param userId
	 * @return
	 */
	@GetMapping(value = "/getPartnerList/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET", value = "任务记录 查询该用户做过的所有任务", notes = "任务记录 查询该用户所有做过的任务<br><br>@athor 董泽东<b></b>")
	public List<Partner> getPartnerList(@ApiParam(value = "用户Id", required = true) @PathVariable Long userId) {
		// userId不能为空
		if (userId == null) {
			return null;
		}
		return userService.getPartnerList(userId);
	}

	/**
	 * 查询某一个任务详细信息
	 * @param taskId
	 * @return
	 */
	@PostMapping(value = "/getOneTask/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "POST", value = "任务记录 查询某一个任务详细信息", notes = "任务记录 查询某一个任务详细信息<br><br>@athor 董泽东<b></b>")
	public Task getOneTask(@ApiParam(value = "任务Id", required = true)  @PathVariable Long taskId) {
		return userService.getOneTask(taskId);
	}
	
	/**
	 * 查询任务下的所有参与者
	 * @param taskId
	 * @return
	 */
	@GetMapping(value = "/getPartnerExamine/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET", value = "审核界面 查询任务下的所有参与者", notes = "审核界面 查询任务下的所有参与者<br><br>@athor 董泽东<b></b>")
	public List<PartnerList> getPartnerExamine(@ApiParam(value = "任务Id", required = true)  @PathVariable Long taskId){
		return userService.getPartnerExamine(taskId);
	}
	
	
	/**
	 * 审核
	 * @param examine
	 * @return
	 */
	@PostMapping(value = "/toExamine", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "POST", value = "审核", notes = "审核接口，在这里需要三个数据，需要将“任务Id、参与者Id,审核是否通过”封装在类里<br><br>@athor 董泽东<b></b>")
	public Double toExamine(@ApiParam(value = "审核封装数据", required = true) @RequestBody ToExamine examine) {
		return userService.toExamine(examine);
	}
	
	/**
	 *查询xx用户参与xx任务的状态 
	 * @param userId
	 * @param taskId
	 * @return:Partner
	 */
	@GetMapping(value = "/pantner", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(httpMethod = "GET", value = "任务下的参与者信息", notes = "任务下的参与者信息<br><br>@athor 董泽东<b></b>")
	public Partner partner(@ApiParam(value = "参与者Id", required = true)@RequestParam Long userId,
			 @ApiParam(value = "任务Id", required = true)@RequestParam Long taskId) {
		 return userService.Partner(userId, taskId);
	 }
}
