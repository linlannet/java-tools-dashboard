
javaDashboard.controller('boardViewCtrl', function ($timeout, $rootScope, $scope, $state, $stateParams, $http, ModalUtils, chartService, $interval, $uibModal, dataService) {

    $scope.loading = true;
    $scope.paramInit = 0;
    $scope.relations = JSON.stringify([]);
    $http.get("dash/dataset/list.do").success(function (response) {
        $scope.datasetList = response;
        $scope.realtimeDataset = {};
        $scope.datasetMeta = {};
        $scope.intervals = [];
        $scope.datasetFilters = {};
        $scope.widgetFilters = {};
        $scope.relationFilters = {};
        $scope.load(false);
    });

    $scope.timelineColor = ['bg-light-blue', 'bg-red', 'bg-aqua', 'bg-green', 'bg-yellow', 'bg-gray', 'bg-navy', 'bg-teal', 'bg-purple', 'bg-orange', 'bg-maroon', 'bg-black'];

    var groupTimeline = function () {
        $scope.timeline = [];
        var group = undefined;
        _.each($scope.board.layout.rows, function (row, idx) {
            if (idx == 0) {
                $scope.timelineFilter = row;
                return;
            }
            row.show = false;
            if (row.node == 'parent') {
                if (group) {
                    $scope.timeline.push(group);
                }
                group = [];
                row.show = true;
            }
            group.push(row);
        });
        $scope.timeline.push(group);
    };

    $scope.openCloseParentNode = function (group) {
        var find = _.find(group, function (row) {
            return row.node != 'parent' && row.show;
        });
        if (find) {
            _.each(group, function (row) {
                if (row.node != 'parent') {
                    row.show = false;
                    _.each(row.widgets, function (widget) {
                        widget.show = false;
                    });
                }
            });
        } else {
            _.each(group, function (row) {
                if (row.node != 'parent') {
                    row.show = true;
                    _.each(row.widgets, function (widget) {
                        widget.show = true;
                    });
                }
            });
        }
    };

    $scope.openCloseNode = function (row) {
        if (row.show) {
            row.show = false;
            _.each(row.widgets, function (widget) {
                widget.show = false;
            });
        } else {
            row.show = true;
            _.each(row.widgets, function (widget) {
                widget.show = true;
            });
        }
    };

    $http.post("dash/menu/isConfig.do", {type: 'widget'}).success(function (response) {
        $scope.widgetCfg = response;
    });

    var buildRender = function (widget, reload) {
        //console.log(widget);
        // 将 .data 改成 content 主要是未发现有 data 这个key
        var widgetConfig = injectFilter(widget.widget).content;
        //console.log(widgetConfig);
        widget.render = function (container, optionFilter, scope) {
            // 百度地图特殊处理
            var charType = injectFilter(widget.widget).content.config.chart_type;
            if(charType == 'chinaMapBmap'){
                chartService.renderChart(container, widgetConfig, {
                    optionFilter: optionFilter,
                    scope: scope,
                    reload: reload
                }).then(
                    function() {widget.loading = false;}
                );
            } else {
                chartService.renderChart(container, widgetConfig, {
                    optionFilter: optionFilter,
                    scope: scope,
                    reload: reload,
                    relations: widget.relations
                }).then(function (d) {
                    widget.realTimeTicket = d;
                    widget.loading = false;
                });
            }
            widget.realTimeOption = {optionFilter: optionFilter, scope: scope};
        };
        widget.modalRender = function (container, optionFilter, scope) {
            widget.modalLoading = true;
            widget.modalRealTimeTicket = chartService.renderChart(container, widgetConfig, {
                optionFilter: optionFilter,
                scope: scope
            }).then(function () {
                widget.modalLoading = false;
            });
            widget.modalRealTimeOption = {optionFilter: optionFilter, scope: scope};
        };
    };

    $rootScope.$on('$stateChangeSuccess', function (event, toState, toParams, fromState) {
            if (fromState.controller == 'boardViewCtrl') {
                _.each($scope.intervals, function (i) {
                    $interval.cancel(i);
                })
            }
        }
    );

    $scope.export = function () {
        if ($scope.exportStatus) {
            return;
        }
        $scope.exportStatus = true;
        $http({
            url: "dash/board/export.do?id=" + $stateParams.id,
            method: "POST",
            headers: {
                'Content-type': 'application/json'
            },
            responseType: 'arraybuffer'
        }).success(function (data) {
            var blob = new Blob([data], {type: "application/vnd.ms-excel"});
            var objectUrl = URL.createObjectURL(blob);
            var aForExcel = $("<a><span class='forExcel'>下载excel</span></a>").attr("href", objectUrl);
            aForExcel.attr("download", $scope.board.name);
            $("body").append(aForExcel);
            $(".forExcel").click();
            aForExcel.remove();
            $scope.exportStatus = false;
        }).error(function (data, status, headers, config, statusText) {
            $scope.exportStatus = false;
            ModalUtils.alert("Export error, please ask admin to check server side log.", "modal-warning", "lg");
        });
    };

    var refreshParam = function () {
        _.each($scope.board.layout.rows, function (row) {
            _.each(row.params, function (param) {
                if (param.refresh) {
                    param.refresh();
                }
            });
        });
        paramToFilter();
    };

    var initDsReloadStatus = function(reload) {
        var dsReloadStatus = new Map();
        _.each($scope.board.layout.rows, function(row) {
            _.each(row.widgets, function (widget) {
                var dataSetId = widget.widget.content.datasetId;
                if (dataSetId != undefined) {
                    dsReloadStatus.set(dataSetId, reload);
                }
            });
        });
        return dsReloadStatus;
    };

    var loadWidget = function (reload) {
        paramToFilter();
        var dsReloadStatus = initDsReloadStatus(reload);
        _.each($scope.board.layout.rows, function (row) {
            _.each(row.widgets, function (widget) {
                if (!_.isUndefined(widget.hasRole) && !widget.hasRole) {
                    return;
                }
                var datasetId = widget.widget.content.datasetId;
                var needReload = reload;
                // avoid repeat load offline dataset data
                if (datasetId != undefined && reload) {
                    var needReload = dsReloadStatus.get(datasetId) ? true : false;
                    dsReloadStatus.set(datasetId, false);
                }
                buildRender(widget, needReload);
                widget.loading = true;
                if ($scope.board.layout.type == 'timeline') {
                    if (row.show) {
                        widget.show = true;
                    }
                } else {
                    widget.show = true;
                }
                //real time load task
                var w = widget.widget.content;
                var ds = _.find($scope.datasetList, function (e) {
                    return e.id == w.datasetId;
                });
                if (ds && ds.content.interval && ds.content.interval > 0) {
                    if (!$scope.intervalGroup[w.datasetId] && !widget.sourceId) {
                        $scope.intervalGroup[w.datasetId] = [];
                        $scope.intervals.push($interval(function () {
                            refreshParam();
                            _.each($scope.intervalGroup[w.datasetId], function (e) {
                                e();
                            });
                        }, ds.content.interval * 1000));
                    }
                    $scope.intervalGroup[w.datasetId].push(function () {
                        try {
                            if (widget.show) {
                                chartService.realTimeRender(widget.realTimeTicket, injectFilter(widget.widget).data);
                                if (widget.modalRealTimeTicket) {
                                    chartService.realTimeRender(widget.modalRealTimeTicket, injectFilter(widget.widget).data, widget.modalRealTimeOption.optionFilter, null);
                                }
                            }
                        } catch (e) {
                            console.error(e);
                        }
                    });
                }
            });
        });
    };

    var paramInitListener;
    $scope.load = function (reload) {
        $scope.paramInit = 0;
        $scope.loading = true;
        $("#relations").val(JSON.stringify([]));
        _.each($scope.intervals, function (e) {
            $interval.cancel(e);
        });
        $scope.intervals = [];

        if ($scope.board) {
            _.each($scope.board.layout.rows, function (row) {
                _.each(row.widgets, function (widget) {
                    widget.show = false;
                });
            });
        }
        $http.get("dash/board/info.do?id=" + $stateParams.id).success(function (response) {
            $scope.intervalGroup = {};
            $scope.loading = false;
            $scope.board = response;
            _.each($scope.board.layout.rows, function (row) {
                _.each(row.params, function (param) {
                    if (!param.paramType) {
                        param.paramType = 'selector';
                    }
                });
            });
            if (paramInitListener) {
                paramInitListener(reload);
            }
            _.each($scope.board.layout.rows, function (row) {
                _.each(row.params, function (param) {
                    $scope.paramInit++;
                });
            });
            if ($scope.board.layout.type == 'timeline') {
                groupTimeline();
            }
            if ($scope.paramInit == 0) {
                loadWidget(reload);
            }
            paramInitListener = $scope.$on('paramInitFinish', function (e, d) {
                $scope.paramInit--;
                if ($scope.paramInit == 0) {
                    loadWidget(reload)
                }
            });
        });
    };

    var injectFilter = function (widget) {
        // debugger;
        var boardFilters = [];
        if(!_.isUndefined($scope.widgetFilters[widget.id])){
            _.each($scope.widgetFilters[widget.id], function(e){
                boardFilters.push(e);
            });
        }
        if(!_.isUndefined($scope.datasetFilters[widget.content.datasetId])){
            _.each($scope.datasetFilters[widget.content.datasetId], function(e){
                boardFilters.push(e);
            });
        }
        if(!_.isUndefined($scope.relationFilters[widget.id])){
            _.each($scope.relationFilters[widget.id], function(e){
                boardFilters.push(e);
            });
        }
        widget.content.config.boardFilters = boardFilters;
        return widget;
    };

    var paramToFilter = function () {
        $scope.widgetFilters = [];
        $scope.datasetFilters = [];
        $scope.relationFilters = [];

        //将点击的参数赋值到看板上的参数中
        //"{"targetId":3,"params":[{"targetField":"logo","value":"iphone"},{"targetField":"logo1","value":"上海市"}]}" targetField==param.name
        if(location.href.split("?")[1]) {
            var urlParam = JSON.parse(decodeURI(location.href.split("?")[1]));
            _.each($scope.board.layout.rows, function (row) {
                _.each(row.params, function (param) {
                    var p = _.find(urlParam.params, function (e) {
                        return e.targetField == param.name;
                    });
                    if(p){
                        param.values.push(p.value);
                    }
                });
            });
            location.href = location.href.split("?")[0];
        }

        _.each($scope.board.layout.rows, function (row) {
            _.each(row.params, function (param) {
                if (param.values.length <= 0) {
                    return;
                }
                _.each(param.col, function (col) {
                    var p = {
                        col: col.column,
                        type: param.type,
                        values: param.values
                    };
                    if (_.isUndefined(col.datasetId)) {
                        if (!$scope.widgetFilters[col.widgetId]) {
                            $scope.widgetFilters[col.widgetId] = [];
                        }
                        $scope.widgetFilters[col.widgetId].push(p);
                    } else {
                        if (!$scope.datasetFilters[col.datasetId]) {
                            $scope.datasetFilters[col.datasetId] = [];
                        }
                        $scope.datasetFilters[col.datasetId].push(p);
                    }
                });
            });
        });
        updateParamTitle();
        //将点击的参数赋值到relationFilters中
        if(_.isUndefined($("#relations").val())){
            return;
        }
        var relations = JSON.parse($("#relations").val());
        for(var i=0;i<relations.length;i++){
            if(relations[i].targetId && relations[i].params && relations[i].params.length>0){
                for(var j=0;j<relations[i].params.length;j++) {
                    var p = {
                        col: relations[i].params[j].targetField,
                        type: "=",
                        values: [relations[i].params[j].value]
                    };
                    if (!$scope.relationFilters[relations[i].targetId]) {
                        $scope.relationFilters[relations[i].targetId] = [];
                    }
                    $scope.relationFilters[relations[i].targetId].push(p); //relation.targetId == widgetId
                }
            }
        }
    };

    $scope.applyParamFilter = function () {
        paramToFilter();
        _.each($scope.board.layout.rows, function (row) {
            _.each(row.widgets, function (w) {
                try {
                    chartService.realTimeRender(w.realTimeTicket, injectFilter(w.widget).data, null, null, w, true);
                } catch (e) {
                    console.error(e);
                }
            });
        });
    };

    $scope.paramToString = function (row) {
        return _.filter(_.map(row.params, function (e) {
            return e.title;
        }), function (e) {
            return e && e.length > 0;
        }).join('; ');
    };

    $scope.modalChart = function (widget) {
        $uibModal.open({
            templateUrl: 'admin/html/board/view/util/modal/chart.html',
            windowTemplateUrl: 'admin/html/board/view/util/modal/window.html',
            windowClass: 'modal-fit',
            backdrop: false,
            controller: function ($scope, $uibModalInstance, chartService) {
                $scope.widget = widget;
                $scope.close = function () {
                    $uibModalInstance.close();
                    delete widget.modalRealTimeTicket;
                    delete widget.modalRealTimeOption;
                };
                $scope.render1 = function () {
                    widget.modalRender($('#modal_chart'), function (option) {
                        option.toolbox = {
                            feature: {
                                //saveAsImage: {},
                                dataView: {
                                    show: true,
                                    readOnly: true
                                },
                                magicType: {
                                    type: ['line', 'bar', 'stack', 'tiled']
                                },
                                dataZoom: {
                                    show: true
                                },
                                restore: {
                                    show: true
                                }
                            }
                        };
                    }, null);
                };
            }
        });
    };

    $scope.modalTable = function (widget) {
        $uibModal.open({
            templateUrl: 'admin/html/board/view/util/modal/chart.html',
            windowTemplateUrl: 'admin/html/board/view/util/modal/window.html',
            windowClass: 'modal-fit',
            backdrop: false,
            controller: function ($scope, $uibModalInstance, chartService) {
                $scope.widget = widget;
                $scope.close = function () {
                    $uibModalInstance.close();
                };
                $scope.render1 = function () {
                    widget.modalRender($('#modal_chart'), null, null);
                };
            }
        });
    };

    $scope.config = function (widget) {
        $state.go('config.widget', {id: widget.widget.id});
    };

    $scope.reload = function (widget) {
        paramToFilter();
        widget.widget.data = injectFilter(widget.widget).data;
        widget.show = false;
        widget.showDiv = true;
        widget.render = function (content, optionFilter, scope) {
            //百度地图特殊处理
            var charType = widget.widget.content.config.chart_type;
            var widgetConfig = widget.widget.data;
            if(charType == 'chinaMapBmap'){
                chartService.renderChart(content, widgetConfig, {
                    optionFilter: optionFilter,
                    scope: scope,
                    reload: true
                });
                widget.loading = false;
            } else {
                chartService.renderChart(content, widgetConfig, {
                    optionFilter: optionFilter,
                    scope: scope,
                    reload: true,
                    relations: widget.relations
                }).then(function (d) {
                    widget.realTimeTicket = d;
                    widget.loading = false;
                });
            }
            widget.realTimeOption = {optionFilter: optionFilter, scope: scope};
        };
        $timeout(function () {
            widget.loading = true;
            widget.show = true;
        });
    };

    $http.get("dash/board/getParam.do?boardId=" + $stateParams.id).success(function (response) {
        if (response) {
            $scope.boardParams = JSON.parse(response.config);
        } else {
            $scope.boardParams = [];
        }
    });

    $scope.newBoardParam = function (name) {
        if (name == '') {
            return;
        }
        var params = {};
        _.each($scope.board.layout.rows, function (row) {
            _.each(row.params, function (param) {
                if ('slider' != param.paramType) {
                    params[param.name] = {type: param.type, values: param.values};
                }
            });
        });
        $scope.boardParams.unshift({name: name, params: params});
        $http.post("dash/board/saveParam.do", {
            boardId: $stateParams.id,
            content: angular.toJson($scope.boardParams)
        }).success(function (response) {
        });
    };

    $scope.editBoard = function() {
        $state.go('config.board', {boardId: $stateParams.id});
    };

    $scope.deleteBoardParam = function (index) {
        $scope.boardParams.splice(index,1);
        $http.post("dash/board/saveParam.do", {
            boardId: $stateParams.id,
            config: angular.toJson($scope.boardParams)
        }).success(function (response) {
        });
    };

    $scope.applyBoardParam = function (param) {
        for (var name in param) {
            _.each($scope.board.layout.rows, function (row) {
                _.each(row.params, function (p) {
                    if (p.name == name) {
                        p.type = param[name].type;
                        p.values = param[name].values;
                    }
                });
            });
        }
        $scope.applyParamFilter();
    };

    var updateParamTitle = function () {
        _.each($scope.board.layout.rows, function (row) {
            _.each(row.params, function (param) {
                if ('slider' == param.paramType) {
                    return;
                }
                var paramObj;
                switch (param.type) {
                    case '=':
                    case '≠':
                        paramObj = param.name + ' ' + param.type + ' (' + param.values + ')';
                        break;
                    case '>':
                    case '<':
                    case '≥':
                    case '≤':
                        paramObj = param.name + ' ' + param.type + ' ' + param.values;
                        break;
                    case '(a,b]':
                    case '[a,b)':
                    case '(a,b)':
                    case '[a,b]':
                        var leftBrackets = param.type.split('a')[0];
                        var rightBrackets = param.type.split('b')[1];
                        paramObj = param.name + ' between ' + leftBrackets + param.values[0] + ',' + param.values[1] + rightBrackets;
                        break;
                }
                param.title = param.values.length > 0 ? paramObj : undefined;
            });
        });
    }


});