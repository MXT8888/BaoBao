Page({
  // 页面的初始数据
  data: {
    // windowWidth: "",
    // windowHeight: "",
    text: "通过",
    textNo: "未通过",
    result: "",
    taskContent: "最多显示十五个字，显示十五个字",
    resultDisplay: false,//显示审核是否通过
    display: false,//控制模态窗的显示
    header: "https://wx.qlogo.cn/mmopen/vi_32/XXhmfBm43pXVadqR3q4iah1WooPJFJsFFvibKQQ1IyHS2aJM9Khn3ED84B0xKL7onHCgK1bS9PbwGJlgKy1qEGkQ/132",
    taskInfo: "",//题目的详细信息
    user_id: "",
    participant: [

    ],
    click_id: '',//点击某个用户时的用户id
    click_task_id: "",//题目id
    btn: true,//邀请按钮的控制
    hint: "hint",
    index: ""
  },
  // 邀请做任务事件
  click: function (e) {
    wx.navigateTo({
      url: "/pages/InviteDoTask/IssueTwoCode/IssueTwoCode?statusCodeORTaskId=" + this.data.task_id,
    })
  },
  // 生命周期函数--监听页面加载
  onLoad: function (res) {
    var that = this;
    wx.getStorage({
      key: 'userId',
      success: function (e) {
        that.setData({
          user_id: e.data.user_id,
          task_id: res.task_id
        })
        that.taskInformation();
      },
    })
  },
  //获取当前任务的参与者的详细信息
  taskInformation() {
    var that = this;
    wx.request({
      url: 'http://10.20.58.126:8080/v1/open/task/getOneTask/' + that.data.task_id,
      method: "POST",
      success(e) {
        console.log("--------------------当前任务的详细信息---------------------")
        console.log(e);
        that.setData({
          taskInfo: e.data
        })
        if (e.data.user_id == that.data.user_id) {
          wx.request({
            url: 'http://10.20.58.126:8080/v1/open/task/getPartnerExamine/' + that.data.task_id,
            success(e) {
              console.log("---------------------发布者下的参与者页面--------------------------");
              console.log(e)
              that.setData({
                participant: e.data
              })
              for (var i = 0; i < that.data.participant.length; i++) {
                if (that.data.participant[i].status == "待审核") {
                  that.data.participant[i].hint = true
                } else if (that.data.participant[i].status == "通过") {
                  that.data.participant[i].status = that.data.participant[i].bounty + "元"
                }
              }
              that.setData({
                participant: that.data.participant
              })
            }
          })
        } else {
          wx.request({
            url: 'http://10.20.58.126:8080/v1/open/task/pantner?userId=' + that.data.user_id + '&taskId=' + that.data.task_id,
            success(e) {
              console.log("-------------普通参与者页面---------------------");
              console.log(e)
              that.setData({
                participant: e.data,
              })
              if (that.data.participant.status == "通过") {//如果是已通过的状态就显示金额
                that.data.taskInfo.bounty = that.data.participant.bounty,
                  that.data.taskInfo.sta = that.data.participant.status
                that.data.btn = false
              } else if (i = 0) {
              } else {//如果是未通过状态就显示当前状态
                that.data.taskInfo.bounty = that.data.participant.status
                that.data.taskInfo.sta = "已提交"
                that.data.btn = false
              }
              that.setData({
                btn: that.data.btn,
                taskInfo: that.data.taskInfo
              })
            }
          })
          //查询参与者界面下的参与者信息
          wx.request({
            url: 'http://10.20.58.126:8080/v1/open/task/getPartnerExamine/' + that.data.task_id,
            success(e) {
              console.log("-------------发布者下的参与者页面-----------------");
              console.log(e)
              that.setData({
                participant: e.data
              })
              for (var i = 0; i < that.data.participant.length; i++) {
                if (that.data.participant[i].status == "待审核") {
                } else if (that.data.participant[i].status == "通过") {
                  that.data.participant[i].status = that.data.participant[i].bounty + "元"
                }
              }
              that.setData({
                participant: that.data.participant
              })
            }
          })
        }
      }
    })
  },
  
  hint: function (e) {
    var that = this;
    that.setData({
      click_id: e.currentTarget.dataset.user_id,
      click_task_id: e.currentTarget.dataset.click_task_id,
      hint: "",
      index: e.currentTarget.dataset.index,
    })
    this.setData({
      display: true
    })
  },
  close: function (e) {
    var that = this;
    console.log(e)
    console.log(e.currentTarget.dataset.text)
    wx.request({
      url: 'http://10.20.58.126:8080/v1/open/task/toExamine',
      method: "POST",
      data: {
        "stateCode": e.currentTarget.dataset.text,
        "taskId": that.data.click_task_id,
        "userId": that.data.click_id
      },
      success(e) {
        console.log(e);
        that.taskInformation();
      }
    })
    this.setData({
      display: false,
      hint: false,
      result: e.currentTarget.dataset.text,
      resultDisplay: false
    })
  },
  TaskContent: function (e) {
    wx.navigateTo({
      url: '/pages/taskContent/content?img_url=' + e.currentTarget.dataset.img_url,
    })
  },
})