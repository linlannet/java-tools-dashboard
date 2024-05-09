
javaDashboard.controller('jobCtrl', function ($scope, $rootScope, $http, dataService, $uibModal, ModalUtils, $filter, $interval) {
    var translate = $filter('translate');

    $scope.jobTypes = [{name: 'Send Mail', type: 'mail'}];

    $scope.interval = $interval(function () {
        $http.get("dash/operationjob/list.do").success(function (response) {
            _.each($scope.jobList, function (e) {
                var j = _.find(response, function (r) {
                    return e.id == r.id;
                });
                e.jobStatus = j.jobStatus;
                e.execLog = j.execLog;
            });
        });
    }, 5000);

    $rootScope.$on('$stateChangeSuccess', function (event, toState, toParams, fromState) {
            if (fromState.controller == 'jobCtrl') {
                $interval.cancel($scope.interval);
            }
        }
    );

    $scope.loadJobList = function () {
        $http.get("dash/operationjob/list.do").success(function (response) {
            $scope.jobList = response;
        });
    };
    $scope.loadJobList();

    $scope.getName = function (type) {
        return _.find($scope.jobTypes, function (e) {
            return e.type == type;
        }).name;
    };

    $scope.getTime = function (t) {
        if (t) {
            return moment(Number(t)).format("YYYY-MM-DD HH:mm:ss");
        } else {
            return "N/A"
        }
    };

    $scope.getDateRange = function (d) {
        if (d) {
            return moment(d.startDate).format("YYYY-MM-DD") + ' ~ ' + moment(d.endDate).format("YYYY-MM-DD");
        } else {
            return "N/A"
        }
    };

    $scope.getStatus = function (t) {
        if (t != null) {
            switch (t) {
                case 0:
                    return "CONFIG.JOB.FAIL";
                case 1:
                    return "CONFIG.JOB.FINISH";
                case 2:
                    return "CONFIG.JOB.PROCESSING";
            }
        } else {
            return "N/A"
        }
    };

    $scope.showLog = function (job) {
        ModalUtils.alert(job.execLog ? job.execLog : "N/A", null, "lg");
    };

    $scope.runJob = function (job) {
        $http.post("dash/operationjob/execute.do", {id: job.id}).success(function (rcode) {
            if (rcode.code == 0) {
                job.jobStatus = 2;
                // $scope.loadJobList();
            } else {
                $scope.alerts = [{msg: rcode.msg, type: 'danger'}];
            }
        });
    };

    $scope.editJob = function (job) {
        $uibModal.open({
            templateUrl: 'admin/html/board/view/config/modal/job/edit.html',
            windowTemplateUrl: 'admin/html/board/view/util/modal/window.html',
            backdrop: false,
            size: 'lg',
            scope: $scope,
            controller: function ($scope, $uibModalInstance) {
                $scope.cronConfig = {
                    quartz: true,
                    allowMultiple: true,
                    options: {
                        allowYear: false
                    }
                };
                $scope.dateRangeCfg = {
                    locale: {
                        format: "YYYY-MM-DD"
                    }
                };
                if (job) {
                    $scope.job = angular.copy(job);
                    $scope.job.daterange.startDate = moment($scope.job.daterange.startDate);
                    $scope.job.daterange.endDate = moment($scope.job.daterange.endDate);
                } else {
                    $scope.job = {daterange: {startDate: null, endDate: null}, jobType: 'mail'};

                }
                $scope.close = function () {
                    $uibModalInstance.close();
                };
                $scope.ok = function () {
                    if (job) {
                        $http.post("dash/operationjob/update.do", {json: angular.toJson($scope.job)}).success(function (rcode) {
                            if (rcode.code == 0) {
                                ModalUtils.alert(translate("COMMON.SUCCESS"), "modal-success", "sm");
                                $scope.$parent.loadJobList();
                                $uibModalInstance.close();
                            } else {
                                ModalUtils.alert(rcode.msg, "modal-warning", "lg");
                            }
                        });
                    } else {
                        $http.post("dash/operationjob/save.do", {json: angular.toJson($scope.job)}).success(function (rcode) {
                            if (rcode.code == 0) {
                                ModalUtils.alert(translate("COMMON.SUCCESS"), "modal-success", "sm");
                                $scope.$parent.loadJobList();
                                $uibModalInstance.close();
                            } else {
                                //$scope.alerts = [{msg: rcode.msg, type: 'danger'}];
                            }
                        });
                    }
                };
                $scope.config = function () {
                    $uibModal.open({
                        templateUrl: 'admin/html/board/view/config/modal/job/' + $scope.job.jobType + '.html',
                        windowTemplateUrl: 'admin/html/board/view/util/modal/window.html',
                        backdrop: false,
                        size: 'lg',
                        scope: $scope,
                        controller: $scope.job.jobType + 'JobCtrl'
                    });
                }
            }
        });
    };

    $scope.deleteJob = function (job) {
        ModalUtils.confirm(translate("COMMON.CONFIRM_DELETE"), "modal-info", "lg", function () {
            $http.post("dash/operationjob/delete.do", {id: job.id}).success(function (rcode) {
                if (rcode.code == 0) {
                    $scope.loadJobList();
                } else {
                    ModalUtils.alert(rcode.msg, "modal-warning", "lg");
                }
            });
        });
    };
});