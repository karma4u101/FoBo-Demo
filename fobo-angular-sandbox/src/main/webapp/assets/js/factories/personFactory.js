app.factory('PersonFactory',['$log',function($log) {
	
	var factory = {}; 
	var cacheData=null;
	
	factory.personsQuery = function(invalidateCache) {
		if(cacheData!=null && invalidateCache==false){
		  $log.debug('PersonFactory:factory.personsQuery: returning cached data invalidateCache='+invalidateCache); 
		  return cacheData;
		}else{
		  var promise = myRTFunctions.personsQuery(); 
		  $log.debug('PersonFactory:factory.personsQuery: fetching data from server invalidateCache='+invalidateCache); 
		  return cacheData = promise;
		}
    }
	
    return factory;
}]);