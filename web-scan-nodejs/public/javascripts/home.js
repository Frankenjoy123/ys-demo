$(document).ready(function () {

    var productKey = 'okl-xPnHS-eRwx_mdfrTyh';
    var appId = '2k4d3xp24wpxxcf4myt';

    var postObject = {
        key: productKey,
        device_code: '00000-00000-00000-00000',
        app_id: appId,
        location: '未知地址',
        latitude: "",
        longitude: ""
    };

    $.ajax({
        type: 'GET',
        url: 'http://api.map.baidu.com/location/ip',
        data: {
            ak: 'a3RT1irKVTWDAuLKuvTtagMa',
            coor: 'bd09ll',
            callback: 'getCurrentPos'
        },
        dataType: 'jsonp',
        success: function (dataStr) {
            postObject.longitude = dataStr.content.point.x;
            postObject.latitude = dataStr.content.point.y;
            postObject.location = "Address:" + dataStr.content.address + ";Country:中国;State:" + dataStr.content.address_detail.province + ";City:" + dataStr.content.address_detail.city + ";";

            getData();
        },
        error: function (dataStr) {
            getData();
        }
    });

    function getData() {
        if (location.href.lastIndexOf('#scaned') > 0) {
            $.ajax({
                type: 'GET',
                url: '/api-rabbit/scan/' + productKey,
                dataType: 'text/json',
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
                contentType: 'application/json',
                success: function (dataStr) {
                    showData(dataStr);

                    $("#loading").css('display', 'none');
                }
            });
        }
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

        $("#imgManufacture").attr("src", "/api-rabbit/organization/" + data.manufacturer.id + "/logo-mobile");
        $("#imgProduct").attr("src", "/api-rabbit/productbase/" + data.product.product_base_id + "/full-mobile");
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
            url: '/api-rabbit/productbase/' + data.product.product_base_id + '/notes/json',
            dataType: 'text/json',
            success: function (dataStr) {

                var data = $.parseJSON(dataStr);

                if (data.details != null) {
                    var details = data.details;
                    if (details != null) {
                        for (var detail in details) {
                            var td1 = $('<td></td>').css("width", "30%").html(detail);
                            var td2 = $('<td></td>').css("width", "70%").html(details[detail]);
                            var tr = $('<tr></tr>').append(td1).append(td2);
                            var tdSpace = $('<td></td>').css('height', '10px');
                            var trSpace = $('<tr></tr>').append(tdSpace);

                            $("#detailItems").append(tr).append(trSpace);
                        }
                    }
                }

                if (data.contact != null) {
                    $("#contactPhone").attr('href', 'tel:' + data.contact.hotline);
                }

                if (data['e-commerce'] != null) {
                    $("#buyStore").attr('href', data['e-commerce'].tmall);
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
                var plocation;
                if (!(firstScan.location.indexOf(":") >= 0))
                    plocation = "未知地点";
                else {
                    var details = firstScan.location.split(";");
                    for (var detail in details) {
                        var dat = details[detail];
                        var datItems = dat.split(":");
                        if (!(datItems == null || !(datItems.length >= 2))) {
                            if (datItems[0] == "City") {
                                plocation = datItems[1];
                                break;
                            }
                        }
                    }
                }

                $("#proStatus").html("请注意，本产品码已失效" + "<br/>" + "首次扫描时间：<b>" + getDateString(firstScan.created_datetime) + "</b>" + "<br/>首次扫描地点：<b>" + plocation + "</b>");
            }

            $("#scanRecords").html("");

            var p = $('<p></p>').html(latestScan.detail);

            var plocation;
            if (!(latestScan.location.indexOf(":") >= 0))
                plocation = $('<span></span>').html("未知" + " " + getDateString(latestScan.created_datetime));
            else {
                var details = latestScan.location.split(";");
                for (var detail in details) {
                    var dat = details[detail];
                    var datItems = dat.split(":");
                    if (!(datItems == null || !(datItems.length >= 2))) {
                        if (datItems[0] == "City") {
                            plocation = $('<span></span>').html(datItems[1] + " " + getDateString(latestScan.created_datetime));
                            break;
                        }
                    }
                }
            }

            var imglocation = $('<img/>').addClass("img-rounded").attr("src", "images/location.png").css("height", "20px").css("width", "20px");
            var divPanel = $('<div></div>').addClass("timeline-panel").append(p).append(imglocation).append(plocation);
            var div = $('<div></div>').addClass("timeline-badge down");
            var li = $('<li></li>').append(div);
            var lii = li.append(divPanel);

            $("#scanRecords").append(lii);

            if (otherScans.length > 0) {
                for (var otherScan in otherScans) {
                    p = $('<p></p>').html(otherScans[otherScan].detail);

                    if (!(otherScans[otherScan].location.indexOf(":") >= 0))
                        plocation = $('<span></span>').html("未知" + " " + getDateString(otherScans[otherScan].created_datetime));
                    else {
                        var details = otherScans[otherScan].location.split(";");
                        for (var detail in details) {
                            var dat = details[detail];
                            var datItems = dat.split(":");
                            if (!(datItems == null || !(datItems.length >= 2))) {
                                if (datItems[0] == "City") {
                                    plocation = $('<span></span>').html(datItems[1] + " " + getDateString(otherScans[otherScan].created_datetime));
                                    break;
                                }
                            }
                        }
                    }

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