(function () {
  var app = angular.module('root');

  app.factory('messageEditService', ['$http', function ($http) {
    return {};
  }]);

  app.controller('messageEditCtrl', ['$scope', '$timeout', 'messageEditService', '$location', function ($scope, $timeout, messageEditService, $location) {

    var __refreshPageTimmer;

    var oldData = {
      title: '',
      author: '',
      fileid: '',
      show_cover_pic: "1",
      content: '',
      digest: ''
    };

    var localData = window.localStorage ? {
      set: function (key, value) {
        window.localStorage.setItem(key, value);
      },
      get: function (key) {
        return window.localStorage.getItem(key);
      },
      remove: function (key) {
        localStorage.removeItem(key);
      }
    } : null;

    var ue = UE.getEditor('editor', {
      toolbars: [
        ['fullscreen', 'source', 'undo', 'redo', 'bold', 'italic', 'underline', 'fontborder', 'strikethrough', 'removeformat', 'autotypeset', 'blockquote', 'pasteplain', 'forecolor', 'backcolor', 'insertorderedlist', 'insertunorderedlist', 'selectall', 'cleardoc', 'rowspacingtop', 'rowspacingbottom', 'lineheight', 'fontsize', 'indent', 'justifyleft', 'justifycenter', 'justifyright', 'justifyjustify', 'touppercase', 'tolowercase', 'simpleupload', 'emotion', 'map', 'date', 'time', 'spechars', 'searchreplace']
      ]
    });

    if (localData && localData.get('wxDataone')) {
      oldData = JSON.parse(localData.get('wxDataone'));
    }

    allfunction();

    function allfunction() {
      ue.ready(function () {
        $('input[name="msgTitle"]').val(oldData.title);
        $('input[name="msgAuthor"]').val(oldData.author);
        UE.getEditor('editor').setContent(oldData.content);
        if (oldData.digest !== "") {
          $("#J_digest").val(oldData.digest)
          $(".J_digest").hide().attr("data-show", "true").next(".js-url-area").show();
        }

        if (oldData.show_cover_pic == "0") {
          $('input[name="ifshowMain"]').attr({"data-show": "0", "checked": false})
        }

        var client = new ZeroClipboard($(".user-invite-copy-link"));
        ZeroClipboard.config({
          swfPath: "../../lib//ueditor/third-party/zeroclipboard/ZeroClipboard.swf"
        });
        client.on('ready', function (event) {
          client.on('copy', function (event) {
            var html = UE.getEditor('editor').getContent();
            event.clipboardData.setData('text/html', html);
            event.clipboardData.setData('text/plain', html);

          });
          client.on('aftercopy', function (event) {
            toastr.success('正文内容已经复制到剪切板！', "提示");
          });
        });

        ue.addListener('selectionchange', function (ue) {
          refreshPage();

          if (!UE.getEditor('editor').hasContents()) {
            $(".user-invite-copy-link").addClass('disabled')
          } else {
            $(".user-invite-copy-link").removeClass('disabled')
          }
        })
      });

      var html = renderPreview();
      var ifr_win = $('#J_iframe')[0].contentWindow;
      $(ifr_win.document).find('body').html(html);

      $('#showPic').change(function () {
        ifshow = $('[name="ifshowMain"]').is(':checked');
        if (ifshow) {
          $("#showPic").attr("data-show", "1");
        } else {
          $("#showPic").attr("data-show", "0");
        }
        refreshPage();
      });

      $('input[name="msgTitle"],input[name="msgAuthor"],input[name="linkUrl"],.J_msgDigest').keyup(function () {
        refreshPage();
      });

      var head_image = $('#addPic_avatar').YUploadImg({
        name: 'head_image',
        url: '',
        acceptFileTypes: /(\.|\/)(gif|jpe?g|png)$/i,
        maxFileSize: 2000000,
        value: oldData.fileid,
        onChange: function () {
          refreshPage();
        }
      });
    }

    function renderPreview(data) {
      var nowTime = new DateTime(new Date());
      var defaultConfig = {
        title: '',
        date: nowTime.toString('yyyy-MM-dd'),
        author: '',
        fileid: '',
        show_cover_pic: "1",
        content: '',
        digest: ''
      };

      var _data = $.extend({}, true, defaultConfig, data);

      var style = '<style>html{-ms-text-size-adjust:100%;-webkit-text-size-adjust:100%}p{margin:0;}img{max-width:100%;}body{line-height:1.6;font-family:"Helvetica Neue",Helvetica,Arial,sans-serif;font-size:16px}body,h1,h2,h3,h4,h5,p,ul,ol,dl,dd,fieldset,textarea{margin:0}fieldset,legend,textarea,input,button{padding:0}button,input,select,textarea{font-family:inherit;font-size:100%;margin:0;*font-family:"Helvetica Neue",Helvetica,Arial,sans-serif}ul,ol{padding-left:0;list-style-type:none;list-style-position:inside}a img,fieldset{border:0}a{text-decoration:none}a{color:#607fa6}.rich_media_inner{padding:15px}.rich_media_title{line-height:24px;font-weight:700;font-size:20px;word-wrap:break-word;-webkit-hyphens:auto;-ms-hyphens:auto;hyphens:auto}.rich_media_title .rich_media_meta{vertical-align:middle;position:relative}.rich_media_meta{display:inline-block;vertical-align:middle;font-weight:400;font-style:normal;margin-right:.5em;font-size:12px;width:auto;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;word-wrap:normal;max-width:none}.rich_media_meta.text{color:#8c8c8c}.rich_media_thumb{font-size:0;margin-top:18px}.rich_media_thumb img{width:100%}.rich_media_content{margin-top:18px;color:#3e3e3e;word-wrap:break-word;-webkit-hyphens:auto;-ms-hyphens:auto;hyphens:auto}.rich_media_content p{*zoom:1;min-height:1em;word-wrap:normal;white-space:pre-wrap;margin-top:1em;margin-bottom:1em}.rich_media_content *{max-width:100%!important;word-wrap:break-word!important;-webkit-box-sizing:border-box!important;box-sizing:border-box!important}.rich_media_content img{height:auto!important}.rich_media_tool{*zoom:1;padding:18px 0;font-size:14px}.rich_media_tool .media_tool_meta i,.rich_media_tool .media_tool_meta .icon_meta{vertical-align:0;position:relative;top:1px;margin-right:3px}.rich_media_tool .meta_primary{float:left;margin-right:14px}.rich_media_tool .meta_extra{float:right;margin-left:14px}.rich_media_tool .link_primary{color:#8c8c8c}.rich_media_extra{padding-top:0}@media screen and (min-width:1023px){.rich_media{width:740px;margin-left:auto;margin-right:auto}.rich_media_inner{padding:20px;background-color:#fff;border:1px solid #d9dadc;border-top-width:0}.rich_media_meta{max-width:none}.rich_media_content{min-height:350px}.rich_media_title{padding-bottom:10px;margin-bottom:5px;border-bottom:1px solid #e7e7eb}}.line_tips_wrp{margin-top:20px;text-align:center;border-top:1px dotted #a8a8a7;line-height:16px}.line_tips{display:inline-block;position:relative;top:-10px;padding:0 16px;font-size:14px;color:#cfcfcf;background-color:#f8f7f5;text-decoration:none}body{background-color:#f8f7f5;-webkit-touch-callout:none}h1,h2,h3,h4,h5,h6{font-weight:400;font-style:normal;font-size:100%}.icon_arrow_gray{width:7px}.icon_loading_white{width:16px}.icon_praise_gray{width:13px}.line_tips_wrp{margin-bottom:10px}.rich_media_meta.nickname{max-width:10em}.rich_media_extra{position:relative}.rich_media_extra .appmsg_banner{max-height:166px;width:100%}.rich_media_extra .ad_msg_mask{position:absolute;width:100%;height:100%;text-align:center;line-height:200px;background-color:#000;filter:alpha(opacity=20);-moz-opacity:.2;-khtml-opacity:.2;opacity:.2;left:0;top:0}.rich_media_content{font-size:16px}.rich_media_content p{margin-top:0;margin-bottom:0}.rich_media_tool .praise_num{display:inline-block;vertical-align:top;width:auto;overflow:hidden;text-overflow:ellipsis;white-space:nowrap;word-wrap:normal;min-width:3em}.rich_media_tool .meta_praise{-webkit-tap-highlight-color:rgba(0,0,0,0);outline:0;margin-right:0}.icon_praise_gray{background:transparent url(http://mmbiz.qpic.cn/mmbiz/ByCS3p9sHiam47qqib840uVr9ZH6ORLqhqmFibrmxWeY5icJ7ZE8Un8AibB18U19fCMUg9tibw8vgOdl4/0) no-repeat 0 0;width:13px;height:13px;vertical-align:middle;display:inline-block;-webkit-background-size:100% auto;background-size:100% auto}.icon_praise_gray.praised{background-position:0 -18px}.praised .icon_praise_gray{background-position:0 -18px}.rich_media_extra{padding-bottom:10px;font-size:14px}.rich_media_extra .extra_link{display:block}.rich_media_extra img{vertical-align:middle;margin-top:-3px}.rich_media_extra .icon_loading_white{margin-left:1em}.global_error_msg{padding:60px 30px}.global_error_msg strong{display:block}.global_error_msg.warn{color:#f00}.selectTdClass{background-color:#edf5fa!important}table.noBorderTable td,table.noBorderTable th,table.noBorderTable caption{border:1px dashed #ddd!important}table{margin-bottom:10px;border-collapse:collapse;display:table;width:100%!important}td,th{word-wrap:break-word;word-break:break-all;padding:5px 10px;border:1px solid #DDD}caption{border:1px dashed #DDD;border-bottom:0;padding:3px;text-align:center}th{border-top:2px solid #BBB;background:#f7f7f7}.ue-table-interlace-color-single{background-color:#fcfcfc}.ue-table-interlace-color-double{background-color:#f7faff}td p{margin:0;padding:0}.res_iframe{width:100%;background-color:transparent;border:0}.vote_area{position:relative;display:block;margin:14px 0;white-space:normal!important}.vote_iframe{width:100%;height:100%;background-color:transparent;border:0}.rich_media_bottom{width:100%;margin-top:50px;}.rich_media_bottom a{color:#607fa6;text-decoration:none;float:left;}.rich_media_bottom span{color:gray;float:right;}</style>';

      var imgHtml = '<div class="rich_media_thumb" id="media"> <img onerror="//this.parentNode.removeChild(this);" src="' + _data.fileid + '"> </div>';

      if (_data.fileid == "" || _data.show_cover_pic == "0") {
        imgHtml = "";
      }

      var html = '<div class="rich_media"><div class="rich_media_inner"> <h2 class="rich_media_title" id="activity-name"> ' + _data.title + ' </h2> <div class="rich_media_meta_list"> <em id="post-date" class="rich_media_meta text">' + _data.date + '</em> <a class="rich_media_meta link nickname" href="javascript:void(0);" id="post-user">' + _data.author + '</a> </div> <div id="page-content"> <div id="img-content"> ' + imgHtml + ' <div class="rich_media_content" id="js_content">' + _data.content + '<p>&nbsp;</p></div> </div> </div>' + '</div> </div>';

      return style + html;
    }

    function getPageData() {
      var data = {
        title: $('[name="msgTitle"]').val(),
        author: $('[name="msgAuthor"]').val(),
        show_cover_pic: $('[name="ifshowMain"]').attr('data-show'),
        fileid: $('[name="head_image"]').val(),
        digest: $(".J_msgDigest").val(),
        content: UE.getEditor('editor').getContent()
      };

      return data;
    }

    function refreshPage() {
      clearTimeout(__refreshPageTimmer);

      __refreshPageTimmer = setTimeout(function () {
        var html = renderPreview(getPageData());
        oldData = getPageData();
        var ifr_win = $('#J_iframe')[0].contentWindow;
        $(ifr_win.document).find('body').html(html);
      }, 300);
    }

    $scope.tempSave = function () {
      localData.set("wxDataone", JSON.stringify(oldData));
    }

    $scope.submit = function () {
      var content = getPageData();

      if (content.title == "") {
        $scope.utils.alert('info', '标题不能为空');
        $('input[name="msgTitle"]').focus();
        return false;
      }
      if (content.title.length > 64) {
        $scope.utils.alert('info', '标题长度最大64个字');
        $('input[name="msgTitle"]').focus();
        return false;
      }
      if (content.fileid == "") {
        $('body').scrollTop(0);
        $scope.utils.alert('info', '封面图片不能为空');
        return false;
      }
      if (content.content == "") {
        $scope.utils.alert('info', '正文不能为空');
        return false;
      }

      $scope.utils.alert('info', '推送功能正在开发中...');
    };
  }]);
})();