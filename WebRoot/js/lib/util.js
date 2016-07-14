  function checkUsername(username) {
    if (username == "" || username == null) {
      alert("请先填写用户名！");
      return false;
    }
    switch (isUsername(username)) {
      case 0:
        break;
      case 1:
        {
          alert("您选择的用户名格式不正确，用户名不能以数字开头");
          return false;
        }
      case 2:
        {
          alert("您选择的用户名字符长度有误，合法长度为6-20个字符");
          return false;
        }
      case 3:
        {
          alert("您选择的用户名含有非法字符，用户名只能包含_,英文字母，数字");
          return false;
        }
      case 4:
        {
          alert("您选择的用户名格式不正确，用户名只能包含_,英文字母，数字");
          return false;
        }
    }
    return true;
  }

  function isUsername(username) {
    if (/^\d.*$/.test(username)) {
      return 1;
    }
    if (!/^.{6,20}$/.test(username)) {
      return 2;
    }
    if (!/^[\w_]*$/.test(username)) {
      return 3;
    }
    if (!/^([a-z]|[A-Z])[\w_]{5,19}$/.test(username)) {
      return 4;
    }
    return 0;
  }
  //判断密码是否符合规范
  function checkPassword(password) {
    if (password.length < 6) {
      alert('密码不能少于六位')
      return false
    }
    return true
  }

  //判断重复密码是否相等
  function checkRepassword(password, repassword) {
    if (password === repassword) {
      return true
    } else {
      alert('重复密码和密码不一致，请重新输入！')
      return false
    }
  }

  function getUrlParam(n) {
    var m = window.location.hash.match(new RegExp("(\\?|&)" + n + "=([^&*])(&|$)"))
    return !m ? "" : decodeURIComponent(m[2])
  }
