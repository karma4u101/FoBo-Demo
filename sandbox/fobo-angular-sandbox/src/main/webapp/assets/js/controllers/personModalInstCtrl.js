app.controller('PersonModalInstCtrl',['$scope', '$modalInstance','$log', 'entity', function ($scope,$modalInstance,$log,entity) {

	$scope.entity = entity;
	$log.debug('$scope.entity: ', $scope.entity);
	
    $scope.ok = function () {
        $modalInstance.close('ok');
    };
    
    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };

}]);