function Music() {
    this.init();
}
(function () {
    var pages = [],
        panels = [],
        selectedItem = null;
    Music.prototype = {
        pageSize: 6,

        init: function () {
            var me = this;
            domUtils.on($G("J_searchName"), "keyup", function (event) {
                var e = window.event || event;
                if (e.keyCode == 13) {
                    me.dosearch();
                }
            });
            domUtils.on($G("J_searchBtn"), "click", function () {
                me.dosearch();
            });
        },
        callback: function (data) {
            var me = this;
            me.data = data.list;
            setTimeout(function () {
                $G('J_resultBar').innerHTML = me._renderTemplate(data.list);
            }, 300);
        },
        dosearch: function () {
            var me = this;
            selectedItem = null;
            var key = $G('J_searchName').value;
            if (utils.trim(key) == "")return false;
            key = encodeURIComponent(key);
            me._sent(key);
        },
        doselect: function (i) {
            var me = this;
            console.log(i);
            if (typeof i == 'object') {
                selectedItem = i;
            } else if (typeof i == 'number') {
                selectedItem = me.data[i];
            }
        },
        onpageclick: function (id) {
            var me = this;
            for (var i = 0; i < pages.length; i++) {
                $G(pages[i]).className = 'pageoff';
                $G(panels[i]).className = 'paneloff';
            }
            $G('page' + id).className = 'pageon';
            $G('panel' + id).className = 'panelon';
        },
        listenTest: function (elem) {
            var me = this,
                elem = $(elem),
                audio = $('#audioPlay'),
                is_play_action = elem.hasClass('m-try');
            var data=me._getData(elem);
            var f=data.f.split('|');
            var mid=f[20];
            var href='http://y.qq.com/#type=song&mid='+mid+'&play=1';
            window.top.wxbalert.notice({text:'试听页面已在您的默认浏览器中打开'});
            window.top.wxb.gui.Shell.openExternal(href);

        },
        _sent: function (key,page) {
            var me = this;
            $G('J_resultBar').innerHTML = '<div class="loading"></div>';
            var _winTop=window.top;
            _winTop.Material.searchQQMusic(key,page).then(function (data) {
                me.callback(data);
            }).catch(function (err) {
                me.callback(err);
            });
        },
        _removeHtml: function (str) {
            var reg = /<\s*\/?\s*[^>]*\s*>/gi;
            return str.replace(reg, "");
        },
        _getUrl: function (isTryListen) {
            var me = this;
            var param = 'from=tiebasongwidget&url=&name=' + encodeURIComponent(me._removeHtml(selectedItem.title)) + '&artist='
                + encodeURIComponent(me._removeHtml(selectedItem.author)) + '&extra='
                + encodeURIComponent(me._removeHtml(selectedItem.album_title))
                + '&autoPlay=' + isTryListen + '' + '&loop=true';
            return me.playerUrl + "?" + param;
        },
        _getData: function (item) {
            var me = this;
            var label=item.closest('.m-m');
            var id=label.attr('data-id');
            for (var i = 0; i < me.data.length; i++) {
                var obj = me.data[i];
                if(id==obj.id){
                    return obj;
                }
            }
            return {};
        },
        _buildMusicHtml: function (playerUrl) {
            var html = '<embed class="BDE_try_Music" allowfullscreen="false" pluginspage="http://www.macromedia.com/go/getflashplayer"';
            html += ' src="' + playerUrl + '"';
            html += ' width="1" height="1" style="position:absolute;left:-2000px;"';
            html += ' type="application/x-shockwave-flash" wmode="transparent" play="true" loop="false"';
            html += ' menu="false" allowscriptaccess="never" scale="noborder">';
            return html;
        },
        _byteLength: function (str) {
            return str.replace(/[^\u0000-\u007f]/g, "\u0061\u0061").length;
        },
        _getMaxText: function (s) {
            return s;
        },
        _rebuildData: function (data) {
            var me = this,
                newData = [],
                d = me.pageSize,
                itembox;
            for (var i = 0; i < data.length; i++) {
                if ((i + d) % d == 0) {
                    itembox = [];
                    newData.push(itembox)
                }
                itembox.push(data[i]);
            }
            return newData;
        },
        _renderTemplate: function (data) {
            var me = this;
            if (data.length == 0)return '<div class="empty">' + lang.emptyTxt + '</div>';
            data = me._rebuildData(data);
            var s = [], p = [], t = [];
            s.push('<div id="J_listPanel" class="listPanel">');
            p.push('<div class="page">');
            for (var i = 0 ; i<data.length;i++) {
                var tmpList = data[i];
                panels.push('panel' + i);
                pages.push('page' + i);
                if (i == 0) {
                    s.push('<div id="panel' + i + '" class="panelon">');
                    if (data.length != 1) {
                        t.push('<div id="page' + i + '" onclick="music.onpageclick(' + i + ')" class="pageon">' + (i+1 ) + '</div>');
                    }
                } else {
                    s.push('<div id="panel' + i + '" class="paneloff">');
                    t.push('<div id="page' + i + '" onclick="music.onpageclick(' + i + ')" class="pageoff">' + (i+1 ) + '</div>');
                }
                s.push('<div class="m-box">');
                s.push('<div class="m-h"><span class="m-t">歌名</span><span class="m-s">歌曲信息</span><span class="m-z">专辑</span><span class="m-try-t">' + lang.listenTest + '</span></div>');
                for (var j = 0; j< tmpList.length;j++) {
                    var tmpObj = tmpList[j];
                    s.push('<label id="music_'+tmpObj.id+'" data-id="'+tmpObj.id+'" for="radio-' + i + '-' + j + '" class="m-m">');
                    s.push('<input type="radio" id="radio-' + i + '-' + j + '" name="musicId" class="m-l" onclick="music.doselect(' + (me.pageSize * i + j ) + ')"/>');
                    s.push('<span title="'+tmpObj.songname+'" class="m-t">' + me._getMaxText(tmpObj.songname) + '</span>');
                    s.push('<span title="'+tmpObj.singername+'" class="m-s">' + tmpObj.singername + '</span>');
                    s.push('<span title="'+tmpObj.albumname+'" class="m-z">' + me._getMaxText(tmpObj.albumname) + '</span>');
                    s.push('<span class="m-try" onclick="music.doselect(' + (me.pageSize * i + j ) + ');music.listenTest(this)"></span>');
                    s.push('</label>');
                }
                s.push('</div>');
                s.push('</div>');
            }
            t.reverse();
            p.push(t.join(''));
            s.push('</div>');
            p.push('</div>');
            return s.join('') + p.join('');
        },
        exec: function () {
            var me = this;
            if (selectedItem == null)   return;
            var f=selectedItem.f.split('|');
            var albumurlData=f[22];
            var html='<p></p><p class="wxb-musci-wrap"><qqmusic class="res_iframe qqmusic_iframe js_editor_qqmusic" ' +
                'musicid="'+f[0]+'"' +
                ' mid="'+f[20]+'" ' +
                'albumurl="/'+albumurlData.substr(-2,1)+'/'+albumurlData.substr(-1)+'/'+albumurlData+'.jpg" ' +
                'audiourl="'+selectedItem.m4a+'" ' +
                'music_name="'+selectedItem.songname+'" ' +
                'commentid="00111" ' +
                'singer="'+selectedItem.singername+'" ' +
                'play_length="237000" ' +
                'src="/cgi-bin/readtemplate?t=tmpl/qqmusic_tmpl&amp;singer='+encodeURIComponent(selectedItem.singername)+'&amp;music_name='+encodeURIComponent(selectedItem.songname)+'"' +
                '></qqmusic></p><p>&nbsp;</p>';
            editor.execCommand('inserthtml', html);
        }
    };
})();



