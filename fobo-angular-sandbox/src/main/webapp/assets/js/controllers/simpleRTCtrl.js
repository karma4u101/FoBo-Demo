
/*Fetches a JString from the server*/
app.controller('SimpleRTCtrl',['$scope','$log', function($scope,$log){	
	$scope.lrtResModel="Click and I will do a server roundtrip";
	
	$scope.doSimpleLiftRT = function() {
		$log.debug('SimpleRTCtrl:doSimpleLiftRT: about to call doSimpleRT');	
	    var promise = myRTFunctions.doSimpleRT(); // call to lift function
	    return promise.then(function(data) {
	      $scope.$apply(function() {
	    	$log.debug('SimpleRTCtrl:doSimpleLiftRT: in apply');  
	        $scope.lrtResModel = data;
	      })
	      return data;
	    });			
	};
	$scope.doResetRT = function() {
		return $scope.lrtResModel="Click again and I will do another roundtrip";
	};
	
}]);




