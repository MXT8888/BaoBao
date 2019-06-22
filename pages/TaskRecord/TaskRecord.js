// pages/issue/issue.js
//任务记录界面
Page({

  /**
   * 页面的初始数据
   */
  data: {
    currentTab: 0,
    bg: true,  //改变颜色，默认为红色字以及红色下边框
    bg2: true,
    userInfo: {},  //用户信息
    successList: [],//发布的任务
    total_bounty: 0,
    total_red: 0,
    didList: [],//做过的任务
    didtotal_bounty: 0,
    didtotal_red: 0,
    height: "100vh",
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this
    let query = wx.createSelectorQuery();
    wx.getStorage({
      key: 'userId',
      success: function (res) {
        //发布任务请求
        wx.request({
          url: 'http://10.20.58.126:8080/v1/open/task/getTask/' + res.data.user_id * 1,
          success(e) {
            console.log("发布的任务");
            console.log(e);
            that.setData({
              successList: e.data,
              total_bounty: 0,
              total_red: 0
            })


            for (var i = 0; i < that.data.successList.length; i++) {
              if (that.data.successList[i].status == "已结束") {
                that.data.successList[i].issueShow = true
              }
              that.data.total_bounty += that.data.successList[i].total_bounty * 1//发布总金额
              that.data.total_red += that.data.successList[i].total_red//发布红包个数
            }
            that.data.didtotal_bounty = parseFloat(that.data.didtotal_bounty).toFixed(2)
            that.setData({
              total_red: that.data.total_red,
              total_bounty: that.data.total_bounty
            })
            wx.request({
              url: 'http://10.20.58.126:8080/v1/open/task/getPartnerList/' + res.data.user_id * 1,
              success(e) {
                console.log("做过得任务");
                console.log(e)
                that.setData({
                  didList: e.data,
                  didtotal_red: 0,
                  didtotal_bounty: 0
                })
                for (var i = 0; i < that.data.didList.length; i++) {

                  that.data.didtotal_bounty += that.data.didList[i].bounty * 1//获得总金额
                  if (that.data.didList[i].status == "通过") {
                    that.data.didtotal_red += 1
                    that.data.didList[i].status = that.data.didList[i].bounty
                  }
                }
                that.data.didtotal_bounty = parseFloat(that.data.didtotal_bounty).toFixed(2)
                that.setData({
                  didtotal_red: that.data.didtotal_red,
                  didtotal_bounty: that.data.didtotal_bounty,
                  didList: that.data.didList
                })

                //判断数组长度看看是否需要重新给swiper赋值
                if (that.data.didList.length > 4 || that.data.successList.length > 4) {

                  //给swpier高度赋值
                  if (that.data.didList.length > that.data.successList.length) {
                    query.select('.bot').boundingClientRect(rect => {
                      console.log(rect.height + "元素高度");
                      let height = 2 * (rect.height * that.data.didList.length + 440) + "rpx";
                      console.log(height);
                      that.setData({
                        height: height
                      });
                    }).exec();
                  } else {
                    query.select('.bot').boundingClientRect(rect => {
                      let height = 2 * (rect.height * that.data.successList.length + 440) + "rpx";
                      console.log(height);
                      that.setData({
                        height: height
                      });
                    }).exec();
                  }
                }

                console.log(that.data.didtotal_bounty);
                console.log(that.data.didtotal_red);

              }
            })


            console.log(that.data.total_red);
            console.log(that.data.total_bounty);
          },
          fail(e) {
            console.log("失败" + e)
          }
        })
        //做过任务的

      }
    })


    console.log(options)

    //获取用户信息
    wx.getUserInfo({
      success: res => {
        getApp().globalData.userInfo = res.userInfo
        this.setData({
          userInfo: res.userInfo,
          hasUserInfo: true
        })
      }
    })
    this.setData({
      userInfo: getApp().globalData.userInfo
    })
    console.log(this.data.userInfo)
  },

  swiperTab: function (e) {
    var that = this;
    that.setData({
      currentTab: e.detail.current
    })
  },
  clickIssue: function (e) {
    var that = this;
    if (this.data.currentTab === e.target.dataset.current) {
      return false;
    }
    else {
      that.setData({
        currentTab: e.target.dataset.current
      })
    }
    that.setData({
      bg2: true,
      bg: true
    })
  },
  clickDid: function (e) {
    var that = this;
    if (this.data.currentTab === e.target.dataset.current) {
      return false;
    }
    else {
      that.setData({
        currentTab: e.target.dataset.current
      })
    }
    that.setData({
      bg2: false,
      bg: false
    })
  },
  click: function (e) {
    console.log(e);
    wx.navigateTo({
      url: '/pages/InviteDoTask/InviteDoTask?task_id=' + e.currentTarget.dataset.task_id,
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
    this.onLoad();
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