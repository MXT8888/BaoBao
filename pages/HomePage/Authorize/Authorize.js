Page({
  data: {
    isLogin: false,     //授权按钮初始状态是显示
    canIUse: wx.canIUse('button.open-type.getUserInfo'),     //授权后显示用户头像和昵称
    detectionStatus: true,      //登陆注册按钮状态
    // 用数组来存放定义的变量
    userinfoNew: {
      code: "",
      head_portrait: "",
      nickname: "",
    },
    isIpx:false
  },
  // 初始化先执行onLoad方法
  onLoad: function () {
    var that = this
    that.public();
  // iphonex 手机适配背景图片
    wx.getSystemInfo({
      success: function (res) {
        let model = res.model.substring(0, res.model.indexOf("X")) + "X";
        if (model == 'iPhone X') {
          that.setData({
            isIpx: true
          })
        }
      }
    })
  },
  // 公共方法
  public:function(){
    var that = this;       //当前的this指定为that
    //获取本地信息
    var code = "userinfoNew.code"
    var head_portrait = "userinfoNew.head_portrait"
    var nickname = "userinfoNew.nickname"
    // 登陆方法
    wx.login({
      success: res => {
        // 发送 res.code 到后台换取 userId, sessionKey
        that.setData({
          [code]: res.code,
        })
        //判断授权方法
        wx.getSetting({
          // 授权成功方法
          success(res) {
            if (res.authSetting['scope.userInfo']) {
              // 授权成功后，可以直接调用 getUserInfo 获取头像昵称
              wx.getUserInfo({
                // 获取用户信息成功方法
                success: function (res) {
                  //用缓存来保存用户信息
                  wx.setStorage({
                    key: 'userInfo',    //通过key将用户信息进行保存
                    data: res.userInfo    //获取本地用户信息缓存进data
                  })
                  // 获取到的本地信息
                  console.log("-------获取到的本地信息---------")
                  console.log(res.userInfo)   
                  //封装用户对象，把本地用户信息进行封装到上面的userinfoNew
                  that.setData({
                    [head_portrait]: res.userInfo.avatarUrl,
                    [nickname]: res.userInfo.nickName,
                  })
                  //通过接口向后端发送请求
                  wx.request({
                    url: 'http://10.20.58.126:8080/v1/open/task/',   //插入用户信息接口
                    method: "POST",    //请求方式
                    data: that.data.userinfoNew,    //向服务器发送userinfoNew里面封装好的本地数据
                    // 发送信息成功后
                    success: function (e) {
                      console.log("-------发送成功后返回到数据--------")
                      console.log(e)
                      // 通过Key---缓存获取到的userid
                      wx.setStorage({
                        key: 'userId',//用户信息 内有金额与用户id
                        data: e.data
                      })
                      that.setData({
                        isLogin: true    //授权按钮状态为隐藏
                      })
                      // 获取到用户usesrId
                      console.log("-------获取到用户usesrId详细信息--------")
                      console.log(e.data)
                      getApp().globalData.totalMoney = e.data.overage
                      setTimeout(function () {
                        wx.switchTab({
                          url: '../../HomePage/HomePage',
                        })
                      }, 50)
                    },
                  })
                }
              })
            }
            else {
              that.setData({
                isLogin: false
              })
            }
          }
        })
      }
    })
  },
  // 点击按钮进行授权-----授权后按钮隐藏
  bindGetUserInfo(e) {
    this.setData({
      isLogin: true
    });
    this.onLoad()
  },
  // 点击包包大冒险跳转到发布任务界面
  damaoxian: function () {
    wx.navigateTo({
      url: '../HomePage/ReleaseTask/ReleaseTask',
    })
  }
})