// pages/participant/task_content/task_content.js
const qiniuUploader = require("../../../utils/qiniuUploader");
const util = require("../../../utils/util");
const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    task: '',
    sum_money: '',
    task_id: '1',
    imageURL: '',
    photo: "+",
    pd: true,
    name: '',
    show: 1
  },
  upload_photo() {
    var that = this
    // 通过微信的api选择图片，暂存到本地文件夹，并且通过路径名可以拿到该图片
    wx.chooseImage({
      sizeType: ['original', 'compressed'], //相片是压缩还是原画 
      sourceType: ['album', 'camera'], //相机或者拍照
      success: function (res) {
        let tempFilePaths = res.tempFilePaths[0];
        //将本地的照片上传到服务器（返回得到该图片的网络url）
        that.getToken(tempFilePaths);
      }
    })
  },
  add_photo() {
    var that = this
    console.log(that.data.name);
    console.log(that.data.imageURL);
    console.log(that.data.show);
    console.log(that.data.task_id);
    console.log(app.globalData.user_id);
    if (that.data.imageURL == '') {
      wx.showModal({
        title: '提示',
        content: '请添加任务图片'
      })
    } else {
      if (that.data.name == '') {
        wx.showModal({
          title: '提示',
          content: '请添加文字'
        })
      } else {
        wx.showModal({
          title: '提示',
          content: '确定上传任务图片？',
          success: function (res) {
            if (res.confirm) {
              wx.request({
                url: 'http://10.20.58.126:8080/v1/open/participant/addPartner',
                data: {
                  image_url: that.data.imageURL,
                  is_public: that.data.show,
                  note: that.data.name,
                  task_id: that.data.task_id,
                  user_id: app.globalData.user_id
                },
                method: "POST",
                dataType: 'json',
                header: { //头部返回信息
                  'content-type': 'application/json'
                },
                success: function (res) {
                  console.log(res)
                  wx: wx.redirectTo({
                    url: '../../DoTask/CheckenPding/CheckenPding',
                    success: function (res) { },
                    fail: function (res) { },
                    complete: function (res) { },
                  })
                },
                fail: function (res) { },
                complete: function (res) { },
              })
            }
          }
        })
      } //if text
    } //if photo
  },
  delete_photo() {
    var that = this
    that.setData({
      pd: true
    })
  },
  formName: function (e) {
    this.setData({
      name: e.detail.value
    })
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this

    wx.request({ //获取信息
      url: 'http://10.20.58.126:8080/v1/open/task/getOneTask/' + app.globalData.task_id,
      method: "POST",
      dataType: 'json',
      header: { //头部返回信息
        'content-type': 'application/json'
      },
      success: function (res) {

        //  console.log(res)
        that.setData({
          task_id: res.data.task_id,
          task: res.data.claim,
          sum_money: res.data.total_bounty,
        })
      },
      fail: function (res) { },
      complete: function (res) { },
    })
  },

  getToken: function (filePath) { //换取服务器的gettoken
    var that = this
    var rUrl = "http://10.20.58.126:8080/v1/open/task/getToken"
    wx.request({
      url: rUrl,
      header: {
        'content-type': 'application/json' // 默认值
      },
      success: function (res) {
        wx.hideLoading();
        that.updateImg(filePath, res.data.uptoken);
        console.log("getToken");
      },
      fail: function (res) {
        wx.hideLoading();
      }
    })
  },
  //上传图片到服务器
  updateImg: function (filePath, uptoken) {
    const qiniuUploader = require("../../../utils/qiniuUploader")
    //随机生成文件名
    var prname = util.guid2()
    var that = this
    //上传图片
    qiniuUploader.upload(filePath, (res) => {
      //console.log(res);
      //获得上传的图片网络路径
      that.setData({
        imageURL: res.imageURL,
        pd: false
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
  switch1Change: function (e) {
    var that = this
    if (e.detail.value == true) {
      that.setData({
        show: 0
      })
    } else {
      that.setData({
        show: 1
      })
    }
  },
})