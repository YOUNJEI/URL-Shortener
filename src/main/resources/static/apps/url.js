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
            $('#short').val(null);

            var response = JSON.parse(error.responseText);
            if (response.message != null)
                alert(response.message);
            else
                alert(JSON.stringify(error.responseText));
        });
    }
};
main.init();