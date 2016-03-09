/**
 * Created by tanjie on 12/22/15.
 */
$(document).ready(function () {
    var aQuery = window.location.href.split("?");
    if(aQuery.length!=2){
        window.location.href = "index.html";
    }
    var ps = aQuery[1].split("&")[0];
    var qs = ps.split("=");
    if(qs.length!=2){
        window.location.href = "index.html";
    }
    if(qs[0] != "userid" && qs[1].trim()==""){
        window.location.href = "index.html";
    }
    var id = qs[1].trim();

    $.post("rest/Weibos/queryUser","userid="+id, function (data) {
        if(data == undefined) {
            //window.location.href = "index.html";
        }
        console.log(data);
        $("#user_div").append("<h2>" + data.name + "(" + data.id + ")" + "</h2>");
        /**
         * 数据内容加载
         */
        $("#weibodata").bootstrapTable({
            striped: true,
            ajax: function(params){
                console.log(params.data);
                setTimeout(function () {
                    var param = "userid="+id + "&";
                    if(params.data.search == undefined)
                        param += "search=&";
                    else
                        param += "search=" + params.data.search + "&";
                    if(params.data.sort == undefined)
                        param += "field=id&";
                    else
                        param += "field=" + params.data.sort + "&";
                    param += "asc=" + params.data.order + "&pn=" + params.data.offset + "&cp=" + params.data.limit;
                    $.post("rest/Weibos/queryUserWeibos", param, function(data){
                        console.log(data);
                        params.success(data);
                        params.complete();
                        
                        $(".creditWeibo").unbind('click').click(function () {
                            $("#detailbody").empty();
                            $("#credit_div").empty();
                            $("#cal_wait").hide();
                            var id = $(this).attr("id").substring(4);
                            $("#current_weibo").val(id);
                            $.post("rest/Weibos/queryWeibo", "weiboid="+id, function (data) {
                                $("#detailbody").append("<h3>" + data.userinfo.name + "(" + data.userinfo.id + ")" + "</h3>")
                                    .append("<p>" + data.weiboinfo.content +"</p>")
                                    .append("<p class='text-right'>" + "赞:" + data.weiboinfo.zan + "&nbsp;&nbsp;&nbsp;&nbsp;转发:" + data.weiboinfo.trans + "&nbsp;&nbsp;&nbsp;&nbsp;评论:" + data.weiboinfo.comm);
                                $("#detail_modal").modal();
                                $("#calCretBtn").unbind('click').click(function () {
                                    $("#credit_div").empty();
                                    $("#cal_wait").show();
                                    var weiboid=$("#current_weibo").val();
                                    $.post("rest/Weibos/handleweibo","weiboid="+weiboid, function (data) {
                                        console.log(data);
                                        $("#credit_div").append("<hr>");
                                        $("#credit_div").append("<p><b>分词:</b>" + data.seg + "</p>")
                                            .append("<p>长度:" + data.info.length + "&nbsp;&nbsp;情感倾向:" + data.info.em + "&nbsp;&nbsp;外链数:" + data.info.url + "&nbsp;&nbsp;at:" + data.info.at + "&nbsp;&nbsp;表情:" + data.info.emo + "</p>")
                                            .append("<p>情感词数:" + data.info.emwords + "&nbsp;&nbsp;话题数:" + data.info.tp + "&nbsp;&nbsp;书名号:" + data.info.book + "&nbsp;&nbsp;名词数:" + data.info.noun + "</p>")
                                            .append("<p><b>谣言预测:</b>" + data.rumour + "</p>")
                                            .append("<h4><b>可信度:</b>" + data.credit.toFixed(4) + "</h4>");
                                        $("#cal_wait").hide();
                                    },"json");
                                    //setTimeout(function () {
                                    //    var weiboid=$("#current_weibo").val();
                                    //    $.post("/rest/Weibos/handleweibo","weiboid="+weiboid, function (data) {
                                    //        console.log(data);
                                    //        $("#credit_div").append("<hr>");
                                    //        $("#credit_div").append("<p>分词:" + data.seg + "</p>")
                                    //            .append("<p>谣言预测:" + data.rumour + "</p>")
                                    //            .append("<p>可信度:" + data.credit.toFixed(4) + "</p>");
                                    //        $("#cal_wait").hide();
                                    //    },"json");
                                    //},500);
                                });
                            },"json");
                        });
                    }, "json");
                }, 100);
            },
            search: true,
            sidePagination: 'server',
            pageList: [8, 20, 50, 100, 200],
            pageSize: 8,
            pagination: true,
            columns: [{
                width: '5%',
                field: 'linenumber',
                sortable: false,
                halign:'center',
                align: 'right',
                valign: 'middle',
                title: '序号'
            }, {
                field: 'content',
                sortable: false,
                halign:'center',
                valign: 'middle',
                title: '微博内容'
            }, {
                width: '5%',
                visible: false,
                field: 'zan',
                halign:'center',
                align: 'right',
                valign: 'middle',
                sortable: true,
                title: '赞'
            }, {
                width: '5%',
                visible: false,
                field: 'trans',
                halign:'center',
                align: 'right',
                valign: 'middle',
                sortable: true,
                title: '转发'
            }, {
                width: '5%',
                visible: false,
                field: 'comm',
                align: 'center',
                halign:'center',
                valign: 'middle',
                sortable: true,
                title: '评论'
            }, {
                width: '10%',
                title: ' 操作',
                sortable: false,
                field: "id",
                align: 'center',
                halign:'center',
                valign: 'middle',
                formatter: function(v){
                    var buttons = "<div class='btn-group' role='group' aria-label='...'>"
                        + "<button class='btn btn-info creditWeibo' id='btn_" + v + "'>综合</button>"
                        + "</div>";
                    return buttons;
                }
            }]
        });
    }, "json");
});