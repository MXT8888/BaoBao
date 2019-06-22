// pages/question/question.js
const qiniuUploader = require("../../utils/qiniuUploader");
const util = require("../../utils/util");
Page({

  /**
   * 页面的初始数据
   */
  data: {
    imageURL: ''
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

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

  },

  //获取上传token
  getToken: function (filePath) {
    var that = this
    var rUrl = "http://127.0.0.1:8080/v1/open/user/getToken"
    wx.request({
      url: rUrl,
      header: {
        'content-type': 'application/json' // 默认值
      },
      success: function (res) {
        wx.hideLoading();
        that.updateImg(filePath, res.data.uptoken);
      },
      fail: function (res) {
        wx.hideLoading();
      }
    })
  },
  //上传图片到服务器
  updateImg: function (filePath, uptoken) {
    const qiniuUploader = require("../../utils/qiniuUploader")
    //随机生成文件名
    var prname = util.guid2()
    var that = this
    //上传图片
    qiniuUploader.upload(filePath, (res) => {
      //console.log(res);
      //获得上传的图片网络路径
      that.setData({
        imageURL: res.imageURL,
      });
    }, (error) => {
      console.log('error: ' + error);
    }, {
        key: prname,
        region: 'ECN',
        uptoken: uptoken,
        uploadURL: 'https://up.qiniup.com',
        domain: 'http://pt0s1sl8l.bkt.clouddn.com/',
      });

  },

  //上传图片的page中
  handleUploadImage: function () {
    var that = this
    // 通过微信的api选择图片，暂存到本地文件夹，并且通过路径名可以拿到该图片
    wx.chooseImage({
      sizeType: ['original', 'compressed'],
      sourceType: ['album', 'camera'],
      success: function (res) {//返回临时文件的路径表
        let tempFilePaths = res.tempFilePaths[0]; 
        //将本地的照片上传到服务器（返回得到该图片的网络url）
        that.getToken(tempFilePaths);
      }
    })
  },
})