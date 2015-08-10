(function ($) {
  $.fn.YUploadImg = function (config) {
    var defaultConfig = {
      name: '',
      url: '',
      dataType: 'json',
      formData: {},
      autoUpload: false,
      acceptFileTypes: /(\.|\/)(gif|jpe?g|png)$/i,
      value:false,
      pasteZone:false,
      maxFileSize: 500000,
      maxNumberOfFiles:false,
      onChange:false
    };
    return this.each(function () {
      config = $.extend(true, {
        getNumberOfFiles: function () {
          return newFile.length+imgs.length-1;
        }}, defaultConfig, config);
      var $this=$(this);
      var uploadBtn=$this.closest('.fileinput-button');
      var processBar;
      var imgs=[];
      var isMultiple=$this.attr('multiple');
      var newFile=[];
      var _ajax=null;
      var hiddenVal = $("<input type='text' class='js-upload-value' style='width:0;height: 0;display: inline-block;border: 0;' name='" + config.name + "' "+(config.value==false?"":"value='"+config.value+"'")+"/>");
      uploadBtn.append(hiddenVal);
      var previewArea=$('<ul style="display: none;" class="upload-prev-area clearfix"></ul>');
      uploadBtn.after(previewArea);
      config.value= $.trim(config.value);
      if(config.value != "false" && config.value.length>0){
        imgs=config.value.split(',');
        addImgHtml('init');
      }
      if(config.maxNumberOfFiles===0){
        uploadBtn.hide();
      }
      //var thisUpload=$(this).fileupload(config)
      //    .on('fileuploadstart', function (e, data) {
      //      imgs=[];
      //      previewArea.find('.upload-prev-img ').each(function(){
      //        imgs.push($(this).attr('data-value'));
      //      });
      //      var html = '<div style="margin-bottom: 10px;" class="progress progress-striped active"><div class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100" style="width: 5%"></div></div>';
      //      processBar = $(html);
      //      $(e.target).closest('.fileinput-button').hide().after(processBar);
      //    }).on('fileuploadprocessalways', function (e, data) {
      //      var index = data.index,
      //          file = data.files[index];
      //      if (file.error) {
      //        toastr.error(file.error, '上传文件失败');
      //        return false;
      //      }
      //    }).on('fileuploadprogressall', function (e, data) {
      //      if(!processBar){
      //        return false;
      //      }
      //      var progress = parseInt(data.loaded / data.total * 100, 10);
      //      processBar.find('.progress-bar').css('width', progress + '%');
      //    }).on('fileuploaddone', function (e, data) {
      //      if(!processBar){
      //        return false;
      //      }
      //      processBar.remove();
      //      if (data.result.error == 0) {
      //        imgs.push(data.result.url);
      //      } else {
      //        toastr.error(data.result.message, '上传文件失败');
      //      }
      //    }).on('fileuploadadd', function (e, data) {
      //      newFile=data.originalFiles;
      //    }).on('fileuploadstop', function (e, data) {
      //      addImgHtml();
      //    }).on('fileuploadfail', function (e, data) {
      //      toastr.error('上传文件失败');
      //      console.log('fileuploadfail', arguments);
      //    });

      previewArea.on('click','.remove-btn',function(){
        var $this=$(this);
        Win.confirm({
          content:'是否删除这张图片？',
          noText:'取消',
          yesFunc:function(){
            var thisLi=$this.closest('li');
            var index=thisLi.index();
            imgs.splice(index,1);
            addImgHtml();
          }
        });
      });

      function addImgHtml(type){
        var imgHtml='';
        var url='';
        var imgSrc='';
        for (var i = 0, len = imgs.length; i < len; i++) {
          url = imgs[i];
          if(!/^http/.test(url)){
            imgSrc = SITE_UPLOAD + url;
          }else{
            imgSrc=url;
          }
          imgHtml += '<li><a class="upload-prev-img-wrap fancybox-img" data-rel="'+config.name+'" href="'+imgSrc+'"><img data-value="'+url+'" class="upload-prev-img " src="' + imgSrc + '"></a><button type="button" class="btn remove-btn btn-link"><i class="fa fa-trash-o"></i>删 除</button></li>';
        }
        imgHtml=$(imgHtml);
        previewArea.html(imgHtml);
        uploadBtn.find('[name="' + config.name + '"]').val(imgs.join(','));
        previewArea.show();
        if(isMultiple==undefined){
          if(imgs.length==0){
            uploadBtn.show();
            previewArea.hide();
          }else{
            uploadBtn.hide();
          }
        }else{
          uploadBtn.show();
        }
        if(config.maxNumberOfFiles && imgs.length>=config.maxNumberOfFiles){
          uploadBtn.addClass('disabled');
        }else{
          uploadBtn.removeClass('disabled');
        }
        handleFancybox();

        if(type!='init' && typeof config.onChange=="function"){
          config.onChange(imgHtml);
        }
      }

      function handleFancybox () {
        if (!$.fancybox) {
          return;
        }
        if ($(".fancybox-img").size() > 0) {
          $(".fancybox-img").fancybox({
            href : $(this).attr('href'),
            groupAttr: 'data-rel',
            openEffect : 'elastic',
            prevEffect: 'none',
            nextEffect: 'none',
            closeBtn: true
          });
        }
      }
    });
  };
})(jQuery);
(function ($) {
  $.fn.YUploadFile = function (config) {
    var defaultConfig = {
      name: '',
      url: '',
      dataType: 'json',
      formData: {},
      autoUpload: false,
      acceptFileTypes: /(\.|\/)(gif|jpe?g|png)$/i,
      value:false,
      pasteZone:false,
      maxFileSize: 500000,
      maxNumberOfFiles:false
    };
    return this.each(function () {
      config = $.extend(true, {
        getNumberOfFiles: function () {
          return newFile.length+imgs.length-1;
        }}, defaultConfig, config);
      var $this=$(this);
      var uploadBtn=$this.closest('.fileinput-button');
      var processBar;
      var imgs=[];
      var isMultiple=$this.attr('multiple');
      var newFile=[];
      var _ajax=null;
      var hiddenVal = $("<input type='text' class='js-upload-value' style='width:0;height: 0;display: inline-block;border: 0;' name='" + config.name + "' "+(config.value==false?"":"value='"+config.value+"'")+"/>");
      uploadBtn.append(hiddenVal);
      var previewArea=$('<ul style="display: none;" class="upload-prev-area clearfix"></ul>');
      uploadBtn.after(previewArea);
      config.value= $.trim(config.value);
      if(config.value != "false" && config.value.length>0){
        imgs=config.value.split(',');
        addImgHtml();
      }
      var thisUpload=$(this).fileupload(config)
          .on('fileuploadstart', function (e, data) {
            imgs=[];
            previewArea.find('.upload-prev-img ').each(function(){
              imgs.push($(this).attr('data-value'));
            });
            var html = '<div style="margin-bottom: 10px;" class="progress progress-striped active"><div class="progress-bar progress-bar-success" role="progressbar" aria-valuenow="40" aria-valuemin="0" aria-valuemax="100" style="width: 5%"></div></div>';
            processBar = $(html);
            $(e.target).closest('.fileinput-button').hide().after(processBar);
          }).on('fileuploadprocessalways', function (e, data) {
            var index = data.index,
                file = data.files[index];
            if (file.error) {
              toastr.error(file.error, '上传文件失败');
              return false;
            }
          }).on('fileuploadprogressall', function (e, data) {
            if(!processBar){
              return false;
            }
            var progress = parseInt(data.loaded / data.total * 100, 10);
            processBar.find('.progress-bar').css('width', progress + '%');
          }).on('fileuploaddone', function (e, data) {
            if(!processBar){
              return false;
            }
            processBar.remove();
            if (data.result.error == 0) {
              imgs.push(data.result.url);
            } else {
              toastr.error(data.result.message, '上传文件失败');
            }
          }).on('fileuploadadd', function (e, data) {
            newFile=data.originalFiles;
          }).on('fileuploadstop', function (e, data) {
            addImgHtml();
          }).on('fileuploadfail', function (e, data) {
            toastr.error('上传文件失败');
            console.log('fileuploadfail', arguments);
          });

      previewArea.on('click','.remove-btn',function(){
        var $this=$(this);
        Win.confirm({
          content:'是否删除这张图片？',
          noText:'取消',
          yesFunc:function(){
            var thisLi=$this.closest('li');
            var index=thisLi.index();
            imgs.splice(index,1);
            addImgHtml();
          }
        });
      });

      function addImgHtml(){
        var fileHtml='';
        for (var i = 0, len = imgs.length; i < len; i++) {
          var url = imgs[i];
          var imgSrc = SITE_UPLOAD + url;
          fileHtml += '<li class="upload-file"><a class="upload-prev-file-wrap" data-rel="'+config.name+'" href="javascript:void(0);"><img data-value="'+url+'" class="upload-prev-file " src="' + SITE_PUBLIC + 'img/upload-file-icon.png"></a><button type="button" class="btn remove-btn btn-link"><i class="fa fa-trash-o"></i>删 除</button></li>';
        }
        fileHtml=$(fileHtml);
        previewArea.html(fileHtml);
        uploadBtn.find('[name="' + config.name + '"]').val(imgs.join(','));
        previewArea.show();
        if(isMultiple==undefined){
          if(imgs.length==0){
            uploadBtn.show();
            previewArea.hide();
          }else{
            uploadBtn.hide();
          }
        }else{
          uploadBtn.show();
        }
        handleFancybox();
      }
      function handleFancybox () {
        if (!$.fancybox) {
          return;
        }
        if ($(".fancybox-img").size() > 0) {
          $(".fancybox-img").fancybox({
            groupAttr: 'data-rel',
            openEffect : 'elastic',
            prevEffect: 'none',
            nextEffect: 'none',
            closeBtn: true
          });
        }
      }
    });
  };
})(jQuery);