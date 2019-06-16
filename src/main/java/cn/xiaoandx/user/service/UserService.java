package cn.xiaoandx.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import cn.xiaoandx.commons.core.DaoCode;
import cn.xiaoandx.commons.core.Parameter;
import cn.xiaoandx.commons.core.PublicErrorCode;
import cn.xiaoandx.commons.exception.CommonException;
import cn.xiaoandx.commons.utils.GetWeChatOpenId;
import cn.xiaoandx.commons.utils.IdAndTimeUtil;
import cn.xiaoandx.commons.utils.RedUtils;
import cn.xiaoandx.user.dao.UserDao;
import cn.xiaoandx.user.entity.Deal;
import cn.xiaoandx.user.entity.Partner;
import cn.xiaoandx.user.entity.Task;
import cn.xiaoandx.user.entity.User;
import cn.xiaoandx.user.vo.OperationalStatusVO;
import cn.xiaoandx.user.vo.PartnerList;
import cn.xiaoandx.user.vo.TaskVO;
import cn.xiaoandx.user.vo.ToExamine;
import cn.xiaoandx.user.vo.TotalTasks;
import cn.xiaoandx.user.vo.WUserVO;
import lombok.extern.slf4j.Slf4j;

@Component
@Transactional
@Slf4j
public class UserService implements DaoCode, Parameter {

	@Autowired
	private UserDao userDao;
	// 微信开发者APPID
	@Value("${weixings.appId}")
	private String appId;
	// 微信开发者APPSecurity
	@Value("${weixings.appSecurity}")
	private String appSecurity;
	// 七牛云accessKey
	@Value("${qinliu.accessKey}")
	private String accessKey;
	// 七牛云secretKey
	@Value("${qinliu.secretKey}")
	private String secretKey;
	// 七牛云bucket（文件储存库的名字）
	@Value("${qinliu.bucket}")
	private String bucket;
	
	/**
	 * <p>
	 * 获取openid
	 * </p>
	 * 
	 * @Title: getWeixinOpenInfo
	 * @version:V2.0
	 * @param code 传入code
	 * @return:String 传出openid
	 */
	public String getWeixinOpenInfo(String code) {
		GetWeChatOpenId getWeChatOpenId = new GetWeChatOpenId();
		return getWeChatOpenId.getWeixinOpenInfo(code, appId, appSecurity);
	}

	/**
	 * <p>
	 * 先根据code查询是否已经注册，若注册跳转检测界面，若未注册就进行注册
	 * </p>
	 * 
	 * @Title: findUser
	 * @version:V2.0
	 * @param openid 用户的微信openid
	 * @return:User 用户对象
	 */
	public User findUser(String openid, WUserVO user) {
		if (null != openid) {
			List<User> listuserservice = userDao.findUser(openid);
			if (ERROR == listuserservice.size()) {
				int number = userDao.reUser(user, openid);
				if (ERROR != number) {
					log.info("注册用户成功");
					User user2 = userDao.findByOpenId(openid);
					int number2 = userDao.addDealByUserId(user2.getUser_id(), INITIALIZATION, DEFAULT_MONEY);
					if (ERROR != number2) {
						return user2;
					}
					throw new CommonException(PublicErrorCode.PARAM_EXCEPTION.getIntValue(), "赠送金额失败");
				}
			} else {
				for (User u : listuserservice) {
					if (null != u.getUser_id()) {
						return u;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * <p>
	 * 添加插入一个任务
	 * </p>
	 * 
	 * @Title: addTask
	 * @version:V0.1
	 * @param taskVO 新的任务对象
	 * @return:OperationalStatusVO 任务状态
	 */
	public OperationalStatusVO addTask(TaskVO taskVO) {
		int task_id = userDao.addAndGetId(taskVO);
		if (ERROR != task_id) {
			if (ERROR != userDao.updateRed(taskVO.getTotal_bounty(), taskVO.getUser_id(), ENTER_NUMBER)) {
				if (ENTER_NUMBER != taskVO.getTotal_bounty()) {
					if (ERROR != userDao.addDealByUserId(taskVO.getUser_id(),
							(taskVO.getTotal_bounty() != DOUBLE_NUMBER ? RELEASE_TASK : UNPAID_ERROR),
							-(taskVO.getTotal_bounty()))) {
						return new OperationalStatusVO(task_id, "success , Above attributes is task_id");
					}
				}
				throw new CommonException(PublicErrorCode.PARAM_EXCEPTION.getIntValue(), "无金额");
			}
			return new OperationalStatusVO(40040, "error");
		}
		return new OperationalStatusVO(40040, "error");
	}
	
	/**
	 * <p>
	 * 查询XXX用户的总金额
	 * </p>
	 * 
	 * @Title: findUserById
	 * @version:V0.1
	 * @param user_id 用户id
	 * @return:User 返回的对象
	 */
	public User findUserById(int user_id) {
		return userDao.findUserById(user_id);
	}
	
	/**
	 * 
	 * <p>查询某个用户距离当前现在6个月交易记录 </p>
	 * @param userId 用户ID
	 * @return List<Deal> 交易记录实体
	 */
	public List<Deal> findDealByUserId(Integer userId) {
		return userDao.findDealByUserId(userId);
	}
	
	/**
	 * 查询“发布过的任务”界面的总任务数，总金额数，总红包数
	 * @param userId 用户Id
	 * @return TasksPublished
	 */
	public TotalTasks getTasksPublished(Long userId) {
		List<TotalTasks> list = userDao.getTotalTask(userId);
		if(list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}
	/**
	 *查询“发布过的任务”界面的具体任务集合
	 * @param userId
	 * @return
	 */
	public List<Task> getTask(Long userId){
		return userDao.getTaskList(userId);
	}
	
	/**
	 * 查询“做过的任务”界面的总任务数，总领取金额数，总领取红包数
	 * @param userId
	 * @return
	 */
	public TotalTasks getPartner(Long userId) {
		List<TotalTasks> list = userDao.getTaskAndBounty(userId);
		List<Integer> red = userDao.getRed(userId);
		if (list.isEmpty() && red.isEmpty()) {
			return null;
		} else {
			TotalTasks totakTasks = list.get(0);
			totakTasks.setTotalRedNum(red.get(0));
			return totakTasks;
		}
	}
	/**
	 *  做过的所有任务集合
	 * @param userId
	 * @return
	 */
	public List<Partner> getPartnerList(Long userId){
		return userDao.getPartnerList(userId);
	}
	
	/**
	 * 审核界面所有参与者
	 * @param 任务Id
	 * @return
	 */
	public List<Partner> getPartnerTaskList(Long taskId){
		return userDao.getPartnerTaskList(taskId);
	}
	
	/**
	 * 审核界面top
	 * @param giveId
	 * @return
	 */
	public Task getOneTask(Long taskId) {
		Task task = userDao.getOneTask(taskId);
		if(task.getTotal_partner()==task.getPartner() && userDao.IsExamine(taskId) == 0) {
			
			//修改用户金额
			userDao.updateUser(task.getUser_id(),task.getBounty());
			//修改任务表金额
			userDao.updateTask(0.00, taskId, TASK_END);
		}
		return task;
	}
	
	/**
	 * 审核
	 */
	public Double toExamine(ToExamine examine) {
		//获取到该任务
		Task task = userDao.getOneTask(examine.getTaskId());
		
		//获取当前参与者信息
		Partner partner = userDao.getPartnerTaskt(examine.getUserId(), examine.getTaskId());
		
		if (examine.getStateCode().equals(OK_STA)) {
			//当前余额
			double bounty = task.getBounty();
			//当前已领红包数
			int redNumber = task.getRed();
			
			//当前剩余红包
			int surplus=task.getTotal_red()-redNumber;
			//如果红包领完
//			if(redNumber == task.getTotal_red()) {
//				return null;
//			}
			
			//如果还剩下最后一个红包
			if(surplus==1) {
				//修改参与者数据
				userDao.updatePartner(partner.getPartner_id(),OK_STA, bounty, String.valueOf(IdAndTimeUtil.getNewDate()));
				//修改任务表数据
				userDao.updateTask(0.00, examine.getTaskId(),TASK_END);
				
				//修改参与者用户表数据
				userDao.updateUser(examine.getUserId(),bounty);
				//插入参与者交易数据
				userDao.insertDeal(new Deal(null,partner.getUser_id(),PARTICIPATE_TASK,bounty,IdAndTimeUtil.getNewDate()));
				//插入发布者交易数据
				userDao.insertDeal(new Deal(null,task.getUser_id(),RELEASE_TASK,-task.getTotal_bounty(),IdAndTimeUtil.getNewDate()));
				return bounty;
			}
			
			//抽取红包
			double red = RedUtils.getRed(bounty, surplus);
			System.out.println(red);
			bounty=bounty-red;
			bounty = Double.valueOf(String.format("%.2f", bounty));
			System.out.println("bounty "+bounty);
			//修改参与者数据
			userDao.updatePartner(partner.getPartner_id(),OK_STA, red, String.valueOf(IdAndTimeUtil.getNewDate()));
			//修改任务表数据
			userDao.updateTask(bounty, examine.getTaskId(),RELEASE_OK);
			
			//修改参与者用户表数据
			userDao.updateUser(examine.getUserId(),red);
			//插入参与者交易数据
			userDao.insertDeal(new Deal(null,partner.getUser_id(),PARTICIPATE_TASK,red,IdAndTimeUtil.getNewDate()));
			//插入发布者交易数据
			//userDao.insertDeal(new Deal(null,task.getUser_id(),NkUtlis.PUBLISHING,-task.getTotal_bounty(),IdAndTimeUtil.getNewDate()));
			return red;
		} else {
			//未通过
			//修改参与者数据
			userDao.updatePartner(partner.getPartner_id(),NO_STA, 0.00, partner.getTime());
			return null;
		}
	}
	
	/**
	 *  任务下所有参与者
	 * @param taskId
	 * @return
	 */
	public List<PartnerList> getPartnerExamine(Long taskId){
		return userDao.getPartnerExamine(taskId);
	}
	
	/**
	 * 获得参与者信息
	 * @param userId
	 * @param taskId
	 * @return
	 */
	public Partner Partner(Long userId,Long taskId){
		if(userId==null || taskId == null) {
			throw new CommonException(PublicErrorCode.PARAM_EXCEPTION.getIntValue(), "参数不能为空");
		}
		return userDao.getPartnerTaskt(userId, taskId);
	}
	
}