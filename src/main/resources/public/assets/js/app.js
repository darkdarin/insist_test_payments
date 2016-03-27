/**
 * Created by Dark on 27.03.2016.
 */

var dateFormatOptions = {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    hour: 'numeric',
    minute: 'numeric',
    second: 'numeric'
};
var $items, $columns, $noItems;

$(function(){
    $noItems = $('._alertNoItems');
    $items = $('table._items tbody');
    $columns = $('table._items thead th');

    //после загрузки страницы получаем список платежей
    getPayments();
});

$(document).on('submit', 'form[data-submit="ajax"]', function () {
    var $form = $(this);
    $form.ajaxSubmit({
        success: function (data) {
            if (function_exists($form.data('submit-action'))) {
                window[$form.data('submit-action')](data, $form);
            }
        }
    });
    return false;
}).on('click', 'a[data-action="approved"]', function(){
    var id = $(this).parents('tr').data('id');

    $.ajax({
        url: "/payments/approved",
        data: { id: id },
        method: "post",
        type: "json",
        success: changeStatusPayment,
        error: ajaxError
    });

}).on('click', 'a[data-action="cancel"]', function(){
    var id = $(this).parents('tr').data('id');

    $.ajax({
        url: "/payments/cancel",
        data: { id: id },
        method: "post",
        type: "json",
        success: changeStatusPayment,
        error: ajaxError
    });
});

function getPayments() {
    var url = "/payments/";
    switch (context) {
        case "user": url += "list"; break;
        case "manager": url += "new"; break;
        case "topmanager": url += "preapproved"; break;
    }

    $.ajax({
        url: url,
        method: "get",
        type: "json",
        success: showPaymentsTable,
        error: ajaxError
    });
}

function ajaxError(data) {
    if (data.responseJSON != undefined) {
        data = data.responseJSON;
        if (data.error != undefined) {
            console.error(data.error + '(' + data.status + '): ' + data.message);
            alert(data.message);
            return;
        }
    }
    console.error(data.statusText + '(' + data.status + '): Unknown error');
    alert('Unknown error');
}

function addPayment(data) {
    $('#addPayment').modal('hide');
    $noItems.hide();
    addTableRow(data);
}

function changeStatusPayment(data) {
    updateTableRow(data);
}

function showPaymentsTable(data) {
    clearTable();
    $noItems.hide();

    if (data.error != undefined) {
        console.error(data.error + '(' + data.status + '): ' + data.message);
        alert(data.message);
        return;
    }

    if (data.length == 0) $noItems.show();

    for (var i = 0; i < data.length; i++) {
        addTableRow(data[i])
    }
}

function addTableRow(obj) {
    console.log(obj);
    $items.append(getTableRow($columns, obj));
}

function clearTable() {
    $items.empty();
}

function deleteTableRow(id) {
    $items.find('tr[data-id="' + id + '"]').remove();
}

function updateTableRow(data) {
    var row = $items.find('tr[data-id="' + data.id + '"]');
    var newRow = getTableRow($columns, data);
    row.html(newRow.html());
}

function getTableRow(columns, obj) {
    var row = $('<tr data-id="' + obj.id + '"></tr>');

    for (var i = 0; i < columns.length; i++) {
        var html = '';
        var type = $(columns[i]).data('type');
        if (type == undefined) type = "text";
        var name = $(columns[i]).data('name');

        switch (type) {
            case 'text':
                html = obj[name];
                break;
            case 'date':
                var date = new Date(obj[name]);
                html = date.toLocaleString("ru", dateFormatOptions);
                break;
            case 'status':
                switch(obj.status) {
                    case 'new': html = '<i class="text-primary">Ожидает обработки</i>'; break;
                    case 'preapproved': html = '<i class="text-info">Обрабатывается</i>'; break;
                    case 'approved': html = '<i class="text-success">Оплачен</i>'; break;
                    case 'cancel': html = '<i class="text-warning">Отклонен</i>'; break;
                    default: html = '<i class="text-danger">Ошибка</i>';
                }
                break;
            case 'user':
                html = obj[name].username;
                break;
            case 'commands':
                switch(obj.status) {
                    case 'preapproved':
                        if (context == 'manager')
                            html = '<i class="text-success">Одобрен</i>';
                        else
                            html = $('._controls').html();
                        break;
                    case 'approved': html = '<i class="text-success">Одобрен</i>'; break;
                    case 'cancel': html = '<i class="text-danger">Отклонен</i>'; break;
                    default: html = $('._controls').html();
                }
                break;
        }
        addCellToRow(row, html);
    }
    return row;
}

function addCellToRow(row, value) {
    var cell = $('<td></td>');
    cell.html(value);
    row.append(cell);
    return row;
}

function function_exists(function_name) {
    if (!function_name) return false;
    if (typeof function_name == 'string') {
        return (typeof window[function_name] == 'function');
    } else {
        return (function_name instanceof Function);
    }
}