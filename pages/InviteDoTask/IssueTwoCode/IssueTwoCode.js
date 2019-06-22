// pages/share/share.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    QRcode: "",
    userInfo: "",
    display: false,
    task_id: 0
  },
  //模态窗
  click: function () {
    if (this.data.display != true) {
      this.setData({
        display: true
      })
    } else {
      this.setData({
        display: false
      })
    }

  },
  //进入画布转图片、保存图片页面
  img: function () {
    var that = this
    wx.navigateTo({
      url: "/pages/InviteDoTask/IssueImages/IssueImages?qrcode=" + this.data.QRcode + "&task_id=" + that.data.task_id,
    })

    console.log("画布转图片")
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (e) {
    console.log(e)
    var that = this;
    that.setData({
      task_id: e.statusCodeORTaskId
    })
    //通过微信接口拿到access_token 两小时刷新一次
    wx.request({
      url: 'https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wxe3ab9e4a9e5b3c00&secret=e780fad35e15d943566cebed72779baf',
      method: "GET",
      header: {
        'Content-Type': 'application/json' // 默认值
      },
      success: function (enevt) {

        console.log(enevt);
        var access_token = enevt.data.access_token;
        //请求接口生成二维码
        wx.request({
          url: 'https://api.weixin.qq.com/cgi-bin/wxaapp/createwxaqrcode?access_token=' + access_token,
          method: 'POST',
          //传递二维码参数
          data: {
            path: "pages/InviteDoTask/IssueImages/IssueImages",
            width: 280,
            task_id: e.statusCodeORTaskId
          },
          header: {
            'Content-Type': 'application/json' // 默认值
          },

          responseType: "arraybuffer",
          //在二维码生成后转码为base64
          success: function (res) {
            var url = wx.arrayBufferToBase64(res.data)
            that.data.QRcode = url
            console.log("%c 小程序二维码", "color:red;font-size:30px;");
            console.log(res);
            console.log(url);

            that.setData({
              QRcode: url
            })

          },

        })
      }
    })
    //获取用户信息
    wx.getUserInfo({
      success: function (res) {
        console.log(res);
        that.setData({
          userInfo: res.userInfo
        })
      }
    })
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {

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

  }
})