/**
 * Created by tanjie on 10/16/15.
 */
$(document).ready(function(){
    //svg
    var loadImage = function(userid){
        $("#svg").empty();
        $.get("rest/SocialNet/usernet?id=" + userid,function(data){
            console.log(data);
            var nodes = {};

            data.forEach(function(link) {
                link.source = nodes[link.source] || (nodes[link.source] = {name: link.source});
                link.target = nodes[link.target] || (nodes[link.target] = {name: link.target});
            });

            var width = 720,
                height = 600;

            var force = d3.layout.force()
                .nodes(d3.values(nodes))
                .links(data)
                .size([width, height])
                .linkDistance(60)
                .charge(-300)
                .on("tick", tick)
                .start();

            var svg = d3.select("#svg").append("svg")
                .attr("width", width)
                .attr("height", height);

            svg.append("defs").selectAll("marker")
                .data(["suit"])
                .enter().append("marker")
                .attr("id", function(d) {
                    return d;
                })
                .attr("viewBox", "0 -5 10 10")
                .attr("refX", 15)
                .attr("refY", -1.5)
                .attr("markerWidth", 6)
                .attr("markerHeight", 6)
                .attr("orient", "auto")
                .append("path")
                .attr("d", "M0,-5L10,0L0,5");

            var path = svg.append("g").selectAll("path")
                .data(force.links())
                .enter().append("path")
                .attr("class", function(d) { return "link suit"; })
                .attr("marker-end", function(d) { return "url(#suit)"; });

            var circle = svg.append("g").selectAll("circle")
                .data(force.nodes())
                .enter().append("circle")
                .attr("r", 6)
                .call(force.drag);

            var text = svg.append("g").selectAll("text")
                .data(force.nodes())
                .enter().append("text")
                .attr("x", 8)
                .attr("y", ".31em")
                .text(
                function(d) {
                    return d.name;
                });

            function tick() {
                path.attr("d", linkArc);
                circle.attr("transform", transform);
                text.attr("transform", transform);
            }

            function linkArc(d) {
                var dx = d.target.x - d.source.x,
                    dy = d.target.y - d.source.y,
                    dr = Math.sqrt(dx * dx + dy * dy);
                return "M" + d.source.x + "," + d.source.y + "A" + dr + "," + dr + " 0 0,1 " + d.target.x + "," + d.target.y;
            }

            function transform(d) {
                return "translate(" + d.x + "," + d.y + ")";
            }
        });
    };

    //微博用户列表
    $("#userData").bootstrapTable({
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
                $.post("rest/Users/queryUsersInPage", param, function(data){
                    console.log(data);
                    params.success(data);
                    params.complete();

                    /**
                     * 关系网络图
                     */
                    $(".imageBtn").unbind('click').click(function () {
                        var id = $(this).attr("id").substr(4);
                        //console.log(id);
                        loadImage(id);
                        $("#svg_modal").modal();
                    });

                    /**
                     * 用户详细资料，用户权威度
                     */
                    $(".detailBtn").unbind('click').click(function () {
                        $("#cal_wait").hide();
                        var id = $(this).attr("id").substr(4);
                        $("#current_user").val(id);
                        var param = "userid=" + id;
                        $.post("rest/Users/queryUser", param, function (data) {
                            if(data == undefined){
                                alert("error!");
                                return;
                            }
                            $("#detailbody").empty();
                            $("#credit_div").empty();
                            $("#detailbody").append("<h2>" + data.name + "(" + data.id + ")" + "</h2>").append("<p>")
                                .append("性别:" + data.sex)
                                .append(data.area==""?"":("&nbsp;&nbsp;&nbsp;地区:" + data.area))
                                .append(data.birthday==""?"":("&nbsp;&nbsp;&nbsp;生日:" + data.birthday))
                                .append("</p>")
                                .append(data.official==""?"":"<h4>认证</h4><p>" + data.official + "</p>")
                                .append(data.summary==""?"":"<h4>简介</h4><p>" + data.summary + "</p>")
                                .append(data.tag==""?"":"<h4>标签</h4><p>" + data.tag + "</p>");
                            $("#detail_modal").modal();
                            $("#calCretBtn").unbind('click').click(function () {
                                $("#cal_wait").show();
                                $("#credit_div").empty();
                                setTimeout(function () {
                                    $.post("rest/Users/calCredit","userid="+$("#current_user").val(), function (data) {
                                        if(data == undefined){
                                            alert("error!");
                                            return;
                                        }
                                        $("#cal_wait").hide();
                                        $("#credit_div").append("<hr>")
                                            .append("<h3>用户权威值:" + data.credit.toFixed(4) + "</h3>")
                                            .append("<h5>信息完整度:" + data.info.toFixed(4) + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;传播影响力:" + data.trans.toFixed(4) + "</h5>")
                                            .append("<h5>用户活跃度:" + data.active.toFixed(4) + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;平台认证值:" + data.plat.toFixed(4) + "</h5>");
                                    },"json");
                                }, 500);
                            });
                        }, "json");
                    });

                    /**
                     * 跳转微博页面
                     */
                    $(".creditBtn").unbind('click').click(function () {
                        var id = $(this).attr("id").substr(5);
                        window.location.href="weibo.html?userid="+id;
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
            width: '12%',
            field: 'id',
            sortable: true,
            halign:'center',
            align: 'left',
            valign: 'middle',
            title: '编号'
        }, {
            width: '17%',
            field: 'name',
            sortable: true,
            halign:'center',
            align: 'left',
            valign: 'middle',
            title: '昵称'
        }, {
            width: '10%',
            field: 'sex',
            align: 'center',
            valign: 'middle',
            sortable: true,
            title: '性别'
        }, {
            width: '17%',
            field: 'area',
            halign:'center',
            sortable: true,
            valign: 'middle',
            title: '地区'
        }, {
            width: '14%',
            field: 'birthday',
            halign:'center',
            align: 'center',
            valign: 'middle',
            sortable: true,
            title: '生日'
        }, {
            width: '10%',
            field: 'weibos',
            halign:'center',
            align: 'right',
            valign: 'middle',
            sortable: true,
            title: '发微博数'
        }, {
            title: ' 操作',
            sortable: true,
            field: "id",
            halign:'center',
            align: 'center',
            valign: 'middle',
            formatter: function(v){
                var buttons = "<div class='btn-group' role='group' aria-label='...'>"
                    + "<button class='btn btn-info imageBtn' id='btn_" + v + "'>图</button>"
                    + "<button class='btn btn-info detailBtn' id='btx_" + v + "'>详</button>"
                    + "<button class='btn btn-info creditBtn' id='btkx_" + v + "'>信</button>"
                    + "</div>";
                return buttons;
            }
        }]
    });

    //userrank表内容
    var userRankTable = function () {
        $("#userRankData").empty();
        $("#userRankData").bootstrapTable({
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
                    $.post("rest/Users/queryRankInPage", param, function(data){
                        console.log(data);
                        params.success(data);
                        params.complete();
                    }, "json");
                }, 100);
            },
            search: true,
            sidePagination: 'server',
            pageList: [5, 20, 50, 100, 200],
            pageSize: 5,
            pagination: true,
            columns: [{
                width: '8%',
                field: 'id',
                sortable: true,
                halign:'center',
                valign: 'middle',
                title: '编号',
            }, {
                field: 'username',
                sortable: true,
                valign: 'middle',
                title: '昵称'
            }, {
                width: '7%',
                field: 'zan',
                align: 'right',
                halign:'center',
                valign: 'middle',
                sortable: true,
                title: '获赞'
            }, {
                width: '10%',
                field: 'trans',
                align: 'right',
                halign:'center',
                valign: 'middle',
                sortable: true,
                title: '被转发数'
            }, {
                width: '10%',
                field: 'comm',
                align: 'right',
                halign:'center',
                valign: 'middle',
                sortable: true,
                title: '被评论数'
            }, {
                width: '7%',
                field: 'att',
                align: 'right',
                halign:'center',
                valign: 'middle',
                sortable: true,
                title: '关注数'
            }, {
                width: '10%',
                field: 'fans',
                align: 'right',
                halign:'center',
                valign: 'middle',
                sortable: true,
                title: '粉丝数'
            }, {
                width: '10%',
                field: 'rank',
                align: 'right',
                halign:'center',
                valign: 'middle',
                sortable: true,
                title: '影响力',
                formatter:function(v){
                    return v.toFixed(5);
                }
            }, {
                width: '10%',
                field: 'valuerank',
                align: 'right',
                halign:'center',
                valign: 'middle',
                sortable: true,
                title: '归一',
                formatter:function(v){
                    return v.toFixed(5);
                }
            }]
        });
    };

    $("#calBtn").click(function () {
        $.post("rest/Users/reset", "", function (data) {
            if(data == "ok"){
                $("#userRank_modal").modal();
                userRankTable();
            } else{
                alert("error!");
            }
        }, "text");
    });
});