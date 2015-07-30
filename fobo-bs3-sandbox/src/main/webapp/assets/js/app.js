
function copyVals() {
    console.log('here');
    $('#mytxtoutput').html($('#mytxtinput').val());
}



!function ($) {
        	    	

    	$(window).load(
	    function() {
		$('#sidebarLD > a').on(
			'click',
			function(e) {
			    e.preventDefault();
			    if (!$(this).hasClass("active")) {
				var lastActive = $(this).closest("#sidebarLD")
					.children(".active");
				lastActive.removeClass("active");
				lastActive.next('div').collapse('hide');
				$(this).addClass("active");
				$(this).next('div').collapse('show');
			    }
			});
	    });

    $(window).load(
	    function() {
		$('#sidebarFid > a').on(
			'click',
			function(e) {
			    e.preventDefault();
			    if (!$(this).hasClass("active")) {
				var lastActive = $(this).closest("#sidebarFid")
					.children(".active");
				lastActive.removeClass("active");
				lastActive.next('div').collapse('hide');
				$(this).addClass("active");
				$(this).next('div').collapse('show');
			    }
			});
	    });

    $('#mytxtinput').wysihtml5({
	toolbar : {
	    'html' : true,
	    fa : true
	},
	parserRules : {
	    classes : {
		'mytxtimg' : 1
	    },
	    tags : {
		'img' : {
		    'set_class' : 'mytxtimg'
		}
	    }
	}
    });	    	
        
  $(function(){

    var $window = $(window)
    var $body   = $(document.body)

    var navHeight = $('.navbar').outerHeight(true) - 60

    $body.scrollspy({
      target: '.bs-sidebar',
      offset: navHeight
    })

    $window.on('load', function () {
      $body.scrollspy('refresh')
    })
    
    // back to top
    setTimeout(function () {
      var $sideBar = $('.bs-sidebar')

      $sideBar.affix({
        offset: {
          top: function () {
            var offsetTop      = $sideBar.offset().top
            var sideBarMargin  = parseInt($sideBar.children(0).css('margin-top'), 10)
            var navOuterHeight = $('.bs-docs-nav').height()

            return (this.top = offsetTop - navOuterHeight - sideBarMargin)
          }
        , bottom: function () {
            return (this.bottom = $('.bs-footer').outerHeight(true))
          }
        }
      })
    }, 100)
    
    
  })
}(window.jQuery)