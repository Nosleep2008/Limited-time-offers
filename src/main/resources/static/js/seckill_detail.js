// JavaScript
var seckill = {
    //encapsulate url address of ajax
    URL: {
        now: function () {
            return '/seckill/time/now';
        },
        exposer: function(seckillId){
            return '/seckill/' + seckillId + '/exposer';
        },
        execution : function(seckillId, md5){
            return '/seckill/' + seckillId + '/' + md5 + '/execution';
        }
    },
    //check phone number
    validatePhone: function (phone) {
        if (phone && phone.length == 11 && !isNaN(phone)) {
            return true;
        } else {
            return false;
        }
    },
    //
    handleSeckill: function(seckillId, node, money){
        //get buying url，control display logic，execute
        node.hide().html('<button class="btn btn-primary btn-lg" id="killBtn">开始秒杀</button>');
        $.post(seckill.URL.exposer(seckillId), {}, function(result){

            if (result && result['success']){
                var exposer = result['data'];
                if (exposer['exposed']){

                    var md5 = exposer['md5'];
                    var killUrl = seckill.URL.execution(seckillId, md5);
                    console.log('killUrl:' + killUrl);

                    $('#killBtn').one('click', function(){


                        $(this).addClass('disabled');

                        $.post(killUrl, {money: money}, function(result){
                            if (result && result['success']){
                                var killResult = result['data'];
                                var stateInfo = killResult['stateInfo'];

                                node.html('<span class="label label-success">' + stateInfo + '</span>');
                            }
                        })
                    });
                    node.show();
                } else{

                    var now = exposer['now'];
                    var start = exposer['start'];
                    var end = exposer['end'];
                    seckill.countdown(seckillId, now, start, end);
                }
            } else{
                console.log('result:' + result);
            }
        });
    },

    countdown: function (seckillId, nowTime, startTime, endTime, money) {
        var seckillBox = $('#seckill-box');
        var seckillTimeSpan = $('#seckill-time-span');

        if (nowTime > endTime){

            seckillTimeSpan.html('秒杀结束');
            seckillBox.hide();
        }else if(nowTime < startTime){

            var killTime = new Date(startTime + 1000);
            seckillTimeSpan.countdown(killTime, function(event){

                var format = event.strftime('秒杀开始倒计时： %D天 %H时 %M分 %S秒');
                seckillTimeSpan.html(format);

            }).on('finish.countdown', function(){

                seckill.handleSeckill(seckillId, seckillBox, money);
            });
        }else{

            seckill.handleSeckill(seckillId, seckillBox, money);


            var killEndTime = new Date(endTime + 1000);
            seckillTimeSpan.countdown(killEndTime, function(event){

                var format = event.strftime('距离秒杀结束： %D天 %H时 %M分 %S秒');
                seckillTimeSpan.html(format);
            });
        }
    },

    detail: {

        init: function (params) {

            var killPhone = $.cookie('killPhone');

            if (!seckill.validatePhone(killPhone)) {

                var killPhoneModal = $('#killPhoneModal');
                killPhoneModal.modal({
                    show: true,
                    backdrop: 'static',
                    keyboard: false
                });
                $("#killPhoneBtn").click(function () {
                    var inputPhone = $('#killPhoneKey').val();
                    if (seckill.validatePhone(inputPhone)) {

                        $.cookie('killPhone', inputPhone, {expires: 7, path: '/seckill/'});

                        window.location.reload();
                    } else {
                        $("#killPhoneMessage").hide().html('<lable class="label label-danger">手机号错误！</lable>').show(300);
                    }
                });
            }

            var startTime = params['startTime'];
            var endTime = params['endTime'];
            var seckillId = params['seckillId'];
            var money = params['money'];
            $.get(seckill.URL.now(), {}, function (result) {
                if (result && result['success']) {
                    var nowTime = result['data'];

                    seckill.countdown(seckillId, nowTime, startTime, endTime, money);
                } else {
                    console.log('result:' + result);
                }
            });
        }
    }
};