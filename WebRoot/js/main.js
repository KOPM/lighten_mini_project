var pillarHeight;

function getSize() {
  var pageWidth = document.documentElement.clientWidth || document.body.clientWidth;
  var pageHeight = window.innerHeight-50;

  var DEFAULT_FACE_WIDTH = 250;
  var DEFAULT_FIST_WIDTH = 200;
  var DEFAULT_PILLAR_WIDTH = 244;
  var DEFAULT_PILLAR_HEIGHT = 480;

  var faceWidth;

  if (pageWidth > 1280) {
    faceWidth = DEFAULT_FACE_WIDTH;
    fistWidth = DEFAULT_FIST_WIDTH;
    cWidth = 1280;
  } else {
    faceWidth = pageWidth * DEFAULT_FACE_WIDTH / 1280;
    fistWidth = pageWidth * DEFAULT_FIST_WIDTH / 1280;
    cWidth = pageWidth;
  }
  cHeight = pageHeight;
  var faceLeft = pageWidth / 2 - faceWidth / 2;
  return {
    fullWidth: pageWidth,
    faceWidth: faceWidth,
    fistWidth: fistWidth,
    faceLeft: faceLeft,
    cWidth: cWidth,
    cHeight: cHeight,
    point: { x: faceLeft + faceWidth / 2, y: 50 + 0.34 * pillarHeight / 2, r: faceWidth / 2 * 0.9 },
    rect: { x: faceLeft + faceWidth / 2, y: 50 + 0.34 * pillarHeight, w: 0.34 * faceWidth, h: 0.4 * pillarHeight }
  }
}


function drawFist() {
  var oSize = getSize();
  var fistImg = new Image();
  fistImg.src = "../imgs/fist.png";

  fistImg.onload = function () {
    var fistHeight = fistImg.height * oSize.fistWidth / fistImg.width;
    var fistLeft = oSize.faceLeft + oSize.faceWidth - 200;
    cxt.drawImage(fistImg, fistLeft, faceHeight - 100, oSize.fistWidth, fistHeight);
  }
}

function init() {
  var oSize = getSize();
  var pillar = document.getElementById("pillar");
  pillar.style["z-index"] = "0";
  pillarHeight = pillar.height * oSize.faceWidth / pillar.width;
  pillar.height = pillarHeight;
  pillar.width = oSize.faceWidth;
  pillar.style.left = oSize.faceLeft + "px";
  pillar.style.top = "50px";

  var mainBg = document.getElementById("mainBg");
  mainBg.width = oSize.fullWidth;
  mainBg.height = oSize.cHeight;
}

var i = 0;

function rotatePillar(fistPosition, speed) {
  addRotate(i, speed);
  i = 20;
  setTimeout(function () {
    minusRotate(i, speed);
  }, 200);

}

function addRotate(i, speed) {
  var pillar = document.getElementById("pillar");
  var addTime = setTimeout(function () {
    if (i < 20) {
      i++;
      pillar.style.transform = "rotate(" + i * speed + "deg)";
      addRotate(i, speed);

    } else {
      clearTimeout(addTime);
    }

  }, 10);
}

function minusRotate(i, speed) {
  var pillar = document.getElementById("pillar");
  var minusTime = setTimeout(function () {
    if (i > 0) {
      i--;
      pillar.style.transform = "rotate(" + i * speed + "deg)";
      minusRotate(i, speed);

    } else {
      clearTimeout(minusTime);
    }

  }, 10);
}


var canvas = document.getElementById("myCanvas");
var ctx = canvas.getContext("2d");

var oSize = getSize();
canvas.width = oSize.cWidth;
canvas.height = oSize.cHeight;
var mostIn = -100;
var mostOut = 100;
var normalFistSize = 100;
var rightFist = new Image();
var leftFist = new Image();
var leftId = 0;
var rightId = 1;
var lastHandId = 0;
var bitted = 0;


rightFist.src = "imgs/rightFist.png";
leftFist.src = "imgs/leftFist.png";

function leapMotion() {
  var controller = new Leap.Controller();
  controller.connect();
  controller.on('deviceAttached', onAttached);
  controller.on('deviceDisconnected', onDeviceDisconnect);

  var frameArr = new Array();
  var graspCheckLength = 20;
  var graspThresh = 25;
  var lastGesture = -1; //-1 null, 0 grasp,1 circle,2 key,3 screen ,4 swipe,after 1 sec it will be reset to -1
  var timer = 0;

  Leap.loop({ frameEventName: "animationFrame" }, function (frame) {

    if (timer < 30) timer++;
    else {
      lastGesture = -1;
      timer = 0;
    }

  });

  Leap.loop({ frameEventName: "animationFrame" }, function (frame) {
    ctx.clearRect(0, 0, ctx.canvas.width, ctx.canvas.height);

    frame.hands.forEach(function (hand) {
      //basic info of hand
      if (zf.start === 0) {
        zf.showTimer();
        zf.start = 1;
      }

      var position = hand.palmPosition;
      var deep = position[2]
      if (deep < mostIn) deep = mostIn;
      if (deep > mostOut) deep = mostOut;
      var normalized = frame.interactionBox.normalizePoint(position);
      var x = ctx.canvas.width * normalized[0];
      var y = ctx.canvas.height * (1 - normalized[1]);

      // recognize left or right

      if (hand.id > lastHandId) {
        if (getSide(hand) == 1) leftId = hand.id;
        else rightId = hand.id;

      } else if (frame.hands.length == 2) {
        if (frame.hands[0].palmPosition[0] < frame.hands[1].palmPosition[0]) {
          leftId = frame.hands[0].id;
          rightId = frame.hands[1].id;
        } else {
          leftId = frame.hands[1].id;
          rightId = frame.hands[0].id;
        }
      }

      //draw image of fists

      ctx.beginPath();
      var fistSize = getFistSize(deep, mostIn, mostOut, normalFistSize);
      if (hand.id == rightId) {
        ctx.drawImage(rightFist, x, y, fistSize, fistSize);
      } else ctx.drawImage(leftFist, x, y, fistSize, fistSize);


      var point = getSize().point;
      var rect = getSize().rect;
      var detect = null;
      if (hand.id == rightId) detect = getDetectPointR(x, y, fistSize, fistSize);
      else detect = getDetectPointL(x, y, fistSize, fistSize);



      // detect collision

      var speed = Math.sqrt((hand.palmVelocity[0] * hand.palmVelocity[0]) + (hand.palmVelocity[2] * hand.palmVelocity[2]));

      if (
        bitted === 0 &&
        (((detect.x - point.x) * (detect.x - point.x) + (detect.y - point.y) * (detect.y - point.y) <= point.r * point.r) ||
          ((detect.x > rect.x - rect.w / 2) && (detect.x < rect.x + rect.w / 2) && (detect.y > rect.y) && (detect.y < rect.y + rect.h)))
      ) {
        
        if (hand.id == leftId) {
          if (hand.palmVelocity[0] > 0) {
            rotatePillar(0, speed * (normalized[1] + 0.5) * 4 / 3000);
            zf.swingPic();
            bitted = 1;
          }
        } else {
          if (hand.palmVelocity[0] < 0) {
            rotatePillar(1, -speed * (normalized[1] + 0.5) * 4 / 3000);
            zf.swingPic();
            bitted = 1;
          }
        }

      } else if (
        ((detect.x - point.x) * (detect.x - point.x) + (detect.y - point.y) * (detect.y - point.y) > point.r * point.r) &&
        ((detect.x < rect.x - rect.w / 2) || (detect.x > rect.x + rect.w / 2) || (detect.y < rect.y) || (detect.y > rect.y + rect.h))
      ) {
        bitted = 0;
      }

      if (hand.id > lastHandId) lastHandId = hand.id;
    });
  });

  Leap.loop({ frameEventName: "animationFrame" }, function (frame) {


    if (frameArr.length < 20) frameArr.push(frame);
    else {
      frameArr.push(frame);
      frameArr.shift();

      var prevP = 0;
      var afterP = 0;
      var prevH = 0;
      var afterH = 0;

      for (var i = 0; i < graspCheckLength / 2; ++i) prevP += frameArr[i].pointables.length;
      for (var i = graspCheckLength / 2; i < graspCheckLength; ++i) afterP += frameArr[i].pointables.length;
      for (var i = 0; i < graspCheckLength / 2; ++i) prevH += frameArr[i].hands.length;
      for (var i = graspCheckLength / 2; i < graspCheckLength; ++i) afterH += frameArr[i].hands.length;


      if (prevP - afterP > graspThresh && prevH == afterH && lastGesture != 0) { //get a new grasp

        var graspX = 0;
        var graspY = 0;
        for (var i = 0; i < graspCheckLength; ++i) {
          graspX += getPosition(frameArr[i])[0];
          graspY += getPosition(frameArr[i])[1];
        }
        graspX /= graspCheckLength;
        graspY /= graspCheckLength;

        //Here output to explorer the info of a grasp.
        lastGesture = 0;
        zf.hideWarning();
      }
    }

  });


  Leap.loop({ enableGestures: true }, function (frame) {

    if (frame.valid && frame.gestures.length > 0 && frame.pointables.length > 0) {

      frame.gestures.forEach(function (gesture, pointable) {
        switch (gesture.type) {
          case "circle":
            if (lastGesture != 1) { //Here output to explorer the info of a circle
              lastGesture = 1;
            }
            break;
          case "keyTap":
            if (lastGesture != 2 && frame.pointables.length == 1) { //Here output to explorer the info of a key tap
              lastGesture = 2;
            }
            break;
          case "screenTap":
            if (lastGesture != 3 && frame.pointables.length == 1) { //Here output to explorer the info of a screen tap
              lastGesture = 3;
            }
            break;
          case "swipe":
            if (lastGesture != 4) { //Here output to explorer the info of a swipe
              lastGesture = 4;
            }
            break;
        }
      });

    }


  });

}


function onAttached() {
  zf.start = 1;
  zf.hideWarning();
}

function onDeviceDisconnect() {
  zf.start = 0;
  zf.showWarning();
}


function getPosition(f) {
  var position = new Array();
  var gestureX = 0;
  var gestureY = 0;
  var counter = 0;

  f.pointables.forEach(function (pointable) {
    var position = pointable.stabilizedTipPosition;
    var normalized = f.interactionBox.normalizePoint(position);
    var x = ctx.canvas.width * normalized[0];
    var y = ctx.canvas.height * (1 - normalized[1]);
    gestureX += x;
    gestureY += y;
    counter++;
  });

  if (counter != 0) {
    gestureX /= counter;
    gestureY /= counter;
  } else {
    gestureX = null;
    gestureY = null;
  }
  position.push(gestureX);
  position.push(gestureY);

  return position;
}


function getFistSize(deep, mostIn, mostOut, normalFistSize) {
  return ((deep - mostIn) * 2 / (mostOut - mostIn) + 1) * normalFistSize;
}

function getSide(hand) {
  if (hand.direction[0] < 0 && hand.direction[2] < 0) return 0 //0 means right hand
  else return 1; //1 means left hand
}

function getDetectPointR(x, y, w, h) {
  return { x: x + w * 45.0 / 256 - 20, y: y + h * 70.0 / 240 };
}

function getDetectPointL(x, y, w, h) {
  return { x: x + w + 20, y: y + h * 70.0 / 240 };
}


window.onload = function () {
  init();
  zf.showWarning();
  leapMotion();
  if (zf.timerCanvas.getContext) {
    zf.context = zf.timerCanvas.getContext("2d");
  }
  var circle = new Image();
  circle.src = 'imgs/circle.png';
  circle.onload = function () {
    zf.context.drawImage(circle, 0, 0, 200, 200);
  }

  document.getElementById("swing").onclick = zf.swingPic;

}
