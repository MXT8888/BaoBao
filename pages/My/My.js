const app = getApp()
Page({
  data: {
    userInfo: {},
    hasUserInfo: false,
    canIUse: wx.canIUse('button.open-type.getUserInfo'),
    totalMoney: 0
  },
  onShow: function() {
    var that = this
    wx.getStorage({
      key: 'userId',
      success: function(res) {
        //console.log(res)
        wx.request({
          url: 'http://10.20.58.126:8080/v1/open/task/findUserById/' + res.data.user_id,
          method:"GET",
          success:function(e){
            that.setData({
              totalMoney: e.data.overage
            })
          }
        }) 
      },
    })
  },

  //跳转至常见问题
  gotoQuestion: function() {
    wx.navigateTo({
      url: "Usualprob/Usualprob",
    })
  },
  //跳转至余额提现
  gotoCash: function () {
    wx.navigateTo({
      url: "cash/cash",
    })
  },
  //跳转至交易记录
  gotoRecord: function() {
    wx.navigateTo({
      url: "DealRecord/DealRecord",
    })
  },
  onLoad: function() {
    var that = this;
    //console.log(getApp().globalData.totalMoney)

    that.setData({
      totalMoney: getApp().globalData.totalMoney
    })

    if (app.globalData.userInfo) {
      this.setData({
        userInfo: app.globalData.userInfo,
        hasUserInfo: true
      })
    } else if (this.data.canIUse) {
      // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
      // 所以此处加入 callback 以防止这种情况
      app.userInfoReadyCallback = res => {
        this.setData({
          userInfo: res.userInfo,
          hasUserInfo: true
        })
      }
    } else {
      // 在没有 open-type=getUserInfo 版本的兼容处理
      wx.getUserInfo({
        success: res => {
          app.globalData.userInfo = res.userInfo
          this.setData({
            userInfo: res.userInfo,
            hasUserInfo: true
          })
        }
      })
    }
  },
  getUserInfo: function(e) {
    //console.log(e)
    app.globalData.userInfo = e.detail.userInfo
    this.setData({
      userInfo: e.detail.userInfo,
      hasUserInfo: true
    })
  }
})