var app = angular.module("UserManagement", []);

app.controller("UserController", function ($scope, $http) {

    $scope.users = [];
    $scope.userForm = {
        id: null,
        surname: "",
        name: ""
    };

    _refreshUserData();

    $scope.submitUser = function () {

        var method = "";
        var url = "";

        if ($scope.userForm.id == null) {
            method = "POST";
            url = 'users';
        } else {
            method = "PUT";
            url = 'users';
        }

        $http({
            method: method,
            url: url,
            data: angular.toJson($scope.userForm),
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(_success, _error);
    };

    $scope.deleteUser = function (user) {
        $http({
            method: 'DELETE',
            url: 'users/' + user.id
        }).then(_success, _error);
    };

    $scope.editUser = function (user) {
        $scope.userForm.id = user.id;
        $scope.userForm.surname = user.surname;
        $scope.userForm.name = user.name;
    };

    function _refreshUserData() {
        $http({
            method: 'GET',
            url: 'users'
        }).then(
            function (res) { // success
                $scope.users = res.data;
            },
            function (res) { // error
                console.log("Error: " + res.status + " : " + res.data);
            }
        );
    }

    function _success(res) {
        _refreshUserData();
        _clearFormData();
    }

    function _error(res) {
        var data = res.data;
        var status = res.status;
        alert("Error: " + status + ":" + data);
    }

    function _clearFormData() {
        $scope.userForm.id = null;
        $scope.userForm.surname = "";
        $scope.userForm.name = ""
    };
});

function init_modal() {
    angular.element('.modal').modal();
};