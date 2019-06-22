const app = getApp();
Page({
  data: {
    partner: [{
      head_portrait: 'http://img4.imgtn.bdimg.com/it/u=2643267806,3088281804&fm=26&gp=0.jpg',
      image_url: 'http://img4.imgtn.bdimg.com/it/u=2643267806,3088281804&fm=26&gp=0.jpg',
      note: '',
      status: '',
    }],
    sum_money: 100,
    sum_page: 10,
    receive_page: 5,
    task: "",
    task_id: "1",
    but: "做任务"
  },
  onLoad: function (options) {
    var that = this
    wx.request({ //参与者信息
      url: 'http://10.20.58.126:8080/v1/open/task/getPartnerExamine/' + app.globalData.task_id,
      method: "GET",
      dataType: 'json',
      header: { //头部返回信息
        'content-type': 'application/json'
      },
      success: function (res) {
        console.log(res)
        that.setData({
          partner: res.data
        })
      },
      fail: function (res) { },
      complete: function (res) { },
    })
    wx.request({ //请求任务总信息
      url: 'http://10.20.58.126:8080/v1/open/task/getOneTask/' + app.globalData.task_id,
      method: "POST",
      dataType: 'json',
      header: { //头部返回信息
        'content-type': 'application/json'
      },
      success: function (res) {
        console.log(res)
        that.setData({
          task: res.data.claim,
          sum_page: res.data.total_red,
          sum_money: res.data.total_bounty,
          receive_page: res.data.red,
        })
        if (res.data.red >= res.data.total_red) {
          that.setData({
            but: '任务已经结束'
          })
        }
      },
      fail: function (res) { },
      complete: function (res) { },
    })
  },

  to() {
    var that = this
    if (that.data.but == '做任务') {
      wx.navigateTo({
        url: '../DoTask/UploadingImages/UploadingImages',
        success: function (res) { },
        fail: function (res) { },
        complete: function (res) { },
      })
    } else {
      wx.showModal({
        title: '提示',
        content: '任务已经结束了，去试试其他的吧',
        success: function (res) {
          if (res.confirm) {
            console.log('用户点击确定');
            wx.redirectTo({
              url: '../HomePage/Authorize/Authorize',
              success: function (res) {
                console.log("123123")
              },
              fail: function (res) {
                console.log(res);
                console.log("失败");
              },
              complete: function (res) {
                console.log("没进来");
              },
            })
          }
        }
      })
    }
  }
})