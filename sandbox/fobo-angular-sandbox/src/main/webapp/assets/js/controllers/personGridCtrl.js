
app.controller('PersonGridCtrl',['$scope','$log','$modal','PersonFactory',function($scope,$log,$modal,PersonFactory) {
	
	$scope.myData = []
	
	$scope.doPopulate = function() {			
		var promise = PersonFactory.personsQuery();
		return promise.then(function(data){
			$scope.$apply(function() {
		      $log.debug('PersonGridCtrl:doPopulate: in apply')
			  $scope.myData = data;
			})
		    return data;		    
		}); 
	};
	$scope.doPopulate();
		
	
    $scope.remove = function(entity) {    
    	$log.debug('PersonGridCtrl:remove: about to delete person: ' + entity.name);
    	var modalInstance = $scope.openDeletePersonAnswerModal(entity);
    	//result - a promise that is resolved when a modal is closed and rejected when a modal is dismissed.
    	//So if the module is closed and not rejected we eventually get a result. 
    	//In our case the controller is so simple that we do not need to 'check' the result answer
    	//as it will always be 'ok', but in case the controller will be changed in the future 
    	//we check that the answer is 'ok' anyway.
    	modalInstance.result.then(function (answer) {
        	$log.debug('answer: ' + answer);
        	if(answer=='ok'){
        		$log.debug('PersonGridCtrl:remove: Are you sure(?) modal dialog got answer='+answer+' proceding with deleting person: ' + entity.name);
                var json = angular.toJson(entity);
                var promise = PersonFactory.deletePersonCmd(json);
        	    return promise.then(function(data) {
        		      $scope.$apply(function() {
        		    	$log.debug("PersonGridCtrl:remove:deletePersonCmd in apply data.deleted="+data.deleted);  
        		        //$scope.myData = data;
        		    	if(data.deleted){
        		    	  $scope.doPopulate();	
        		    	}
        		      })
        		      return data;
        		    });         		
        	}
        }, function (reason) {
        	$log.debug('DeletePersonAnswerModal dismissed at: ' + new Date() + ' reason: '+reason+' so aborting delete of person: '+ entity.name);
        });
    }    

    $scope.openDeletePersonAnswerModal = function(entity){
    	return $modal.open({
    		     templateUrl: 'myModalTemplate.html',
    	         controller: 'PersonModalInstCtrl',
    	         size: 'sm',
    	         resolve: {
    	    	   entity: function () {
    	              return entity;
    	           }
    	         }    	    
              });    	
    }
    
    //The age is declared as a input type="number" but here we 
    //set person with empty values to enable the placeholder text
    $scope.resetPersonFields = function() {
       $scope.person = { id: 0, name: "", age: "" };        
    }
    $scope.resetPersonFields();
    
    $scope.doAdd = function() {
    	var json = angular.toJson($scope.person);
    	$log.debug('PersonGridCtrl:doAdd: person json=' + json);
    	//var promise = myRTFunctions.addPersonCmd(json);
    	var promise = PersonFactory.addPersonCmd(json);
    	$scope.resetPersonFields();
	    return promise.then(function(data) {
		      $scope.$apply(function() {
		    	$log.debug("PersonGridCtrl:doAdd in apply data.inserted="+data.inserted);   
                if(data.inserted) {
                	$log.debug("PersonGridCtrl:doAdd in apply repopulate the list"); 	
                	$scope.doPopulate();
                }
		      })
		      return data;
		    });	
    };  
    
    $scope.updatePerson = function(entity) {
    	$log.debug('PersonGridCtrl:updatePerson: about to update person ' + entity);
        var json = angular.toJson(entity);
    	//var promise = myRTFunctions.updatePersonCmd(json);
    	var promise = PersonFactory.updatePersonCmd(json);
	    return promise.then(function(data) {
		      $scope.$apply(function() {
		    	$log.debug("PersonGridCtrl:updatePerson in apply data.updated="+data.updated);  
		        if(data.updated){
                	$log.debug("PersonGridCtrl:updatePerson in apply repopulate the list"); 	
                	$scope.doPopulate();		        	
		        }
		      })
		      return data;
		    });     	
    }
    
    
    //a ng-grid callback event that can be used to handle cell edit updates
    $scope.$on('ngGridEventEndCellEdit', function(evt){
    	//$scope.done = false;
    	$log.debug("PersonGridCtrl:ngGridEventEndCellEdit: evt - about to update entity=");
        $log.debug(evt.targetScope.row.entity);  // the underlying data bound to the row
        // Detect changes and send entity to server 
	    $scope.updatePerson(evt.targetScope.row.entity)     
    });    
        
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
            width: '55'
        }]
    };     
    
}]);


