$(document).ready(() => {
    changePageAndSize();
});

changePageAndSize = () => {
    $('#pageSizeSelect').change(evt => {
        window.location.replace(`/person/?pageSize=${evt.target.value}&page=1`);
    });
}

