$(function () {
    $('.js-open').click(function () {
        $('#overlay, .modal-window').fadeIn();
    });
    $('.js-close').click(function () {
        $('#overlay, .modal-window').fadeOut();
    });
});