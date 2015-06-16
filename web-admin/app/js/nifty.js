/*
 * NIFTY ADMIN TEMPLATE JAVASCRIPT
 * ======================================================================
 * NOTE : All JavaScript plugins require jQuery to be included
 * http://jquery.com/
 *
 */





/* ========================================================================
 * SELECTOR CACHE v.1.0
 * -------------------------------------------------------------------------
 * - themeOn.net -
 * ========================================================================*/
/*
To improve performance and load time, you don't need to create a new variable to get main selector,
for the main selector has been cached and used in all of plugins, just need to call the variables.

Example:
To get selector "#container" maybe you can use

var $container = $ ('# container');
$container.addClass('effect');


For more efficient, simply called "nifty.container".


nifty.container.addClass('effect');


Both of the above methods will produce the same results.

*/

!function ($) {
	"use strict";

	window.nifty = {
		'container'         : $('#container'),
		'contentContainer'  : $('#content-container'),
		'navbar'            : $('#navbar'),
		'mainNav'           : $('#mainnav-container'),
		'aside'             : $('#aside-container'),
		'footer'            : $('#footer'),
		//'scrollTop'         : $('#scroll-top'),

		'window'            : $(window),
		'body'              : $('body'),
		'bodyHtml'          : $('body, html'),
		'document'          : $(document),
		'screenSize'        : '', // return value xs, sm, md, lg
		'isMobile'          : function(){
				return ( /Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent) )
		}(),
		'randomInt'         : function(min,max){
			return Math.floor(Math.random()*(max-min+1)+min);
		},
		'transition'          : function(){
			var thisBody = document.body || document.documentElement,
			thisStyle = thisBody.style,
			support = thisStyle.transition !== undefined || thisStyle.WebkitTransition !== undefined;
			return support
		}()
	};

	nifty.window.on('load', function(){
		//Activate the Bootstrap tooltips
		var tooltip = $('.add-tooltip');
		if (tooltip.length)tooltip.tooltip();

		var popover = $('.add-popover');
		if (popover.length)popover.popover();


		// STYLEABLE SCROLLBARS
		// =================================================================
		// Require nanoScroller
		// http://jamesflorentino.github.io/nanoScrollerJS/
		// =================================================================
		var nano = $('.nano');
		if(nano.length) nano.nanoScroller({
			preventPageScrolling: true
		});

		// Update nancoscroller
		$('#navbar-container .navbar-top-links').on('shown.bs.dropdown', '.dropdown', function () {
			$(this).find('.nano').nanoScroller({preventPageScrolling: true});
		});


		nifty.body.addClass('nifty-ready');
	});


}(jQuery);



/* ========================================================================
 * MEGA DROPDOWN v1.0.1
 * -------------------------------------------------------------------------
 * By ThemeOn.net
 * ========================================================================*/
!function ($) {
	"use strict";

	var megadropdown = null,
	mega = function(el){
		var megaBtn = el.find('.mega-dropdown-toggle'), megaMenu = el.find('.mega-dropdown-menu');

		megaBtn.on('click', function(e){
			e.preventDefault();
			el.toggleClass('open');
		});
	},
	methods = {
		toggle : function(){
			this.toggleClass('open');
			return null;
		},
		show : function(){
			this.addClass('open');
			return null;
		},
		hide : function(){
			this.removeClass('open');
			return null;
		}
	};

	$.fn.niftyMega = function(method){
		var chk = false;
		this.each(function(){
			if(methods[method]){
				chk = methods[method].apply($(this).find('input'),Array.prototype.slice.call(arguments, 1));
			}else if (typeof method === 'object' || !method) {
				mega($(this));
			}
		});
		return chk;
	};

	nifty.window.on('load', function() {
		megadropdown = $('.mega-dropdown');
		if(megadropdown.length) megadropdown.niftyMega();

		$('html').on('click', function (e) {
			if(megadropdown.length){
				if (!$(e.target).closest('.mega-dropdown').length) {
					megadropdown.removeClass('open');
				}
			}
		});
	});

}(jQuery);





/* ========================================================================
 * PANEL REMOVAL v1.0
 * -------------------------------------------------------------------------
 * Optional Font Icon : By Font Awesome
 * http://fortawesome.github.io/Font-Awesome/
 * ========================================================================*/
!function ($) {
	"use strict";

	nifty.window.on('load', function() {
		var closebtn = $('[data-dismiss="panel"]');

		if (closebtn.length) {
			closebtn.one('click', function(e){
				e.preventDefault();
				var el = $(this).parents('.panel');

				el.addClass('remove').on('transitionend webkitTransitionEnd oTransitionEnd MSTransitionEnd', function(e){
					if (e.originalEvent.propertyName == "opacity") {
						el.remove();
					}
				});
			});
		}
	});

}(jQuery);



/* ========================================================================
 * NIFTY OVERLAY v1.0
 * -------------------------------------------------------------------------
 * Optional Font Icon : By Font Awesome
 * http://fortawesome.github.io/Font-Awesome/
 * ========================================================================*/
!function ($) {
	"use strict";

	var defaults = {
		'displayIcon'	: true,
		// DESC	 		: Should we display the icon or not.
		// VALUE	 	: true or false
		// TYPE 	 	: Boolean


		'iconColor'		: 'text-dark',
		// DESC	 		: The color of the icon..
		// VALUE	 	: text-light || text-primary || text-info || text-success || text-warning || text-danger || text-mint || text-purple || text-pink || text-dark
		// TYPE 	 	: String

		'iconClass'		: 'fa fa-refresh fa-spin fa-2x',
		// DESC  		: Class name of the font awesome icons", Currently we use font-awesome for default value.
		// VALUE 		: (Icon Class Name)
		// TYPE			: String


		'title'			: '',
		// DESC			: Overlay title
		// TYPE			: String

		'desc'			: ''
		// DESC			: Descrition
		// TYPE			: String


	},
	uID = function() {
		return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
	},
	methods = {
		'show' : function(el){
			var target = $(el.attr('data-target')),
			ovId = 'nifty-overlay-' + uID() + uID()+"-" + uID(),
			panelOv = $('<div id="'+ ovId +'" class="panel-overlay"></div>');

			el.prop('disabled', true).data('niftyOverlay',ovId);
			target.addClass('panel-overlay-wrap');
			panelOv.appendTo(target).html(el.data('overlayTemplate'));
			return null;
		},
		'hide': function(el){
			var target = $(el.attr('data-target'));
			var boxLoad = $('#'+ el.data('niftyOverlay'));

			if (boxLoad.length) {
				el.prop('disabled', false);
				target.removeClass('panel-overlay-wrap');
				boxLoad.hide().remove();
			}
			return null;
		}
	},
	loadBox = function(el,options){
		if (el.data('overlayTemplate')) {
			return null;
		}
		var opt = $.extend({},defaults,options),
		icon = (opt.displayIcon)?'<span class="panel-overlay-icon '+opt.iconColor+'"><i class="'+opt.iconClass+'"></i></span>':'';
		el.data('overlayTemplate', '<div class="panel-overlay-content pad-all unselectable">'+icon+'<h4 class="panel-overlay-title">'+opt.title+'</h4><p>'+opt.desc+'</p></div>');
		return null;
	};

	$.fn.niftyOverlay = function(method){
		if (methods[method]){
			return methods[method](this);
		}else if (typeof method === 'object' || !method) {
			return this.each(function () {
				loadBox($(this), method);
			});
		}
		return null;
	};

}(jQuery);



/* ========================================================================
 * NIFTY NOTIFICATION v1.1
 * -------------------------------------------------------------------------
 * By ThemeOn.net
 * ========================================================================*/
!function ($) {
	"use strict";

	var pageHolder, floatContainer = {}, notyContainer, addNew = false;
	$.niftyNoty = function(options){
		var defaults = {
			type        : 'primary',
			// DESC     : Specify style for the alerts.
			// VALUE    : primary || info || success || warning || danger || mint || purple || pink ||  dark
			// TYPE     : String


			icon        : '',
			// DESC     : Icon class names
			// VALUE    : (Icon Class Name)
			// TYPE     : String


			title       : '',
			// VALUE    : (The title of the alert)
			// TYPE     : String

			message     : '',
			// VALUE    : (Message of the alert.)
			// TYPE     : String


			closeBtn    : true,
			// VALUE    : Show or hide the close button.
			// TYPE     : Boolean



			container   : 'page',
			// DESC     : This option is particularly useful in that it allows you to position the notification.
			// VALUE    : page || floating ||  "specified target name"
			// TYPE     : STRING


			floating    : {
				position    : 'top-right',
				// Floating position.
				// Currently only supports "top-right". We will make further development for the next version.


				animationIn : 'jellyIn',
				// Please use the animated class name from animate.css

				animationOut: 'fadeOut'
				// Please use the animated class name from animate.css

			},

			html        : null,
			// Insert HTML into the notification.  If false, jQuery's text method will be used to insert content into the DOM.


			focus       : true,
			//Scroll to the notification


			timer       : 0
			// DESC     : To enable the "auto close" alerts, please specify the time to show the alert before it closed.
			// VALUE    : Value is in milliseconds. (0 to disable the autoclose.)
			// TYPE     : Number

		},
		opt = $.extend({},defaults, options ), el = $('<div class="alert-wrap"></div>'),
		iconTemplate = function(){
			var icon = '';
			if (options && options.icon) {
				icon = '<div class="media-left"><span class="icon-wrap icon-wrap-xs icon-circle alert-icon"><i class="'+ opt.icon +'"></i></span></div>';
			}
			return icon;
		},
		alertTimer,
		template = function(){
			var clsBtn = opt.closeBtn ? '<button class="close" type="button"><i class="fa fa-times-circle"></i></button>' : '';
			var defTemplate = '<div class="alert alert-'+ opt.type + '" role="alert">'+ clsBtn + '<div class="media">';
			if (!opt.html) {
				return defTemplate + iconTemplate() + '<div class="media-body"><h4 class="alert-title">'+ opt.title +'</h4><p class="alert-message">'+ opt.message +'</p></div></div>';
			}
			return defTemplate + opt.html +'</div></div>';
		}(),
		closeAlert = function(e){
			if (opt.container === 'floating' && opt.floating.animationOut) {
				el.removeClass(opt.floating.animationIn).addClass(opt.floating.animationOut);
				if (!nifty.transition) {
					el.remove();
				}
			}

			el.removeClass('in').on('transitionend webkitTransitionEnd oTransitionEnd MSTransitionEnd', function(e){
				if (e.originalEvent.propertyName == "max-height") {
					el.remove();
				}
			});
			clearInterval(alertTimer);
			return null;
		},
		focusElement = function(pos){
			nifty.bodyHtml.animate({scrollTop: pos}, 300, function(){
				el.addClass('in');
			});
		},
		init = function(){
			if (opt.container === 'page') {
				if (!pageHolder) {
					pageHolder = $('<div id="page-alert"></div>');
					nifty.contentContainer.prepend(pageHolder);
				}

				notyContainer = pageHolder;
				if (opt.focus) focusElement(0);

			}else if (opt.container === 'floating') {
				if (!floatContainer[opt.floating.position]) {
					floatContainer[opt.floating.position] = $('<div id="floating-' + opt.floating.position + '" class="floating-container"></div>');
					nifty.container.append(floatContainer[opt.floating.position]);
				}

				notyContainer = floatContainer[opt.floating.position];

				if (opt.floating.animationIn) el.addClass('in animated ' + opt.floating.animationIn );
				opt.focus = false;
			}else {
				var $ct =  $(opt.container);
				var $panelct = $ct.children('.panel-alert');
				var $panelhd = $ct.children('.panel-heading');

				if (!$ct.length) {
					addNew = false;
					return false;
				}


				if(!$panelct.length){
					notyContainer = $('<div class="panel-alert"></div>');
					if($panelhd.length){
						$panelhd.after(notyContainer);
					}else{
						$ct.prepend(notyContainer)
					}
				}else{
					notyContainer = $panelct;
				}

				if (opt.focus) focusElement($ct.offset().top - 30);

			}
			addNew = true;
			return false;
		}();

		if (addNew) {
			notyContainer.append(el.html(template));
			el.find('[data-dismiss="noty"]').one('click', closeAlert);
			if(opt.closeBtn) el.find('.close').one('click', closeAlert);
			if (opt.timer > 0)alertTimer = setInterval(closeAlert, opt.timer);
			if (!opt.focus) var addIn = setInterval(function(){el.addClass('in');clearInterval(addIn);},200);
		}
	};

}(jQuery);



/* ========================================================================
 * NIFTY CHECK v1.1
 * -------------------------------------------------------------------------
 * - ThemeOn.net -
 * ========================================================================*/
!function ($) {
	"use strict";

	var allFormEl,
	formElement = function(el){
		if (el.data('nifty-check')){
			return;
		}else{
			el.data('nifty-check', true);
			if (el.text().trim().length){
				el.addClass("form-text");
			}else{
				el.removeClass("form-text");
			}
		}


		var input 	= el.find('input')[0],
		groupName 	= input.name,
		$groupInput	= function(){
			if (input.type == 'radio' && groupName) {
				return $('.form-radio').not(el).find('input').filter('input[name='+groupName+']').parent();
			}else{
				return false;
			}
		}(),
		changed = function(){
			if(input.type == 'radio' && $groupInput.length) {
				$groupInput.each(function(){
					var $gi = $(this);
					if ($gi.hasClass('active')) $gi.trigger('nifty.ch.unchecked');
					$gi.removeClass('active');
				});
			}


			if (input.checked) {
				el.addClass('active').trigger('nifty.ch.checked');
			}else{
				el.removeClass('active').trigger('nifty.ch.unchecked');
			}
		};

		if (input.checked) {
			el.addClass('active');
		}else{
			el.removeClass('active');
		}

		$(input).on('change', changed);
	},
	methods = {
		isChecked : function(){
			return this[0].checked;
		},
		toggle : function(){
			this[0].checked = !this[0].checked;
			this.trigger('change');
			return null;
		},
		toggleOn : function(){
			if(!this[0].checked){
				this[0].checked = true;
				this.trigger('change');
			}
			return null;
		},
		toggleOff : function(){
			if(this[0].checked && this[0].type == 'checkbox'){
				this[0].checked = false;
				this.trigger('change');
			}
			return null;
		}
	};

	$.fn.niftyCheck = function(method){
		var chk = false;
		this.each(function(){
			if(methods[method]){
				chk = methods[method].apply($(this).find('input'),Array.prototype.slice.call(arguments, 1));
			}else if (typeof method === 'object' || !method) {
				formElement($(this));
			}
		});
		return chk;
	};

	nifty.document.ready(function() {
		allFormEl = $('.form-checkbox, .form-radio');
		if(allFormEl.length) allFormEl.niftyCheck();
	});

	nifty.document.on('change', '.btn-file :file', function() {
		var input = $(this),
		numFiles = input.get(0).files ? input.get(0).files.length : 1,
		label = input.val().replace(/\\/g, '/').replace(/.*\//, ''),
		size = function(){
			try{
				return input[0].files[0].size;
			}catch(err){
				return 'Nan';
			}
		}(),
		fileSize = function(){
			if (size == 'Nan' ) {
				return "Unknown";
			}
			var rSize = Math.floor( Math.log(size) / Math.log(1024) );
			return ( size / Math.pow(1024, rSize) ).toFixed(2) * 1 + ' ' + ['B', 'kB', 'MB', 'GB', 'TB'][rSize];
		}();



		input.trigger('fileselect', [numFiles, label, fileSize]);
	});
}(jQuery);



/* ========================================================================
* NIFTY AFFIX v1.0
* -------------------------------------------------------------------------
* Require Bootstrap Affix
* http://getbootstrap.com/javascript/#affix
* ========================================================================*/

!function ($) {
	"use strict";

	$.fn.niftyAffix = function(method){
		return this.each(function(){
			var el = $(this), className;

			if (typeof method === 'object' || !method){
				className = method.className;
				el.data('nifty.af.class', method.className);
			}else if (method == 'update') {
				className = el.data('nifty.af.class');
			}

			if (nifty.container.hasClass(className) && !nifty.container.hasClass('navbar-fixed') ) {
				el.affix({
					offset:{
					top:$('#navbar').outerHeight()
					}
				});
			}else if (!nifty.container.hasClass(className) || nifty.container.hasClass('navbar-fixed')) {
				nifty.window.off(el.attr('id') +'.affix');
				el.removeClass('affix affix-top affix-bottom').removeData('bs.affix');
			}
		});
	};


	nifty.window.on('load', function(){
		if (nifty.mainNav.length) {
			nifty.mainNav.niftyAffix({className : 'mainnav-fixed'});
		}

		if (nifty.aside.length) {
			nifty.aside.niftyAffix({className : 'aside-fixed'});
		}
	});

}(jQuery);


