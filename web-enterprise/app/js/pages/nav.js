(function () {
    var app = angular.module("nav", []);

    app.controller("navCtrl", ["$scope", function ($scope) {
            
        }]);
    
    app.directive("treeview", function(){
        return function(scope, element, attrs){
            element.children().first().click(function(){
                if ($(this).children().last().hasClass("fa-angle-left")){
                    $(this).next().slideDown();
                }else{
                    $(this).next().slideUp();
                }
                $(this).children().last().toggleClass("fa-angle-down fa-angle-left");
            });
        };
    });
})();