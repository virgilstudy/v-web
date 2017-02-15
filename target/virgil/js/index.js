/**
 * Created by Virgil on 2017/1/31.
 */
//create menu
var menuItem = Vue.extend({
    name: 'menu-item',
    props: {item: {}},
    template: [
        '<li>',
        '<a v-if="item.type===0" href="javascript:;">',
        '<span>{{item.name}}</span>',
        '<i class="fa fa-angle-left pull-right"></i>',
        '</a>',
        '<ul v-if="item.type===0" class="treeview-menu">',
        '<menu-item :item="item" v-for="item in item.list"></menu-item>',
        '</ul>',
        '<a v-if="item.type === 1" :href="\'#\'+item.url"><i v-if="item.icon != null" :class="item.icon"></i><i v-else class="fa fa-circle-o"></i> {{item.name}}</a>',
        '</li>'
    ].join('')
});
//注册菜单组件
Vue.component('menuItem', menuItem);
    //fuck
var vm = new Vue({
        el: '#vapp',
        data: {
            user: {},
            menuList: {},
            main: 'sys/main.html',
            password: '',
            newPassword: '',
            navTitle: "控制台"
        },
        created: function () {
            console.log('created');
            this.getMenuList();
            //this.getUser();
        },
        updated: function () {
            //router
            var router = new Router();
            routerList(router, vm.menuList);
            router.start();
        },
        methods: {
            getMenuList: function (event) {
                console.log('getMenu');
                $.getJSON("sys/menu/user?_" + $.now(), function (r) {
                    vm.menuList = r.menuList;
                });
            },
            getUser: function () {
                $.getJSON("sys/user/info?_" + $.now(), function (r) {
                    vm.user = r.user;
                })
            },
            updatePassword: function () {
                layer.open({
                    type: 1,
                    skin: 'layui-layer-molv',
                    title: "修改密码",
                    area: ['550px', '300px'],
                    shadeClose: false,
                    content: jQuery("#passwordLayer"),
                    btn: ['修改', '取消'],
                    btn1: function (index) {
                        var data = "password=" + vm.password + "&newPassword=" + vm.newPassword;
                        $.ajax({
                            type: "POST",
                            url: "sys/user/password",
                            data: data,
                            dataType: "json",
                            success: function (result) {
                                if (result.code == 0) {
                                    layer.close(index);
                                    layer.alert('修改成功', function (index) {
                                        location.reload();
                                    });
                                } else {
                                    layer.alert(result.msg);
                                }
                            }
                        });
                    }
                })
            }

        }
    });

function routerList(router, menuList) {
    for (var key in menuList) {
        var menu = menuList[key];
        if (menu.type == 0) {
            routerList(router, menu.list);
        } else if (menu.type == 1) {
            router.add('#' + menu.url, function () {
                var url = window.location.hash;
                //替换iframe的url
                vm.main = url.replace('#', '');
                //open
                $(".treeview-menu li").removeClass("active");
                $("a[href='" + url + "']").parents("li").addClass("active");
                vm.navTitle = $("a[href='" + url + "']").text();
            });
        }
    }
}