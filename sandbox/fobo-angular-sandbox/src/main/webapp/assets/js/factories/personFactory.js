app.factory('PersonFactory',['$log',function($log) {
	
	var factory = {}; 
	var cacheData=null;
	var invalidateCashe=false;
	
	factory.personsQuery = function() {
		if(cacheData!=null && isCashInvalidated()==false){
		  $log.debug('PersonFactory:factory.personsQuery: returning cached data isCashInvalidated()='+isCashInvalidated()); 
		  return cacheData;
		}else{
		  var promise = myRTFunctions.personsQuery(); 
		  $log.debug('PersonFactory:factory.personsQuery: fetching data from server isCashInvalidated()='+isCashInvalidated()); 		  
		  setCasheInvalidated(false);
		  return cacheData = promise;
		}
    }
	
	factory.deletePersonCmd = function(json){
		var promise = myRTFunctions.deletePersonCmd(json);
		setCasheInvalidated(true);
		return promise;
	}
	
	factory.addPersonCmd = function(json){
		var promise = myRTFunctions.addPersonCmd(json);
		setCasheInvalidated(true);
		return promise;
	}
	
	factory.updatePersonCmd = function(json){
		var promise = myRTFunctions.updatePersonCmd(json);
		setCasheInvalidated(true);
		return promise;
	}	
	
	setCasheInvalidated = function(inv){
		//$log.debug('PersonFactory:invalidateCashe: about to invalidate cache'); 
		invalidateCashe=inv;
	}
	
	isCashInvalidated = function(){
		//$log.debug('PersonFactory:isCasheInvalidated: returns '+invalidateCashe); 
		return invalidateCashe;
	}	
	
    return factory;
}]);