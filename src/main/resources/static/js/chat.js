    $(function() {

        let getMessageElement = function(message){
            let item = $('<div class="message-item"></div>');
            let header = $('<div class="message-header"></div>');
            header.append($('<div class="datetime">' + message.datetime + '</div>'));
            header.append($('<div class="username">' + message.username + '</div>'));
            let text = $('<div class="message-text"></div>');
            text.text(message.text);
            item.append(header, text);
            return item;
        };

        let updateMessage = function(){
            $.get('/message', {}, function(response){
                if (response.length == 0) {
                    return;
                }

                $('.message-list').html('');
                for (i in response) {
                    let element = getMessageElement(response[i]);
                    $('.message-list').append(element);
                }
            });
        };

        let initApplication = function(){
            $('.messages-and-users').css({display: 'flex'});
            $('.controls').css({display: 'flex'});
            $('.send-message').click(function(){
                let message = $('.new-message').val();
                $.post('/message', {message: message}, function(response){
                    if(response.result) {
                        $('.new-message').val('');
                    } else {
                        alert('Something went wrong...');
                    }
                });
            });
            setInterval(updateMessage, 1000);
        };

        let registerUser = function(name){
            $.post('/auth', {name: name}, function(response){
                if (response.result) {
                    initApplication();
                }
            });
        };

        $.get('/init', {}, function(response){
                    if (!response.result) {
                        let name = prompt('Enter your name:');
                        registerUser(name);
                        return;
                    }
                    initApplication();
                });
    });