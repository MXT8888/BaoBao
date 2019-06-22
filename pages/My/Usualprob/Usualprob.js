Page({
  data: {
    list:[{
      question_left:"红包大冒险怎么玩？",
      answer:"发起者设置红包金额，个数并且出任务。参加的好友识别图中二维码或者点击分享链接就可以上传任务照片，待发起者审核通过后，即可领取随机数额的红包奖励。",
      hidden: false
    }, {
        question_left: "未被领取的红包会退吗？",
        answer: "未被领取完的红包，会在24小时后自动退回账户。",
        hidden: false,
      },
      {
        question_left: "发送的红包能再次转发吗？",
        answer: "已发送的红包不支持再次转发。",
        hidden: false
      }]
  },
  onLoad: function () {
  }, 
  //点击是否展示解决问题的方法
  isOpen: function (e) {
    var that = this;
    //获得点击问题的序号
    var number = e.currentTarget.dataset.option;
    console.log(number);
    var lists = that.data.list;
    for(var index in lists){
      // 点击问题的序号与解决方法的序号相同，则显示
      if (number == index) {
        lists[index].hidden = true;
      }else{
        lists[index].hidden = false;
      }
    }
    that.setData({
      list: lists
    })
  }
})
