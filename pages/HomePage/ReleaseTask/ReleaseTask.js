var app = getApp();
Page({
  // 页面的初始数据
  data: {
    // 将获取到的数据封装起来
    farenwu: {
      claim: "",                //任务要求
      status: "发布成功", //定义一个默认状态发布成功
      total_bounty: 0,    //总赏金
      total_partner: 0,   //红包数
      total_red: 0,         //个数
      user_id: 0,           //userId默认为0
      task_id:0,           //会返回一个任务Id
    },
    renwuyaoqiu: "",   //任务要求
    zongjine: 0,          //总金额
    hongbaoshu: 0,  //红包个数
    newOverage:0      //新的余额
  },
  // 发布任务监听事件
  loginBtnClick: function () {
    var that = this;    //当前的that等于this
    // 任务要求判断
    if (that.data.renwuyaoqiu != "") {
      // 总赏金判断
      if (that.data.zongjine > 0 && that.data.newOverage >= that.data.zongjine) {
        // 红包个数判断
        if (that.data.hongbaoshu > 0 && 100> that.data.hongbaoshu) {
          // 如果判断执行此方法
          that.addTask(that);
        } else {
          wx.showToast({
            title: '红包个数不匹配',
            image: '../../images/error.png',
            duration: 1000
          })
        }
      } else {
        wx.showToast({
          title: '红包余额不足',
          image: '../../images/error.png',
          duration: 1000
        })
      }
    } else {
      wx.showToast({
        title: '发布信息不完整',
        image: '../../images/error.png',
        duration: 1000
      })
    }
  },
  // 封装的点击事件方法
  addTask: function (that) {
    var claim = "farenwu.claim";    //任务
    var total_bounty = "farenwu.total_bounty";    //总金额
    var total_partner = "farenwu.total_partner";    //红包个数
    var user_id = "farenwu.user_id";    //用户的userId
    var total_red = "farenwu.total_red";    //红包个数
    wx.login({
      success: e => {
        // 获取界面缓存的userId
        wx.getStorage({
          key: 'userId',
          success: function (e) {
            that.setData({
              [user_id]: e.data.user_id,
              [claim]: that.data.renwuyaoqiu,
              [total_bounty]: that.data.zongjine,
              [total_partner]: that.data.hongbaoshu,
              [total_red]: that.data.hongbaoshu
            })
            // console.log("--------通过缓存获取到userId后的数据----------")
            // console.log(e);
            wx.request({
              url: 'http://10.20.58.126:8080/v1/open/task/addTask',
              method: 'POST',    //请求方式
              data: that.data.farenwu,
              header: {
                "Content-Type": "application/json"
              },
              // 请求成功后执行的方法
              success: function (e) {
                // 显示提示框
                wx.showToast({
                  title: '发布任务成功',
                  duration: 1000
                })
                // console.log("----------发布任务成功后获取到的数据-------------")
                // console.log(e)
                that.setData({
                  task_id:e.data
                })
                wx.setStorage({
                  key: 'TaskId',
                  data: e.data
                })
                // 通过缓存保存userId
                wx.setStorage({
                  key: 'farenwu',
                  data: that.data.farenwu
                })
                // console.log("-----------缓存下来的发布信息-----------------")
                // console.log(that.data.farenwu);
                // console.log(that.data.task_id.statusCodeORTaskId);
                wx.navigateTo({
                  url: "/pages/InviteDoTask/InviteDoTask?task_id="+that.data.task_id.statusCodeORTaskId,
                })
              }
            })
          }, fail: function (e) {
            console.log("请求失败")
          }
        })
      }
    })
  },
  // 任务要求监听
  renwuyaoqiu: function (event) {
    this.setData({ renwuyaoqiu: event.detail.value })
  },
  // 总金额监听
  zongjine: function (event) {
    var zongjines = parseInt(event.detail.value)
    this.setData({ zongjine: zongjines })
  },
  // 红包个数监听
  hongbaoshu: function (event) {
    var hongbaoshus = parseInt(event.detail.value)
    this.setData({ hongbaoshu: hongbaoshus })
  },
  //  生命周期函数--监听页面加载
  onLoad: function () {
  },
  //生命周期函数--监听页面初次渲染完成
  onReady: function () {
  },
  // 生命周期函数--监听页面显示
  onShow: function () {
    var that = this
    wx.getStorage({
      key: 'userId',
      success: function(res) {
        wx.request({
          url: 'http://10.20.58.126:8080/v1/open/task/findUserById/' + res.data.user_id,
          method:"GET",
          success:function(e){
            // console.log(e)
            getApp().globalData.totalMoney = e.data.overage
            that.setData({
              newOverage: e.data.overage
            })
          }
        })
      },
      
    })
    
  },
  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {
  },
  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {
  },
  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {
  },
  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {
  },
  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {
  },
})