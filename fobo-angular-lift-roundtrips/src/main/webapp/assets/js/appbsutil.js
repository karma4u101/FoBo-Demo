!function ($) {

  $(function(){

    var $window = $(window)
    var $body   = $(document.body)

    //var navHeight = $('.navbar').outerHeight(true) - 60

    $body.scrollspy({
      target: '.bs-sidebar'//,
      //offset: navHeight
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
            var offsetTop      = $sideBar.offset().top ;
            var sideBarMargin  = parseInt($sideBar.children(0).css('margin-top'), 10)
            var navOuterHeight = $('.rt-nav').height() 
            //var sum = offsetTop - navOuterHeight - sideBarMargin;
            return (this.top = offsetTop - navOuterHeight - sideBarMargin)
          }
        , bottom: function () {
            return (this.bottom = $('.bs-footer').outerHeight(true))
          }
        }
      })
    }, 100)    

    //Add some animation to the scrollspy function -- just remove this function if it causes problems.
    $(".bs-sidebar ul li a[href^='#']").on('click', function(e) {

 	   // prevent default anchor click behavior
 	   e.preventDefault();

 	   // store hash
 	   var hash = this.hash;

 	   // animate
 	   $('html, body').animate({
 	       scrollTop: $(this.hash).offset().top
 	     }, 500, function(){

 	       // when done, add hash to url
 	       // (default click behaviour)
 	       window.location.hash = hash;
 	    
 	     });
 	});      
    
    
  })
}(window.jQuery)

