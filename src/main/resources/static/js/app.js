$(document).ready(() => {
    changePageAndSize();
});

changePageAndSize = () => {
    $('#pageSizeSelect').change(evt => {
        window.location.replace(`/patient/?pageSize=${evt.target.value}&page=1`);
    });
}

