//http://stackoverflow.com/questions/15647981/angularjs-and-ng-grid-auto-save-data-to-the-server-after-a-cell-was-changed/15832417#15832417
//ng-change=\"updateEntity(row.entity)\"
/* 
 * ngGrid example
 * Fetches a JArray [{name: "Moroni", age: 50},{name: "Tiancum", age: 43}, ....] from the server
 * and sets some grid options.
 * 
*/
app.controller('GridCtrl',['$scope',function($scope) {
    
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
    
}]);