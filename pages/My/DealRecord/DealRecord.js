Page({
  data: {
    requestLoading:false,
    userid:"3",
    recordlist:[]
  },
  onLoad:function(option){
    var that = this;
    var recordlist = that.data;
    //请求交易记录数据
    wx.getStorage({
      key: 'userId',
      success(e){
        that.setData({
          userid: e.data.user_id
        })
        wx.request({
          url: 'http://10.20.58.126:8080/v1/open/task/findDeal/' + that.data.userid*1,
          method: "GET",
          success(e) {
            console.log("交易记录数据")
            console.log(e)
            that.setData({
              recordlist: e.data
            })
            // 控制完成任务的钱和发布任务的钱所显示的颜色
            for (var index in that.data.recordlist) {
              if (that.data.recordlist[index].sum > 0) {
                // 大于0时字体变为红色
                that.data.recordlist[index].show = true;
                console.log(that.data.recordlist)
                that.setData({
                  recordlist: that.data.recordlist
                })
                console.log(recordlist)
              } else {
                // 小于0时字体变为黑色
                that.data.recordlist[index].show = false;
                that.setData({
                  recordlist: that.data.recordlist
                })
              }
            }
          }
        })
      }
    })
  },
  //下拉触底事件：没有更多了～
  onReachBottom: function () {
    var that = this;
    that.setData({
      requestLoading: true
    })
  }
})