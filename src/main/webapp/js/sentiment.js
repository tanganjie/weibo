/**
 * Created by tanjie on 10/16/15.
 */
$(document).ready(function(){
    var trainData = $('#trainData');
    var testData = $('#testData');
    var loadTrain = function () {
        trainData.bootstrapTable({
            striped: true,
            ajax: function(params){
                console.log(params.data);
                setTimeout(function () {
                    var param = "";
                    if(params.data.search == undefined)
                        param += "search=&";
                    else
                        param += "search=" + params.data.search + "&";
                    if(params.data.sort == undefined)
                        param += "field=id&";
                    else
                        param += "field=" + params.data.sort + "&";
                    param += "asc=" + params.data.order + "&pn=" + params.data.offset + "&cp=" + params.data.limit;
                    $.post("rest/Sentiment/queryTrainDataInPage", param, function(data){
                        console.log(data);
                        params.success(data);
                        params.complete();
                    }, "json");

                }, 100);
            },
            search: true,
            sidePagination: 'server',
            pageList: [8, 10, 20, 50, 100, 200],
            pageSize: 8,
            pagination: true,
            columns: [{
                width: '7%',
                field: 'linenumber',
                title: '序号'
            }, {
                field: 'id',
                visible: false,
                title: '编号'
            }, {
                field: 'content',
                sortable: true,
                title: '微博内容'
            }, {
                width: '10%',
                field: 'weight',
                sortable: true,
                title: '情感极性'
            }]
        });
    }


    var loadTest = function () {
        testData.bootstrapTable({
            striped: true,
            ajax: function(params){
                console.log(params.data);
                setTimeout(function () {
                    var param = "";
                    if(params.data.search == undefined)
                        param += "search=&";
                    else
                        param += "search=" + params.data.search + "&";
                    if(params.data.sort == undefined)
                        param += "field=id&";
                    else
                        param += "field=" + params.data.sort + "&";
                    param += "asc=" + params.data.order + "&pn=" + params.data.offset + "&cp=" + params.data.limit;
                    $.post("rest/Sentiment/queryTestDataInPage", param, function(data){
                        console.log(data);
                        params.success(data);
                        params.complete();
                    }, "json");

                }, 100);
            },
            search: true,
            sidePagination: 'server',
            pageList: [8, 10, 20, 50, 100, 200],
            pageSize: 8,
            pagination: true,
            columns: [{
                width: '7%',
                field: 'linenumber',
                title: '序号'
            }, {
                field: 'id',
                visible: false,
                title: '编号'
            }, {
                field: 'content',
                sortable: true,
                title: '微博内容'
            }, {
                width: '10%',
                field: 'weight',
                sortable: true,
                title: '情感极性'
            }, {
                width: '10%',
                field: 'correct',
                sortable: true,
                title: '预测结果'
            }]
        });
    }

    loadTrain();
    loadTest();

    $("#trainDataDiv").hide();
    $("#testDataDiv").hide();
    $("#sentimentIndex").click(function () {
        $("#sentimentMain").show();
        $("#trainDataDiv").hide();
        $("#testDataDiv").hide();
    });
    $("#showTrain").click(function(){
        $("#sentimentMain").hide();
        $("#trainDataDiv").show();
        $("#testDataDiv").hide();
        trainData.bootstrapTable('refresh', {silent: true});
    });
    $("#showTest").click(function(){
        $("#sentimentMain").hide();
        $("#trainDataDiv").hide();
        $("#testDataDiv").show();
        testData.bootstrapTable('refresh', {silent: true});
    });
    $("#result").hide();
    $("#analysisBtn").click(function(){
        var param = $("#analysisForm").serialize();
        console.log(param);
        var showTxt = "<label>分析中...请稍候...</label>";
        $("#result").html(showTxt);
        $("#result").show();

        $.post("rest/Sentiment/analysis", param, function(data){
            console.log(data);
            var html = "";
            if(data.status == "error") {
                html += "<label class='resultError'>出错</label>";
            } else {
                html += "<label>分析结果：</label>"
                html += "<label>" + data.sentiment + "</label>";
            }
            $("#result").html(html);
        }, "json");
    });
});