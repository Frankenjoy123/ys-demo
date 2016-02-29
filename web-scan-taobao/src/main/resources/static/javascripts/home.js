$(document).ready(function () {

  var productKey = $('#key').text().trim();
  var ipAddress = $('#address').text().trim();
  var ipAddressObj = JSON.parse(ipAddress);

  console.log('productKey: ' + productKey);
  console.log('ipAddressObj: ' + ipAddressObj);

  var appId = '2k4d3xp24wpxxcf4myt';
  var deviceId = '00000-00000-00000-00000';

  var postObject = {
    key: productKey,
    state: '未知',
    city: '未知',
    latitude: 0,
    longitude: 0
  };

  if (ipAddressObj.province && ipAddressObj.province != 'None') {
    postObject.state = ipAddressObj.province;
  }

  if (ipAddressObj.city && ipAddressObj.city != 'None') {
    postObject.city = ipAddressObj.city;
  }

  if (location.href.lastIndexOf('#scaned') > 0) {
    $.ajax({
      type: 'GET',
      url: '/api-rabbit/scan/' + productKey,
      dataType: 'text/json',
      headers: {'X-YS-DeviceId': deviceId, 'X-YS-AppId': appId},
      success: function (dataStr) {
        showData(dataStr);

        $("#loading").css('display', 'none');
      }
    });
  }
  else {
    location.href += '#scaned';

    $.ajax({
      type: 'POST',
      url: '/api-rabbit/scan',
      data: JSON.stringify(postObject),
      dataType: 'text/json',
      headers: {'X-YS-DeviceId': deviceId, 'X-YS-AppId': appId},
      contentType: 'application/json',
      success: function (dataStr) {
        showData(dataStr);

        $("#loading").css('display', 'none');
      }
    });
  }

  var control = navigator.control || {};
  if (control.gesture) {
    control.gesture(false);
  }

  function activeTabOne() {
    $("#tab_1").addClass("active");
    $("#li_tab_a_1").addClass("active");

    $("#tab_2").removeClass("active");
    $("#li_tab_a_2").removeClass("active");

    $("#tab_3").removeClass("active");
    $("#li_tab_a_3").removeClass("active");
  }

  function activeTabTwo() {
    $("#tab_2").addClass("active");
    $("#li_tab_a_2").addClass("active");

    $("#tab_1").removeClass("active");
    $("#li_tab_a_1").removeClass("active");

    $("#tab_3").removeClass("active");
    $("#li_tab_a_3").removeClass("active");
  }

  function activeTabThree() {
    $("#tab_3").addClass("active");
    $("#li_tab_a_3").addClass("active");

    $("#tab_1").removeClass("active");
    $("#li_tab_a_1").removeClass("active");

    $("#tab_2").removeClass("active");
    $("#li_tab_a_2").removeClass("active");
  }

  $("#tab_a_1").click(function () {
    activeTabOne();
  });

  $("#tab_a_2").click(function () {
    activeTabTwo();
  });

  $("#tab_a_3").click(function () {
    activeTabThree();
  });

  function is_weixn() {
    var ua = navigator.userAgent.toLowerCase();
    if (ua.match(/MicroMessenger/i) == "micromessenger") {
      return true;
    } else {
      return false;
    }
  }

  var tabIndex = 0, swipedir, startX,
      startY,
      distX,
      distY,
      threshold = 60, //required min distance traveled to be considered swipe
      restraint = 100, // maximum distance allowed at the same time in perpendicular direction
      allowedTime = 200, // maximum time allowed to travel that distance
      elapsedTime,
      startTime;

  $("#tabContent").bind('touchstart', function (e) {
    var touchobj = e.changedTouches[0];
    swipedir = 'none';
    dist = 0;
    startX = touchobj.pageX;
    startY = touchobj.pageY;
    startTime = new Date().getTime(); // record time when finger first makes contact with surface
  });

  $("#tabContent").bind('touchend', function (e) {
    var touchobj = e.changedTouches[0];
    distX = touchobj.pageX - startX; // get horizontal dist traveled by finger while in contact with surface
    distY = touchobj.pageY - startY; // get vertical dist traveled by finger while in contact with surface
    elapsedTime = new Date().getTime() - startTime; // get time elapsed
    if (allowedTime >= elapsedTime) { // first condition for awipe met
      if (Math.abs(distX) >= threshold) { // 2nd condition for horizontal swipe met
        if (restraint >= Math.abs(distY)) {
          swipedir = (0 > distX) ? 'left' : 'right'; // if dist traveled is negative, it indicates left swipe

          if (swipedir == 'left') {
            tabIndex++;
            tabIndex = tabIndex % 3;
          }
          else if (swipedir == 'right') {
            tabIndex--;
            tabIndex = (tabIndex + 3) % 3;
          }

          if (tabIndex == 0) {
            activeTabOne();
          }
          else if (tabIndex == 1) {
            activeTabTwo();
          }
          else if (tabIndex == 2) {
            activeTabThree();
          }

          e.preventDefault();
        }
      }
    }
  });

  Date.prototype.toCommonCase = function () {
    var xYear = this.getYear();
    xYear = xYear + 1900;

    var xMonth = this.getMonth() + 1;
    if (!(xMonth > 9)) {
      xMonth = "0" + xMonth;
    }

    var xDay = this.getDate();
    if (!(xDay > 9)) {
      xDay = "0" + xDay;
    }

    var xHours = this.getHours();
    if (!(xHours > 9)) {
      xHours = "0" + xHours;
    }

    var xMinutes = this.getMinutes();
    if (!(xMinutes > 9)) {
      xMinutes = "0" + xMinutes;
    }

    var xSeconds = this.getSeconds();
    if (!(xSeconds > 9)) {
      xSeconds = "0" + xSeconds;
    }

    return xYear + "-" + xMonth + "-" + xDay + " " + xHours + ":" + xMinutes + ":" + xSeconds;
  }

  Date.prototype.toShortCase = function () {
    var xYear = this.getYear();
    xYear = xYear + 1900;

    var xMonth = this.getMonth() + 1;
    if (!(xMonth > 9)) {
      xMonth = "0" + xMonth;
    }

    var xDay = this.getDate();
    if (!(xDay > 9)) {
      xDay = "0" + xDay;
    }

    return xYear + "-" + xMonth + "-" + xDay;
  }

  var getDateString = function (value) {
    var date = new Date(value);
    return date.toCommonCase();
  };

  var getShortDateString = function (value) {
    var date = new Date(value);
    return date.toShortCase();
  };

  function showData(dataStr) {
    var data = $.parseJSON(dataStr);

    $("#divBody").css('display', '');

    if (data == null || data.product == null || data.validation_result == null || data.validation_result.toLowerCase() == "fake") {
      $("#img44").attr("src", "images/icon-false.png");

      $("#divScanInValidKey").css('display', '');
      $("#divScanDetailsInValidKey").css('display', '');
      $("#divFooterValidKey").css('display', 'none');

      return;
    }

    $("title").html('云溯科技：' + data.product.name);
    $("#divScanValidKey").css('display', '');
    $("#divScanResultValidKey").css('display', '');

    $("#imgManufacture").attr("src", "/api-rabbit/organization/" + data.manufacturer.id + "/logo?image_name=image-128x128");
    $("#imgProduct").attr("src", "/api-rabbit/productbase/" + data.product.product_base_id + "/image/image-800x400");
    if (data.validation_result.toLowerCase() == "uncertain") {
      $("#imgProStatus").attr("src", "images/icon-warning.png");
      $("#proStatus").html("此产品不确定");
    }
    else if (data.validation_result.toLowerCase() == "real") {
      $("#imgProStatus").attr("src", "images/icon-truer.png");
      $("#proStatus").html("此产品为真品");
    }

    $("#divRemind").css('display', '');
    $("#divScanDetailsValidKey").css('display', '');

    var td1 = $('<td></td>').css("width", "30%").html("商品码");
    var td2 = $('<td></td>').css("width", "70%").html(data.product.barcode);
    var tr = $('<tr></tr>').append(td1).append(td2);
    var tdSpace = $('<td></td>').css('height', '10px');
    var trSpace = $('<tr></tr>').append(tdSpace);
    $("#detailItems").append(tr).append(trSpace);

    td1 = $('<td></td>').css("width", "30%").html("物品名称");
    td2 = $('<td></td>').css("width", "70%").html(data.product.name);
    tr = $('<tr></tr>').append(td1).append(td2);
    tdSpace = $('<td></td>').css('height', '10px');
    trSpace = $('<tr></tr>').append(tdSpace);
    $("#detailItems").append(tr).append(trSpace);

    $.ajax({
      type: 'GET',
      url: '/api-rabbit/productbase/' + data.product.product_base_id + '/details',
      dataType: 'text/json',
      headers: {'X-YS-DeviceId': deviceId, 'X-YS-AppId': appId},
      success: function (dataStr) {

        var data = $.parseJSON(dataStr);

        if (data != null && data.details != null) {
          var details = data.details;
          for (var index = 0; index < details.length; index++) {
            var item = details[index];

            var td1 = $('<td></td>').css("width", "30%").html(item.name);
            var td2 = $('<td></td>').css("width", "70%").html(item.value);
            var tr = $('<tr></tr>').append(td1).append(td2);
            var tdSpace = $('<td></td>').css('height', '10px');
            var trSpace = $('<tr></tr>').append(tdSpace);

            $("#detailItems").append(tr).append(trSpace);
          }
        }

        if (data.contact != null) {
          $("#contactPhone").attr('href', 'tel:' + data.contact.hotline);
        }

        if (data['e_commerce'] != null && data['e_commerce'].length > 0) {
          $("#buyStore").attr('href', data['e_commerce'][0].url);
        }
      }
    });

    if (data.scan_record_list != null) {

      if (data.scan_record_list.length == 0)
        return;

      var latestScan = data.scan_record_list[0];
      var otherScans = data.scan_record_list.slice(1);
      var firstScan = data.scan_record_list[data.scan_record_list.length - 1];

      if (data.validation_result.toLowerCase() == "uncertain") {

        var province = firstScan.province || '';
        var city = firstScan.city || '未知';
        var plocation = province + city;

        $("#proStatus").html("请注意，本产品码已失效" + "<br/>" + "首次扫描时间：<b>" + getDateString(firstScan.created_datetime) + "</b>" + "<br/>首次扫描地点：<b>" + plocation + "</b>");
      }

      $("#scanRecords").html("");

      var p = $('<p></p>').html(latestScan.detail);
      var province = latestScan.province || '';
      var city = latestScan.city || '未知';
      var plocation = $('<span></span>').html(province + city + " " + getDateString(latestScan.created_datetime));

      var imglocation = $('<img/>').addClass("img-rounded").attr("src", "images/location.png").css("height", "20px").css("width", "20px");
      var divPanel = $('<div></div>').addClass("timeline-panel").append(p).append(imglocation).append(plocation);
      var div = $('<div></div>').addClass("timeline-badge down");
      var li = $('<li></li>').append(div);
      var lii = li.append(divPanel);

      $("#scanRecords").append(lii);

      if (otherScans.length > 0) {
        for (var otherScan in otherScans) {
          p = $('<p></p>').html(otherScans[otherScan].detail);

          var province = otherScans[otherScan].province || '';
          var city = otherScans[otherScan].city || '未知';
          var plocation = $('<span></span>').html(province + city + " " + getDateString(otherScans[otherScan].created_datetime));

          imglocation = $('<img/>').addClass("img-rounded").attr("src", "images/location.png").css("height", "20px").css("width", "20px");
          divPanel = $('<div></div>').addClass("timeline-panel").append(p).append(imglocation).append(plocation);
          div = $('<div></div>').addClass("timeline-badge primary");
          li = $('<li></li>').append(div);
          lii = li.append(divPanel);

          $("#scanRecords").append(lii);
        }
      }
    }

    if (data.logistics_list != null) {

      if (data.logistics_list.length == 0)
        return;

      var latestLogistics = data.logistics_list[0];
      var otherLogistics = data.logistics_list.slice(1);

      $("#divLogistics").html("");

      var p = $('<p></p>').html(getDateString(latestLogistics.datetime) + " " + latestLogistics.message + " " + latestLogistics.location);
      var divPanel = $('<div></div>').addClass("timeline-panel").append(p);
      var div = $('<div></div>').addClass("timeline-badge down");
      var li = $('<li></li>').append(div);
      var lii = li.append(divPanel);

      $("#divLogistics").append(lii);

      if (otherLogistics.length > 0) {
        for (var otherLogistic in otherLogistics) {
          p = $('<p></p>').html(getDateString(otherLogistics[otherLogistic].datetime) + " " + otherLogistics[otherLogistic].message + " " + otherLogistics[otherLogistic].location);
          divPanel = $('<div></div>').addClass("timeline-panel").append(p);
          div = $('<div></div>').addClass("timeline-badge primary");
          li = $('<li></li>').append(div);
          lii = li.append(divPanel);

          $("#divLogistics").append(lii);
        }
      }
    }
  }
});