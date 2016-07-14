var zf = {
  num3: document.getElementById('num3'),
  num2: document.getElementById('num2'),
  num1: document.getElementById('num1'),
  go: document.getElementById('go'),
  ko: document.getElementsByClassName('ko')[0],
  k: document.getElementsByClassName('k')[0],
  o: document.getElementsByClassName('o')[0],
  modal: document.getElementById('modal'),
  warning: document.getElementById('warning'),
  listModal: document.getElementById('list'),
  timerCanvas: document.getElementById('timerCanvas'),
  timerNumber: document.getElementById('timerNumber'),
  userDOM: document.getElementById('user'),
  hitCounts: document.getElementById('hitCounts'),
  tip: document.getElementById('tip'),

  flag: true,
  isKo: false,
  score: localStorage.getItem('score') || 0,
  nowTime: 15,
  start: 0,
  user: '',

  countTimes: 0,
  rotateValue: 0,
  context: null,
  timeBegin: 0,
  timeEnd: 0,


  init: function () {
    this.flag = true
    this.isKo = false
    this.score = 0
    this.nowTime = 15
    this.start = 0
    this.countTimes = 0
    this.rotateValue = 0
      // this.hideModal()
      // this.hideList()
    return this
  },
  disableFlag: function () {
    this.flag = false
  },

  showTimer: function () {
    if (this.flag === false) {
      return this;
    }
    this.flag = false
    this.num3.style.display = 'block'
    setTimeout(function () {
      this.num3.style.display = 'none'
      this.num2.style.display = 'block'
    }.bind(this), 1000)
    setTimeout(function () {
      this.num2.style.display = 'none'
      this.num1.style.display = 'block'
    }.bind(this), 2000)
    setTimeout(function () {
      this.num1.style.display = 'none'
      this.go.style.display = 'block'
    }.bind(this), 3000)
    setTimeout(function () {
      this.go.style.display = 'none'
      this.start = 2
      this.countTime(15)
    }.bind(this), 4000)
    return this
  },

  showKo: function () {
    if (this.isKo === true) {
      return
    }
    this.isKo = true
    this.ko.style["z-index"] = 10
    this.ko.style.display = "block"
    this.o.style.display = "none"
    this.k.className = "k kfade"
    setTimeout(function () {
      this.o.style.display = "block"
      this.o.className = "o ofade"
    }.bind(this), 700);
    setTimeout(function () {
      this.ko.style["z-index"] = -1;
      this.k.className = 'k'
      this.o.className = 'o'
      this.o.style.display = "none"
      this.k.style.display = "none"
    }.bind(this), 1500);
    return this
  },

  showModal: function () {
    this.hitCounts.innerHTML = '恭喜你，你已经击打产品经理' + this.countTimes + '次'
    this.modal.style.display = 'block'
    return this
  },

  hideModal: function () {
    this.modal.style.display = 'none'
    return this
  },

  showTip: function () {
    this.tip.style.display = 'block'
    return this
  },

  hideTip: function () {
    this.tip.style.display = 'none'
    return this
  },

  showWarning: function () {
    this.warning.style.display = 'block'
    return this
  },

  hideWarning: function () {
    this.warning.style.display = 'none'
    return this
  },

  showList: function () {
    var urlStr = 'http://www.violachen.cn/KOPM/saveScore?score=' + this.score
    zfJquery.ajax({
      url: urlStr,
      type: 'GET',
      success: function (data) {
        if (data == '-1') {
          window.location.href = '../login.html'
        } else {
          data = data['root']
          var str = '';
          for (var i = 0, len = data.length; i < len; i++) {
            if (i === 7) {
              if (zf.user === data[i + 1]['username']) {
                str += "<li><span class='listname'>......</span><span class='listscore'>......</span></li>"
                str += "<li class='listme'><span class='listname'>" + data[i + 1]['username'] + "</span><span class='listscore'>" + data[i]['score'] + "</span></li>"
                i = i + 1
              } else if (zf.user === data[i]['username']) {
                str += "<li class='listme'><span class='listname'>" + data[i]['username'] + "</span><span class='listscore'>" + data[i]['score'] + "</span></li>"
              } else {
                str += "<li><span class='listname'>" + data[i]['username'] + "</span><span class='listscore'>" + data[i]['score'] + "</span></li>"
              }
            } else {
              if (zf.user === data[i]['username']) {
                str += "<li class='listme'><span class='listname'>" + data[i]['username'] + "</span><span class='listscore'>" + data[i]['score'] + "</span></li>"
              } else {
                str += "<li><span class='listname'>" + data[i]['username'] + "</span><span class='listscore'>" + data[i]['score'] + "</span></li>"
              }

            }
          }
          document.getElementById('allList').innerHTML = str;
        }
      }
    })
    this.listModal.style.display = 'block'
    return this
  },

  hideList: function () {
    this.listModal.style.display = 'none'
    return this
  },

  startTimer: function () {
    return this;
  },

  swingPic: function () {
    if (this.start === 1 || this.start === 0) {
      return
    }
    var countPosx = ['-375px', '-375px', '-375px', '-375px', '-440px', '-440px', '-440px', '-440px', '-507px', '-500px', '-500px', '-500px'];
    var countPosy = ['-160px', '-267px', '-375px', '-474px', '-160px', '-267px', '-374px', '-474px', '-54px', '-160px', '-265px', '-373px'];
    var leftnum = document.getElementById("leftNumber");
    var rightnum = document.getElementById("rightNumber");
    var d = new Date();
    this.timeBegin = d.getTime();
    if (this.timeBegin - this.timeEnd > 100){
      this.countTimes++;

      var kickMusic = document.getElementById("kickMusic");
      kickMusic.pause();
      kickMusic.currentTime = 0;
      kickMusic.play();
    }

    this.timeEnd = d.getTime();
    if (this.countTimes > 99) {
      leftnum.style.backgroundPositionX = parseInt(countPosx[9]) + 'px';
      leftnum.style.backgroundPositionY = parseInt(countPosy[9]) + 'px';
      rightnum.style.backgroundPositionX = parseInt(countPosx[9]) + 'px';
      rightnum.style.backgroundPositionY = parseInt(countPosy[9]) + 'px';
      this.showKo();
      return;
    } else {
      this.rotateValue = (this.rotateValue + 12) % 361;
      var targetPic = document.getElementById('swing');
      targetPic.style.webkitTransform = "rotate(" + this.rotateValue + "deg)";
      leftnum.style.backgroundPositionX = parseInt(countPosx[parseInt(this.countTimes / 10)]) + 'px';
      leftnum.style.backgroundPositionY = parseInt(countPosy[parseInt(this.countTimes / 10)]) + 'px';
      rightnum.style.backgroundPositionX = parseInt(countPosx[parseInt(this.countTimes % 10)]) + 'px';
      rightnum.style.backgroundPositionY = parseInt(countPosy[parseInt(this.countTimes % 10)]) + 'px';
    
    }
  },
  countTime: function (process) {
    if (process == 15) {
      this.timerCanvasFunc(process);
      process--;
      this.countTime(process)
    } else if (process >= 0) {
      setTimeout(function () {
        if (process == 0) {
          this.timerCanvasFunc(0);
          this.start = 0;
          setTimeout(this.showModal.bind(this), 2000)
        } else {
          this.timerCanvasFunc(process);
          process--;
          this.countTime(process)
        }
      }.bind(this), 1000);
    }
  },
  timerCanvasFunc: function (process) {
    var numPosx = ['-30px', '-30px', '-30px', '-30px', '-30px', '-120px', '-120px', '-120px', '-120px', '-120px', '-235px', '-235px', '-235px', '-235px', '-235px', '-367px'];
    var numPosy = ['-50px', '-155px', '-265px', '-373px', '-475px', '-50px', '-155px', '-268px', '-370px', '-475px', '-50px', '-155px', '-265px', '-370px', '-475px', '-50px'];
    var circle = new Image();
    circle.src = 'imgs/circle.png';
    circle.onload = function () {
      this.context.clearRect(0,0,200,200);
      this.context.drawImage(circle, 0, 0, 200, 200);
      this.context.beginPath();
      this.context
        .moveTo(100, 100);
      // this.context.arc(100,100,80,Math.PI*1.5, Math.PI*(1.5-2*process/15));
      if (process != 15 && process != 0) {
        this.context.arc(100, 100, 80, Math.PI * 1.5, Math.PI * (1.5 - 2 * process / 15));
      }
      this.context.closePath();
      this.context.fillStyle = "#6b6b6b";
      this.context.fill();
      this.context.beginPath();
      this.context.arc(100, 100, 64, 0, Math.PI * 2);
      this.context.closePath();
      this.context.fillStyle = '#2f2f2f';
      this.context.fill();
      this.timerNumber.style.backgroundPositionX = parseInt(numPosx[process]) + 'px';
      this.timerNumber.style.backgroundPositionY = parseInt(numPosy[process]) + 'px';
    }.bind(this)
  }
}
