
app.controller('PersonGridCtrl',['$scope','$log',function($scope,$log) {
	
	$scope.populate = true;
	$scope.myData = []
	
	$scope.doPopulate = function() {
	  $log.debug('PersonGridCtrl:doPopulate: about to call personsQuery');	
	  var promise = myRTFunctions.personsQuery();  
	  return promise.then(function(data) {
		      $scope.$apply(function() {
		        $scope.myData = data; 
		        $log.debug('PersonGridCtrl:doPopulate: in apply data set'); 
		      })
		      return data;
		    });	 
	};
	
//	if($scope.populate){
//	  $scope.doPopulate();
//	  $scope.populate = false;
//    }   	
	$scope.doPopulate();
		
	
    $scope.remove = function(entity) {    
    	//$ModalDemoCtrl.open('sm');
    	$log.debug('PersonGridCtrl:remove: about to delete person: ' + entity.name);
    	//open a confirm dialog
    	var answer = confirm('GridCtrl:remove: Are you sure you wish to delete person: ' + entity.name )
    	if (answer){
    		$log.debug('PersonGridCtrl:remove: Are you sure(?) dialog got answer='+answer+' proceding with deleting person: ' + entity.name);
            var json = angular.toJson(entity);
        	var promise = myRTFunctions.deletePersonCmd(json);
    	    return promise.then(function(data) {
    		      $scope.$apply(function() {
    		    	$log.debug("PersonGridCtrl:remove:deletePersonCmd in $apply data.deleted="+data.deleted);  
    		        //$scope.myData = data;
    		    	if(data.deleted){
    		    	  $scope.doPopulate();	
    		    	}
    		      })
    		      return data;
    		    });     		
    	}
    	else{
    		$log.debug('Are you sure(?) dialog got answer='+answer+' aborted delete of person: ' + entity.name);
    	}
        //alert('Are you sure you wish to delete person: ' + entity.name + ' delete ' + remove  );
    	//console.log('about to delete person: ' + entity.name);
    }    
        
    
    
    $scope.doAdd = function() {
    	$log.debug('PersonGridCtrl:doAdd: about to add person ' + $scope.person);
    	var json = angular.toJson($scope.person);
    	var promise = myRTFunctions.addPersonCmd(json);
    	$scope.resetPersonFields();
	    return promise.then(function(data) {
		      $scope.$apply(function() {
		    	$log.debug("PersonGridCtrl:doAdd in $apply data.inserted="+data.inserted);   
                if(data.inserted) {
                	$log.debug("PersonGridCtrl:doAdd in $apply repopulate the list"); 	
                	$scope.doPopulate();
                }
		      })
		      return data;
		    });	
    };  
    
    
    $scope.updatePerson = function(entity) {
    	$log.debug('PersonGridCtrl:updatePerson: about to update person ' + entity);
        var json = angular.toJson(entity);
    	var promise = myRTFunctions.updatePersonCmd(json);
	    return promise.then(function(data) {
		      $scope.$apply(function() {
		    	$log.debug("PersonGridCtrl:updatePerson in $apply data="+data.updated);  
		        if(data.updated){
                	$log.debug("PersonGridCtrl:updatePerson in $apply repopulate the list"); 	
                	$scope.doPopulate();		        	
		        }
		      })
		      return data;
		    });     	
    }
    
    
    //a ng-grid callback event that can be used to handle cell edit updates
    $scope.$on('ngGridEventEndCellEdit', function(evt){
    	$scope.done = false;
    	$log.debug("PersonGridCtrl:ngGridEventEndCellEdit: evt - about to update entity=");
        $log.debug(evt.targetScope.row.entity);  // the underlying data bound to the row
        // Detect changes and send entity to server 
	    $scope.updatePerson(evt.targetScope.row.entity)     
    });    
    

    //The age is declared as a input type="number" but here we 
    //set person with empty values to enable the placeholder text
    $scope.resetPersonFields = function() {
    $scope.person = {
            id: 0,
            name: "",
            age: ""
        };        
    }
	//$scope.resetPersonFields();
    
    $scope.simpleGridOptions = { 
            data: 'myData',
            enableRowSelection: false,       
            columnDefs: [{field: 'id', visible: false},
                         {field: 'name', displayName: 'Name'}, 
                         {field:'age', displayName:'Age'}]
        };   
    
    $scope.gridOptions = { 
        data: 'myData',      
        enableRowSelection: false,
        multiSelect: false,        
        enableCellSelection: true,   
        columnDefs: [{
        	field: 'id', 
        	visible: false
        },{
        	field: 'name', 
        	displayName: 'Name', 
        	enableCellEdit: true
        },{
        	field:'age', 
        	displayName:'Age', 
        	enableCellEdit: true
        },{ 
        	field: 'remove',
            displayName: 'Delete',
            cellTemplate: '<button type="button" ng-model="row.entity.remove" ng-click="remove(row.entity)" class="close" style="float:left;margin-left:5px;color: darkred;opacity: 0.3;"><span aria-hidden="true">&times;</span><span class="sr-only">Remove</span></button>',
            width: '55px'
        }]
    };     
    
}]);
