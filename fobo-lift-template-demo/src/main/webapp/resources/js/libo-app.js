!function ($) {
	 $(function(){
		 
   var $window = $(window)		 
    // side bar
    $('.bs-docs-sidenav').affix({
      offset: {
        top: function () { return $window.width() <= 980 ? 680 : 600 }
      , bottom: 300
      }
    })
    
  })
}(window.jQuery)  