(function (window, docment) {
  'use strict';
  var w = window,
    doc = docment;
  var Zf = function (selector) {
    return Zf.prototype.init(selector);
  };
  Zf.prototype = {
    constructor: Zf,
    length: 0,
    splice: [].splice(),
    selector: '',
    init: function (selector) {
      var element, i;
      if (!selector) {
        return this;
      }

      if (typeof selector === 'object') {
        selector = [selector];
        for (i = 0; i < selector.length; i += 1) {
          this[i] = selector[i];
        }
        this.length = selector.length;
        return this;
      } else if (typeof selector === 'function') {
        Zf.ready(selector);
        return;
      }

      selector = selector.trim();

      if (selector.charAt(0) === "#" && !selector.match('\\s')) {

        selector = selector.substring(1);
        element = doc.getElementById(selector);
        this.selector = selector;
        this.length = 1;
        this[0] = element;
        return this;

      } else if (selector.charAt(0) === ".") {

        selector = selector.substring(1);
        element = doc.getElementsByClassName(selector);
        for (i = 0; i < element.length; i++) {
          this[i] = element[i];
        }
        this.selector = selector;
        this.length = element.length;
        return this;

      } else {

        element = doc.querySelectorAll(selector);
        for (i = 0; i < element.length; i++) {
          this[i] = element[i];
        }
        this.selector = selector;
        this.length = element.length;
        return this;
      }
    },

    css: function (attr, val) {
      var i;
      for (i = 0; i < this.length; i++) {
        if (typeof attr === 'string') {
          if (arguments.length === 1) {
            return getComputedStyle(this[i], null)[attr];
          }
          this[i].style[attr] = val;
        } else {
          var _this = this[i];
          w.zf.each(attr, function (attr, val) {
            w.console.log(this);
            _this.style.cssText += '' + attr + ':' + val + ';';
          });
        }
      }
      return this;
    },
    hasClass: function (cls) {
      var reg = new RegExp('(\\s|^)' + cls + '(\\s|$)');
      for (var i = 0; i < this.length; i++) {
        if (this[i].className.match(reg)) {
          return true;
        }
      }
      return false;
    },
    addClass: function (cls) {
      var reg = new RegExp('(\\s|^)' + cls + '(\\s|$)');
      for (var i = 0, len = this.length; i < len; i++) {
        if (!this[i].className.match(reg)) {
          if (this[i].className) {
            this[i].className += ' ' + cls;
          } else {
            this[i].className += cls;
          }
        }
      }
      return this;
    },
    removeClass: function (cls) {
      var reg = new RegExp('(\\s|^)' + cls + '(\\s|$)');
      var classes = new Array();
      for (var i = 0, len = this.length; i < len; i++) {
        if (this[i].className.match(reg)) {
          classes = this[i].className.split(' ');
          if (classes.length > 1) {
            this[i].className = this[i].className.replace(' ' + cls, '');
          } else {
            this[i].className = this[i].className.replace(cls, '');
          }

        }
      }
      return this;
    },
    bind: function (type, callback) {
      if (doc.addEventListener) {
        for (var i = 0; i < this.length; i++) {
          this[i].addEventListener(type, callback, false);
        }
      } else if (doc.attachEvent) {
        for (var i = 0; i < this.length; i++) {
          this[i].attachEvent(type, callback);
        }
      }

      return this;
    }

  };

  Zf.each = function (obj, callback) {
    var
      len = obj.length,
      con = obj.constructor,
      i,
      val;
    if (con === w.zf) {
      for (i = 0; i < len; i++) {
        val = callback.call(obj[i], i, obj[i]);
        if (val === false) {
          break;
        }
      }
    } else if (isArray(obj)) { //数组
      for (i = 0; i < len; i++) {
        val = callback.call(obj[i], i, obj[i]);
        if (val === false) {
          break;
        }
      }
    } else {
      for (i in obj) {
        //val = callback.call(obj[i], i, obj[i]);
        val = callback(i, obj[i]);
        if (val === false) {
          break;
        }
      }
    }
  };

  Zf.getWidth = function () {
    return (doc.body.clientWidth + doc.body.scrollLeft);
  }

  Zf.getHeight = function () {
    return (doc.body.clientHeight + doc.body.scrollTop);
  }

  //不完善，对ie不兼容
  Zf.ready = function (fn) {
    doc.addEventListener('DOMContentLoaded', function () {
      fn && fn();
    }, false);
    doc.removeEventListener('DOMContentLoaded', fn, true);
  };

  Zf.dele = delegate;

  //直接挂载方法  可zf.ajax调用
  Zf.ajax = function (options) {
    var defaultOptions = {
      url: false,
      type: 'GET',
      data: false,
      success: false, //数据成功返回后的回调方法
      complete: false, //ajax完成后的回调方法
      reType: 'json' //返回格式
    };
    var xhr = createXHR();
    var i;
    for (i in defaultOptions) {
      if (options[i] === undefined) {
        options[i] = defaultOptions[i];
      }
    }

    xhr.open(options.type, options.url);
    xhr.onreadystatechange = function () {
      if (xhr.readyState === 4) {
        var result,
          status = xhr.status;

        if ((status >= 200 && status < 300) || status === 304) {
          result = xhr.responseText;
          if (options.reType === 'text') {
            result = result
          } else if (w.JSON) {
            result = JSON.parse(result);
          } else {
            result = w.eval('(' + result + ')');
          }
          ajaxSuccess(result, xhr);
        } else {
          w.console.log("ERR", xhr.status);
        }
      }
    };

    //只能发送json数据
    if (options.type === 'POST' || options.type === 'post') {
      xhr.setRequestHeader('Content-Type', 'application/json');
    }

    xhr.send(options.data ? JSON.stringify(options.data) : null);

    function createXHR() {
      if (typeof XMLHttpRequest !== "undefined") {
        return new XMLHttpRequest();
      } else if (typeof ActiveXObject !== "undefined") {
        if (typeof arguments.callee.activeXString !== "string") {
          var versions = ["MSXML2.XMLHttp.6.0", "MSXML2.XMLHttp.3.0", "MSXML2.XMLHttp"],
            i, len;
          for (i = 0, len = versions.length; i < len; i++) {
            try {
              new ActiveXObject(versions[i]);
              arguments.callee.activeXString = versions[i];
              break;
            } catch (ex) {

            }
          }
        }
        return new ActiveXObject(arguments.callee.activeXString);
      } else {
        throw new Error("No XHR object available");
      }
    }


    function ajaxSuccess(data, xhr) {
      var status = 'success';
      //判断 options.success 这个function是否存在，如果存在则执行option.success(data,options.status.xhr)
      options.success && options.success(data, options, status, xhr)
      ajaxComplete(status);
    }

    function ajaxComplete(status) {
      options.complete && options.complete(status);
    }

  };

  //事件代理
  //agent是父元素，selector是子元素
  function delegate(agent, type, selector, fn) {
    agent.addEventListener(type, function (e) {
      var target = e.target; //目标元素,点击的元素
      var cTarget = e.currentTarget; //当前正在处理事件的元素，父元素
      var bubbles = true;
      while (bubbles && target !== cTarget) {
        if (filter(agent, selector, target)) {
          //filter为true，执行回调
          //将this改为target
          bubbles = fn.call(target, e);
        }
        target = target.parentNode;
        return bubbles;
      }
    }, false);

    //判断目标元素是否在代理中
    function filter(agent, selector, target) {
      var nodes = agent.querySelectorAll(selector);
      var i;
      for (i = 0; i < nodes.length; i++) {
        if (nodes[i] === target) {
          return true;
        }
      }
    }
  }

  function isArray(obj) {
    return Array.isArray(obj);
  }

  //保证init.prototype和Zf.prototype相等
  Zf.prototype.init.prototype = Zf.prototype;
  w.zfJquery = Zf;
})(window, document);
