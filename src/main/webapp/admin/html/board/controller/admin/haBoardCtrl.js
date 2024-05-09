
javaDashboard.controller('haBoardCtrl', function ($rootScope, $scope, $location, $http, $q, $filter, $uibModal, ModalUtils) {

    var translate = $filter('translate');

    $rootScope.alert = function (msg) {
        ModalUtils.alert(msg);
    };

    $http.get("dash/common/getUserDetail.do").success(function (response) {
        $scope.user = response;
        var avatarUrl = 'imgs/user-male-circle-blue-128.png';
        $scope.user.avatar = avatarUrl;
    });

    var getMenuList = function () {
        $http.get("dash/menu/list.do").success(function (response) {
            $scope.menuList = response;
        });
    };

    var getFolderList = function () {
        $http.post('dash/folder/familyTree.do', {folderIds: $scope.folderIds}).success(function (response) {
            var data = response;

            $scope.folderList = [];
            for (var i=0; i<data.length; i++){
                var folderName = data[i].name.toString();
                if(data[i].parentId == 10000) {
                    $scope.folderList.push({
                        "id": data[i].id,
                        "name": folderName == ".private" ? translate("CONFIG.DASHBOARD.MY_DASHBOARD") : folderName,
                        "isPrivate": data[i].isPrivate,
                        "hasBaord": $scope.folderIds.indexOf(data[i].id)>0 ? 1 : 0
                    });
                }
            }
        });
    };

    var getBoardList = function () {
        $http.get("dash/board/list.do").success(function (response) {
            $scope.boardList = response;

            var tmp = $scope.boardList.map(function (item) {
                return item.folderId;
            });
            // tmp.push($scope.privateFolder.id);
            $scope.folderIds = angular.toJson(tmp);

            getFolderList();
        });
    };

    $scope.$on("boardChange", function () {
        getBoardList();
    });

    $scope.isShowMenu = function (code) {
        return !_.isUndefined(_.find($scope.menuList, function (menu) {
            return menu.menuCode == code
        }));
    };

    getMenuList();
    getBoardList();

    $scope.changePwd = function () {
        $uibModal.open({
            templateUrl: 'admin/html/board/view/admin/changePwd.html',
            windowTemplateUrl: 'admin/html/board/view/util/modal/window.html',
            backdrop: false,
            size: 'sm',
            controller: function ($scope, $uibModalInstance) {
                $scope.close = function () {
                    $uibModalInstance.close();
                };
                $scope.ok = function () {
                    $http.post("dash/adminuser/changePwd.do", {
                        curPwd: $scope.curPwd,
                        newPwd: $scope.newPwd,
                        cfmPwd: $scope.cfmPwd
                    }).success(function (rcode) {
                        if (rcode.code == 0) {
                            ModalUtils.alert(translate("COMMON.SUCCESS"), "modal-success", "sm");
                            $uibModalInstance.close();
                        } else {
                            ModalUtils.alert(rcode.msg, "modal-warning", "lg");
                        }
                    });
                };
            }
        });
    }
});