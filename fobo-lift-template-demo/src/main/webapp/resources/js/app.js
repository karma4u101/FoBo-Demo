!function(d,s,id){
    var js,fjs=d.getElementsByTagName(s)[0];
    if(!d.getElementById(id)){
	js=d.createElement(s);
	js.id=id;
	js.src="/resources/js/twitter-widgets.js";
//	js.src="//platform.twitter.com/widgets.js";
	fjs.parentNode.insertBefore(js,fjs);
}}(document,"script","twitter-wjs");

!function ($) {
    
  $(function(){
  
      var $window = $(window)
      var $body   = $(document.body)
      
      $window.on('load', function () {
	      prettyPrint();
	    })      
      
  })
  
  var _gaq = _gaq || [];
  _gaq.push(['_setAccount','UA-28767506-1']);
  _gaq.push(['_setDomainName', 'media4u101.se']);
  _gaq.push(['_trackPageview']);

  $(function() {
    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
  })
  
  $(function() {
      var po = document.createElement('script'); po.type = 'text/javascript'; po.async = true;
      po.src = 'https://apis.google.com/js/plusone.js';
      var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(po, s);
   })
  
}(window.jQuery)
  