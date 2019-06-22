Page({
  data: {
  },
  gotoUsualprob:function(){
    wx.navigateTo({
      url: '../Usualprob/Usualprob',
    })
  },
  //提交按钮功能
  gotoCash:function(){
    wx.showModal({
      title: '提示',
      content: '提现申请已提交，将在1-5个工作日之内到达您的微信钱包',
      // 确认按钮的文字，最多 4 个字符
      confirmText: "确定",
      // 取消按钮的文字，最多 4 个字符
      cancelText: "取消",
      //是否显示取消按钮
      showCancel: false,
      confirmColor: "#07c160"
    })
  }
})