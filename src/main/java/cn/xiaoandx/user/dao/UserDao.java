package cn.xiaoandx.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import cn.xiaoandx.commons.core.PublicErrorCode;
import cn.xiaoandx.commons.exception.CommonException;
import cn.xiaoandx.commons.utils.IdAndTimeUtil;
import cn.xiaoandx.user.entity.Deal;
import cn.xiaoandx.user.entity.Partner;
import cn.xiaoandx.user.entity.Task;
import cn.xiaoandx.user.entity.User;
import cn.xiaoandx.user.vo.DealPageSaVO;
import cn.xiaoandx.user.vo.PartnerList;
import cn.xiaoandx.user.vo.PartnerVO;
import cn.xiaoandx.user.vo.TaskVO;
import cn.xiaoandx.user.vo.TotalTasks;
import cn.xiaoandx.user.vo.WUserVO;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UserDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	/**  
	 *<p>根据opengid查询是否该微信用户注册过</p> 
	 * @Title: findUser    
	 * @version:V2.0     
	 * @param openid	用户的openid
	 * @return:List<User>	xx用户的数据集合
	 */
	public List<User> findUser(String openid) {
		RowMapper<User> rowMapper=new BeanPropertyRowMapper<User>(User.class);
		String sql = "SELECT u.`user_id`,u.`head_portrait`,u.`nickname`,u.`overage` "
				+ "FROM `user` AS u "
				+ "WHERE u.`open_id` = ?";
		return jdbcTemplate.query(sql, rowMapper, openid);
	}

	/**  
	 *<p>注册用户</p> 
	 * @Title: reUser    
	 * @version:V2.0     
	 * @param user	包含code与用户信息的对象
	 * @param openid	用户的openid
	 * @return:int	操作结果
	 */
	public int reUser(WUserVO user, String openid) {
		String sql = "INSERT INTO  `user` "
				+ "(`open_id`,`head_portrait`,`nickname`,`overage`) "
				+ "VALUES (?,?,?,?)";
		int number = jdbcTemplate.update(sql,openid,user.getHead_portrait(),user.getNickname(),"200.00");
		log.info("==========注册用户========"+String.valueOf(number));
		return number;
	}

	/**  
	 *<p>获取用户数据</p> 
	 * @Title: findByOpenId    
	 * @version:V2.0     
	 * @param openid	用户的openId
	 * @return:User		用户信息对象
	 */
	public User findByOpenId(String openid) {
		RowMapper<User> rowMapper=new BeanPropertyRowMapper<User>(User.class);
		String sql = "SELECT u.`user_id`,u.`head_portrait`,u.`nickname`,u.`overage` "
				+ "FROM `user` AS u "
				+ "WHERE u.`open_id` = ?";
		return jdbcTemplate.queryForObject(sql, rowMapper, openid);
	}
	
	/**  
	 *<p>交易记录（初始化）</p> 
	 * @Title: defManay    
	 * @version:V0.1     
	 * @param user2
	 * @return:int
	 */
	public int addDealByUserId(Long userId, String cont, double sum) {
		String sql = "INSERT INTO  `deal` "
				+ "(`user_id`,`content`,`sum`,`time`) "
				+ "VALUES (?,?,?,?)";
		int number = jdbcTemplate.update(sql,userId,cont,sum,IdAndTimeUtil.getNewDate());
		return number;
	}
	
	/**
	 *<p>添加新的任务(版本二)</p> 
	 * @Title: addTask    
	 * @version:V0.1     
	 * @param taskVO 	发布任务数据对象
	 * @return:int		该新任务的id
	 */
	public int addAndGetId(TaskVO taskVO) {
		String sql = "INSERT INTO task "
				+ "(user_id,`status`,claim,bounty,total_bounty,total_red,total_partner,`time`) "
				+ "VALUES (?,?,?,?,?,?,?,?)";
	    KeyHolder keyHolder = new GeneratedKeyHolder();
	    jdbcTemplate.update(new PreparedStatementCreator() {
	        @Override
	        public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
	            PreparedStatement ps  = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
	            ps.setObject(1, taskVO.getUser_id());
	            ps.setObject(2, (taskVO.getTotal_bounty() == 0 ? "未支付" : "发布成功"));
	            ps.setObject(3, taskVO.getClaim());
	            ps.setObject(4, taskVO.getTotal_bounty());
	            ps.setObject(5, taskVO.getTotal_bounty());
	            ps.setObject(6, taskVO.getTotal_red());
	            ps.setObject(7, taskVO.getTotal_partner());
	            ps.setObject(8, IdAndTimeUtil.getNewDate());
	            return ps;
	        }
	    }, keyHolder);
	    return keyHolder.getKey().intValue();
	}
	
	/**  
	 *<p>更新用户表的余额信息</p> 
	 * @Title: updateRed    
	 * @version:V0.1     
	 * @param total_bounty	发布一个任务包的红包数
	 * @param user_id		用户id
	 * @return:Integer		状态
	 */
	public Integer updateRed(double total_bounty, Long user_id, int staY) {
		String aft = "UPDATE `user` SET overage = overage ";
		String endf = " ? WHERE user_id = ?";
		String sql = null;
		if(0 == staY) {
			sql = aft + "-" + endf;
		}else {
			sql = aft + "+" + endf;
		}
		return jdbcTemplate.update(sql, total_bounty, user_id);
	}
	
	/**
	 *<p>查询XXX用户的总金额</p> 
	 * @Title: findUserById    
	 * @version:V0.1     
	 * @param user_id	用户id
	 * @return:User		返回的对象
	 */
	public User findUserById(int user_id) {
		RowMapper<User> rowMapper=new BeanPropertyRowMapper<User>(User.class);
		String sql = "SELECT u.`user_id`,u.`overage` "
				+ "FROM `user` AS u "
				+ "WHERE u.`user_id` = ?";
		try {
			return jdbcTemplate.queryForObject(sql, rowMapper, user_id);
		} catch (DataAccessException e) {
			throw new CommonException(PublicErrorCode.QUERY_EXCEPTION.getIntValue(), "user_id Non-existent - select userTable overage error");
		}
	}
	public User findUserByIds(Long user_id) {
		RowMapper<User> rowMapper=new BeanPropertyRowMapper<User>(User.class);
		String sql = "SELECT u.`user_id`,u.`overage` "
				+ "FROM `user` AS u "
				+ "WHERE u.`user_id` = ?";
		try {
			return jdbcTemplate.queryForObject(sql, rowMapper, user_id);
		} catch (DataAccessException e) {
			throw new CommonException(PublicErrorCode.QUERY_EXCEPTION.getIntValue(), "user_id Non-existent - select userTable overage error");
		}
	}
	
	
	/**  
	 * 
	 *<p>根据用户userId查询某个用户所有交易记录</p> 
	 * @Title: findDealByUserID    
	 * @version:V0.1     
	 * @param userId
	 * @return:List<Deal>
	 */
	public List<Deal> findDealByUserId(Integer userId) {
		RowMapper<Deal> rowMapper = new BeanPropertyRowMapper<Deal>(Deal.class);
		String optionSql = "select `deal_id`,`user_id`,`content`,`sum`,`time` from deal WHERE user_id = ? ORDER BY time DESC";
		return jdbcTemplate.query(optionSql, rowMapper,userId);
	}
	
	/**
	 * 查询“发布过的任务”界面的总任务数，总金额数，总红包数
	 * @param userId 用户Id
	 * @return
	 */
	public List<TotalTasks> getTotalTask(Long userId){
		String sql = "SELECT COUNT(*) `t_task`,IFNULL(SUM(`total_bounty`),0) `t_bounty`,IFNULL(SUM(`total_red`),0) `t_red` FROM `task` WHERE `user_id`=?";
		List<TotalTasks> list = jdbcTemplate.query(sql,new Object[] {userId}, (ResultSet rs,int rowNum)->new TotalTasks(rs.getInt("t_task"),rs.getDouble("t_bounty"),rs.getInt("t_red")));
		return list;
	}
	
	/**
	 * 查询“发布过的任务”界面的具体任务集合
	 * @param userId
	 * @return
	 */
	public List<Task> getTaskList(Long userId){
		String sql ="SELECT `task_id`,`user_id`,`status`,`claim`,`bounty`,`total_bounty`,`red`,"
				+ "`total_red`,`partner`,`total_partner`,`time` FROM task WHERE `user_id`=? "
				+ "ORDER BY time DESC";
		List<Task> list = jdbcTemplate.query(sql,new Object[] {userId}, (ResultSet rs,int rowNum)->new Task(rs.getLong("task_id"),rs.getLong("user_id"),
				rs.getString("status"),rs.getString("claim"),rs.getDouble("bounty"),rs.getDouble("total_bounty"),rs.getInt("red"),rs.getInt("total_red"),
				rs.getInt("partner"),rs.getInt("total_partner"),rs.getString("time")));
		return list;
	}
	
	/**
	 * 查询参与者的总任务数和总领取金额数
	 * @param userId 用户Id
	 * @return
	 */
	public List<TotalTasks> getTaskAndBounty(Long userId){
		String sql = "SELECT COUNT(*) `t_task`,IFNULL(SUM(`bounty`),0) `t_bounty` FROM `partner` WHERE `user_id`=?";
		List<TotalTasks> list = jdbcTemplate.query(sql,new Object[] {userId}, (ResultSet rs,int rowNum)->new TotalTasks(rs.getInt("t_task"),rs.getDouble("t_bounty")));
		return list;
	}
	
	/**
	 * 查询领取红包数
	 * @param userId
	 * @return
	 */
	public List<Integer> getRed(Long userId){
		String sql = "SELECT COUNT(*) `t_red` FROM `partner` WHERE `user_id`=? AND status='通过'";
		List<Integer> list = jdbcTemplate.query(sql,new Object[] {userId}, (ResultSet rs,int rowNum)->rs.getInt("t_red"));
		return list;
	}
	
	/**
	 * 参与表查询
	 * @param userId
	 * @return
	 */
	public List<Partner> getPartnerList(Long userId){
		String sql = "SELECT `partner_id`,`user_id`,`task_id`,`image_url`,`is_public`,`note`,`status`,`bounty`,`time` FROM `partner` WHERE `user_id`=? ORDER BY time DESC";
		List<Partner> list = jdbcTemplate.query(sql,new Object[] {userId}, 
				(ResultSet rs,int rowNum)->new Partner(rs.getLong("partner_id"),rs.getLong("user_id"),rs.getLong("task_id"),rs.getString("image_url"),rs.getInt("is_public"),
								rs.getString("note"),rs.getString("status"),rs.getDouble("bounty"),rs.getString("time")));
		return list;
	}
	
	public Task getOneTask(Long taskId) {
		String sql = "SELECT `task_id`,`user_id`,`status`,`claim`,`bounty`,`total_bounty`,`red`,`total_red`,`partner`,`total_partner`,`time` FROM task WHERE task_id=? ORDER BY time DESC";
		List<Task> list = jdbcTemplate.query(sql,new Object[] {taskId}, (ResultSet rs,int rowNum)->new Task(rs.getLong("task_id"),rs.getLong("user_id"),
				rs.getString("status"),rs.getString("claim"),rs.getDouble("bounty"),rs.getDouble("total_bounty"),rs.getInt("red"),rs.getInt("total_red"),
				rs.getInt("partner"),rs.getInt("total_partner"),rs.getString("time")));
		if(list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}
	
	/**
	 * 根据任务Id参与表查询
	 * @param taskId
	 * @return
	 */
	public List<Partner> getPartnerTaskList(Long taskId){
		String sql = "SELECT `partner_id`,`user_id`,`task_id`,`image_url`,`is_public`,`note`,`status`,`bounty`,`time` FROM `partner` WHERE `task_id`=? ORDER BY time DESC";
		List<Partner> list = jdbcTemplate.query(sql,new Object[] {taskId}, 
				(ResultSet rs,int rowNum)->new Partner(rs.getLong("partner_id"),rs.getLong("user_id"),rs.getLong("task_id"),rs.getString("image_url"),rs.getInt("is_public"),
								rs.getString("note"),rs.getString("status"),rs.getDouble("bounty"),rs.getString("time")));
		return list;
	}
	
	/**
	 * 获得参与者任务
	 * @param userId
	 * @param taskId
	 * @return
	 */
	public Partner getPartnerTaskt(Long userId,Long taskId){
		String sql = "SELECT `partner_id`,`user_id`,`task_id`,`image_url`,`is_public`,`note`,`status`,`bounty`,`time` FROM `partner` WHERE `user_id`=? AND task_id=?";
		List<Partner> list = jdbcTemplate.query(sql,new Object[] {userId,taskId}, 
				(ResultSet rs,int rowNum)->new Partner(rs.getLong("partner_id"),rs.getLong("user_id"),rs.getLong("task_id"),rs.getString("image_url"),rs.getInt("is_public"),
							rs.getString("note"),rs.getString("status"),rs.getDouble("bounty"),rs.getString("time")));
		if(list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}
	
	/**
	 * 修改参与者表
	 * @param partnerId
	 * @param note
	 * @param status
	 * @param bounty
	 * @param time
	 */
	public int updatePartner(Long partnerId,String status,Double bounty,String time) {
		String sql = "UPDATE `partner` SET `status`=?,`bounty`=?,time = ? WHERE `partner_id`=?";
		return jdbcTemplate.update(sql, new Object[] {status,bounty,time,partnerId}, new int[] {Types.VARCHAR,Types.DOUBLE,Types.VARCHAR,Types.BIGINT});
	}
	
	/**
	 * 修改任务表数据
	 * @param bounty 余额
	 * @param taskId 任务Id
	 * @return 
	 */
	public int updateTask(Double bounty,Long taskId,String status) {
		String sql = "UPDATE `task` SET bounty=?,red=red+1,`status`=? WHERE task_id =?";
		return jdbcTemplate.update(sql, new Object[] {bounty,status,taskId}, new int[] {Types.DOUBLE,Types.VARCHAR,Types.BIGINT});
	}
	
	/**
	 * 修改任务表金额数据
	 * @param bounty 余额
	 * @param taskId 任务Id
	 * @return 
	 */
	public int updateTaskMo(Double bounty,Long taskId,String status) {
		String sql = "UPDATE `task` SET bounty=?,`status`=? WHERE task_id =?";
		return jdbcTemplate.update(sql, new Object[] {bounty,status,taskId}, new int[] {Types.DOUBLE,Types.VARCHAR,Types.BIGINT});
	}
	 
	 /***
	  * 参与者的总余额修改
	  * @param userId 用户id
	  * @param overage 肮脏的金钱
	  */
	public int updateUser(Long userId,Double overage) {
		String sql = "UPDATE `user` SET overage=overage+? WHERE user_id=?";
		return jdbcTemplate.update(sql, new Object[] {overage,userId}, new int[] {Types.DOUBLE,Types.BIGINT});
	}
	
	/**
	 * 交易记录表插入数据
	 */
	public int insertDeal(Deal deal) {
		String sql = "INSERT INTO `deal` (`user_id`,`content`,`sum`,`time`) VALUES (?,?,?,?)";
		return jdbcTemplate.update(sql, new Object[] {deal.getUser_id(),deal.getContent(),deal.getSum(),deal.getTime()}, new int[] {Types.BIGINT,Types.VARCHAR,Types.DOUBLE
				,Types.TIMESTAMP});
	}
	
	/**
	 * 判断是否审核完
	 * @param taskId
	 * @return
	 */
	public Integer IsExamine(Long taskId) {
		String sql = "SELECT COUNT(*) num FROM partner WHERE task_id=? AND `status`='待审核'";
		List<Integer> list = jdbcTemplate.query(sql, new Object[] {taskId}, (ResultSet rs,int rowNum)->rs.getInt("num"));
		if(list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}
	
	/**
	 * 
	 * @param taskId 任务id
	 * @return
	 */
	public List<PartnerList> getPartnerExamine(Long taskId){
		String sql = "SELECT u.`head_portrait`,u.`nickname`,p.`partner_id`,p.`user_id`,p.`task_id`,p.`image_url`,p.`is_public`,p.`note`,p.`status`,p.`bounty`,p.`time` FROM `partner` AS p JOIN `user` AS u ON p.user_id = u.user_id WHERE `task_id`=? ORDER BY time DESC";
		List<PartnerList> list = jdbcTemplate.query(sql,new Object[] {taskId}, 
				(ResultSet rs,int rowNum)->new PartnerList(rs.getString("head_portrait"),rs.getString("nickname"),rs.getLong("partner_id"),rs.getLong("user_id"),rs.getLong("task_id"),rs.getString("image_url"),rs.getInt("is_public"),
						rs.getString("note"),rs.getString("status"),rs.getDouble("bounty"),rs.getString("time")));
		return list;
	}
	
	/**  
	 *<p>提交保存xx用户参与的任务数据</p> 
	 * @Title: addPartner    
	 * @version:V0.1     
	 * @param partnerVO	参与任务数据
	 * @param defaultSta 默认状态（待审核）
	 * @param doubleNumber 默认金额（0）
	 * @return:int	操作状态
	 */
	public int addPartner(PartnerVO partnerVO, String defaultSta, Double doubleNumber) {
		String sql = "INSERT INTO partner(user_id, task_id, image_url, is_public, note, `status`, bounty, time) "
				+ "VALUES (?,?,?,?,?,?,?,?)";
		return jdbcTemplate.update(sql, partnerVO.getUser_id(), partnerVO.getTask_id(), 
				partnerVO.getImage_url(), partnerVO.getIs_public(), partnerVO.getNote(), 
				defaultSta, doubleNumber, IdAndTimeUtil.getNewDate());
	}
	
	/**  
	 *<p>获得xx任务详情</p> 
	 * @Title: findTaskByTaskId    
	 * @version:V0.1     
	 * @param task_id	任务id
	 * @return:Task		任务对象
	 */
	public Task findTaskByTaskId(int task_id) {
		try {
			RowMapper<Task> rowMaprrer = new BeanPropertyRowMapper<Task>(Task.class);
			String sql = "SELECT task_id,user_id,`status`,claim,bounty,total_bounty,red,total_red,partner,total_partner,time FROM task WHERE task_id = ?";
			return jdbcTemplate.queryForObject(sql, rowMaprrer, task_id);
		} catch (DataAccessException e) {
			throw new CommonException(PublicErrorCode.QUERY_EXCEPTION.getIntValue(), "task_id Non-existent ");
		}
		
	}
	
	/**  
	 *<p>更新xx任务的参与人数</p> 
	 * @Title: updateByTaskId    
	 * @version:V0.1     
	 * @param task_id	任务id
	 * @return:int	操作结果
	 */
	public int updateByTaskId(int task_id) {
		String sql = "UPDATE task SET partner = partner + 1 WHERE task_id = ?";
		return jdbcTemplate.update(sql,task_id);
	}

	/**  
	 *<p>查询xx用户的所有交易记录数</p> 
	 * @Title: findByUserIdCount    
	 * @version:V0.1     
	 * @param userId
	 * @return:Integer
	 */
	public DealPageSaVO findByUserIdCount(Long userId) {
		RowMapper<DealPageSaVO> rowMapper = new BeanPropertyRowMapper<DealPageSaVO>(DealPageSaVO.class);
		String sql = "SELECT COUNT(*) as pageCount FROM deal WHERE user_id = ?";
		return jdbcTemplate.queryForObject(sql, rowMapper, userId);
	}

	/**  
	 *<p>查询具体的分页数据</p> 
	 * @Title: finDealById    
	 * @version:V0.1     
	 * @param userId
	 * @param currentPage
	 * @param pageSize
	 * @return    
	 * @return:List<Deal>
	 */
	public List<Deal> finDealById(Long userId, Integer currentPage, Integer pageSize) {
		RowMapper<Deal> rowMapper = new BeanPropertyRowMapper<Deal>(Deal.class);
		int currentPageo = (currentPage - 1) * pageSize;
		try {
			String sql = "SELECT deal_id,user_id,content,sum,time FROM deal WHERE user_id = ? ORDER BY `time` DESC LIMIT ?, ?";
			return jdbcTemplate.query(sql, rowMapper, userId, currentPageo, pageSize);
		} catch (DataAccessException e) {
			throw new CommonException(PublicErrorCode.QUERY_EXCEPTION.getIntValue(), "user OR currentPage OR pageSize error");
		}
	}

	/**  
	 *<p>根据userId+taskId</p> 
	 * @Title: findParByUserId    
	 * @version:V0.1     
	 * @param task_id	任务id
	 * @param user_id	用户id
	 * @return:List<Partner>	数据集合
	 */
	public List<Partner> findParByUserId(int task_id, int user_id) {
		try {
			RowMapper<Partner> rowMapper = new BeanPropertyRowMapper<Partner>(Partner.class);
			String sql = "SELECT partner_id, user_id, task_id,`status` FROM partner WHERE task_id =?  AND user_id =?";
			return jdbcTemplate.query(sql, rowMapper, task_id, user_id);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			throw new CommonException(PublicErrorCode.QUERY_EXCEPTION.getIntValue(), "user_id OR task_id OR Non-existent");
		}
	}

	/**  
	 *<p>查询所有的发布成功还未完结的列表</p> 
	 * @Title: findBySta    
	 * @version:V0.1     
	 * @param releaseOk
	 * @return:List<Task>
	 */
	public List<Task> findBySta(String releaseOk) {
		String sql ="SELECT `task_id`,`user_id`,`status`,`claim`,`bounty`,`total_bounty`,`red`,"
				+ "`total_red`,`partner`,`total_partner`,`time` FROM task WHERE `status`=? "
				+ "ORDER BY time ASC";
		List<Task> list = jdbcTemplate.query(sql,new Object[] {releaseOk}, (ResultSet rs,int rowNum)->new Task(rs.getLong("task_id"),rs.getLong("user_id"),
				rs.getString("status"),rs.getString("claim"),rs.getDouble("bounty"),rs.getDouble("total_bounty"),rs.getInt("red"),rs.getInt("total_red"),
				rs.getInt("partner"),rs.getInt("total_partner"),rs.getString("time")));
		return list;
	}

	/**  
	 *<p>判断xx任务的审核状况</p> 
	 * @Title: findParByTaskId    
	 * @version:V0.1     
	 * @param task_id
	 * @param defaultSta
	 * @return:List<Partner>
	 */
	public List<Partner> findParByTaskId(Long task_id, String defaultSta) {
		try {
			RowMapper<Partner> rowMapper = new BeanPropertyRowMapper<Partner>(Partner.class);
			String sql = "SELECT partner_id, user_id, task_id,`status` FROM partner WHERE task_id =?  AND status =?";
			return jdbcTemplate.query(sql, rowMapper, task_id, defaultSta);
		} catch (DataAccessException e) {
			// TODO Auto-generated catch block
			throw new CommonException(PublicErrorCode.QUERY_EXCEPTION.getIntValue(), "defaultSta OR task_id OR Non-existent");
		}
	}
}
