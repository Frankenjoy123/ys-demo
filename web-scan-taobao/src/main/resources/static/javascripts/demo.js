/**
 * Created by yan on 1/22/2016.
 */
$(document).ready(function(){
    var url = window.location.href;
    if(url.indexOf("type=2")>0)
        $("div[type='2']").show();
    else if (url.indexOf("type=3")>0)
        $("div[type='3']").show();
    else
        $("div[type='1']").show();
});