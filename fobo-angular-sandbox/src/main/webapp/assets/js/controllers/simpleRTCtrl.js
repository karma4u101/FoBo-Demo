
/*Fetches a JString from the server*/
app.controller('SimpleRTCtrl',['$scope', function($scope){	
	$scope.lrtResModel="Click and I will do a server roundtrip";
	
	$scope.doSimpleLiftRT = function() {
	    var promise = myRTFunctions.doSimpleRT(); // call to lift function
	    return promise.then(function(data) {
	      $scope.$apply(function() {
	        $scope.lrtResModel = data;
	      })
	      return data;
	    });			
	};
	$scope.doResetRT = function() {
		return $scope.lrtResModel="Click again and I will do another roundtrip";
	};
	
}]);




