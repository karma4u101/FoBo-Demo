
app.controller('Grid2Ctrl',['$scope',function($scope) {
    
	$scope.myData = function() {
	  var promise = myRTFunctions.getPersonGridData();
	    return promise.then(function(data) {
		      $scope.$apply(function() {
		        $scope.myData = data;
		      })
		      return data;
		    });	  
	};
	$scope.myData();
	
    $scope.gridOptions = { 
        data: 'myData',
        showSelectionCheckbox: true,
        enableCellSelection: true,
        enableRowSelection: false,
        enableCellEdit: true,        
        columnDefs: [{field: 'id', visible: false},
                     {field: 'name', displayName: 'Name', enableCellEdit: true}, 
                     {field:'age', displayName:'Age', enableCellEdit: true}]
    };
    
    //The age is declared as a input type="number" but here we 
    //set person with empty values to enable the placeholder text
    var resetPersonFields = function() {
    $scope.person = {
            id: 0,
            name: "",
            age: "" 
        };        
    }
    resetPersonFields();  
    $scope.doAdd = function() {
    	var json = angular.toJson($scope.person);
    	var promise = myRTFunctions.addPersonGridData(json);
    	resetPersonFields();
	    return promise.then(function(data) {
		      $scope.$apply(function() {
		        $scope.myData = data;
		      })
		      return data;
		    });	      	
    };  
    
    //a ng-grid callback event that can be used to handle cell edit updates
    $scope.$on('ngGridEventEndCellEdit', function(evt){
    	console.log("about to update entity=")
        console.log(evt.targetScope.row.entity);  // the underlying data bound to the row
        // Detect changes and send entity to server 
        var json = angular.toJson(evt.targetScope.row.entity);
    	var promise = myRTFunctions.updatePersonGridData(json);
    	//resetPersonFields();
	    return promise.then(function(data) {
		      $scope.$apply(function() {
		        $scope.myData = data;
		      })
		      return data;
		    });          
    });    
    
}]);