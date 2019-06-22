const ctx = wx.createCanvasContext('secondCanvas')
Page({
  // 页面的初始数据
  data: {
    img: "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=326330914,3876458603&fm=27&gp=0.jpg",
    userhead: "",
    screenWidth: "",//手机宽
    winHeight: "",//手机高
    ratio: "",//分辨率
    qrcode: "",//二维码
    fingerprint: "",//指纹
    qecodeBianKuang: "",//二维码边框
    task_id: "",
  },
  click: function () {

  },
  save: function () {
    wx.canvasToTempFilePath({
      x: 0,
      y: 0,
      width: 400,
      height: 600,
      destWidth: 500,
      destHeight: 800,
      canvasId: 'secondCanvas',
      success(res) {
        console.log(res.tempFilePath)
        wx.saveImageToPhotosAlbum({
          filePath: res.tempFilePath,
          success(e) {
            console.log("保存成功")
          }
        })
      }
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    console.log(options);
    var that = this;
    that.setData({
      task_id: options.task_id
    })
    wx.getSystemInfo({
      success: res => {
        that.setData({
          screenWidth: res.screenWidth,
          winHeight: res.windowHeight,
          ratio: res.pixelRatio,
        })
      }
    })
    wx.getImageInfo({
      src: 'https://hbimg.huabanimg.com/81fa5b0268669256495d3d2c4b5f53c12e0d51bc1980-qnW7aA_fw658',//指纹
      success(res) {
        that.setData({
          fingerprint: res.path
        })
      }
    })
    wx.getImageInfo({
      src: 'http://47.102.217.131:8080/88/image/qecodeBianKuang.png',//二维码边框
      success(res) {
        that.setData({
          qecodeBianKuang: res.path
        })
      }
    })
    wx.getUserInfo({
      success(res) {
        console.log(res)
        wx.getImageInfo({
          src: 'http://47.102.217.131:8080/88/image/qrcode.png',//二维码
          success(e) {
            console.log(e);
            that.setData({
              qrcode: e.path
            })
          }
        })
        wx.getImageInfo({
          src: res.userInfo.avatarUrl,
          success(e) {
            console.log("用户头像地址")
            that.setData({
              userhead: e.path
            })
            console.log(that.data.userhead);

            wx.getImageInfo({
              src: that.data.img,
              success(res) {
                console.log("图片临时路径")
                console.log(res)

                //
                ctx.setFillStyle('#c63329')//背景色
                ctx.setStrokeStyle('#c63329')
                ctx.rect(0, 0, that.data.screenWidth, 900)
                ctx.fill()
                //画弧线
                ctx.beginPath()
                ctx.fill()
                // 开口弧线
                ctx.arc(that.data.screenWidth / 2, 0, that.data.screenWidth / 2, 0, 2 * Math.PI)//位置
                ctx.setLineWidth('1')
                ctx.setStrokeStyle('rgb(213, 43, 77)')
                ctx.setFillStyle('#dc3a2f')
                ctx.fill()
                ctx.drawImage(that.data.qecodeBianKuang, that.data.screenWidth / 3.5, 180, 180, 180)
                ctx.drawImage(that.data.qrcode, that.data.screenWidth / 2.8, 206, 120, 120);//二维码
                // 使用 wx.createContext 获取绘图上下文 context

                ctx.drawImage(that.data.fingerprint, that.data.screenWidth / 2.3, 390, 50, 50);//指纹

                ctx.setFontSize(15)
                ctx.setFillStyle('white')
                ctx.fillText('发布了一个大冒险红包', that.data.screenWidth / 3, 100)

                ctx.setFontSize(20)
                ctx.setFillStyle('white')

                ctx.fillText('完成大冒险即可领取红包', that.data.screenWidth / 4.2, 130)

                ctx.setFontSize(15)
                ctx.setFillStyle('white')
                ctx.fillText('长按识别小程序，玩大冒险领红包', that.data.screenWidth / 5, 480)
                ctx.save(); // 保存当前ctx的状态
                ctx.beginPath()//路径设置
                ctx.arc(that.data.screenWidth / 2, 45, 35, 0, 2 * Math.PI); //画出圆
                ctx.clip(); //裁剪上面的圆形

                ctx.drawImage(that.data.userhead, that.data.screenWidth / 2.7, 0, 100, 100); // 在刚刚裁剪的园上画图 头像

                // ctx.drawImage(that.data.userhead, 80, 90, swidth, sheight, x, y, width, height)
                ctx.restore(); // 还原状态

                // ctx.draw(that.save())//执行save方法，先执行方法在执行draw

                ctx.draw(true, () => {
                  //回调函数进行调用，执行完毕draw方法后
                  that.save()
                })
              }
            })

          }
        })
      }
    })


  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function (e) {



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
    console.log(this.data.task_id)
    let that = this;
    return {
      title: '包包大冒险一起来抢包包', // 转发后 所显示的title
      path: "pages/index/index?task_id=" + that.data.task_id, // 相对的路径
      success: (res) => {    // 成功后要做的事情
        console.log(res.shareTickets[0])
        // console.log

        wx.getShareInfo({
          shareTicket: res.shareTickets[0],
          success: (res) => {
            that.setData({
              isShow: true
            })
            console.log(that.setData.isShow)
          },
          fail: function (res) { console.log(res) },
          complete: function (res) { console.log(res) }
        })
      },
      fail: function (res) {
        // 分享失败
        console.log(res)
      }
    }

  }
})