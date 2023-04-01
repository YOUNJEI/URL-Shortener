var main = {
    init: function () {
        var _this = this;
        $('#btn-create').on('click', function () {
            _this.create();
        });
    },

    create: function () {
        var data = {
            origin: $('#origin').val()
        };

        $.ajax({
            type: 'POST',
            url: '/api/v1/url',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function (response) {
            $('#short').val(response.shortUrl);
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }
};
main.init();