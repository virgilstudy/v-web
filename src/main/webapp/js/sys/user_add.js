/**
 * Created by Virgil on 2017/2/16.
 */
var userId = T.p("userId");
var vm = new Vue({
    el: '#vapp',
    data: {
        title: "新增管理员",
        roleList: [],
        user: {
            status: 1,
            roleIdList: []
        }
    },
    created: function () {
        if (userId != null) {
            this.title = "修改管理员";
            this.getUser(userId);
        }
        //this.getRoleList();
    },
    methods: {
        getUser: function (userId) {
            $.get("../sys/user/info" + userId, function (r) {
                console.log(r);
                vm.user = r.user;
            });
        },
        back: function (event) {
            history.go(-1);
        }
    }
});