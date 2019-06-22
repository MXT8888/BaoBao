Page({
  // 页面的初始数据
  data: {
    animation: '',
    animation2: ''
  },
  // 初始化加载方法
  onLoad: function (options) {
  },
  // 下拉刷新方法
  onPullDownRefresh: function () {
  },
  // 点击多多和其他会弹出提示框
  diandian:function(){
    wx.showToast({
      title: '功能开发中，敬请期待哦',
      icon: 'none',
      duration: 1000//持续的时间
    })
  },
  // 点击包包开始大冒险
  damaoxian: function () {
    wx.navigateTo({
      url: '../HomePage/ReleaseTask/ReleaseTask',
    })
  }
})
