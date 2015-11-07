var app = angular.module('FoBoDemo', [ 'ngMaterial' ]);

app.config(function($mdThemingProvider) {
    $mdThemingProvider.theme('default')
      .primaryPalette('light-blue',{
	      'default': '500', // 
	      'hue-1': '100', // 
	      'hue-2': '900', // for example used in toolbar to make it a little bit liftich 
	      'hue-3': 'A700' // 
	    })
      .accentPalette('orange');
  
    $mdThemingProvider.theme('error-toast').primaryPalette('red').accentPalette('orange');
    $mdThemingProvider.theme('warning-toast').primaryPalette('orange').accentPalette('deep-orange');
    $mdThemingProvider.theme('success-toast').primaryPalette('green');
  });

app.controller('AppCtrl', function ($scope, $timeout, $mdSidenav, $mdUtil, $log) {
    $scope.toggleLeft = buildToggler('left');
    $scope.toggleRight = buildToggler('right');
    /**
     * Build handler to open/close a SideNav; when animation finishes
     * report completion in console
     */
    function buildToggler(navID) {
      var debounceFn =  $mdUtil.debounce(function(){
            $mdSidenav(navID)
              .toggle()
              .then(function () {
                $log.debug("toggle " + navID + " is done");
              });
          },200);
      return debounceFn;
    }
  });

app.controller('LeftCtrl', function ($scope, $timeout, $mdSidenav, $log) {
    $scope.close = function () {
      $mdSidenav('left').close()
        .then(function () {
          $log.debug("close LEFT is done");
        });
    };  
    
  });

app.controller('RightCtrl', function ($scope, $timeout, $mdSidenav, $log) {
    $scope.close = function () {
      $mdSidenav('right').close()
        .then(function () {
          $log.debug("close RIGHT is done");
        });
    };
        
  });

//$mdToast.simple().content('Epostadressen finns inte registrerad').position('bottom right').theme('bottom right').hideDelay(3000);;});
app.controller('LiftMsgToastCtrl', function($scope, $mdToast, $animate, $log){
    $scope.showSimpleToast = function(msg,position,theme) {
	$mdToast.show(
		$mdToast.simple().content(msg).position(position).theme(theme).hideDelay(6000)	
	);
    }
});
//app.controller('LiftMsgToastCtrl', function($scope, $mdToast, $animate, $log){
//    $scope.showSimpleToast = function() {
//	$log.debug("in LiftMsgToasCtrl::showSimpleToast");
//	$mdToast.show({
//	      controller: 'ToastCtrl',
//	      templateUrl: '',
//	      hideDelay: 3000,
//	      position: 'bottom right'
//	    });
//    };
//    $scope.closeToast = function() {
//	      $mdToast.hide();
//	    };
//    $log.debug("in LiftMsgToasCtrl about to show toast");	    
//    $scope.showSimpleToast();	  
//});
//app.controller('ToastCtrl', function($scope, $mdToast) {
//    $scope.closeToast = function() {
//      $mdToast.hide();
//    };
//  });